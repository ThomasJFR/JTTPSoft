package nwh2018.jttpsoft.soundbomb.BroadcastReceivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import nwh2018.jttpsoft.soundbomb.Activities.ReceiverActivity;

/**
 * Created by Thomas on 2018-01-13.
 */

public class LocalReceiver extends BroadcastReceiver{

    private static Map<Integer, Activity> activityList = new HashMap<>();
    public static final int SOURCE_INDEX = 0;
    public static final int RECEIVER_INDEX = 1;

    private static final String DATA_RECEIVED_MASTER_UPDATE = "jttpsoft.soundbomb.DATA_RECEIVED_MASTER_UPDATE";
    private static final String MASTER_STATE = "jttpsoft.soundbomb.MASTER_STATE";
    private static final String DATA_RECEIVED_FILE_DETAILS = "jttpsoft.soundbomb.DATA_RECEIVED_FILE_DETAILS";
    private static final String DATA_RECEIVED_TRACK_STATE = "jttpsoft.soundbomb.DATA_RECEIVED_TRACK_STATE";
    private static final String DATA_RECEIVED_TRACK = "jttpsoft.soundbomb.DATA_RECEIVED_TRACK";
    private static final String BROADCAST_RECEIVED_PLAY = "jttpsoft.soundbomb.PLAY_TIMESTAMP";
    private static final String BROADCAST_RECEIVED_PAUSE = "jttpsoft.soundbomb.PAUSE_TIMESTAMP";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        switch(action){
            case DATA_RECEIVED_MASTER_UPDATE:
                switch(intent.getIntExtra(MASTER_STATE,-1)){
                    case 1:
                        break;
                    case -1:
                    default:
                        break;
                }
                break;
            case DATA_RECEIVED_FILE_DETAILS:
                break;
            case DATA_RECEIVED_TRACK_STATE:
                if(intent.getIntExtra(DATA_RECEIVED_TRACK, -1) != -1){
                    ((ReceiverActivity)activityList.get(RECEIVER_INDEX)).playTrack();
                }
                if(intent.getIntExtra(BROADCAST_RECEIVED_PLAY, -1) != -1)
                    Toast.makeText(context, "Received a play command", Toast.LENGTH_SHORT).show();
                if(intent.getIntExtra(BROADCAST_RECEIVED_PAUSE, -1) != -1)
                    Toast.makeText(context, "Received a pause command", Toast.LENGTH_SHORT).show();
            default:
                break;
        }

    }

    public static IntentFilter generateIntentFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DATA_RECEIVED_MASTER_UPDATE);
        intentFilter.addAction(MASTER_STATE);
        intentFilter.addAction(DATA_RECEIVED_FILE_DETAILS);
        intentFilter.addAction(DATA_RECEIVED_TRACK_STATE);
        intentFilter.addAction(DATA_RECEIVED_TRACK);
        intentFilter.addAction(BROADCAST_RECEIVED_PLAY);
        intentFilter.addAction(BROADCAST_RECEIVED_PAUSE);

        return intentFilter;
    }

    public static void subscribeToUpdates(int index, Activity activity){
        activityList.put(index, activity);
    }
}
