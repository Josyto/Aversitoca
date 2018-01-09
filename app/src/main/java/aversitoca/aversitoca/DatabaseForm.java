package aversitoca.aversitoca;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by josyt on 09/01/2018.
 */

public class DatabaseForm {
    public static final String DB_NAME = "timeline_db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "status";
    public static final String DEFAULT_SORT = Column.ID + " DESC";

    //Constantes del content provider
    public static final String AUTHORITY = "aversitoca.aversitoca.StatusProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int STATUS_ITEM = 1;
    public static final int STATUS_DIR = 2;



    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String BOLETO = "boleto";
        public static final String PREMIO = "premio";
        public static final String SORTEO = "sorteo";
    }
}

