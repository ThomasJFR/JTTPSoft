package nwh2018.jttpsoft.soundbomb;

import android.app.Activity;

import java.util.HashSet;

import io.left.rightmesh.android.AndroidMeshManager;
import io.left.rightmesh.android.MeshService;
import io.left.rightmesh.id.MeshID;
import io.left.rightmesh.mesh.MeshStateListener;

/**
 * Created by Thomas on 2018-01-13.
 */

public class MeshConnector implements MeshStateListener {

    AndroidMeshManager meshManager = null;
    HashSet<MeshID> users = new HashSet<>();

    /**
     * Manages all current RightMesh connections.
     * @param srcActivity The activity from which the MeshManager is being used.
     */
    public MeshConnector(Activity srcActivity){
        meshManager = AndroidMeshManager.getInstance(srcActivity, this);
    }

    public void resume() {
        try {
            meshManager.resume();
        } catch (MeshService.ServiceDisconnectedException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            meshManager.stop();
        } catch (MeshService.ServiceDisconnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void meshStateChanged(MeshID meshID, int i) {

    }
}
