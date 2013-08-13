package com.cidesign.jianghomestylephone.async;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.adapter.CommunityViewpagerAdapter;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.tools.CategoryDataLoadingLogic;
import com.cidesign.jianghomestylephone.tools.LoadingDataFromDB;
import com.cidesign.jianghomestylephone.tools.WidgetCache;


public class AsyncInitCommunityData extends AsyncTask<String, Void, List<ArticleEntity>>
{
	private Activity activity;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;

	private HashMap<Integer, View> widgetCache;

	private ViewPager communityViewPager;
	private CommunityViewpagerAdapter communityViewpagerAdapter;
	private int screenWidth;
	
	public AsyncInitCommunityData(Activity activity, DatabaseHelper dbHelper,int screenWidth)
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
		communityViewPager = (ViewPager)widgetCache.get(R.id.communityViewPager);
	}
	
	@Override
	protected List<ArticleEntity> doInBackground(String... params)
	{
		return loadingDataFromDB.loadCommunityArticle(dbHelper.getArticleListDataDao());
	}
	
	@Override
	protected void onPostExecute(List<ArticleEntity> list)
	{
		if (list != null && list.size() > 0)
		{
			communityViewpagerAdapter = new CommunityViewpagerAdapter();
			communityViewpagerAdapter.setActivity(activity);
			communityViewpagerAdapter.setScreenWidth(screenWidth);
			communityViewpagerAdapter.getList().addAll(list);
			communityViewPager.setAdapter(communityViewpagerAdapter);
		}
	}
}
