package com.nikunj.messenger.utils;

import android.content.Context;
import android.widget.Toast;

public class Extn {
    public static void makeToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();

    }
}
