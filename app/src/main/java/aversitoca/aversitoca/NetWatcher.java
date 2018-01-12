package aversitoca.aversitoca;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by josyt on 11/01/2018.
 */

public class NetWatcher extends BroadcastReceiver {

    private static final String LOG_TAG = "NetworkChangeReceiver";
    private boolean isConnected = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        isNetworkAvailable(context);
        if (info != null) {
            if (info.isConnected()) {
                //start service
                intent = new Intent(context, RefreshService.class);
                context.startService(intent);
            }
            else {
                //stop service
                intent = new Intent(context, RefreshService.class);
                context.stopService(intent);
            }
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            Log.v(LOG_TAG, "Estás conectado a Internet!");
                            isConnected = true;
                            // do your processing here ---
                            // if you need to post any data to the server or get
                            // status
                            // update from the server
                        }
                        return true;
                    }
                }
            }
        }
        Log.v(LOG_TAG, "La conexión a Internet no está disponible");
        Toast.makeText(context, R.string.noNet, Toast.LENGTH_SHORT).show();
        isConnected = false;
        return false;
    }
}
