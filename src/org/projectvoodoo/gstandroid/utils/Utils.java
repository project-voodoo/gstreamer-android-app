
package org.projectvoodoo.gstandroid.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.projectvoodoo.gstandroid.App;
import org.projectvoodoo.gstandroid.Shellcmd;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

public class Utils {

    private static final String TAG = "Gstreamer Android Utils";

    public static boolean installBinaries() {
        Context context = App.context;

        SharedPreferences prefs = context.getSharedPreferences(App.BIN_PATH, 0);

        try {
            String assetsCheckSums = new String(getAssetAsBuffer(App.BINARIES_KEY));

            File binDir = context.getDir("bin", 0);
            // copy new binaries
            if (!assetsCheckSums.equals(prefs.getString(App.BINARIES_KEY, ""))) {

                // Delete files
                for (File file : context.getFilesDir().listFiles())
                    file.delete();

                for (String fileName : context.getAssets().list(App.BIN_PATH)) {
                    byte[] buffer = getAssetAsBuffer(App.BIN_PATH + "/" + fileName);
                    FileOutputStream out = context.openFileOutput(fileName, 0);
                    out.write(buffer);
                    out.close();

                    String wrapperFileName = binDir + "/" + fileName;
                    FileOutputStream wrapper = new FileOutputStream(wrapperFileName);
                    wrapper.write(getWrapperString(fileName).getBytes());
                    wrapper.close();

                    Shellcmd.run("/system/bin/chmod 755 " +
                            context.getFilesDir() + "/" + fileName + " "
                            + wrapperFileName);

                    Log.i(TAG, "Installed binary: " + fileName);
                }

                // save binaries checksums
                prefs.edit()
                        .putString(App.BINARIES_KEY, assetsCheckSums)
                        .apply();
                
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    private static byte[] getAssetAsBuffer(String assetName) throws IOException {
        AssetManager assets = App.context.getAssets();
        InputStream assetIs = assets.open(assetName);
        byte[] buffer = new byte[assetIs.available()];
        assetIs.read(buffer);
        return buffer;
    }

    private static String getWrapperString(String fileName) {
        return Shellcmd.getGstEnv() +
                "run-as " + App.context.getPackageName() + " " +
                App.context.getFilesDir() + "/" + fileName + " $*\n";
    }

}
