package aversitoca.aversitoca;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
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


public class RefreshService extends IntentService {
    static final String TAG = "RefreshService";
    static final int DELAY = 30000; //medio minuto
    private boolean runFlag = false;
    String id,boleto,premio,line;
    HttpURLConnection urlConnection;
    URL url;
    StringBuilder result = new StringBuilder();
    JSONObject jsonObject;
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


        //SELECT
        Cursor cursor = getContentResolver().query(DatabaseForm.CONTENT_URI,null,null,null,null);


        try {
            while (runFlag) {
                if (cursor.getCount() > 0) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();

                        id = cursor.getString(0);
                        boleto = cursor.getString(1);

                        //SOLICITUD A API
                        url = new URL("https://api.elpais.com/ws/LoteriaNavidadPremiados?n="+boleto);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));


                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        line = result.toString();
                        result = new StringBuilder();
                        String[] split = line.split("=");
                        line = split[1];
                        jsonObject = new JSONObject(line);


                        if(jsonObject.getString("error").equals("0")){
                            premio = jsonObject.getString("premio");
                        }else premio = "0";

                        contentValues = new ContentValues();
                        contentValues.put("PREMIO",premio);
                        //UPDATE
                        getContentResolver().update(DatabaseForm.CONTENT_URI,contentValues, DatabaseForm.Column.BOLETO + " = ?", new String[]{boleto});

                    }
                }

                Thread.sleep(DELAY);
                cursor = getContentResolver().query(DatabaseForm.CONTENT_URI,null,null,null,null);
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
