package com.cidesign.jianghomestylephone.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cidesign.jianghomestylephone.DetailActivity;
import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.entity.RelativeLayoutRulesEntity;
import com.cidesign.jianghomestylephone.tools.LoadingImageTools;
import com.cidesign.jianghomestylephone.tools.StorageUtils;
import com.cidesign.jianghomestylephone.tools.TimeTools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HumanityViewpagerAdapter extends PagerAdapter
{
	private static final String TAG = LandscapeViewpagerAdapter.class.getSimpleName();

	private HashMap<Integer, Bitmap> bitHashMap = new HashMap<Integer, Bitmap>();

	private List<ArticleEntity> list = new ArrayList<ArticleEntity>();

	private int screenWidth;

	private Activity activity;

	public void setScreenWidth(int screenWidth)
	{
		this.screenWidth = screenWidth;
	}

	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}

	public List<ArticleEntity> getList()
	{
		return list;
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view == object;
	}

	@Override
	public void destroyItem(View collection, int position, Object o)
	{
		Log.d(TAG, "destory the resources...");

		Bitmap bitmap = bitHashMap.get(position);
		if (bitmap != null && !bitmap.isRecycled())
		{
			Log.d(TAG, "recycle the bitmap...");
			bitmap.recycle();
			bitmap = null;
		}
		View view = (View) o;
		((ViewPager) collection).removeView(view);
		view = null;
	}

	@Override
	public Object instantiateItem(View context, int position)
	{

		LayoutInflater inflater = LayoutInflater.from(activity.getApplicationContext());
		ArticleEntity aEntity = null;

		Bitmap bitMap = null;

		RelativeLayout.LayoutParams imageViewLayout = LayoutCaculateAdapter.getRelativeLayout((screenWidth - 100), 4);
		RelativeLayout.LayoutParams bigLayout = LayoutCaculateAdapter.getRelativeLayout((screenWidth - 100), 2);
		RelativeLayoutRulesEntity rulesEntity = new RelativeLayoutRulesEntity();
		rulesEntity.setBelowOfValue(R.id.humanityThumbImg);
		RelativeLayout.LayoutParams textLayout = LayoutCaculateAdapter.getBigRelativeLayoutOfParam((screenWidth - 100), 4,
				rulesEntity);

		rulesEntity = new RelativeLayoutRulesEntity();
		rulesEntity.setRightOfVlaue(R.id.humanityThumbImg);
		RelativeLayout.LayoutParams contentLayout = LayoutCaculateAdapter.getRelativeRectangleLayout((screenWidth - 100), 2,
				rulesEntity);

		View view = inflater.inflate(R.layout.humanity_template, null);
		
		try
		{
			aEntity = list.get(position);

			ImageView humanityBgImg = (ImageView) activity.findViewById(R.id.humanityBgImg);
			if (position == 0 && aEntity.getMax_bg_img() != null && !aEntity.getMax_bg_img().equals(""))
			{
				bitMap = LoadingImageTools.getImageBitmap(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/"
						+ aEntity.getMax_bg_img());
			}
			else
			{
				bitMap = LoadingImageTools.loadingNative(activity.getApplicationContext(), "bg2.jpg");
			}
			if (bitMap != null)
			{
				humanityBgImg.setImageBitmap(bitMap);
			}
			ImageView firstImg = (ImageView) view.findViewById(R.id.humanityThumbImg);
			bitMap = LoadingImageTools.getImageBitmap(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/"
					+ aEntity.getProfile_path());
			if (bitMap != null)
			{
				firstImg.setImageBitmap(bitMap);
				bitHashMap.put(position, bitMap);
			}
			firstImg.setLayoutParams(imageViewLayout);

			TextView tv1 = (TextView) view.findViewById(R.id.humanityTitle);
			tv1.setText(aEntity.getTitle());
			TextView tv2 = (TextView) view.findViewById(R.id.humanityTime);
			tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
			TextView tv3 = (TextView) view.findViewById(R.id.humanityContent);
			tv3.setText(aEntity.getDescription());
			tv3.setLayoutParams(contentLayout);
			((LinearLayout) view.findViewById(R.id.mainContentLayout)).setLayoutParams(textLayout);
			RelativeLayout humanityLayout = (RelativeLayout) view.findViewById(R.id.humanityLayout);
			humanityLayout.setTag(aEntity);
			humanityLayout.setLayoutParams(bigLayout);
			humanityLayout.setOnClickListener(new ClickPop());
			((ViewPager) context).addView(view);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return view;
	}

	class ClickPop implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			ArticleEntity aEntity = (ArticleEntity) v.getTag();
			String url = "file://" + StorageUtils.FILE_ROOT + aEntity.getServerID() + "/doc/main.phone.html";
			Intent intent = new Intent(activity, DetailActivity.class);
			intent.putExtra("url", url);
			intent.putExtra("title", aEntity.getTitle());
			activity.startActivity(intent);
		}
	}
}
