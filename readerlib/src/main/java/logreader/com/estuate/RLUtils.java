package logreader.com.estuate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by gourav on 21/4/17.
 */

public class RLUtils {

    public static boolean isInternetAvailable(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getCurrentTime(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(timestamp.getTime());

        // S is the millisecond
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy' 'HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
