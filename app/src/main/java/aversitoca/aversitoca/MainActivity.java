package aversitoca.aversitoca;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    List<Decimo> listDecimos;
    RecyclerView recyclerDecimos;
     CoordinatorLayout coordinatorLayout;
    private AdapterDecimos mAdapter;
    private SwipeRefreshLayout swipeContainer;

    // Cuando se inicia la aplicación, se genera el listado completo de los boletos (en caso de que no
    // aparezca ninguno, se indica al usuario que la lista se encuentra vacia).
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeContainer = findViewById(R.id.swipeContainer);


        recyclerDecimos = findViewById(R.id.recyclerId);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        listDecimos = new ArrayList<>();
        mAdapter = new AdapterDecimos(this, listDecimos);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDecimos.setLayoutManager(new LinearLayoutManager(this));
        recyclerDecimos.setLayoutManager(mLayoutManager);
        recyclerDecimos.setItemAnimator(new DefaultItemAnimator());
        recyclerDecimos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerDecimos.setAdapter(mAdapter);

        // Actualizamos la lista de boletos
        consultarListaDecimos();

        // Si arrastramos hacia la izquierda, eliminamos el elemento
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerDecimos);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                consultarListaDecimos();
            }

        });


        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);

    }

    // Cuando el usuario arrastra un elemento se le presentan la opcion de eliminar un boleto
    // Si este selecciona eliminar puede o bien restaurarlo deshaciendo la accion o bien esperar
    // a que se confirme el cambio
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdapterDecimos.ViewHolderDecimos) {

            String name = listDecimos.get(viewHolder.getAdapterPosition()).getNumero();

            // Hacemos una copia del elemento eliminado para restaurarlo
            final Decimo deletedItem = listDecimos.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            int boleto = deletedItem.getId();
            String boletoString = String.valueOf(boleto);
            getContentResolver().delete(DatabaseForm.CONTENT_URI,DatabaseForm.Column.ID + " = ?", new String[]{boletoString});

            // Eliminamos el elemento de la vista
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // Mostramos el snackbar con la opcion de deshacer los cambios
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + getString(R.string.Eliminado), Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Si seleccionamos deshacer entonces recuperamos el boleto y
                    // lo introducimos en la base de datos de nuevo
                    mAdapter.restoreItem(deletedItem, deletedIndex);

                    ContentValues values = new ContentValues();
                    values.clear();

                    values.put(DatabaseForm.Column.BOLETO, deletedItem.getNumero());
                    values.put(DatabaseForm.Column.SORTEO, deletedItem.getSorteo());
                    values.put(DatabaseForm.Column.PREMIO, deletedItem.getPremio());
                    values.put(DatabaseForm.Column.COMPROBADO, deletedItem.getComprobado());
                    values.put(DatabaseForm.Column.CELEBRADO, deletedItem.getCelebrado());
                    getContentResolver().insert(DatabaseForm.CONTENT_URI, values);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

    // Se encarga de evaluar si ha habido alguna actualizacion para el premio de los boletos
    // y agregar dichos cambios a la vista
    protected void consultarListaDecimos() {
        Decimo decimo;
        listDecimos.clear();
        stopService(new Intent(this, RefreshService.class));
        startService(new Intent(this, RefreshService.class));

        Cursor cursor = getContentResolver().query(DatabaseForm.CONTENT_URI,null,null,null,null);

        while (cursor.moveToNext()){
            decimo = new Decimo();
            decimo.setId(cursor.getInt(0));
            decimo.setNumero(cursor.getString(1));
            decimo.setPremio(cursor.getString(2));
            decimo.setSorteo(cursor.getString(3));
            decimo.setFoto(cursor.getString(6));
            decimo.setComprobado(cursor.getInt(4));
            decimo.setCelebrado(cursor.getInt(5));

            listDecimos.add(decimo);
        }

        swipeContainer.setRefreshing(false);
        mAdapter.notifyDataSetChanged();

    }

    // Si creamos o volvemos del menu de para insertar un nuevo boleto actualizamos la vista
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5) {
            consultarListaDecimos();
        }
    }

    // Mostramos la lista de opciones de la aplicacion
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Mostramos las distintas opciones para el menu
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.refresh:
                stopService(new Intent(this, RefreshService.class));
                startService(new Intent(this, RefreshService.class));
                return true;
            default:
                return false;
        }
    }

    // Mostramos la vista para introducir un nuevo boleto
    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this,AddActivity.class);
        startActivityForResult(intent,5);
    }
}
