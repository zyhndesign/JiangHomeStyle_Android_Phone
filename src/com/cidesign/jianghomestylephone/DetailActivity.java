package com.cidesign.jianghomestylephone;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailActivity extends Activity
{
	private String url;
	private String title;
	private ProgressBar webLoadingProgressBar;
	private WebView detail_webview;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.article_detail);
		EasyTracker.getInstance().setContext(this);

		url = this.getIntent().getExtras().getString("url");
		title = this.getIntent().getExtras().getString("title");

		TextView tv = (TextView) this.findViewById(R.id.title);
		tv.setText(title);

		detail_webview = (WebView) findViewById(R.id.detail_webview);

		detail_webview.setHorizontalScrollBarEnabled(true);

		detail_webview.setOnKeyListener(new OnKeyListener()
		{
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK)
				{
					if (detail_webview.canGoBack())
					{
						detail_webview.goBack();
					}
				}

				return false;
			}
		});

		webLoadingProgressBar = (ProgressBar) findViewById(R.id.webLoadingProgressBar);
		WebSettings webSettings = detail_webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		detail_webview.addJavascriptInterface(this, "OpenMedia");
		detail_webview.setWebChromeClient(m_chromeClient);
		detail_webview.setWebViewClient(new WebViewClient()
		{

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}
		});

		detail_webview.loadUrl(url);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	private WebChromeClient m_chromeClient = new WebChromeClient()
	{
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback)
		{
			// TODO Auto-generated method stub
		}

		public void onProgressChanged(WebView view, int progress)
		{
			// setTitle("“≥√Êº”‘ÿ÷–£¨«Î…‘∫Ú..." + progress + "%");
			// setProgress(progress * 100);

			if (progress == 100 && webLoadingProgressBar != null)
			{
				webLoadingProgressBar.setVisibility(View.INVISIBLE);
			}
		}
	};

	public void BackBtnClick(View target)
	{
		if (detail_webview != null && detail_webview.canGoBack())
		{
			detail_webview.goBack();
		}
		else
		{
			this.finish();
		}

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (detail_webview != null && detail_webview.canGoBack())
			{
				detail_webview.goBack();
			}
			else
			{
				this.finish();
			}		
		}
		return true;
	}
}
