package com.cidesign.jianghomestylephone.tools;

import java.util.HashMap;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.widget.CommunityRelativeLayout;
import com.cidesign.jianghomestylephone.widget.CustomScrollView;
import com.cidesign.jianghomestylephone.widget.HumanityRelativeLayout;
import com.cidesign.jianghomestylephone.widget.LandscapeRelativeLayout;
import com.cidesign.jianghomestylephone.widget.StoryRelativeLayout;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class WidgetCache
{
	private static HashMap<Integer, View> viewCache = new HashMap<Integer, View>();

	private static WidgetCache widgetCacheInstance = new WidgetCache();

	private WidgetCache()
	{

	}

	public static WidgetCache getInstance()
	{
		return widgetCacheInstance;
	}

	public HashMap<Integer, View> getViewCache()
	{
		return viewCache;
	}

	public void init(Activity activity)
	{
		viewCache.put(R.id.loadingProgressBar, (ProgressBar) activity.findViewById(R.id.loadingProgressBar));
		// 主界面的控件元素
		CustomScrollView scrollView = (CustomScrollView) activity.findViewById(R.id.mainScrollView);
		viewCache.put(R.id.mainScrollView, scrollView);

		viewCache.put(R.id.recommandLayout, (LinearLayout) activity.findViewById(R.id.recommandLayout));
		viewCache.put(R.id.homeBigBg, (ImageView) activity.findViewById(R.id.homeBigBg));
		viewCache.put(R.id.homeArticleTitle, (TextView) activity.findViewById(R.id.homeArticleTitle));
		viewCache.put(R.id.homeArticleTime, (TextView) activity.findViewById(R.id.homeArticleTime));
		viewCache.put(R.id.homeLinearLayout, (LinearLayout) activity.findViewById(R.id.homeLinearLayout));
		viewCache.put(R.id.recommandLayout, (LinearLayout) activity.findViewById(R.id.recommandLayout));
		viewCache.put(R.id.videoView, (VideoView) activity.findViewById(R.id.videoView));

		// 风景界面控件元素
		viewCache.put(R.id.landscapeBgImg, (ImageView) activity.findViewById(R.id.landscapeBgImg));
		RelativeLayout landscapeAnimPanel = (RelativeLayout) activity.findViewById(R.id.landscapeAnimPanel);
		viewCache.put(R.id.landscapeAnimPanel, landscapeAnimPanel);
		viewCache.put(R.id.landscapeViewPager, (ViewPager) activity.findViewById(R.id.landscapeViewPager));
		viewCache.put(R.id.landscapePreClick, (ImageButton) activity.findViewById(R.id.landscapePreClick));
		viewCache.put(R.id.landscapeNextClick, (ImageButton) activity.findViewById(R.id.landscapeNextClick));
		viewCache.put(R.id.landscapeRelativeLayout,(LandscapeRelativeLayout)activity.findViewById(R.id.landscapeRelativeLayout));
		
		// 人文界面控件元素
		viewCache.put(R.id.humanityBgImg, (ImageView) activity.findViewById(R.id.humanityBgImg));
		RelativeLayout humanityAnimPanel = (RelativeLayout) activity.findViewById(R.id.humanityAnimPanel);
		viewCache.put(R.id.humanityAnimPanel, humanityAnimPanel);
		viewCache.put(R.id.humanityViewPager, (ViewPager) activity.findViewById(R.id.humanityViewPager));
		viewCache.put(R.id.humanityPreClick, (ImageButton) activity.findViewById(R.id.humanityPreClick));
		viewCache.put(R.id.humanityNextClick, (ImageButton) activity.findViewById(R.id.humanityNextClick));
		viewCache.put(R.id.humanityRelativeLayout,(HumanityRelativeLayout)activity.findViewById(R.id.humanityRelativeLayout));
		
		// 物语界面控件元素
		viewCache.put(R.id.storyBgImg, (ImageView) activity.findViewById(R.id.storyBgImg));
		RelativeLayout storyAnimPanel = (RelativeLayout) activity.findViewById(R.id.storyAnimPanel);
		viewCache.put(R.id.storyAnimPanel, storyAnimPanel);
		viewCache.put(R.id.storyViewPager, (ViewPager) activity.findViewById(R.id.storyViewPager));
		viewCache.put(R.id.storyPreClick, (ImageButton) activity.findViewById(R.id.storyPreClick));
		viewCache.put(R.id.storyNextClick, (ImageButton) activity.findViewById(R.id.storyNextClick));
		viewCache.put(R.id.storyRelativeLayout,(StoryRelativeLayout)activity.findViewById(R.id.storyRelativeLayout));
		
		// 小记界面控件元素
		viewCache.put(R.id.communityBgImg, (ImageView) activity.findViewById(R.id.communityBgImg));
		RelativeLayout communityAnimPanel = (RelativeLayout) activity.findViewById(R.id.communityAnimPanel);
		viewCache.put(R.id.communityAnimPanel, (RelativeLayout) activity.findViewById(R.id.communityAnimPanel));
		viewCache.put(R.id.communityViewPager, (ViewPager) activity.findViewById(R.id.communityViewPager));
		viewCache.put(R.id.communityPreClick, (ImageButton) activity.findViewById(R.id.communityPreClick));
		viewCache.put(R.id.communityNextClick, (ImageButton) activity.findViewById(R.id.communityNextClick));
		viewCache.put(R.id.communityRelativeLayout,(CommunityRelativeLayout)activity.findViewById(R.id.communityRelativeLayout));
		
		scrollView.setLandscapeAnimPanel(landscapeAnimPanel);
		scrollView.setHumanityAnimPanel(humanityAnimPanel);
		scrollView.setStoryAnimPanel(storyAnimPanel);
		scrollView.setCommunityAnimPanel(communityAnimPanel);
	}
}
