package com.cidesign.jianghomestylephone.async;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.adapter.HumanityViewpagerAdapter;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.tools.CategoryDataLoadingLogic;
import com.cidesign.jianghomestylephone.tools.LoadingDataFromDB;
import com.cidesign.jianghomestylephone.tools.WidgetCache;

public class AsyncInitHumanityData extends AsyncTask<String, Void, List<ArticleEntity>>
{
	private Activity activity;
	private int screenWidth;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;

	private HashMap<Integer, View> widgetCache;
	// ÈËÎÄ
	private ViewPager humanityViewPager;
	private HumanityViewpagerAdapter humanityViewpagerAdapter;

	public AsyncInitHumanityData(Activity activity, DatabaseHelper dbHelper, int screenWidth)
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
		humanityViewPager = (ViewPager)widgetCache.get(R.id.humanityViewPager);
	}

	@Override
	protected List<ArticleEntity> doInBackground(String... params)
	{
		return loadingDataFromDB.loadHumanityArticle(dbHelper.getArticleListDataDao());
	}

	@Override
	protected void onPostExecute(List<ArticleEntity> list)
	{
		if (list != null && list.size() > 0)
		{
			humanityViewpagerAdapter = new HumanityViewpagerAdapter();
			humanityViewpagerAdapter.setActivity(activity);
			humanityViewpagerAdapter.setScreenWidth(screenWidth);
			humanityViewpagerAdapter.getList().addAll(list);
			humanityViewPager.setAdapter(humanityViewpagerAdapter);
		}
	}
}
