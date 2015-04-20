package com.hz.howpeople;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.webkit.WebView;
import com.google.gson.Gson;

/**
 * Created by admin on 15-4-17.
 */
public class MyWebViewFragment extends Fragment {

    private WebView mSource;
    private WebView mBackup;
    private Context mCtx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container, Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.webview, container,false);

        mSource = (WebView) viewHierarchy.findViewById(R.id.sourceWv);
        mSource.getSettings().setJavaScriptEnabled(true);
        mSource.addJavascriptInterface(new Proxy(mCtx), "Android");

        mBackup = (WebView) viewHierarchy.findViewById(R.id.backupWv);
        mBackup.getSettings().setJavaScriptEnabled(true);
        mBackup.addJavascriptInterface(new Proxy(mCtx), "Android");

        return viewHierarchy;
    }

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void prepare(String url){
        if(mSource != null){
            mSource.loadUrl("file:///android_asset/www/detail.html");
        }
    }

    public void setContext(Context aCtx){
        mCtx = aCtx;
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
            return new Gson().toJson(null);
        }
    }
}
