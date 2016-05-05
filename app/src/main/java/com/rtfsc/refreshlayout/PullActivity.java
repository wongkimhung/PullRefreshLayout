package com.rtfsc.refreshlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rtfsc.refreshlayout.core.PullToRefreshView;

public class PullActivity extends AppCompatActivity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*PullToRefreshView pullToRefreshView = (PullToRefreshView) findViewById(R.id.lv);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
		);
		View headerView = LayoutInflater.from(this).inflate(R.layout.headview,null);
		pullToRefreshView.addHeaderView(headerView);
		*/

		WebView webView = (WebView) findViewById(R.id.webView);
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://www.baidu.com");
	}

}
