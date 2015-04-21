package com.hz.howpeople;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;


/**
 * Created by admin on 15-4-20.
 */
public class callInterceptor extends BroadcastReceiver {

    Timer mTimer = new Timer();

    /**
     * 此方法利用TimerTask在Toast显示一秒后再显示一次。
     */
    private void execToast(final Toast toast) {

//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                //调主线程方法，否则可能会显示不出来。
//                initToast(toast);
//            }
//
//        }, 1000);

        mTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                //调主线程方法，否则可能会显示不出来。
                initToast(toast);
            }

        }, 0, 1000);
    }

    private void initToast(Toast toast) {
        toast.show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String msg = "友评提示：（";

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            msg += incomingNumber + "):友评得分：8分";


            final Toast mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
            mToast.show();
            execToast(mToast);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTimer.cancel();
                }
            }, 5000);
        }



    }
}
