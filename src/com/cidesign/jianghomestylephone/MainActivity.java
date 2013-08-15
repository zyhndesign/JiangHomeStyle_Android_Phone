package com.cidesign.jianghomestylephone;

import com.androidquery.AQuery;
import com.cidesign.jianghomestylephone.async.AsyncDownTask;
import com.cidesign.jianghomestylephone.async.AsyncInitData;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ContentEntity;
import com.cidesign.jianghomestylephone.http.ArticalOperation;
import com.cidesign.jianghomestylephone.tools.StorageUtils;
import com.cidesign.jianghomestylephone.tools.WidgetCache;
import com.cidesign.jianghomestylephone.widget.CustomScrollView;
import com.cidesign.jianghomestylephone.widget.PopMenu;
import com.google.analytics.tracking.android.EasyTracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends JiangActivity
{
	private static final String TAG = MainActivity.class.getSimpleName();

	private AQuery aq;
	private DatabaseHelper dbHelper;
	private LayoutInflater inflater = null;

	private ArticalOperation aOper = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		EasyTracker.getInstance().setContext(this);

		aq = new AQuery(this);
		dbHelper = new DatabaseHelper(this);

		inflater = LayoutInflater.from(this);

		WidgetCache.getInstance().init(this);

		ImageView logoBtnClick = (ImageView) findViewById(R.id.logoBtnClick);
		logoBtnClick.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				((CustomScrollView) WidgetCache.getInstance().getViewCache().get(R.id.mainScrollView)).smoothScrollTo(0, 0);
			}
		});

		new AsyncInitData(this, dbHelper, inflater, screenWidth).execute();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		// 检测文章是否有更新，存在更新则进行下载更新
		aOper = new ArticalOperation(this, aq, dbHelper);
		aOper.getArticleInfo();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	public void PopMenuClick(View target)
	{
		PopMenu.initPopuWindow(getBaseContext(), this.findViewById(R.id.topBarLayout), R.layout.popmenu,screenWidth);
	}

	public void DetailArticleClick(View target)
	{
		if (target.getTag() != null && (target.getTag() instanceof ContentEntity))
		{
			ContentEntity aEntity = (ContentEntity) target.getTag();
			if(aEntity.getDownloadFlag() == 1)
			{
				openDetailContent(aEntity);
			}
			else
			{
				new AsyncDownTask(this,aEntity).execute();
			}
		}
	}
	
	private void openDetailContent(ContentEntity aEntity)
	{
		String url = "file://" + StorageUtils.FILE_ROOT + aEntity.getServerID() + "/doc/main.phone.html";
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", aEntity.getTitle());
		this.startActivity(intent);
	}
	
}
