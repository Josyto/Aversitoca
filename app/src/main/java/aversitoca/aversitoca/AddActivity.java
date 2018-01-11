package aversitoca.aversitoca;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.content.ContentValues;
import android.widget.Toast;


public class AddActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    CoordinatorLayout coordinatorLayout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_decimo);


        //Cambio titulo Appbar
        setTitle(getString(R.string.add_ticket));

        //Spinner seleccion sorteo
       Spinner spinner = (Spinner) findViewById(R.id.sorteos_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sorteos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        // La imagen del boleto

        FloatingActionButton photoButton = (FloatingActionButton) this.findViewById(R.id.button1);
        Button guardar = (Button) this.findViewById(R.id.btn_signup);

        // Guardamos los datos del usuario
        guardar.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                // La logica de jose
                    String codigo = ((EditText) findViewById(R.id.input_name)).getText().toString();
                    String sorteo = ((Spinner) findViewById(R.id.sorteos_spinner)).getSelectedItem().toString();
                    hideSoftKeyBoard();
                    if (codigo.length()!=5){
                        coordinatorLayout = findViewById(R.id.coordinator_layoutnuevo);
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "El boleto debe tener 5 cifras", Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();
                    }

                    else {
                        //Imprimimos las actualizaciones en el log
                        Log.d("insertBase", String.format("%s", codigo));

                        ContentValues values = new ContentValues();

                        //Insertamos los valores en la base de datos
                        values.clear();
                        values.put(DatabaseForm.Column.BOLETO, codigo);
                        values.put(DatabaseForm.Column.SORTEO, sorteo);
                        values.put(DatabaseForm.Column.PREMIO, 0);
                        getContentResolver().insert(DatabaseForm.CONTENT_URI, values);
                        finish();

                    }

            }
        });

                // Abrimos la camara
                photoButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });


    }


    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    @Override
    protected void onDestroy() {
        Main2Activity mActivity= new Main2Activity(); mActivity.consultarListaDecimos();

        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(imageBitmap);
        }
    }

}
