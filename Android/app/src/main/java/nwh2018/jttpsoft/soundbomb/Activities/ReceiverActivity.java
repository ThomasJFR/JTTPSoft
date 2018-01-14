package nwh2018.jttpsoft.soundbomb.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nwh2018.jttpsoft.soundbomb.R;
import nwh2018.jttpsoft.soundbomb.Services.MeshConnector;

public class ReceiverActivity extends AppCompatActivity {

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
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onDestroy(){
        unbindService(meshServiceConnection);
        super.onDestroy();
    }
}
