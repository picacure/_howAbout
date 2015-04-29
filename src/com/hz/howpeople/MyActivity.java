package com.hz.howpeople;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MyActivity extends Activity implements OnTaskCompleted{
    /**
     * Called when the activity is first created.
     */

    private LinearLayout mLL;
    private Button mSearch;
    private Button mMeBtn;
    private Button mRecBtn;
    private Button mAllBtn;
    private WebView mwv;
    private View.OnClickListener onClickListener;
    private EditText mInput;
    private TableLayout mTL;


    private ArrayList<MyContact> mAllContact;
    private ArrayList<MyContact> mRecContact;

    private OnTaskCompleted onTaskCompleted;


    private ProgressBar mProgressBar;
    private Context mContex;
    private final int MY_REQUEST_ID = -1;

    private enum WEBVIEW_TYPE {
        MINE_DETAIL,
        STRANGER_DETAIL,
        AGREE_DETAIL,
        RECENT_LIST,
        ALL_LIST
    }

    @Override
    public void OnTaskCompleted(CONTACT_TYPE contact_type,WebView mwv){
        if(contact_type == CONTACT_TYPE.ALL && mwv != null){
            mwv.loadUrl("file:///android_asset/www/allcontacts.html");
        }

        if(contact_type == CONTACT_TYPE.RECENT && mwv != null){
            mwv.loadUrl("file:///android_asset/www/recent.html");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        setUpView();

        onTaskCompleted = new MyActivity();
    }

    private void setUpView() {
        mContex = this;

        initClick();

        mLL = (LinearLayout) findViewById(R.id.index);
        mLL.setOnClickListener(onClickListener);

        mTL = (TableLayout) findViewById(R.id.mainTL);

        mInput = (EditText) findViewById(R.id.indexInput);

        mInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mTL.setPadding(0, 0, 0, 100);
                } else {
                    mTL.setPadding(0, 0, 0, 0);
                }
            }
        });

        mSearch = (Button) findViewById(R.id.indexBtn);
        mSearch.setOnClickListener(onClickListener);

        mMeBtn = (Button) findViewById(R.id.meBtn);
        mMeBtn.setOnClickListener(onClickListener);

        mRecBtn = (Button) findViewById(R.id.recentBtn);
        mRecBtn.setOnClickListener(onClickListener);

        mAllBtn = (Button) findViewById(R.id.allBtn);
        mAllBtn.setOnClickListener(onClickListener);


//        mwv= (MyWebView)findViewById(R.id.sourceWv);
//        mwv.enablecrossdomain41();
        mwv = (WebView) findViewById(R.id.sourceWv);

        WebSettings webSettings = mwv.getSettings();
        webSettings.setAppCacheEnabled(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true); //Maybe you don't need this rule
        webSettings.setAllowUniversalAccessFromFileURLs(true);


        //no cache.
        mwv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.clearCache(true);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return null;
            }
        });
        mwv.addJavascriptInterface(new Proxy(this), "Android");

        mProgressBar = (ProgressBar) findViewById(R.id.mProbar);

        Intent cs = new Intent(this, phoneService.class);
        startService(cs);
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

    private void showWebView(WEBVIEW_TYPE wt) {
        mwv.loadUrl("about:blank");

        hideImm();


        if (wt == WEBVIEW_TYPE.MINE_DETAIL) {
            mwv.loadUrl("file:///android_asset/www/me.html");
        }

        if (wt == WEBVIEW_TYPE.RECENT_LIST) {
            if(mRecContact == null){
                new ReadPhoneTask(CONTACT_TYPE.RECENT,onTaskCompleted).execute();
            }
            else{
                mwv.loadUrl("file:///android_asset/www/recent.html");
            }
        }

        if (wt == WEBVIEW_TYPE.STRANGER_DETAIL) {
            mwv.loadUrl("file:///android_asset/www/stranger.html");
        }

        if (wt == WEBVIEW_TYPE.AGREE_DETAIL) {
            mwv.loadUrl("file:///android_asset/www/agreement.html");
        }

        if (wt == WEBVIEW_TYPE.ALL_LIST) {
            if(mAllContact == null){
                new ReadPhoneTask(CONTACT_TYPE.ALL,onTaskCompleted).execute();
            }
            else{
                mwv.loadUrl("file:///android_asset/www/allcontacts.html");
            }
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mwv.setVisibility(View.VISIBLE);
                    }
                }, 500);
    }

    private void hideImm() {
        //隐藏输入框.
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);

    }

    private void initClick() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.indexBtn: {

                        hideImm();
                        showWebView(WEBVIEW_TYPE.STRANGER_DETAIL);

                        break;
                    }

                    case R.id.meBtn: {
                        hideImm();
                        showWebView(WEBVIEW_TYPE.MINE_DETAIL);

                        break;
                    }

                    case R.id.recentBtn: {
                        hideImm();
                        showWebView(WEBVIEW_TYPE.RECENT_LIST);

                        break;
                    }

                    case R.id.allBtn: {
                        hideImm();
                        showWebView(WEBVIEW_TYPE.ALL_LIST);

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

        private CONTACT_TYPE contactType;
        private OnTaskCompleted taskFinishListener;


        public ReadPhoneTask(CONTACT_TYPE contact_type,OnTaskCompleted onTaskCompleted) {
            contactType = contact_type;
            taskFinishListener = onTaskCompleted;
        }

        protected void onPreExecute() {

            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (contactType == CONTACT_TYPE.ALL || contactType == CONTACT_TYPE.RECENT) {
                if (mAllContact == null) mAllContact = new ArrayList<MyContact>();

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

                        mAllContact.add(new MyContact(name, phoneNum));

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
            }


            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            mProgressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Void voids) {
            taskFinishListener.OnTaskCompleted(contactType,mwv);
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
            return new Gson().toJson(mAllContact);
        }

        public String GetAllComments() throws IOException {

            String response = "";
            try{
                response = HttpRequest.get("http://192.168.0.103:3000/all").accept("application/json").body();
            }
            catch (Exception ex){
                Log.e("Error",ex.getMessage());
            }


            return response;
        }
    }
}

