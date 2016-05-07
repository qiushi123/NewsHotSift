package com.newshotsift;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class FragmentNews extends Fragment implements WaveSwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.main_swipe)
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    @InjectView(R.id.mProgress)
    ProgressBar mProgress;

    @InjectView(R.id.webview)
    WebView mWebView;

    private String mUrl;
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    public FragmentNews(String url) {
        super();
        mUrl = url;
    }

    public FragmentNews() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        ButterKnife.inject(this, view);

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);

        mWebView = (WebView) view.findViewById(R.id.webview);
        initweb();
        return view;
    }

    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(mUrl);
            }
        }, 2000);
    }


    @Override
    public void onRefresh() {
        refresh();
    }

    private void initweb() {
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.setWebViewClient(new InnerWebViewClient());
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDatabaseEnabled(true); //开启 database storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);// 开启 DOM storage API 功能

        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (!NetUtil.checkNet(getActivity())) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        } else {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        }

        String cacheDirPath = getActivity().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        //设置数据库缓存路径
        mWebView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgress.setVisibility(View.GONE);
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                } else {
                    if (View.GONE == mProgress.getVisibility()) {
                        mProgress.setVisibility(View.VISIBLE);
                    }
                    mProgress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

                Toast.makeText(getActivity().getApplicationContext(), message, Toast
                        .LENGTH_SHORT).show();

                result.confirm();

                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {


                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {


                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });

        mWebView.loadUrl(mUrl);
    }

    private class InnerWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (NetUtil.isHuxiuArticleUrl(uri)) {
                view.loadUrl(url);
            } else {
                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                intent.putExtra("load_url", url);
                startActivity(intent);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            getActivity().deleteDatabase("webview.db");
            getActivity().deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getActivity().getFilesDir().getAbsolutePath() +
                APP_CACAHE_DIRNAME);

        File webviewCacheDir = new File(getActivity().getCacheDir().getAbsolutePath() + "/webviewCache");

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

    @Override
    public void onDestroy() {
        //        clearWebViewCache();
        super.onDestroy();
    }
}
