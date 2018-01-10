package aversitoca.aversitoca;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    ArrayList<Decimo> listDecimos;
    RecyclerView recyclerDecimos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        listDecimos=new ArrayList<>();
        recyclerDecimos= (RecyclerView) findViewById(R.id.recyclerId);
        recyclerDecimos.setLayoutManager(new LinearLayoutManager(this));

        consultarListaDecimos();


        AdapterDecimos adapter=new AdapterDecimos((listDecimos));
        recyclerDecimos.setAdapter(adapter);


    }

    private void consultarListaDecimos() {
        Decimo decimo =null;

        Cursor cursor = getContentResolver().query(DatabaseForm.CONTENT_URI,null,null,null,null);

        while (cursor.moveToNext()){
            decimo = new Decimo();
            decimo.setId(cursor.getInt(0));
            decimo.setNumero(cursor.getString(1));
            decimo.setPremio(cursor.getString(2));
            decimo.setPremio(cursor.getString(3));
            decimo.setFoto(cursor.getInt(0));

        listDecimos.add(decimo);

        }

    }
}
