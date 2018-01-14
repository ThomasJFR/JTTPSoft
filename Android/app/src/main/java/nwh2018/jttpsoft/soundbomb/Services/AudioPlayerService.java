package nwh2018.jttpsoft.soundbomb.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AudioPlayerService extends Service {
    public AudioPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
