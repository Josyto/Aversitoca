package aversitoca.aversitoca;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;




public class Save {

    // Se encarga de guardar la imagen en la galeria del dispositivo

    private Context TheThis;

    public String SaveImage(Context context, Bitmap ImageToSave) {

        TheThis = context;
        String nameOfFolder = "/Nuevacarpeta";
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + nameOfFolder;
        String CurrentDateAndTime = getCurrentDateAndTime();
        File dir = new File(file_path);

        if (!dir.exists()) {
            dir.mkdirs();

        }

        String nameOfFile = "imagen";
        File file = new File(dir, nameOfFile + CurrentDateAndTime + ".jpg");

        try {
            FileOutputStream fOut = new FileOutputStream(file);

            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
        }

        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            Log.d("ERROR", e.toString());
            UnableToSave();
        }
        return file.getAbsolutePath();
    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        return df.format(c.getTime());
    }

    private void UnableToSave() {
        Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show();
    }


}
