package aversitoca.aversitoca;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
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
    SharedPreferences prefs = PreferenceManager. getDefaultSharedPreferences (this);
    int delayPrefs= prefs.getInt("tiempomin",DELAY);
    static final String TAG = "RefreshService";
    static final int DELAY = 30000; //medio minuto
    static final String URLNAVIDAD = "https://api.elpais.com/ws/LoteriaNavidadPremiados?s=1";
    static final String URLNINO = "https://api.elpais.com/ws/LoteriaNinoPremiados?s=1";
    private boolean runFlag = false;
    InputStream in;
    BufferedReader reader;
    String id,boleto,premio,line, sorteo;
    HttpURLConnection urlConnection;
    URL url;
    String[] split;
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



        try {
            while (runFlag) {

                //COMPROBAMOS SI SE HA REALIZADO EL SORTEO DE NAVIDAD
                url = new URL(URLNAVIDAD);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(in));

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                line = result.toString();
                result = new StringBuilder();
                split = line.split("=");
                line = split[1];
                jsonObject = new JSONObject(line);
                String estadosorteonavidad = jsonObject.getString("status");

                //COMPROBAMOS SI SE HA REALIZADO EL SORTEO DEL NIÑO
                url = new URL(URLNINO);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(in));

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                line = result.toString();
                result = new StringBuilder();
                split = line.split("=");
                line = split[1];
                jsonObject = new JSONObject(line);
                String estadosorteonino = jsonObject.getString("status");

                //Si algún sorteo se está realizando
                if(!estadosorteonavidad.equals("0")||!estadosorteonino.equals("0")) {

                    //SELECT
                    Cursor cursor = getContentResolver().query(DatabaseForm.CONTENT_URI, null, null, null, null);
                    if (cursor.getCount() > 0) {
                        for (int i = 0; i < cursor.getCount(); i++) {
                            cursor.moveToNext();
                            id = cursor.getString(0);
                            boleto = cursor.getString(1);
                            sorteo = cursor.getString(3);


                            if(sorteo.equals("Loteria de Navidad")) {
                                //SOLICITUD A API
                                url = new URL("https://api.elpais.com/ws/LoteriaNavidadPremiados?n=" + boleto);
                                urlConnection = (HttpURLConnection) url.openConnection();
                                in = new BufferedInputStream(urlConnection.getInputStream());
                                reader = new BufferedReader(new InputStreamReader(in));


                                while ((line = reader.readLine()) != null) {
                                    result.append(line);
                                }

                                line = result.toString();
                                result = new StringBuilder();
                                split = line.split("=");
                                line = split[1];
                                jsonObject = new JSONObject(line);


                                if (jsonObject.getString("error").equals("0")) {
                                    premio = jsonObject.getString("premio");
                                } else premio = "0";

                                contentValues = new ContentValues();
                                contentValues.put("PREMIO", premio);
                                contentValues.put("CELEBRADO", estadosorteonavidad);
                                //UPDATE
                                getContentResolver().update(DatabaseForm.CONTENT_URI, contentValues, DatabaseForm.Column.ID + " = ?", new String[]{id});
                            }
                            else{
                                //SOLICITUD A API
                                url = new URL("https://api.elpais.com/ws/LoteriaNinoPremiados?n=" + boleto);
                                urlConnection = (HttpURLConnection) url.openConnection();
                                in = new BufferedInputStream(urlConnection.getInputStream());
                                reader = new BufferedReader(new InputStreamReader(in));


                                while ((line = reader.readLine()) != null) {
                                    result.append(line);
                                }

                                line = result.toString();
                                result = new StringBuilder();
                                split = line.split("=");
                                line = split[1];
                                jsonObject = new JSONObject(line);


                                if (jsonObject.getString("error").equals("0")) {
                                    premio = jsonObject.getString("premio");
                                } else premio = "0";

                                contentValues = new ContentValues();
                                contentValues.put("PREMIO", premio);
                                contentValues.put("CELEBRADO", estadosorteonino);
                                //UPDATE
                                getContentResolver().update(DatabaseForm.CONTENT_URI, contentValues, DatabaseForm.Column.ID + " = ?", new String[]{id});
                            }



                        }
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
