package de.jr.simpledo;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class IOUtils {


    public static boolean writeFile(Context context, String fileName, String data) {
        return write(data, getFileOuSt(fileName, context));
    }

    public static String readFile(Context context, String fileName) {
        return read(getFileInSt(fileName, context));
    }

    public static boolean appendToFile(Context context, String fileName, String data) {
        return writeFile(context, fileName, read(getFileInSt(fileName, context)) + data);
    }


    private static OutputStream getFileOuSt(String fileName, Context context) {
        File file = new File(context.getFilesDir(), fileName);
        try {
            if (!file.exists())
                if (!file.createNewFile()) return null;
            return context.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (Exception ignore) {
        }
        return null;
    }

    private static boolean write(String data, OutputStream out) {
        if (out == null) return false;
        try {
            OutputStreamWriter osw = new OutputStreamWriter(out);
            osw.write(data);
            osw.close();
        } catch (IOException e) {
            Log.e("IOException", "File write failed: " + e);
        }
        return true;
    }


    private static InputStream getFileInSt(String fileName, Context context) {
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) return null;

        try {
            return new FileInputStream(file);
        } catch (Exception ignore) {
        }
        return null;
    }

    private static String read(InputStream is) {
        if (is == null) return null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            byte[] data = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);
            }

            is.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                return buffer.toString(StandardCharsets.UTF_8);
            else
                //noinspection CharsetObjectCanBeUsed
                return buffer.toString("UTF-8");

        } catch (IOException e) {
            Log.e("IOException", "File load failed: " + e);
            return null;
        }
    }
}
