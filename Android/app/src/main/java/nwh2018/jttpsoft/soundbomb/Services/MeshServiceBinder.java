package nwh2018.jttpsoft.soundbomb.Services;

import android.app.Service;
import android.os.Binder;

import nwh2018.jttpsoft.soundbomb.Services.MeshConnector;

/**
 * Created by tjm on 1/13/18.
 */

public class MeshServiceBinder extends Binder {

    private MeshConnector content;

    public MeshServiceBinder(MeshConnector content){
        this.content = content;
    }

    MeshConnector getService(){
        return this.content;
    }

}
