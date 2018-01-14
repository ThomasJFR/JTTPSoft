package nwh2018.jttpsoft.soundbomb.Activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import io.left.rightmesh.util.RightMeshException;
import nwh2018.jttpsoft.soundbomb.BroadcastReceivers.LocalReceiver;
import nwh2018.jttpsoft.soundbomb.HelperTools.TimeManager;
import nwh2018.jttpsoft.soundbomb.R;
import nwh2018.jttpsoft.soundbomb.Services.MeshConnector;
import nwh2018.jttpsoft.soundbomb.Utilities.Utilities;
public class SourceActivity extends AppCompatActivity implements Button.OnClickListener{

    private MeshConnector meshConnector;
    private ServiceConnection  meshServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MeshConnector.MeshServiceBinder binder = (MeshConnector.MeshServiceBinder)iBinder;
            meshConnector = binder.getService();

            int attemptCounter = 0;
            for(; attemptCounter < 5; attemptCounter++){
                if(meshConnector.applyMaster())
                    break;
            }
            if(attemptCounter == 4)
                Log.e(TAG, "Couldn't apply for master.");

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            meshConnector = null;
        }
    };;
    private Intent meshServiceIntent;

    private static final String TAG = "soundbomb.Source";

    private static final int SELECT_FILE_CODE = 11;

    //--Song Stuff--
    private String currentPath;
    private MediaPlayer mediaPlayer;

    //--UI ELEMENTS--
    TextView tv_currentSong;
    ImageButton btn_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        //--CREATE WORKERS--
        meshConnector = new MeshConnector();

        meshServiceIntent = new Intent(this, MeshConnector.class);
        bindService(meshServiceIntent, meshServiceConnection, Context.BIND_AUTO_CREATE);
        startService(meshServiceIntent);

        LocalReceiver.subscribeToUpdates(LocalReceiver.SOURCE_INDEX, this);

        //--REGISTER UI ELEMENTS--
        tv_currentSong = (TextView)findViewById(R.id.tv_currentSong);

        btn_play = (ImageButton)findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new AlertDialog.Builder(SourceActivity.this)
//                        .setMessage("Would you like to select a new file to play?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent fileExploreIntent = new Intent(
//                                        FileBrowserActivity.INTENT_ACTION_SELECT_FILE,
//                                        null,
//                                        SourceActivity.this,
//                                        FileBrowserActivity.class
//                                );
//                                fileExploreIntent.putExtra(
//                                        FileBrowserActivity.startDirectoryParameter,
//                                        "/system/media/audio");
//                                startActivityForResult(
//                                        fileExploreIntent,
//                                        SELECT_FILE_CODE
//                                );
//                            }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .show();
//            }
//        });

        //SOME STUFF TO RUN ONCE
        new AlertDialog.Builder(this)
                .setMessage("Please select a file to play:")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent fileExploreIntent = new Intent(
                                FileBrowserActivity.INTENT_ACTION_SELECT_FILE,
                                null,
                                SourceActivity.this,
                                FileBrowserActivity.class
                        );
                        fileExploreIntent.putExtra(
                                FileBrowserActivity.startDirectoryParameter,
                                "/system/media/audio");

                        startActivityForResult(
                                fileExploreIntent,
                                SELECT_FILE_CODE
                        );
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case SELECT_FILE_CODE:
                if(resultCode == this.RESULT_OK) {
                    String newFile = data.getStringExtra(
                           FileBrowserActivity.returnFileParameter);

                    Toast.makeText(
                            this,
                            "Received path from file browser:" + newFile,
                            Toast.LENGTH_LONG
                    ).show();

                    tv_currentSong.setText("Current Song: " + Utilities.getFileName(newFile));
                    currentPath = newFile;

                    if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        btn_play.setImageResource(R.drawable.play_button);
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.rideofthevalkyries);
                    mediaPlayer.setLooping(true);

                } else {
                    Toast.makeText(
                            this,
                            "Received NO result from file browser",
                            Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                Log.e(TAG, "Invalid requestCode");
            //Do stuff
        }
    }

    @Override
    public void onDestroy(){
        unbindService(meshServiceConnection);
        mediaPlayer.stop();
        Toast.makeText(this.getApplicationContext(),"Source mode disabled.", Toast.LENGTH_LONG).show();
        meshConnector.revokeMaster();
        super.onDestroy();
    }

    public void onBackPresssed(){
        try {
            meshConnector.sendPause(0);
        } catch (RightMeshException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_play:

                if(mediaPlayer == null || currentPath == null){
                    Toast.makeText(this, "No song queued", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if(mediaPlayer.isPlaying()) {
                        meshConnector.sendPause(0);

                        btn_play.setImageResource(R.drawable.play_button);
                        mediaPlayer.pause();
                    }
                    else{
                        meshConnector.setData(Utilities.getFileAsByteArray(currentPath));
                        //meshConnector.sendFile();
                        long timestamp = TimeManager.getCurrentTimeStamp() + 1; // one second delay for file transfer
                        meshConnector.sendPlay(timestamp);

                        btn_play.setImageResource(R.drawable.pause_button);
                        while(TimeManager.getCurrentTimeStamp()<timestamp); // sleep until timestamp triggered.
                        mediaPlayer.start();
                    }
                }
                catch (RightMeshException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
