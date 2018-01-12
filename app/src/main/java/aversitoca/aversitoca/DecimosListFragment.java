package aversitoca.aversitoca;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;


public class DecimosListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = DecimosListFragment.class.getSimpleName();
    private SimpleCursorAdapter mAdapter;

    private static final int LOADER_ID = 42;

    private static final String [] FROM = {DatabaseForm.Column.BOLETO, DatabaseForm.Column.PREMIO, DatabaseForm.Column.SORTEO};
    private static final int[] TO = {R.id.text_numero_boleto, R.id.first_price_text_view, R.id.nombre_sorteo};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("Sin datos...");
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_decimo2, null, FROM, TO, 0);
        //mAdapter.setViewBinder(new TimelineViewBinder());

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i != LOADER_ID){
            return null;
        }
        Log.d(TAG,"onCreateLoader");
        return new CursorLoader(getActivity(), DatabaseForm.CONTENT_URI, null, null, null, DatabaseForm.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        Log.d(TAG, "onLoadFinished with cursor: " + cursor.getCount());
        mAdapter.swapCursor(cursor);

    }

    /*@Override

    ImageView imgFavorite = (ImageView) this.getView().findViewById(R.id.secondary_action);
    imgFavorite.setClickable(true);
    imgFavorite.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            final String _id = v.getTag().toString();
            DBAdapter dba = new DBAdapter(mActivity);
            dba.open();
            dba.remove(_id);
            Log.i("TAAG", "removed: "+_id);
            dba.close();
        }
    };*/

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mAdapter.swapCursor(null);
    }

    /*class TimelineViewBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            //if (view.getId() != R.id.list_item_text_created_at) return false;

            long timestamp = cursor.getLong(columnIndex);
            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp);
            ((TextView)view).setText(relativeTime);
            return true;
        }
    }*/

}
