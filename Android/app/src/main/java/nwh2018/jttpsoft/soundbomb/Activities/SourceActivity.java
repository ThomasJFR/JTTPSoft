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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

//import nwh2018.jttpsoft.soundbomb.MeshConnector; TODO REENABLE ME
import nwh2018.jttpsoft.soundbomb.R;
import nwh2018.jttpsoft.soundbomb.Services.AudioPlayerService;

public class SourceActivity extends AppCompatActivity {

    private static final String TAG = "soundbomb.Source";

    private static final int SELECT_FILE_CODE = 11;

    //--MESH CONNECTOR--
//   private MeshConnector meshConnector; TODO REENABLE ME

    //--UI ELEMENTS--
    private Switch swt_channelState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        swt_channelState = (Switch)findViewById(R.id.swt_channelState);
        swt_channelState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(SourceActivity.this.getApplicationContext(), "State:" + String.valueOf(b), Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
    public void onStart(){
        super.onStart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case SELECT_FILE_CODE:
                Toast.makeText(SourceActivity.this, "Success!!!",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.e(TAG, "Invalid requestCode");
            //Do stuff
        }
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this.getApplicationContext(),"Source mode disabled.", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

}
