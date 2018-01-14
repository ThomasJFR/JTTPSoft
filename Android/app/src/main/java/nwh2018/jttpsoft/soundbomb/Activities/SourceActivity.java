package nwh2018.jttpsoft.soundbomb.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import nwh2018.jttpsoft.soundbomb.MeshConnector;
import nwh2018.jttpsoft.soundbomb.R;

public class SourceActivity extends AppCompatActivity implements Button.OnClickListener{

    private static final String TAG = "soundbomb.Source";

    private static final int SELECT_FILE_CODE = 11;

    //--MESH CONNECTOR--
    //private MeshConnector meshConnector; TODO REENABLE ME

    //--UI ELEMENTS--
    TextView tv_currentSong;
    Button btn_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        //--CREATE WORKERS--
       // meshConnector = new MeshConnector(this); TODO REENABLE ME

        //--REGISTER UI ELEMENTS--
        tv_currentSong = (TextView)findViewById(R.id.tv_currentSong);

        btn_play = (Button)findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
//        meshConnector.resume(); TODO REENABLE ME
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

                    tv_currentSong.setText("Current Song: " + newFile);

                } else {//if(resultCode == this.RESULT_OK) {
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
//        meshConnector.close();TODO REENABLE ME

        Toast.makeText(this.getApplicationContext(),"Source mode disabled.", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_play:
                btn_play.setText(btn_play.getText() != "Play" ? "Play" : "Pause");
                break;
            default:
                break;
        }
    }
}
