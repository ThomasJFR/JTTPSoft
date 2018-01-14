package nwh2018.jttpsoft.soundbomb.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import java.util.Arrays;

import nwh2018.jttpsoft.soundbomb.BroadcastReceivers.LocalReceiver;
import nwh2018.jttpsoft.soundbomb.R;
import nwh2018.jttpsoft.soundbomb.Services.MeshConnector;
import nwh2018.jttpsoft.soundbomb.Utilities.Utilities;

public class ReceiverActivity extends AppCompatActivity {

    private static final String TAG = "soundbomb.Receiver";

    private MeshConnector meshConnector;
    private ServiceConnection meshServiceConnection;
    private Intent meshServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        meshConnector = new MeshConnector();
        meshServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MeshConnector.MeshServiceBinder binder = (MeshConnector.MeshServiceBinder)iBinder;
                meshConnector = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                meshConnector = null;
            }
        };
        meshServiceIntent = new Intent(this, MeshConnector.class);
        bindService(meshServiceIntent, meshServiceConnection, Context.BIND_AUTO_CREATE);
        startService(meshServiceIntent);

        LocalReceiver.subscribeToUpdates(LocalReceiver.RECEIVER_INDEX, this);

    }

    @Override
    public void onStart(){
        super.onStart();
        TextView peerStatusView = (TextView) findViewById(R.id.peerStatus);
        String peerStatus = meshConnector.getPeerStatus();
        peerStatusView.setText(peerStatus);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        TextView peerStatusView = (TextView) findViewById(R.id.peerStatus);
        String peerStatus = meshConnector.getPeerStatus();
        peerStatusView.setText(peerStatus);
    }

    @Override
    public void onDestroy(){
        unbindService(meshServiceConnection);
        super.onDestroy();
    }

    public void playTrack(){

        Log.e("LOOK HERE!!~~", "Data: " + Arrays.toString(meshConnector.getData()));

        return;

//        Utilities.parseByteArrayAsFile(meshConnector.getData(), ".ogg");
//
//        try {
//            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse("BufferedSong.ogg"));
//            mediaPlayer.setLooping(false);
//            mediaPlayer.start();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
