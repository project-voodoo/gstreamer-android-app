
package org.projectvoodoo.gstandroid;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Shellcmd {

    @SuppressWarnings("unused")
    private static final String TAG = "Gstreamer Android Shellcmd";

    public static ArrayList<String> gstInspect(String args) {
        return run(getGstEnv() + App.context.getFilesDir() + "/gst-inspect " + args);
    }

    public static ArrayList<String> run(String command) {
        return run(command, false);
    }

    public static ArrayList<String> run(String command, boolean withSu) {
        String[] commands = {
                command,
        };
        return run(commands, withSu);
    }

    public static ArrayList<String> run(String[] commands, boolean withSu) {
        String shell = withSu ? "su" : "/system/bin/sh";
        return run(shell, commands);
    }

    public static ArrayList<String> run(String shell, String[] commands) {
        ArrayList<String> output = new ArrayList<String>();

        try {
            Process process = Runtime.getRuntime().exec(shell);

            BufferedOutputStream shellInput =
                    new BufferedOutputStream(process.getOutputStream());
            BufferedReader shellOutput =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            for (String command : commands)
                shellInput.write((command + "\n").getBytes());

            shellInput.write("exit\n".getBytes());
            shellInput.flush();
            process.waitFor();

            String line;
            while ((line = shellOutput.readLine()) != null)
                output.add(line);

            shellInput.close();
            shellOutput.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static String getGstEnv() {
        String libPath = App.context.getFilesDir().getParent() + "/lib";

        String env = "GST_REGISTRY=" + App.context.getCacheDir() + "/registry.bin \\\n";
        env += "GST_PLUGIN_PATH=" + libPath + " \\\n";
        env += "LD_LIBRARY_PATH=" + libPath + " \\\n";

        return env;
    }
}
