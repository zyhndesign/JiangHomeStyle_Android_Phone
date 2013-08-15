package com.cidesign.jianghomestylephone.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.adapter.CommunityViewpagerAdapter;
import com.cidesign.jianghomestylephone.adapter.HumanityViewpagerAdapter;
import com.cidesign.jianghomestylephone.adapter.LandscapeViewpagerAdapter;
import com.cidesign.jianghomestylephone.adapter.StoryViewpagerAdapter;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ContentEntity;
import com.cidesign.jianghomestylephone.tools.CategoryDataLoadingLogic;
import com.cidesign.jianghomestylephone.tools.JiangFinalVariables;
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
import android.app.ProgressDialog;
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

	private ProgressDialog mypDialog = null;

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
		mypDialog = new ProgressDialog(activity);
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置进度条风格，风格为圆形，旋转的
		mypDialog.setTitle("读取内容");
		// 设置ProgressDialog 标题
		mypDialog.setMessage(activity.getResources().getString(R.string.loading));
		// 设置ProgressDialog 提示信息
		mypDialog.setIcon(R.drawable.loading_spinner_bg);

		mypDialog.setIndeterminate(false);
		// 设置ProgressDialog 的进度条是否不明确
		mypDialog.setCancelable(false);
		// 设置ProgressDialog 是否可以按退回按键取消
		mypDialog.show();

		loadingDataFromDB = new LoadingDataFromDB();
		widgetCache = WidgetCache.getInstance().getViewCache();

		landscapePreClick = (ImageButton) widgetCache.get(R.id.landscapePreClick);
		landscapeNextClick = (ImageButton) widgetCache.get(R.id.landscapeNextClick);
		LandscapeRelativeLayout landscapeRelativeLayout = (LandscapeRelativeLayout) widgetCache.get(R.id.landscapeRelativeLayout);
		landscapeRelativeLayout.setLandscapePreClick(landscapePreClick);
		landscapeRelativeLayout.setLandscapeNextClick(landscapeNextClick);
		landscapeViewPager = (ViewPager) widgetCache.get(R.id.landscapeViewPager);

		humanityPreClick = (ImageButton) widgetCache.get(R.id.humanityPreClick);
		humanityNextClick = (ImageButton) widgetCache.get(R.id.humanityNextClick);
		HumanityRelativeLayout humanityRelativeLayout = (HumanityRelativeLayout) widgetCache.get(R.id.humanityRelativeLayout);
		humanityRelativeLayout.setHumanityPreClick(humanityPreClick);
		humanityRelativeLayout.setHumanityNextClick(humanityNextClick);
		humanityViewPager = (ViewPager) widgetCache.get(R.id.humanityViewPager);

		storyPreClick = (ImageButton) widgetCache.get(R.id.storyPreClick);
		storyNextClick = (ImageButton) widgetCache.get(R.id.storyNextClick);
		StoryRelativeLayout storyRelativeLayout = (StoryRelativeLayout) widgetCache.get(R.id.storyRelativeLayout);
		storyRelativeLayout.setStoryPreClick(storyPreClick);
		storyRelativeLayout.setStoryNextClick(storyNextClick);
		storyViewPager = (ViewPager) widgetCache.get(R.id.storyViewPager);

		communityPreClick = (ImageButton) widgetCache.get(R.id.communityPreClick);
		communityNextClick = (ImageButton) widgetCache.get(R.id.communityNextClick);
		CommunityRelativeLayout communityRelativeLayout = (CommunityRelativeLayout) widgetCache.get(R.id.communityRelativeLayout);
		communityRelativeLayout.setCommunityPreClick(communityPreClick);
		communityRelativeLayout.setCommunityNextClick(communityNextClick);
		communityViewPager = (ViewPager) widgetCache.get(R.id.communityViewPager);

	}

	@Override
	protected Object[] doInBackground(Void... arg0)
	{
		Object[] objectArray = new Object[5];
		// 读取数据库加载数据
		List<ContentEntity> topFourList = loadingDataFromDB.loadTopFourArticle(dbHelper.getContentDataDao());
		objectArray[0] = topFourList;

		// 初始化风景
		List<ContentEntity> landscapeList = new ArrayList<ContentEntity>();
		// 初始化人文
		List<ContentEntity> humanityList = new ArrayList<ContentEntity>();
		// 初始化物语
		List<ContentEntity> storyList = new ArrayList<ContentEntity>();
		// 初始化社区
		List<ContentEntity> communityList = new ArrayList<ContentEntity>();

		List<ContentEntity> allArticleList = loadingDataFromDB.loadAllArticle(dbHelper.getContentDataDao());

		for (ContentEntity cEntity : allArticleList)
		{
			if (cEntity.getCategory() == JiangFinalVariables.LANDSCAPE)
			{
				landscapeList.add(cEntity);
			}
			else if (cEntity.getCategory() == JiangFinalVariables.HUMANITY)
			{
				humanityList.add(cEntity);
			}
			else if (cEntity.getCategory() == JiangFinalVariables.COMMUNITY)
			{
				storyList.add(cEntity);
			}
			else if (cEntity.getCategory() == JiangFinalVariables.STORY)
			{
				communityList.add(cEntity);
			}
		}

		objectArray[1] = landscapeList;

		objectArray[2] = humanityList;

		objectArray[3] = storyList;

		objectArray[4] = communityList;

		return objectArray;
	}

	@Override
	protected void onPostExecute(Object[] result)
	{
		@SuppressWarnings("unchecked")
		List<ContentEntity> topFourList = (List<ContentEntity>) result[0];
		@SuppressWarnings("unchecked")
		List<ContentEntity> landscapeList = (List<ContentEntity>) result[1];
		@SuppressWarnings("unchecked")
		List<ContentEntity> humanityList = (List<ContentEntity>) result[2];
		@SuppressWarnings("unchecked")
		List<ContentEntity> storyList = (List<ContentEntity>) result[3];
		@SuppressWarnings("unchecked")
		List<ContentEntity> communityList = (List<ContentEntity>) result[4];

		if (topFourList.size() >= 1)
		{
			ContentEntity cEntity = topFourList.get(0);

			LoadingImageTools loadingImg = new LoadingImageTools();

			ImageView homeBgImg = (ImageView) widgetCache.get(R.id.homeBigBg);
			final VideoView mVideoView = (VideoView) widgetCache.get(R.id.videoView);

			if (cEntity.getMax_bg_img() == null || cEntity.getMax_bg_img().equals(""))
			{
				homeBgImg.setVisibility(View.VISIBLE);
				loadingImg.loadingNativeImage(activity, homeBgImg, "bg1.jpg");
			}
			else if (cEntity.getMax_bg_img().endsWith(".mp4"))
			{
				if (mVideoView != null)
				{
					final String videoPath = "file://" + StorageUtils.FILE_ROOT + cEntity.getServerID() + "/"
							+ cEntity.getMax_bg_img();
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
						.loadingImage(homeBgImg, StorageUtils.FILE_ROOT + cEntity.getServerID() + "/" + cEntity.getMax_bg_img());
			}
			((TextView) widgetCache.get(R.id.homeArticleTitle)).setText(cEntity.getTitle());
			((TextView) widgetCache.get(R.id.homeArticleTime)).setText(TimeTools.getTimeByTimestap(Long.parseLong(cEntity.getTimestamp())));
			((LinearLayout) widgetCache.get(R.id.homeLinearLayout)).setTag(cEntity);
			if (topFourList.size() >= 2)
			{
				CategoryDataLoadingLogic cdLogic = new CategoryDataLoadingLogic();
				cdLogic.loadHeadLineData(topFourList, (LinearLayout) widgetCache.get(R.id.recommandLayout),
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
		if(mypDialog != null)
		{
			mypDialog.dismiss();
		}
	}
}
