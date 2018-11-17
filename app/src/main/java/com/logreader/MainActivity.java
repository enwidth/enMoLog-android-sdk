package com.logreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import logreader.com.estuate.RLLogReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        (findViewById(R.id.btn_generate_log)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RLLogReader.v("Milgya","04-24 10:38:44.361 31930-544/com.logreader D/OpenGLRenderer: Use EGL_SWAP_BEHAVIOR_PRESERVED: true");
            }
        });
        /*String myStringArray= "adb logcat";
        try {
            Process process = Runtime.getRuntime().exec(myStringArray);
            InputStream inputStream = process.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            RLLogReader.logVerbose(total.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //readLogs();

    }

    private final String processId = Integer.toString(android.os.Process
            .myPid());

    public void readLogs() {
        try {
            String[] command = new String[] { "logcat", "-d", "threadtime"};
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(processId)) {
                    RLLogReader.v("Milgya",""+line);
                    break;
                }
            }
        } catch (IOException e) {
        }
    }
}
