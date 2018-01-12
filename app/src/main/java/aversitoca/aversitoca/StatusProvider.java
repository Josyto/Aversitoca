package aversitoca.aversitoca;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;



public class StatusProvider extends ContentProvider {

    private static final String TAG = StatusProvider.class.getSimpleName();
    private Database database;
    private static final UriMatcher sURIMatcher;


    static{
        sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sURIMatcher.addURI(DatabaseForm.AUTHORITY, DatabaseForm.TABLE, DatabaseForm.STATUS_DIR);
        sURIMatcher.addURI(DatabaseForm.AUTHORITY, DatabaseForm.TABLE + "/#", DatabaseForm.STATUS_ITEM);
    }

    @Override
    public boolean onCreate() {
        database = new Database(getContext());
        Log.d(TAG, "onCreated");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        String where;

        switch(sURIMatcher.match(uri)){
            case DatabaseForm.STATUS_DIR:
                where = s;
                break;
            case DatabaseForm.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = DatabaseForm.Column.ID+"="+id+(TextUtils.isEmpty(s)?"" : " and ( " + s + " )");
                break;
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }

        String orderBy = (TextUtils.isEmpty(s1)) ? DatabaseForm.DEFAULT_SORT : s1;
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query(DatabaseForm.TABLE, strings, where, strings1, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        Log.d(TAG, "registros recuperados: " + cursor.getCount());
        return cursor;
    }






    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(sURIMatcher.match(uri)){
            case DatabaseForm.STATUS_DIR:
                Log.d(TAG, "gotType:vnd.android.cursor.dir/vnd.aversitoca.aversitoca.provider.status");
                return "vnd.android.cursor.dir/vnd.aversitoca.aversitoca.provider.status";
            case DatabaseForm.STATUS_ITEM:
                Log.d(TAG, "gotType:vnd.android.cursor.item/vnd.aversitoca.aversitoca.provider.status");
                return "vnd.android.cursor.item/vnd.aversitoca.aversitoca.provider.status";
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }
    }







    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri ret = null;
        SQLiteDatabase db = database.getWritableDatabase();
        long rowId = db.insertWithOnConflict(DatabaseForm.TABLE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        //Nos aseguramos de que la URI es correcta
        if (sURIMatcher.match(uri) != DatabaseForm.STATUS_DIR){
            throw new IllegalArgumentException("uri incorrecta" + uri);
        }

        if (rowId != -1){
            //long id = contentValues.getAsLong(DatabaseForm.Column.ID);
            //ret = ContentUris.withAppendedId(uri, id);
            Log.d(TAG, "uri insertada: " + ret);

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ret;
    }




    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        String where;

        switch(sURIMatcher.match(uri)){
            case DatabaseForm.STATUS_DIR:
                where = s;
                break;
            case DatabaseForm.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = DatabaseForm.Column.ID+"="+id+(TextUtils.isEmpty(s)?"" : " and ( " + s + " )");
                break;
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        int ret = db.delete(DatabaseForm.TABLE, where, strings);
        if (ret > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d(TAG, "registros borrados: " + ret);
        return ret;


    }





    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        String where;

        switch(sURIMatcher.match(uri)){
            case DatabaseForm.STATUS_DIR:
                where = s;
                break;
            case DatabaseForm.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = DatabaseForm.Column.ID+"="+id+(TextUtils.isEmpty(s)?"" : " and ( " + s + " )");
                break;
            default:
                throw new IllegalArgumentException("uri incorrecta: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        int ret = db.update(DatabaseForm.TABLE, contentValues, where, strings);
        if (ret > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d(TAG, "registros actualizados: " + ret);
        return ret;

    }
}
