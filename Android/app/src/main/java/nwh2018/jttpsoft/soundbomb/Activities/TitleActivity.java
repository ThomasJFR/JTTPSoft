package nwh2018.jttpsoft.soundbomb.Activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import nwh2018.jttpsoft.soundbomb.BroadcastReceivers.LocalReceiver;
import nwh2018.jttpsoft.soundbomb.R;
import nwh2018.jttpsoft.soundbomb.Services.MeshConnector;

public class TitleActivity extends AppCompatActivity implements Button.OnClickListener, Button.OnLongClickListener{

    private static final String TAG = "soundbomb.titleActivity";

    private LocalReceiver localReceiver;

    private MeshConnector meshConnector;
    private ServiceConnection meshServiceConnection = new ServiceConnection() {
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
    Intent meshServiceIntent;

    Button btn_mode_source;
    Button btn_mode_receiver;
    ProgressBar spinProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        btn_mode_source = (Button)findViewById(R.id.button_mode_source);
        btn_mode_source.setOnClickListener(this);
        btn_mode_source.setOnLongClickListener(this);

        btn_mode_receiver = (Button)findViewById(R.id.button_mode_receiver);
        btn_mode_receiver.setOnClickListener(this);
        btn_mode_receiver.setOnLongClickListener(this);

        spinProgressBar = (ProgressBar)findViewById(R.id.spinProgressBar);
        spinProgressBar.setIndeterminate(true);

        meshConnector = new MeshConnector();

        meshServiceIntent = new Intent(this, MeshConnector.class);
        bindService(meshServiceIntent, meshServiceConnection, Context.BIND_AUTO_CREATE);
        startService(meshServiceIntent);
    }

    @Override
    public void onStart(){
        localReceiver = new LocalReceiver();
        registerReceiver(localReceiver, LocalReceiver.generateIntentFilter());
        super.onStart();
    }

    @Override
    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view){
        view.getContext();
        switch(view.getId()){
            case R.id.button_mode_source:
                spinProgressBar.setVisibility(ProgressBar.VISIBLE);

                Intent sourceIntent = new Intent(this, SourceActivity.class);
                startActivity(sourceIntent);
                break;
            case R.id.button_mode_receiver:
                spinProgressBar.setVisibility(ProgressBar.VISIBLE);

                Intent receiverIntent = new Intent(this, ReceiverActivity.class);

                startActivity(receiverIntent);
                break;
            default:
                Log.e(TAG, "Button pressed had no associated ID in its listener.");
        }
        spinProgressBar.setVisibility(ProgressBar.INVISIBLE);

    }

    @Override
    public boolean onLongClick(View view) {
        view.getContext();
        switch(view.getId()){
            case R.id.button_mode_source:
                new AlertDialog.Builder(this)
                        .setMessage("Source: Stream to other devices")
                        .setPositiveButton("OK", null)
                        .show();
                break;
            case R.id.button_mode_receiver:
                new AlertDialog.Builder(this)
                        .setMessage("Receiver: Receive a stream from another devices")
                        .setPositiveButton("OK", null)
                        .show();
                break;
            default:
                Log.e(TAG, "Button pressed had no associated ID in its listener.");
        }
        return false;
    }

    @Override
    public void onDestroy(){
        //TODO dont forget to include (MeshManager Object).stop()
        unbindService(meshServiceConnection);
        super.onDestroy();
    }

    public MeshConnector getMeshConnector(){
        return meshConnector;
    }
}
