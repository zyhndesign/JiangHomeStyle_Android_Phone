package com.cidesign.jianghomestylephone;

import java.io.IOException;

import com.androidquery.AQuery;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.http.ArticalOperation;
import com.cidesign.jianghomestylephone.tools.StorageUtils;
import com.cidesign.jianghomestylephone.version.VersionUpdate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends Activity
{
	private DatabaseHelper dbHelper;

	private AQuery aq;
	private ArticalOperation aOper = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.splash);
		dbHelper = new DatabaseHelper(this);
		aq = new AQuery(this);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		aOper = new ArticalOperation(this, aq, dbHelper);
		
		
		if (StorageUtils.isSDCardPresent())
		{
			try
			{
				StorageUtils.mkdir();

				aOper.getArticleInfo();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			//check the version
			VersionUpdate vUpdate = new VersionUpdate(this.getApplicationContext(), SplashActivity.this, aq);
			vUpdate.getServerVerCode();
		}
		else
		{
			Toast.makeText(this, "请插入SD卡，该应用需要SD卡保存数据！", Toast.LENGTH_LONG).show();
		}
	}
}
