package com.hz.howpeople;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by admin on 15-4-20.
 */
public class callInterceptor extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        //开机.
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent i = new Intent("com.hz.howpeople.phoneService");
            i.setClass(context, phoneService.class);
            context.startService(i);
        }
    }

    public callInterceptor(){}
}


