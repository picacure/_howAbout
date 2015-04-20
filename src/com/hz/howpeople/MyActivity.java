package com.hz.howpeople;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private LinearLayout mLL;
    private Button mSearch;
    //    private MyWebViewFragment mWvf;
    private WebView mwv;
    private View.OnClickListener onClickListener;
    private ArrayList<MyContact> mContact;
    private EditText mInput;

    private ProgressBar mProgressBar;

    private final int MY_REQUEST_ID = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        initClick();

        mLL = (LinearLayout) findViewById(R.id.index);
        mLL.setOnClickListener(onClickListener);

        mInput = (EditText) findViewById(R.id.indexInput);

        mSearch = (Button) findViewById(R.id.indexBtn);
        mSearch.setOnClickListener(onClickListener);

        mwv = (WebView) findViewById(R.id.sourceWv);
        mwv.getSettings().setJavaScriptEnabled(true);
        //no cache.
        mwv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.clearCache(true);
            }
        });
        mwv.addJavascriptInterface(new Proxy(this), "Android");

        mProgressBar = (ProgressBar) findViewById(R.id.mProbar);

//        FragmentManager fragmentManager = getFragmentManager();
//        mWvf = (MyWebViewFragment)fragmentManager.findFragmentById(R.id.MWFragment);
//        mWvf.setContext(this);

//        Intent cs = new Intent(this, callService.class);
//        startService(cs);
    }

    private void loginCheck() {
        if (Math.random() > 0.5) {
            Intent reg = new Intent(this, registerActivity.class);
            startActivity(reg);
        } else {
            Intent log = new Intent(this, loginActivity.class);
            startActivity(log);
        }

    }

    private void showWebView(){
        //隐藏输入框.
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);


        if (mContact == null) {
            new ReadPhoneTask().execute();
        } else {
            String mi = mInput.getText().toString().trim().toUpperCase();

            if (mi.length() > 0) {

                if (mi.contains("W")) {
                    Log.e("mi","W");
                    mwv.loadUrl("file:///android_asset/www/me.html");
                } else if (mi.contains("S")) {
                    mwv.loadUrl("file:///android_asset/www/friend.html");
                } else if (mi.contains("M")) {
                    Log.e("mi","M");
                    mwv.loadUrl("file:///android_asset/www/stranger.html");
                } else if (mi.contains("Z")) {
                    mwv.loadUrl("file:///android_asset/www/me.html#recent");
                } else if (mi.contains("L")) {
                    loginCheck();
                }

            } else {
                mwv.loadUrl("file:///android_asset/www/contacts.html");
            }


            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mwv.setVisibility(View.VISIBLE);
                        }
                    }, 500);
        }
    }

    private void initClick() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.indexBtn: {
                        showWebView();

                        break;
                    }
                    default: {
                        mwv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        };
    }

    //activity callback.
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == MY_REQUEST_ID) {
            if (resultCode == RESULT_OK) {
                String myValue = data.getStringExtra("valueName");

            }
        }
    }


    private class ReadPhoneTask extends AsyncTask<Void, Integer, Void> {
        protected void onPreExecute(){
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mContact == null) mContact = new ArrayList<MyContact>();

            ContentResolver resolver = getContentResolver();
            Uri URI = ContactsContract.Contacts.CONTENT_URI;
            String[] columns = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.PhoneLookup.DISPLAY_NAME
            };


            //查询联系人ID和联系人名称两列
            Cursor cursor = resolver.query(
                    URI, columns,
                    ContactsContract.PhoneLookup.HAS_PHONE_NUMBER + "=1",
                    null, null);

            int num = 0;


            try {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(1);

                    String phoneNum = "";
                    Cursor cursor2 = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + cursor.getLong(0)
                            , null, null);

                    //一个联系人可能存在多个号码
                    while (cursor2.moveToNext()) {
                        phoneNum += cursor2.getString(0) + ",";
                    }

                    mContact.add(new MyContact(name, phoneNum));

                    //http://blog.csdn.net/a740169405/article/details/11848651
                    //avoid cursor leak
                    cursor2.close();

                    num++;

                    //更新进度条.
                    publishProgress((int) ((num / 50) * 100));

                    if (num > 50) break;
                }

                cursor.close();

            } catch (Exception ex) {
                Log.e("err", ex.getMessage());
            } finally {
                if (cursor != null)
                    cursor.close();  // RIGHT: ensure resource is always recovered
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            mProgressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Void voids) {
            showWebView();
        }
    }

    public class MyContact {
        public String name;
        public String phone;

        public MyContact(String n, String p) {
            name = n;
            phone = p;
        }
    }

    public class Proxy {

        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        Proxy(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        //@JavascriptInterface
        public String GetAllContacts() {
            return new Gson().toJson(mContact);
        }
    }
}
