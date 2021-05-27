/*
 * Copyright 2019 Smaato Inc.
 * Licensed under the Smaato SDK License Agreement
 * https://www.smaato.com/sdk-license-agreement/
 */

package com.smaato.demoapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.smaato.demoapp.R;
import com.smaato.demoapp.utils.Constants;
import com.smaato.soma.AdDimension;
import com.smaato.soma.AdDownloaderInterface;
import com.smaato.soma.AdListenerInterface;
import com.smaato.soma.BannerView;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.ReceivedBannerInterface;
import com.smaato.soma.debug.Debugger;

public class ProgrammaticPhoneBannerSample extends AppCompatActivity implements OnClickListener,AdListenerInterface {

	BannerView mBannerView;
	Button loadBanner;
	RelativeLayout relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_programmatic_phone_banner_sample);
		Debugger.setDebugMode(Debugger.Level_3);
		loadBanner = (Button) findViewById(R.id.load_ad );
		loadBanner.setOnClickListener(this);
		mBannerView = new BannerView((this));
		relativeLayout = (RelativeLayout) findViewById(R.id.relative);
		relativeLayout.addView(mBannerView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Constants.dpToPx(50) ));

		//mBannerView.getLayoutParams().height =  (int) (50 * Resources.getSystem().getDisplayMetrics().density);
		mBannerView.addAdListener(ProgrammaticPhoneBannerSample.this);
		mBannerView.getAdSettings().setAdDimension(AdDimension.DEFAULT);
		SharedPreferences prefs = ProgrammaticPhoneBannerSample.this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		mBannerView.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID, "0")));
		mBannerView.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID, "0")));

		mBannerView.setAutoReloadEnabled(prefs.getBoolean(Constants.COM_SMAATO_DEMOAPP + Constants.REFRESH_AD, false));
		mBannerView.setAutoReloadFrequency(prefs.getInt(Constants.COM_SMAATO_DEMOAPP + Constants.REFRESH_INTERVAL, 60));
		mBannerView.setLocationUpdateEnabled(prefs.getBoolean(Constants.COM_SMAATO_DEMOAPP + Constants.GPS, false));
		mBannerView.asyncLoadNewBanner();
		setTitleColor(Color.WHITE);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3498db")));
	}

	@Override
	public void onClick(View v) {
		if (v == loadBanner) {
			mBannerView.asyncLoadNewBanner();
		}
	}
	@Override
	public void onReceiveAd(AdDownloaderInterface sender,
							ReceivedBannerInterface receivedBanner) {
		if(receivedBanner.getErrorCode() != ErrorCode.NO_ERROR){
			Toast.makeText(getBaseContext(), receivedBanner.getErrorMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDestroy(){
		if(mBannerView!=null) {
			mBannerView.destroy();
		}
		super.onDestroy();
	}
}