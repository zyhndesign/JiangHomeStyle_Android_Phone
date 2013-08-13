package com.cidesign.jianghomestylephone.async;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.adapter.StoryViewpagerAdapter;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.tools.LoadingDataFromDB;
import com.cidesign.jianghomestylephone.tools.WidgetCache;

public class AsyncInitStoryData extends AsyncTask<Void, Void, List<ArticleEntity>>
{
	private Activity activity;
	private int screenWidth;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;
	// 物语内容面板控件
	private ViewPager storyViewPager;
	private StoryViewpagerAdapter storyViewpagerAdapter;
	private HashMap<Integer, View> widgetCache;

	public AsyncInitStoryData(Activity activity, DatabaseHelper dbHelper, int screenWidth)
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
		storyViewPager = (ViewPager) widgetCache.get(R.id.storyViewPager);
	}

	@Override
	protected List<ArticleEntity> doInBackground(Void... params)
	{
		return loadingDataFromDB.loadStoryArticle(dbHelper.getArticleListDataDao());
	}

	@Override
	protected void onPostExecute(List<ArticleEntity> list)
	{
		if (list != null && list.size() > 0)
		{
			storyViewpagerAdapter = new StoryViewpagerAdapter();
			storyViewpagerAdapter.setActivity(activity);
			storyViewpagerAdapter.setScreenWidth(screenWidth);
			storyViewpagerAdapter.getList().addAll(list);
			storyViewPager.setAdapter(storyViewpagerAdapter);
		}
	}
}
