package com.hz.howpeople;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by admin on 15-4-22.
 */
public class phoneService extends Service {
    private Intent mIntent;
    private Notification mNotification;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart(Intent intent, int startid) {

        mIntent = intent;

        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new TeleListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    class TeleListener extends PhoneStateListener {


        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);


            if (state == TelephonyManager.CALL_STATE_RINGING) {
                final Toast mToast = Toast.makeText(getApplicationContext(), "友评提示 :(" + incomingNumber + ")友评得分 8.0", Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
                mToast.show();
            }
            else{
                if (state == TelephonyManager.CALL_STATE_RINGING)
                {
                    // DO SOME CODE HERE...
                }
                else{
                    if (state == TelephonyManager.CALL_STATE_IDLE)
                    {

                        Context context = getApplicationContext();
                        CharSequence title = "Hello";
                        CharSequence message = "打鸡鸡";
                        NotificationManager notificationManager;
                        notificationManager = (NotificationManager)context
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification;
                        notification = new Notification(
                                R.drawable.ic, "Notifiy.. ",
                                System.currentTimeMillis());
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                                null, 0);
                        notification.setLatestEventInfo(context, title, message, pendingIntent);
                        notificationManager.notify(1010, notification);
                    }
                }
            }

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                default:
                    break;
            }

        }
    }

}
