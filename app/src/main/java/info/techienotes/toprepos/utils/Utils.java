package info.techienotes.toprepos.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;


/**
 * Created by bharatkulratan
 */

public class Utils {
    private final static String TAG = "Utils";

    // Hashtable for different type faces
    static final Hashtable<String, Typeface> typefaces = new Hashtable<>();


    // Fetches the typeface to set for the text views
    public static Typeface getTypeface(Context c, String name) {
        synchronized (typefaces) {
            if (!typefaces.containsKey(name)) {
                try {
                    InputStream inputStream = c.getAssets().open(name);
                    File file = createFileFromInputStream(inputStream, name);
                    if (file == null) {
                        return Typeface.DEFAULT;
                    }
                    Typeface t = Typeface.createFromFile(file);
                    typefaces.put(name, t);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    return Typeface.DEFAULT;
                }
            }
            return typefaces.get(name);
        }
    }

    // Creates the file from input stream
    public static File createFileFromInputStream(InputStream inputStream, String name) {

        OutputStream outputStream = null;

        try {
            File f = File.createTempFile("font", null);
            outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return f;
        } catch (Exception e) {
            // Logging exception
            Log.e(TAG, e.getMessage());
        }finally {
            try {
                if (outputStream != null) outputStream.close();
            }catch (IOException e){
                Log.e(TAG, e.getMessage());
            }

        }

        return null;
    }

    public static String getCreatedDateFromPeriod(String period) {
        switch (period) {
            case "today":
                return new DateTime().minusDays(1).toString(Constants.DATE_FORMAT);
            case "week":
                return new DateTime().minusDays(7).toString(Constants.DATE_FORMAT);
            case "month":
                return new DateTime().minusMonths(1).toString(Constants.DATE_FORMAT);
            case "year":
                return new DateTime().minusYears(1).toString(Constants.DATE_FORMAT);
            default:
                return "";
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context context, View v) {
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
