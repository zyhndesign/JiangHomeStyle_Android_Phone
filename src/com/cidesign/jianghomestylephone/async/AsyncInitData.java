package com.cidesign.jianghomestylephone.async;

import java.util.HashMap;
import java.util.List;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.adapter.CommunityViewpagerAdapter;
import com.cidesign.jianghomestylephone.adapter.HumanityViewpagerAdapter;
import com.cidesign.jianghomestylephone.adapter.LandscapeViewpagerAdapter;
import com.cidesign.jianghomestylephone.adapter.StoryViewpagerAdapter;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.tools.CategoryDataLoadingLogic;
import com.cidesign.jianghomestylephone.tools.LoadingDataFromDB;
import com.cidesign.jianghomestylephone.tools.LoadingImageTools;
import com.cidesign.jianghomestylephone.tools.StorageUtils;
import com.cidesign.jianghomestylephone.tools.TimeTools;
import com.cidesign.jianghomestylephone.tools.WidgetCache;
import com.cidesign.jianghomestylephone.widget.CommunityRelativeLayout;
import com.cidesign.jianghomestylephone.widget.HumanityRelativeLayout;
import com.cidesign.jianghomestylephone.widget.LandscapeRelativeLayout;
import com.cidesign.jianghomestylephone.widget.StoryRelativeLayout;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class AsyncInitData extends AsyncTask<Void, Void, Object[]>
{
	private Activity activity;
	private int screenWidth;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;
	private LayoutInflater inflater = null;

	private ProgressBar progressBar = null;

	private HashMap<Integer, View> widgetCache;

	// 风景内容面板控件
	private ImageButton landscapePreClick;
	private ImageButton landscapeNextClick;
	private ViewPager landscapeViewPager;
	private LandscapeViewpagerAdapter landscapeViewpagerAdapter;

	// 人文
	private ImageButton humanityPreClick;
	private ImageButton humanityNextClick;
	private ViewPager humanityViewPager;
	private HumanityViewpagerAdapter humanityViewpagerAdapter;

	// 物语内容面板控件
	private ImageButton storyPreClick;
	private ImageButton storyNextClick;
	private ViewPager storyViewPager;
	private StoryViewpagerAdapter storyViewpagerAdapter;

	// 小记内容面板控件
	private ImageButton communityPreClick;
	private ImageButton communityNextClick;
	private ViewPager communityViewPager;
	private CommunityViewpagerAdapter communityViewpagerAdapter;
	
	public AsyncInitData(Activity activity, DatabaseHelper dbHelper, LayoutInflater inflater, int screenWidth)
	{
		this.activity = activity;
		this.dbHelper = dbHelper;
		this.inflater = inflater;
		this.screenWidth = screenWidth;
	}

	@Override
	protected void onPreExecute()
	{
		loadingDataFromDB = new LoadingDataFromDB();
		widgetCache = WidgetCache.getInstance().getViewCache();
		
		landscapePreClick = (ImageButton)widgetCache.get(R.id.landscapePreClick);
		landscapeNextClick = (ImageButton)widgetCache.get(R.id.landscapeNextClick);
		LandscapeRelativeLayout landscapeRelativeLayout = (LandscapeRelativeLayout)widgetCache.get(R.id.landscapeRelativeLayout);
		landscapeRelativeLayout.setLandscapePreClick(landscapePreClick);
		landscapeRelativeLayout.setLandscapeNextClick(landscapeNextClick);
		landscapeViewPager = (ViewPager)widgetCache.get(R.id.landscapeViewPager);
		
		
		humanityPreClick = (ImageButton)widgetCache.get(R.id.humanityPreClick);
		humanityNextClick = (ImageButton)widgetCache.get(R.id.humanityNextClick);
		HumanityRelativeLayout humanityRelativeLayout = (HumanityRelativeLayout)widgetCache.get(R.id.humanityRelativeLayout);
		humanityRelativeLayout.setHumanityPreClick(humanityPreClick);
		humanityRelativeLayout.setHumanityNextClick(humanityNextClick);
		humanityViewPager = (ViewPager)widgetCache.get(R.id.humanityViewPager);
		
		
		storyPreClick = (ImageButton)widgetCache.get(R.id.storyPreClick);
		storyNextClick = (ImageButton)widgetCache.get(R.id.storyNextClick);
		StoryRelativeLayout storyRelativeLayout = (StoryRelativeLayout)widgetCache.get(R.id.storyRelativeLayout);
		storyRelativeLayout.setStoryPreClick(storyPreClick);
		storyRelativeLayout.setStoryNextClick(storyNextClick);
		storyViewPager = (ViewPager)widgetCache.get(R.id.storyViewPager);
				
		communityPreClick = (ImageButton)widgetCache.get(R.id.communityPreClick);
		communityNextClick = (ImageButton)widgetCache.get(R.id.communityNextClick);
		CommunityRelativeLayout communityRelativeLayout = (CommunityRelativeLayout)widgetCache.get(R.id.communityRelativeLayout);
		communityRelativeLayout.setCommunityPreClick(communityPreClick);
		communityRelativeLayout.setCommunityNextClick(communityNextClick);
		communityViewPager = (ViewPager)widgetCache.get(R.id.communityViewPager);
		
	}

	@Override
	protected Object[] doInBackground(Void... arg0)
	{
		Object[] objectArray = new Object[5];
		// 读取数据库加载数据
		List<ArticleEntity> topFourList = loadingDataFromDB.loadTopFourArticle(dbHelper.getArticleListDataDao());
		objectArray[0] = topFourList;

		// 初始化风景
		List<ArticleEntity> landscapeList = loadingDataFromDB.loadLandscapeArticle(dbHelper.getArticleListDataDao());
		objectArray[1] = landscapeList;

		// 初始化人文
		List<ArticleEntity> humanityList = loadingDataFromDB.loadHumanityArticle(dbHelper.getArticleListDataDao());
		objectArray[2] = humanityList;

		// 初始化物语
		List<ArticleEntity> storyList = loadingDataFromDB.loadStoryArticle(dbHelper.getArticleListDataDao());
		objectArray[3] = storyList;

		// 初始化社区
		List<ArticleEntity> communityList = loadingDataFromDB.loadCommunityArticle(dbHelper.getArticleListDataDao());
		objectArray[4] = communityList;
		return objectArray;
	}

	@Override
	protected void onPostExecute(Object[] result)
	{
		@SuppressWarnings("unchecked")
		List<ArticleEntity> topFourList = (List<ArticleEntity>) result[0];
		@SuppressWarnings("unchecked")
		List<ArticleEntity> landscapeList = (List<ArticleEntity>) result[1];
		@SuppressWarnings("unchecked")
		List<ArticleEntity> humanityList = (List<ArticleEntity>) result[2];
		@SuppressWarnings("unchecked")
		List<ArticleEntity> storyList = (List<ArticleEntity>) result[3];
		@SuppressWarnings("unchecked")
		List<ArticleEntity> communityList = (List<ArticleEntity>) result[4];

		if (topFourList.size() >= 1)
		{
			ArticleEntity aEntity = topFourList.get(0);

			LoadingImageTools loadingImg = new LoadingImageTools();

			ImageView homeBgImg = (ImageView) widgetCache.get(R.id.homeBigBg);
			final VideoView mVideoView = (VideoView) widgetCache.get(R.id.videoView);

			if (aEntity.getMax_bg_img() == null || aEntity.getMax_bg_img().equals(""))
			{
				homeBgImg.setVisibility(View.VISIBLE);
				loadingImg.loadingNativeImage(activity, homeBgImg, "bg1.jpg");
			}
			else if (aEntity.getMax_bg_img().endsWith(".mp4"))
			{
				if (mVideoView != null)
				{
					final String videoPath = "file://" + StorageUtils.FILE_ROOT + aEntity.getServerID() + "/"
							+ aEntity.getMax_bg_img();
					homeBgImg.setVisibility(View.GONE);
					mVideoView.setVisibility(View.VISIBLE);
					mVideoView.setVideoPath(videoPath);
					mVideoView.start();
					mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
					{

						@Override
						public void onPrepared(MediaPlayer mp)
						{
							mp.start();
							mp.setLooping(true);

						}
					});

					mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
					{

						@Override
						public void onCompletion(MediaPlayer mp)
						{
							mVideoView.setVideoPath(videoPath);
							mVideoView.start();

						}
					});

					mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener()
					{

						@Override
						public boolean onError(MediaPlayer mp, int what, int extra)
						{
							Toast.makeText(activity, "无法播放背景视频文件!", Toast.LENGTH_LONG).show();
							return true;
						}
					});
				}
			}
			else
			{
				homeBgImg.setVisibility(View.VISIBLE);
				loadingImg
						.loadingImage(homeBgImg, StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getMax_bg_img());
			}
			((TextView) widgetCache.get(R.id.homeArticleTitle)).setText(aEntity.getTitle());
			((TextView) widgetCache.get(R.id.homeArticleTime)).setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
			((LinearLayout) widgetCache.get(R.id.homeLinearLayout)).setTag(aEntity);
			if (topFourList.size() >= 2)
			{
				CategoryDataLoadingLogic.loadHeadLineData(topFourList, (LinearLayout) widgetCache.get(R.id.recommandLayout),
						screenWidth, inflater); // 初始化头条
			}
		}

		if (landscapeList != null && landscapeList.size() > 0)
		{
			landscapeViewpagerAdapter = new LandscapeViewpagerAdapter();
			landscapeViewpagerAdapter.setActivity(activity);
			landscapeViewpagerAdapter.setScreenWidth(screenWidth);
			landscapeViewpagerAdapter.getList().addAll(landscapeList);
			landscapeViewPager.setAdapter(landscapeViewpagerAdapter);
		}

		if (humanityList != null && humanityList.size() > 0)
		{
			humanityViewpagerAdapter = new HumanityViewpagerAdapter();
			humanityViewpagerAdapter.setActivity(activity);
			humanityViewpagerAdapter.setScreenWidth(screenWidth);
			humanityViewpagerAdapter.getList().addAll(humanityList);
			humanityViewPager.setAdapter(humanityViewpagerAdapter);
		}

		if (storyList != null && storyList.size() > 0)
		{
			storyViewpagerAdapter = new StoryViewpagerAdapter();
			storyViewpagerAdapter.setActivity(activity);
			storyViewpagerAdapter.setScreenWidth(screenWidth);
			storyViewpagerAdapter.getList().addAll(storyList);
			storyViewPager.setAdapter(storyViewpagerAdapter);
		}

		if (communityList != null && communityList.size() > 0)
		{
			communityViewpagerAdapter = new CommunityViewpagerAdapter();
			communityViewpagerAdapter.setActivity(activity);
			communityViewpagerAdapter.setScreenWidth(screenWidth);
			communityViewpagerAdapter.getList().addAll(communityList);
			communityViewPager.setAdapter(communityViewpagerAdapter);
		}
		((ProgressBar) widgetCache.get(R.id.loadingProgressBar)).setVisibility(View.INVISIBLE);
	}
}
