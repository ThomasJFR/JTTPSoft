//package nwh2018.jttpsoft.soundbomb;
//
//import android.app.Activity;
//
//import java.util.HashSet;
//
///**
// * Created by Thomas on 2018-01-13.
// */
//
//public class MeshConnector implements MeshStateListener{
//
//    AndroidMeshManager meshManager = null;
//    HashSet<MeshID> = new HashSet<>();
//
//    /**
//     * Manages all current RightMesh connections.
//     * @param srcActivity The activity from which the MeshManager is being used.
//     */
//    public MeshConnector(Activity srcActivity){
//        meshManager = AndroidMeshManager.getInstance(srcActivity, this);
//    }
//
//    public void resume() {
//        meshManager.resume();
//    }
//
//    public void close(){
//        meshManager.stop();
//    }
//}
