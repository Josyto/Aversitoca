package aversitoca.aversitoca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Database extends SQLiteOpenHelper {
    private static final String TAG = Database.class.getSimpleName();

    public Database(Context context){
        super(context, DatabaseForm.DB_NAME, null, DatabaseForm.DB_VERSION);

    }

    // Llamado para crear la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s integer primary key autoincrement, %s text, %s integer, %s text, %s integer, %s integer, %s text,CONSTRAINT boleto_unico UNIQUE (%s,%s))",DatabaseForm.TABLE,
                DatabaseForm.Column.ID, DatabaseForm.Column.BOLETO, DatabaseForm.Column.PREMIO, DatabaseForm.Column.SORTEO,DatabaseForm.Column.COMPROBADO, DatabaseForm.Column.CELEBRADO, DatabaseForm.Column.FOTO , DatabaseForm.Column.BOLETO, DatabaseForm.Column.SORTEO);
        Log.d(TAG, "onCreate con SQL: "+ sql);
        db.execSQL(sql);
    }

    // Llamado para actualizar a nueva versión
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DatabaseForm.TABLE);

        onCreate(db);
        Log.d(TAG, "onUpgrade");

    }
}
