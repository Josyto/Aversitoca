package aversitoca.aversitoca;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class RefreshService extends IntentService {
    static final String TAG = "RefreshService";
    static final int DELAY = 30000; //medio minuto
    private boolean runFlag = false;
    ArrayList<String> values = new ArrayList<String>();
    String premio;
    HttpURLConnection urlConnection;
    URL url;
    StringBuilder result = new StringBuilder();
    JSONObject jsonObject;
    JSONArray jsonArray;
    ContentValues contentValues;

    //Base de datos
    Database database;

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

        StatusProvider provider = new StatusProvider();
        Cursor cursor = provider.query(DatabaseForm.CONTENT_URI,null,null,null,null);


        try {
            while (runFlag) {
                if (cursor.getCount() > 0) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();
                        values.set(0,cursor.getString(0));
                        values.set(1,cursor.getString(1));
                        values.set(2,cursor.getString(2));
                        values.set(3,cursor.getString(3));

                        //SOLICITUD A API
                        url = new URL("http://api.elpais.com/ws/LoteriaNavidadPremiados?n="+values.get(1));
                        urlConnection = (HttpURLConnection) url.openConnection();
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        jsonObject = new JSONObject(line);

                        if(jsonObject.getString("error")=="0"){
                            premio = jsonObject.getString("premio");
                        }else premio = "0";

                        contentValues = new ContentValues();
                        contentValues.put("PREMIO",premio);
                        provider.update(DatabaseForm.CONTENT_URI,contentValues, "ID = ?", new String[]{values.get(0)});

                    }
                }

                Thread.sleep(DELAY);
            }
        }
        catch (InterruptedException e){
            runFlag = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.runFlag = false;
        Log.d(TAG, "onDestroyed");


    }

}
