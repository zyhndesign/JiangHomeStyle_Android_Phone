package com.cidesign.jianghomestylephone.async;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.adapter.LandscapeViewpagerAdapter;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.tools.LoadingDataFromDB;
import com.cidesign.jianghomestylephone.tools.WidgetCache;

public class AsyncInitLandscapeData extends AsyncTask<String, Void, List<ArticleEntity>>
{
	private Activity activity;
	private int screenWidth;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;
	private ViewPager landscapeViewPager;
	private LandscapeViewpagerAdapter landscapeViewpagerAdapter;
	private HashMap<Integer, View> widgetCache;

	public AsyncInitLandscapeData(Activity activity, DatabaseHelper dbHelper,int screenWidth)
	{
		this.activity = activity;
		this.dbHelper = dbHelper;
		this.screenWidth = screenWidth;
	}
	
	@Override
	protected void onPreExecute()
	{
		loadingDataFromDB = new LoadingDataFromDB();
		widgetCache = WidgetCache.getInstance().getViewCache();
		landscapeViewPager = (ViewPager)widgetCache.get(R.id.landscapeViewPager);
	}
	
	@Override
	protected List<ArticleEntity> doInBackground(String... params)
	{
		return loadingDataFromDB.loadLandscapeArticle(dbHelper.getArticleListDataDao());
	}
	
	@Override
	protected void onPostExecute(List<ArticleEntity> list)
	{
		if (list != null && list.size() > 0)
		{
			landscapeViewpagerAdapter = new LandscapeViewpagerAdapter();
			landscapeViewpagerAdapter.setActivity(activity);
			landscapeViewpagerAdapter.setScreenWidth(screenWidth);
			landscapeViewpagerAdapter.getList().addAll(list);
			landscapeViewPager.setAdapter(landscapeViewpagerAdapter);
			
		}
	}
}
