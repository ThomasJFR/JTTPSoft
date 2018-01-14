package nwh2018.jttpsoft.soundbomb.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Thomas on 2018-01-13.
 */

public class LocalReceiver extends BroadcastReceiver{

    private static final String DATA_RECEIVED_FILE_DETAILS = "jttpsoft.soundbomb.DATA_RECEIVED_FILE_DETAILS";

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        switch(action){
            case DATA_RECEIVED_FILE_DETAILS:
                break;
            default:
                break;
        }

    }
}
