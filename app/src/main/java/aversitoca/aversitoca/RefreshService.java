package aversitoca.aversitoca;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;


public class RefreshService extends IntentService {
    static final String TAG = "RefreshService";

    static final int DELAY = 30000; //medio minuto
    private boolean runFlag = false;

    //Base de datos
    Database database;
    SQLiteDatabase db;


    public RefreshService() {
        super(TAG);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");

        database = new Database(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onStarted");

        this.runFlag = true;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        while (runFlag) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.runFlag = false;
        Log.d(TAG, "onDestroyed");


    }

}
