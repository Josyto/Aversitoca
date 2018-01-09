package aversitoca.aversitoca;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Marcos Muñoz on 09/01/2018.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comprobar que no se ha creado la actividad antes
        if (savedInstanceState == null) {
            // Crear el fragment
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id. content , fragment,
                            fragment.getClass().getSimpleName())
                    .commit();
        }
    }
}