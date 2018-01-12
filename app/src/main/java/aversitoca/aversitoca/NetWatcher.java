package aversitoca.aversitoca;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;



public class NetWatcher extends BroadcastReceiver {

    // Nos avisa si existe o no conexion

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
        isNetworkAvailable(context);
        if (info != null) {
            if (info.isConnected()) {
                //iniciamos el servicio
                intent = new Intent(context, RefreshService.class);
                context.startService(intent);
            }
            else {
                //paramos el servicio
                intent = new Intent(context, RefreshService.class);
                context.stopService(intent);
            }
        }
    }

    private void isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        Network[] networks = connectivityManager.getAllNetworks();
        NetworkInfo networkInfo;
        for (Network mNetwork : networks) {
            networkInfo = connectivityManager.getNetworkInfo(mNetwork);
            if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                return;
            }
        }
        Toast.makeText(context,context.getString(R.string.connect),Toast.LENGTH_SHORT).show();
    }
}
