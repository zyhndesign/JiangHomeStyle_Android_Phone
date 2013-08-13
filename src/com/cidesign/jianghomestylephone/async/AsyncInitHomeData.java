package com.cidesign.jianghomestylephone.async;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.db.DatabaseHelper;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.tools.CategoryDataLoadingLogic;
import com.cidesign.jianghomestylephone.tools.LoadingDataFromDB;
import com.cidesign.jianghomestylephone.tools.LoadingImageTools;
import com.cidesign.jianghomestylephone.tools.StorageUtils;
import com.cidesign.jianghomestylephone.tools.TimeTools;
import com.cidesign.jianghomestylephone.tools.WidgetCache;

public class AsyncInitHomeData extends AsyncTask<Void, Void, List<ArticleEntity>>
{
	private Activity activity;
	private int screen_width;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;
	private LayoutInflater inflater = null;

	private HashMap<Integer, View> widgetCache;

	public AsyncInitHomeData(Activity activity, DatabaseHelper dbHelper, LayoutInflater inflater, int screen_width)
	{
		this.activity = activity;
		this.dbHelper = dbHelper;
		this.inflater = inflater;
		this.screen_width = screen_width;
	}
	
	@Override
	protected void onPreExecute()
	{
		loadingDataFromDB = new LoadingDataFromDB();
		widgetCache = WidgetCache.getInstance().getViewCache();
	}
	
	@Override
	protected List<ArticleEntity> doInBackground(Void... params)
	{
		return loadingDataFromDB.loadTopFourArticle(dbHelper.getArticleListDataDao());
	}
	
	@Override
	protected void onPostExecute(List<ArticleEntity> list)
	{
		if (list != null && list.size() >= 1)
		{
			ArticleEntity aEntity = list.get(0);

			LoadingImageTools loadingImg = new LoadingImageTools();

			ImageView homeBgImg = (ImageView) widgetCache.get(R.id.homeBigBg);
			final VideoView mVideoView = (VideoView) widgetCache.get(R.id.videoView);

			if (aEntity.getMax_bg_img() == null || aEntity.getMax_bg_img().equals(""))
			{
				homeBgImg.setVisibility(View.VISIBLE);
				mVideoView.setVisibility(View.GONE);
				loadingImg.loadingNativeImage(activity, homeBgImg, "bg1.jpg");
			}
			else if (aEntity.getMax_bg_img().endsWith(".mp4"))
			{
				homeBgImg.setVisibility(View.GONE);
				if (mVideoView != null)
				{
					final String videoPath = "file://" + StorageUtils.FILE_ROOT + aEntity.getServerID() + "/"
							+ aEntity.getMax_bg_img();					
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
				mVideoView.setVisibility(View.GONE);
				homeBgImg.setVisibility(View.VISIBLE);
				loadingImg
						.loadingImage(homeBgImg, StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getMax_bg_img());
			}
			((TextView) widgetCache.get(R.id.homeArticleTitle)).setText(aEntity.getTitle());
			((TextView) widgetCache.get(R.id.homeArticleTime)).setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
			((LinearLayout) widgetCache.get(R.id.homeLinearLayout)).setTag(aEntity);
			if (list.size() >= 2)
			{
				CategoryDataLoadingLogic.loadHeadLineData(list, (LinearLayout) widgetCache.get(R.id.recommandLayout),
						screen_width, inflater); // 初始化头条
			}
		}
	}
}
