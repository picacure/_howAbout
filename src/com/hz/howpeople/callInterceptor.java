package com.hz.howpeople;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by admin on 15-4-20.
 */
public class callInterceptor extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String msg = "友评提示：（";

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state) || TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            msg += incomingNumber + "):友评得分：8分";


            final Toast tt = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            tt.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
            tt.show();

            new CountDownTimer(11000, 1000) {
                public void onTick(long millisUntilFinished) {
                    tt.show();
                }

                public void onFinish() {
                    tt.show();
                }

            }.start();
        }

    }
}
