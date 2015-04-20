package com.hz.howpeople;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by admin on 15-4-20.
 */
public class callService extends Service {

    private Intent mIntent;

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


            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    final Toast outT = Toast.makeText(getBaseContext(), "友评提示：此人（" +  mIntent.getStringExtra(Intent.EXTRA_PHONE_NUMBER) + "）友评得分：7分", Toast.LENGTH_SHORT);
                    outT.show();
                    outT.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);

                    new CountDownTimer(11000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            outT.show();
                        }

                        public void onFinish() {
                            outT.show();
                        }

                    }.start();

                    break;
                case TelephonyManager.CALL_STATE_RINGING:

                    final Toast inT = Toast.makeText(getBaseContext(), "友评提示：此人（" + incomingNumber + "）友评得分：7分", Toast.LENGTH_SHORT);

                    inT.show();
                    inT.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);

                    new CountDownTimer(11000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            inT.show();
                        }

                        public void onFinish() {
                            inT.show();
                        }

                    }.start();
                    break;
                default:
                    break;
            }
        }

    }
}
