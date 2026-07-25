package com.bor.omapidiag;


import android.se.omapi.Reader;
import android.se.omapi.Session;
import android.se.omapi.Channel;

import java.nio.charset.StandardCharsets;



public class OmapiTester {


    public interface Logger {

        void log(String text);

    }



    private final Logger logger;



    public OmapiTester(Logger logger) {

        this.logger = logger;

    }



    private void log(String s) {

        logger.log(s);

    }



    public void testReader(Reader reader) {


        log("");
        log("======================");
        log("Reader test");
        log("======================");


        log(
                "Name: "
                +
                reader.getName()
        );



        try {


            boolean present =
                    reader.isSecureElementPresent();


            log(
                    "Present: "
                    +
                    present
            );


            if(!present) {

                log("Secure element absent");

                return;

            }


        }
        catch(Exception e) {


            log(
                "isSecureElementPresent error:"
            );

            log(
                android.util.Log.getStackTraceString(e)
            );


        }



        Session session = null;


        try {


            log("Opening session...");


            session =
                    reader.openSession();



            log(
                "Session opened OK"
            );



            testSession(session);



        }
        catch(Exception e) {


            log(
                "openSession FAILED"
            );


            log(
                android.util.Log.getStackTraceString(e)
            );


        }
        finally {


            if(session != null) {


                try {

                    session.close();

                    log(
                       "Session closed"
                    );

                }
                catch(Exception ignored){}


            }

        }


    }





    private void testSession(Session session) {


        log("");
        log("--- Session ---");



        try {


            byte[] atr =
                    session.getATR();


            if(atr != null) {


                log(
                    "ATR length: "
                    +
                    atr.length
                );


                log(
                    "ATR HEX: "
                    +
                    bytesToHex(atr)
                );


            }
            else {

                log("ATR unavailable");

            }


        }
        catch(Exception e) {


            log(
              "ATR error:"
            );


            log(
             android.util.Log.getStackTraceString(e)
            );

        }



        testLogicalChannel(session);


    }





    private void testLogicalChannel(Session session) {


        log("");
        log("--- Logical Channel ---");



        /*
         * Тестовый AID.
         *
         * Это не AID UNISIMKA.
         * Он нужен только для проверки
         * возможности открытия канала.
         *
         */


        byte[] aid =
                hexToBytes(
                "A000000151000000"
                );



        try {


            log(
              "Opening logical channel..."
            );



            Channel channel =
                    session.openLogicalChannel(
                            aid
                    );



            if(channel == null) {


                log(
                    "Channel = null"
                );


                return;

            }



            log(
                "Logical channel OPENED"
            );


            try {


                channel.close();


                log(
                    "Channel closed"
                );


            }
            catch(Exception e){}



        }
        catch(Exception e) {


            log(
              "Logical channel FAILED"
            );


            log(
              android.util.Log.getStackTraceString(e)
            );


        }


    }





    private static String bytesToHex(byte[] data) {


        StringBuilder sb =
                new StringBuilder();



        for(byte b:data) {


            sb.append(
                String.format(
                    "%02X",
                    b
                )
            );

        }


        return sb.toString();

    }




    private static byte[] hexToBytes(String s) {


        int len =
                s.length();


        byte[] result =
                new byte[len/2];



        for(int i=0;i<len;i+=2) {


            result[i/2] =
                    (byte)
                    Integer.parseInt(
                        s.substring(i,i+2),
                        16
                    );


        }


        return result;

    }



}
