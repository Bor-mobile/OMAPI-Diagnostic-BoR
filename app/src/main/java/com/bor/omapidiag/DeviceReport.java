package com.bor.omapidiag;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class DeviceReport {


    private final Context context;


    public DeviceReport(Context context) {

        this.context = context;

    }



    public String generate() {


        StringBuilder r =
                new StringBuilder();



        r.append("=== OMAPI Diagnostic Report ===\n\n");



        r.append("=== DEVICE ===\n");

        r.append("Manufacturer: ")
                .append(Build.MANUFACTURER)
                .append("\n");

        r.append("Brand: ")
                .append(Build.BRAND)
                .append("\n");

        r.append("Model: ")
                .append(Build.MODEL)
                .append("\n");

        r.append("Device: ")
                .append(Build.DEVICE)
                .append("\n");

        r.append("Board: ")
                .append(Build.BOARD)
                .append("\n");

        r.append("Android: ")
                .append(Build.VERSION.RELEASE)
                .append("\n");

        r.append("SDK: ")
                .append(Build.VERSION.SDK_INT)
                .append("\n\n");




        r.append("=== FEATURES ===\n");


        String[] features = {

                "android.hardware.se.omapi.uicc",

                "android.hardware.se.omapi.ese",

                "android.hardware.se.omapi.sd",

                "android.hardware.telephony.euicc",

                "android.hardware.nfc"

        };



        PackageManager pm =
                context.getPackageManager();



        for(String f:features) {


            r.append(f)
                    .append(" = ")
                    .append(
                        pm.hasSystemFeature(f)
                    )
                    .append("\n");


        }



        r.append("\n");



        r.append("=== PACKAGES ===\n");


        String[] packages = {

                "com.unisimka.android",

                "com.android.se",

                "com.google.android.euicc"

        };



        for(String p:packages) {


            boolean installed;


            try {

                pm.getPackageInfo(
                        p,
                        0
                );

                installed=true;

            }
            catch(Exception e){

                installed=false;

            }


            r.append(p)
                    .append(" = ")
                    .append(installed)
                    .append("\n");


        }



        return r.toString();


    }


}
