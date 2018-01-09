package aversitoca.aversitoca;


import android.app.ListFragment;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;



public class DecimosListFragment extends ListFragment {

    private SimpleCursorAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("Sin datos...");

    }

}
