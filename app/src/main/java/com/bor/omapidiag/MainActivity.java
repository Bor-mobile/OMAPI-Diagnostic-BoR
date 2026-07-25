package com.bor.omapidiag;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.content.pm.PackageManager;
import android.widget.ScrollView;
import android.widget.TextView;

import android.se.omapi.SEService;

import java.util.concurrent.Executors;


public class MainActivity extends Activity {


    private TextView output;


    private void log(String s) {

        runOnUiThread(() -> {

            output.append(s + "\n");

        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        output = new TextView(this);

        output.setTextSize(14);

        ScrollView scroll = new ScrollView(this);

        scroll.addView(output);

        setContentView(scroll);



        log("=== OMAPI Diagnostic ===");
        log("");

        showDeviceInfo();

        checkFeatures();

        startOmapiTest();

    }



    private void showDeviceInfo() {


        log("=== DEVICE ===");

        log("Manufacturer: " 
                + Build.MANUFACTURER);


        log("Brand: "
                + Build.BRAND);


        log("Model: "
                + Build.MODEL);


        log("Device: "
                + Build.DEVICE);


        log("Android: "
                + Build.VERSION.RELEASE);


        log("SDK: "
                + Build.VERSION.SDK_INT);


        log("");

    }



    private void checkFeatures() {


        log("=== FEATURES ===");


        String[] features = {

                "android.hardware.se.omapi.uicc",

                "android.hardware.se.omapi.ese",

                "android.hardware.se.omapi.sd",

                "android.hardware.telephony.euicc",

                "android.hardware.nfc"

        };


        PackageManager pm = getPackageManager();


        for(String f : features) {


            boolean exists =
                    pm.hasSystemFeature(f);


            log(
                    f +
                    " : "
                    +
                    exists
            );

        }


        log("");

    }



    private void startOmapiTest() {


        log("=== OMAPI ===");


        try {


            new SEService(

                    this,

                    Executors.newSingleThreadExecutor(),


                    service -> {


                        log(
                            "SEService connected = "
                            +
                            service.isConnected()
                        );


                        testReaders(service);


                    }

            );


        }
        catch(Exception e) {


            log(
                "SEService error:"
            );


            log(
                android.util.Log.getStackTraceString(e)
            );

        }

    }



    private void testReaders(SEService service) {


        try {


            android.se.omapi.Reader[] readers =
                    service.getReaders();


            log(
                "Readers count = "
                +
                readers.length
            );


        OmapiTester tester =
                new OmapiTester(
                    this::log
                );
        
        
        for(
                android.se.omapi.Reader r :
                readers
        ) {
        
            tester.testReader(r);
        
        }

        }
        catch(Exception e) {


            log(
                "getReaders error:"
            );


            log(
              android.util.Log.getStackTraceString(e)
            );

        }


    }


}
