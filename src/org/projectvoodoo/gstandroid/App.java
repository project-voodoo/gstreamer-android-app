
package org.projectvoodoo.gstandroid;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

public class App extends Application {

    public static final String BIN_PATH = "bin";
    public static final String BINARIES_KEY = "bin_checksums";

    public static Context context;

    private static final boolean STRICT_MODE = false;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        if (STRICT_MODE) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyDeath()
                            .penaltyLog()
                            .build());

            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .penaltyDeath()
                            .build());
        } else
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .permitAll()
                            .build());
    }
}
