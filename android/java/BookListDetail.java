package gisllps.healthystartkids.healthystart.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

 import com.orthotain.healthystart.R;

/**
 * Created by gisllp on 9/27/2016.
 */

public class BookListDetail extends Activity  {
	
     private ArrayList<String> ImagesArray = new ArrayList<String>();
     


    RelativeLayout verify_member_Layout,digital_contact_layout,edirectory_layout,share_this_Layout,donate_Layout;

    TextView verified_members_TextView,digital_directory_TextView,directory_TextView,share_this_TextView,
    		donate_TextView,title_TextView;

    ImageView verified_ImageView,digital_ImageView,directory_ImageView,share_ImageView,donate_ImageView; 
    
    String add_url="http://www.sikhwalmatrimonial.com",add_s_url_String="http://gisllp.com",add_main_url_String,
    		advertise_url_String="http://sikhwalmatrimonial.com/mob-advertise/m/",type_success_String;
    
    StringBuilder images_StringBuilder,url_StringBuilder;
    
    private static String[] IMAGES_Strings= {}, URL_STR_STRINGS_Strings={};
	
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGE = 0;

	WebView web;
	ProgressBar progressBar;


	ImageButton close_ImageButton;
	String url;
	Intent edirectory_getIntent;

	//TextView heading_web;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		web = (WebView)findViewById(R.id.webview01);
		close_ImageButton=(ImageButton)findViewById(R.id.web_back);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);



		edirectory_getIntent=getIntent();
		progressBar.setVisibility(View.GONE);

		//web.setWebViewClient(new myWebClient());

		web.getSettings().setJavaScriptEnabled(true);

		String url=edirectory_getIntent.getStringExtra("url");

		web.loadUrl("https://docs.google.com/viewer?url="+url);

		close_ImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}


	public class myWebClient extends WebViewClient
	{
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub


			view.loadUrl("https://docs.google.com/viewer?url="+url);

			return true;

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);

			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onPause() {
		System.gc();
		super.onPause();

	}

	@Override
	public void onDestroy() {
         System.gc();
		 super.onDestroy();
	}
}
