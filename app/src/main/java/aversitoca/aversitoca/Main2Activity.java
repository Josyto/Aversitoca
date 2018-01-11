package aversitoca.aversitoca;

import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    ArrayList<Decimo> listDecimos;
    RecyclerView recyclerDecimos;
     CoordinatorLayout coordinatorLayout;
    private AdapterDecimos mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        listDecimos=new ArrayList<>();
        recyclerDecimos= (RecyclerView) findViewById(R.id.recyclerId);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDecimos.setLayoutManager(new LinearLayoutManager(this));
        recyclerDecimos.setLayoutManager(mLayoutManager);
        recyclerDecimos.setItemAnimator(new DefaultItemAnimator());
        recyclerDecimos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        consultarListaDecimos();


        AdapterDecimos adapter=new AdapterDecimos((listDecimos));
        recyclerDecimos.setAdapter(adapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

        //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerDecimos);



    }



    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdapterDecimos.ViewHolderDecimos) {
            // get the removed item name to display it in snack bar
            String name = listDecimos.get(viewHolder.getAdapterPosition()).getPremio();

            // backup of removed item for undo purpose
            final Decimo deletedItem = listDecimos.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }



    private void consultarListaDecimos() {
        Decimo decimo =null;

        Cursor cursor = getContentResolver().query(DatabaseForm.CONTENT_URI,null,null,null,null);

        while (cursor.moveToNext()){
            decimo = new Decimo();
            decimo.setId(cursor.getInt(0));
            decimo.setNumero(cursor.getString(1));
            decimo.setPremio(cursor.getString(2));
            decimo.setSorteo(cursor.getString(3));
            decimo.setFoto(cursor.getInt(0));

        listDecimos.add(decimo);

        }

    }
}
