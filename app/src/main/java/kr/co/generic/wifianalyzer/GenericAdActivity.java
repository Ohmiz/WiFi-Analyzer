package kr.co.generic.wifianalyzer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

public class GenericAdActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.generic_ad);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		WebView mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		// mWebView.getSettings().setBuiltInZoomControls(true);
		// mWebView.getSettings().setSupportZoom(true);
		// mWebView.getSettings().setSupportZoom(true);
		mWebView.setVerticalScrollbarOverlay(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		// mWebView.setInitialScale(1);
		// mWebView.setScaleX(getWallpaperDesiredMinimumWidth());
		// mWebView.setScaleY(getWallpaperDesiredMinimumHeight());

		//ad.jsp
		//ad_skt.jsp
		//ad_naver.jsp
		//ad_slideme.jsp
		mWebView.loadUrl("http://kdh1732.iisweb.co.kr/jsp/ad/ad.jsp");

	}

	@Override
	public void onStart() {
		super.onStart();
		// The rest of your onStart() code.
		//GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		//GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}

	public void ClickExit(View v) {
		setResult(202);
		finish();
	}

}
