package aversitoca.aversitoca;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.Spinner;


public class AddActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_decimo);

        //Cambio titulo Appbar
        setTitle(R.string.add_ticket);

        //Spinner seleccion sorteo
       Spinner spinner = (Spinner) findViewById(R.id.sorteos_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sorteos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        // La imagen del boleto
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        FloatingActionButton photoButton = (FloatingActionButton) this.findViewById(R.id.button1);
        Button guardar = (Button) this.findViewById(R.id.btn_signup);

        // Guardamos los datos del usuario
        guardar.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                // La logica de jose
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

}
