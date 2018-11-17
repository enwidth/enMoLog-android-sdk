package logreader.com.estuate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by gourav on 21/4/17.
 */

public class RLNetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(RLUtils.isInternetAvailable(context)){
            Intent mServiceIntent = new Intent(context, RLBackgroundService.class);
            mServiceIntent.putExtra("readfromfile", "uploadFromFile");
            context.startService(mServiceIntent);
        }
    }
}
