package com.bor.omapidiag;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

import java.util.ArrayList;
import java.util.List;



public class ServiceScanner {


    private final Context context;


    public ServiceScanner(Context context) {

        this.context = context;

    }



    public String scan() {


        StringBuilder out =
                new StringBuilder();


        out.append(
                "=== SYSTEM SERVICE SCAN ===\n\n"
        );


        checkPackages(out);

        checkOmapiClasses(out);

        checkIntentServices(out);


        return out.toString();

    }





    private void checkPackages(
            StringBuilder out
    ) {


        out.append(
                "--- Packages ---\n"
        );


        String[] packages = {


                /*
                 * AOSP Secure Element service
                 */

                "com.android.se",


                /*
                 * Google eSIM
                 */

                "com.google.android.euicc",


                /*
                 * Xiaomi SIM related
                 */

                "com.miui.securitycenter",

                "com.android.phone"


        };



        PackageManager pm =
                context.getPackageManager();



        for(String p:packages) {


            try {


                PackageInfo info =
                        pm.getPackageInfo(
                                p,
                                0
                        );


                out.append(p)
                        .append(" FOUND ");

                out.append(
                        info.versionName
                );


                out.append("\n");


            }
            catch(Exception e) {


                out.append(p)
                        .append(" NOT FOUND\n");


            }


        }


        out.append("\n");


    }





    private void checkOmapiClasses(
            StringBuilder out
    ) {


        out.append(
                "--- Java OMAPI ---\n"
        );


        String[] classes = {


                "android.se.omapi.SEService",

                "android.se.omapi.Reader",

                "android.se.omapi.Session",

                "android.se.omapi.Channel"


        };



        for(String c:classes) {


            try {


                Class.forName(c);


                out.append(c)
                        .append(" OK\n");


            }
            catch(Exception e) {


                out.append(c)
                        .append(" FAIL\n");


            }


        }


        out.append("\n");

    }







    private void checkIntentServices(
            StringBuilder out
    ) {


        out.append(
                "--- Exported Services ---\n"
        );


        PackageManager pm =
                context.getPackageManager();



        try {


            List<android.content.pm.ResolveInfo> list =
                    pm.queryIntentServices(
                            new android.content.Intent(
                                    "android.se.omapi.SERVICE"
                            ),
                            0
                    );



            out.append(
                    "OMAPI intent services: "
            );


            out.append(
                    list.size()
            );


            out.append("\n");



            for(
                android.content.pm.ResolveInfo r:list
            ) {


                ServiceInfo s =
                        r.serviceInfo;


                out.append(
                        s.packageName
                );


                out.append("/");


                out.append(
                        s.name
                );


                out.append("\n");


            }



        }
        catch(Exception e) {


            out.append(
                    "query failed\n"
            );


        }



    }


}
