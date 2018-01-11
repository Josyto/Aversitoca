package aversitoca.aversitoca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {







    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String url="http://api.elpais.com/ws/LoteriaNavidadPremiados?n=99999";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //arrancar servicio
        startService(new Intent(this, RefreshService.class));

        // Comprobar si la actividad ya ha sido creada con anterioridad
        if(savedInstanceState==null) {
            // Crear un fragment mediante FragmentManager() y lo engancha en el sitio especificado
            // del main

            DecimosListFragment decimos = new DecimosListFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment_timeline, decimos,
                    decimos.getClass().getSimpleName()).commit();
        }

        /*JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mTxtDisplay.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTextView.setText("That didn't work!");
                    }
                });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.addButton:
                startActivity(new Intent(this, AddActivity.class));
                return true;
            case R.id.lista2:
                startActivity(new Intent(this, Main2Activity.class));
                return true;
            default:
                return false;
        }
    }



}
