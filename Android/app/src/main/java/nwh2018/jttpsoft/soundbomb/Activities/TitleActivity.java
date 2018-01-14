package nwh2018.jttpsoft.soundbomb.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import nwh2018.jttpsoft.soundbomb.R;

public class TitleActivity extends AppCompatActivity implements Button.OnClickListener, Button.OnLongClickListener{

    private static final String TAG = "soundbomb.titleActivity";

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
    }

    @Override
    public void onBackPressed(){
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
        super.onDestroy();
    }
}
