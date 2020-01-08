package com.traore.abasse.tafsir.application.utility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class Utility {

    public static void log (String msg){

        Log.i("verifier",msg);
    }

    public static void message(Context context, String msg){

        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }


    public static String converted(int duration){

        int munite = (duration/1000)/60;
        int seconde = (duration/1000)%60;

        String c = String.format("%d:%02d",munite,seconde);
        return c;
    }
}
