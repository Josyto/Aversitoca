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
import java.net.URL;

public class RefreshService extends IntentService {
    private static final String TAG = "RefreshService";
    private static final String URLNAVIDAD = "https://api.elpais.com/ws/LoteriaNavidadPremiados?s=1";
    private static final String URLNINO = "https://api.elpais.com/ws/LoteriaNinoPremiados?s=1";
    private boolean runFlag = false;
    private StringBuilder result = new StringBuilder();

    // Se trata de un servicio asincrono para la peticion de los valores de los premios para
    // cada boleto

    public RefreshService() {
        super(TAG);
    }


    // Mostramos un mensaje de que se ha generado el servicio
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");
}

    // Se genera la parte de la logica para el servicio
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onStarted");
        this.runFlag = true;
        //Obtenemos preferencias
        SharedPreferences prefs = PreferenceManager. getDefaultSharedPreferences (this);
        int delayPrefs= prefs.getInt("tiempomin",1);
        //Minutos a milis
        int delaymilis= delayPrefs*60000;



        try {
            while (runFlag) {

                //COMPROBAMOS SI SE HA REALIZADO EL SORTEO DE NAVIDAD
                URL url = new URL(URLNAVIDAD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                line = result.toString();
                result = new StringBuilder();
                String[] split = line.split("=");
                line = split[1];
                JSONObject jsonObject = new JSONObject(line);
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

                //Si algún sorteo se está realizando o se ha realizado pero no se han obtenido
                // ningun resultado
                if(!estadosorteonavidad.equals("0")||!estadosorteonino.equals("0")) {

                    // SELECT
                    Cursor cursor = getContentResolver().query(DatabaseForm.CONTENT_URI, null, null, null, null);
                    if (cursor.getCount() > 0) {
                        for (int i = 0; i < cursor.getCount(); i++) {
                            cursor.moveToNext();
                            String id = cursor.getString(0);
                            String boleto = cursor.getString(1);
                            String sorteo = cursor.getString(3);
                            String comprobado = cursor.getString(4);


                            if(
                                     ( !estadosorteonavidad.equals("4")||!estadosorteonavidad.equals("4") ) ||
                                             ( comprobado.equals("0"))
                                    ) {
                                ContentValues contentValues;
                                String premio;
                                if (sorteo.equals("Loteria de Navidad")) {
                                    //SOLICITUD A API
                                    int cast = Integer.parseInt(boleto);
                                    url = new URL("https://api.elpais.com/ws/LoteriaNavidadPremiados?n=" + cast);
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

                                    // Comprobamos que el resultado de la consulta se OK
                                    if (jsonObject.getString("error").equals("0")) {
                                        premio = jsonObject.getString("premio");
                                    } else premio = "0";

                                    // Actualizamos la base de datos con el nuevo valor del premio
                                    // en que estado del sorteo nos encontramos y si se ha comprobado
                                    // median una consulta a la API
                                    contentValues = new ContentValues();
                                    contentValues.put("PREMIO", premio);
                                    contentValues.put("CELEBRADO", estadosorteonavidad);
                                    contentValues.put("COMPROBADO", "1");
                                    //UPDATE
                                    getContentResolver().update(DatabaseForm.CONTENT_URI, contentValues, DatabaseForm.Column.ID + " = ?", new String[]{id});
                                } else {
                                    //SOLICITUD A API
                                    int cast = Integer.parseInt(boleto);
                                    url = new URL("https://api.elpais.com/ws/LoteriaNinoPremiados?n=" + cast);
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

                                    // Comprobamos que el resultado de la consulta se OK
                                    if (jsonObject.getString("error").equals("0")) {
                                        premio = jsonObject.getString("premio");
                                    } else premio = "0";

                                    // Actualizamos la base de datos con el nuevo valor del premio
                                    // en que estado del sorteo nos encontramos y si se ha comprobado
                                    // median una consulta a la API
                                    contentValues = new ContentValues();
                                    contentValues.put("PREMIO", premio);
                                    contentValues.put("CELEBRADO", estadosorteonino);
                                    contentValues.put("COMPROBADO", "1");
                                    //UPDATE
                                    getContentResolver().update(DatabaseForm.CONTENT_URI, contentValues, DatabaseForm.Column.ID + " = ?", new String[]{id});
                                }
                            }


                        }
                    }
                    // Si el sorteo ha acabado y ya no se pueden consultar los resultados, reseteamos los flags para el siguiente sorteo
                    if(estadosorteonavidad.equals(0)|| estadosorteonino.equals(0)){
                        if (cursor.getCount() > 0) {
                            for (int i = 0; i < cursor.getCount(); i++) {
                                cursor.moveToNext();
                                String id = cursor.getString(0);
                                String sorteo = cursor.getString(3);
                                if (sorteo.equals("Loteria de Navidad") && estadosorteonavidad.equals(0)) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("CELEBRADO", "0");
                                    contentValues.put("COMPROBADO", "0");
                                    //UPDATE
                                    getContentResolver().update(DatabaseForm.CONTENT_URI, contentValues, DatabaseForm.Column.ID + " = ?", new String[]{id});
                                }
                                if (sorteo.equals("Loteria del Niño") && estadosorteonino.equals(0)) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("CELEBRADO", "0");
                                    contentValues.put("COMPROBADO", "0");
                                    //UPDATE
                                    getContentResolver().update(DatabaseForm.CONTENT_URI, contentValues, DatabaseForm.Column.ID + " = ?", new String[]{id});
                                }

                            }
                        }

                    }



                    cursor.close();
                }

                Thread.sleep(delaymilis);

            }
        }
        catch (InterruptedException e){
            runFlag = false;
        } catch (IOException | JSONException e) {
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
