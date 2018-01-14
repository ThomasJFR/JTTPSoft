package nwh2018.jttpsoft.soundbomb.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.left.rightmesh.android.AndroidMeshManager;
import io.left.rightmesh.android.MeshService;
import io.left.rightmesh.id.MeshID;
import io.left.rightmesh.mesh.MeshManager;
import io.left.rightmesh.mesh.MeshStateListener;
import io.left.rightmesh.util.RightMeshException;
import io.reactivex.functions.Consumer;
import nwh2018.jttpsoft.soundbomb.BroadcastReceivers.LocalReceiver;
import nwh2018.jttpsoft.soundbomb.HelperTools.ByteIntConvertor;

import static io.left.rightmesh.mesh.MeshManager.REMOVED;

/**
 * Created by Thomas on 2018-01-13.
 */

public class MeshConnector extends Service implements MeshStateListener {

    // Port to bind app to.
    private final static int MESH_PORT = 2169;
    //private final static int MESH_CTR_PORT = 2169;
    //private final static int MESH_DATA_PORT = 3037;

    // Master MashID
    private static MeshID master = null;
    private final IBinder binder = new MeshServiceBinder();

    // MeshManager instance - interface to the mesh network.
    AndroidMeshManager mm = null;

    // Set to keep track of peers connected to the mesh.
    HashSet<MeshID> users = new HashSet<>();

    // Data Buffer
    private static byte[] dataBuffer;

    @Override
    public int onStartCommand(Intent intent,int flags, int startID){

        if(mm == null)
            mm = AndroidMeshManager.getInstance(MeshConnector.this,MeshConnector.this);

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){

        if(mm == null)
            mm = AndroidMeshManager.getInstance(MeshConnector.this,MeshConnector.this);

        return binder;
    }

    @Override
    public void onDestroy(){
        try {
            super.onDestroy();
            mm.stop();
        } catch (MeshService.ServiceDisconnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void meshStateChanged(MeshID uuid,int state){
        if(state==MeshStateListener.SUCCESS){
            try{
                mm.bind(MESH_PORT);

                mm.on(MeshManager.DATA_RECEIVED,
                        new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                handleDataReceived((MeshManager.RightMeshEvent) o);
                            }
                        });

                mm.on(MeshManager.PEER_CHANGED,
                        new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                handlePeerChanged((MeshManager.RightMeshEvent) o);
                            }
                        });
            } catch (RightMeshException e){
                // Not Implemented yet
            }
        }
    }

    private void handleDataReceived(MeshManager.RightMeshEvent e){
        final MeshManager.DataReceivedEvent event = (MeshManager.DataReceivedEvent) e;
        byte superByte = event.data[0];

        byte[] rawTimeStamp = new byte[8];
        long timestamp = 0;
        switch (superByte){
            case 0x01: // Play
                System.arraycopy(event.data,1,rawTimeStamp,0,8);
                timestamp = ByteIntConvertor.bytesToLong(rawTimeStamp);
                this.notifyPlay(timestamp);
                break;
            case 0x02: // Pause
                System.arraycopy(event.data,1,rawTimeStamp,0,8);
                timestamp = ByteIntConvertor.bytesToLong(rawTimeStamp);
                this.notifyPlay(timestamp);
                break;
            case 0x03: // FileTransfer
                byte[] rawDataLength = new byte[8];
                System.arraycopy(event.data,1,rawDataLength,0,8);
                long dataLength = ByteIntConvertor.bytesToLong(rawDataLength);
                MeshConnector.dataBuffer = new byte[(int) dataLength];
                System.arraycopy(event.data,1+8,MeshConnector.dataBuffer,0,(int) dataLength);
                this.notifyFileReceived();
                break;
            default:
                // Invalid data!
        }
    }


    private void handlePeerChanged(MeshManager.RightMeshEvent e){
        MeshManager.PeerChangedEvent event = (MeshManager.PeerChangedEvent) e;
        if(event.state != REMOVED && !users.contains(event.peerUuid))
            users.add(event.peerUuid);
        else if (event.state == REMOVED){
            users.remove(event.peerUuid);
            if(event.peerUuid.equals(MeshConnector.master)){
                Intent intent = new Intent("jttpsoft.soundbomb.DATA_RECEIVED_MASTER_UPDATE");
                intent.putExtra("jttpsoft.soundbomb.MASTER_STATE",Message.MASTER_QUIT);
                sendBroadcast(intent);
            }
        }
    }

    private void notifyPlay(long timestamp){
        Intent intent = new Intent("jttpsoft.soundbomb.DATA_RECEIVED_TRACK_STATE");
        intent.putExtra("jttpsoft.soundbomb.PLAY_TIMESTAMP",timestamp);
        sendBroadcast(intent);
    }

    private void notifyPause(long timestamp){
        Intent intent = new Intent("jttpsoft.soundbomb.DATA_RECEIVED_TRACK_STATE");
        intent.putExtra("jttpsoft.soundbomb.PAUSE_TIMESTAMP",timestamp);
        sendBroadcast(intent);
    }

    private void notifyFileReceived(){
        Intent intent = new Intent("jttpsoft.soundbomb.DATA_RECEIVED_TRACK_STATE");
        intent.putExtra("jttpsoft.soundbomb.DATA_RECEIVED_TRACK",Message.FILE_RECEIVED);
        sendBroadcast(intent);
    }

    public byte[] getData(){
        return MeshConnector.dataBuffer;
    }

    public void setData(byte[] data){
        MeshConnector.dataBuffer = data;
    }

    public void sendPlay(long timestamp) throws RightMeshException{
        byte[] buffer = new byte[9]; // one byte for command and eight bytes for timestamp(long)
        buffer[0] = (byte) 0x001; // play
        byte[] intermedianTimestamp = ByteIntConvertor.longToBytes(timestamp);
        System.arraycopy(intermedianTimestamp,0,buffer,1,8);
        for(MeshID receiver: this.users){
            mm.sendDataReliable(receiver,MESH_PORT,buffer);
        }
    }

    public void sendPause(long timestamp) throws RightMeshException{
        byte[] buffer = new byte[9]; // one byte for command and eight bytes for timestamp(long)
        buffer[0] = (byte) 0x002; // pause
        byte[] intermedianTimestamp = ByteIntConvertor.longToBytes(timestamp);
        System.arraycopy(intermedianTimestamp,0,buffer,1,8);
        try {
            for (MeshID receiver : this.users) {
                mm.sendDataReliable(receiver, MESH_PORT, buffer);
            }
        }
        catch (RuntimeException re){
            re.printStackTrace();
        }
    }

    public void sendFile() throws RightMeshException{
        // one byte for command and eight bytes for timestamp(long)
        // following by the data stream to send
        byte[] buffer = new byte[9+MeshConnector.dataBuffer.length];
        buffer[0] = (byte) 0x003; // file transfer
        byte[] intermedianLength = ByteIntConvertor.longToBytes(MeshConnector.dataBuffer.length);
        System.arraycopy(intermedianLength,0,buffer,1,8);
        System.arraycopy(MeshConnector.dataBuffer,0,buffer,9,MeshConnector.dataBuffer.length);
        for(MeshID receiver: this.users) {
            mm.sendDataReliable(receiver, MESH_PORT, buffer);
        }
    }

    public Set<MeshID> getPeers(){
        return Collections.unmodifiableSet(this.users);
    }

    public MeshID getMaster(){
        return MeshConnector.master;
    }

    public void setMaster(MeshID master){
        MeshConnector.master = master;
    }

    public class MeshServiceBinder extends Binder {
        public MeshConnector getService(){
            return MeshConnector.this;
        }

    }
}
