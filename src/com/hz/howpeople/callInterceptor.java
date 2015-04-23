package com.hz.howpeople;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by admin on 15-4-20.
 */
public class callInterceptor extends BroadcastReceiver {

    static String lastIncoming = "";
    NotificationManager nm;

    @Override
    public void onReceive(final Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String msg = "友评提示：（";

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            msg += incomingNumber + "):友评得分：8分";

            lastIncoming = incomingNumber;

            final Toast mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
            mToast.show();

        } else {
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            } else {
                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                    nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                    //设置不同Id，同一个App可以显示多次.
                    nm.cancel(1);


                    //防止不同手机拨号后清除和设置被OS优化 而不显示 notification.
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            Calendar calendar = Calendar.getInstance();
                            Notification notification = new Notification(R.drawable.ic, "友评：点此匿名评价益之", calendar.getTimeInMillis());

                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                                    new Intent(context, MyActivity.class), 0);

                            notification.setLatestEventInfo(context, "友评：点此匿名评价益之", lastIncoming, contentIntent);
                            notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

                            nm.notify(1, notification);
                        }
                    }, 1000);
                }
            }
        }
    }
}


