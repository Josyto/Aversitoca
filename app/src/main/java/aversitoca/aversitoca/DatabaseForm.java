package aversitoca.aversitoca;

import android.net.Uri;
import android.provider.BaseColumns;



public class DatabaseForm {
    public static final String DB_NAME = "boletos_db";
    public static final int DB_VERSION = 4;
    public static final String TABLE = "boletos";
    public static final String DEFAULT_SORT = Column.ID + " DESC";

    //Constantes del content provider
    public static final String AUTHORITY = "aversitoca.aversitoca.StatusProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int STATUS_ITEM = 1;
    public static final int STATUS_DIR = 2;

    // La logica de la API para las consultas:
    // Valor 0 el sorteo no ha comenzado aún
    // Valor 1 ha empezado (la lista se carga poco a poco)
    // Valor 2 el sorteo ha terminado y la lista de premios es correcta (pero no oficial)
    // Valor 3 ha terminado y existe lista oficial en PDF
    // Valor 4 (lista definitiva de premios basada en oficial)

    // Si no se ha realizado una petición a la API o no se ha registrado, COMPROBADO vale 0

    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String BOLETO = "boleto";
        public static final String PREMIO = "premio";
        public static final String SORTEO = "sorteo";
        public static final String FOTO = "foto";

        public static final String COMPROBADO = "comprobado";

        public static final String CELEBRADO = "celebrado";
    }
}

