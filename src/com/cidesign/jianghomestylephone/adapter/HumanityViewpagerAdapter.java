package com.cidesign.jianghomestylephone.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cidesign.jianghomestylephone.DetailActivity;
import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.async.AsyncDownTask;
import com.cidesign.jianghomestylephone.entity.ContentEntity;
import com.cidesign.jianghomestylephone.entity.RelativeLayoutRulesEntity;
import com.cidesign.jianghomestylephone.tools.JiangFinalVariables;
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

	private List<ContentEntity> list = new ArrayList<ContentEntity>();

	private int screenWidth;

	private Activity activity;

	private AQuery aq = null;
	
	public void setScreenWidth(int screenWidth)
	{
		this.screenWidth = screenWidth;
	}

	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}

	public List<ContentEntity> getList()
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
		ContentEntity cEntity = null;

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
		
		aq = new AQuery(view);
		
		try
		{
			cEntity = list.get(position);

			ImageView humanityBgImg = (ImageView) activity.findViewById(R.id.humanityBgImg);
			if (position == 0 && cEntity.getMax_bg_img() != null && !cEntity.getMax_bg_img().equals(""))
			{
				bitMap = LoadingImageTools.getImageBitmap(StorageUtils.FILE_ROOT + cEntity.getServerID() + "/"
						+ cEntity.getMax_bg_img());
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

			getThumbImage(cEntity,firstImg,R.id.communityThumbImg,position);
			
			firstImg.setLayoutParams(imageViewLayout);

			TextView tv1 = (TextView) view.findViewById(R.id.humanityTitle);
			tv1.setText(cEntity.getTitle());
			TextView tv2 = (TextView) view.findViewById(R.id.humanityTime);
			tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(cEntity.getPost_date())));
			TextView tv3 = (TextView) view.findViewById(R.id.humanityContent);
			tv3.setText(cEntity.getDescription());
			tv3.setLayoutParams(contentLayout);
			((LinearLayout) view.findViewById(R.id.mainContentLayout)).setLayoutParams(textLayout);
			RelativeLayout humanityLayout = (RelativeLayout) view.findViewById(R.id.humanityLayout);
			humanityLayout.setTag(cEntity);
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
			if (v.getTag() != null && (v.getTag() instanceof ContentEntity))
			{
				ContentEntity aEntity = (ContentEntity) v.getTag();
				if(aEntity.getDownloadFlag() == 1)
				{
					openDetailContent(aEntity);
				}
				else
				{
					new AsyncDownTask(activity,aEntity).execute();
				}
			}
		}		
	}
	
	private void openDetailContent(ContentEntity aEntity)
	{
		String url = "file://" + StorageUtils.FILE_ROOT + aEntity.getServerID() + "/doc/main.phone.html";
		Intent intent = new Intent(activity, DetailActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", aEntity.getTitle());
		activity.startActivity(intent);
	}
	
	private void getThumbImage(final ContentEntity cEntity,final ImageView view,int id,final int position)
	{
		final String filePathDir = StorageUtils.THUMB_IMG_ROOT + cEntity.getServerID() + "/";
		File fileDir = new File(filePathDir);
		if (!fileDir.exists() || !fileDir.isDirectory())
			fileDir.mkdir();
		String fileName = filePathDir + cEntity.getServerID()+".jpg";
		File file = new File(fileName);
		if (file.exists())
		{
			aq.id(id).image(file,400);
		}
		else
		{
			String url = cEntity.getProfile_path().substring(0,  cEntity.getProfile_path().length() - 4) + JiangFinalVariables.SQUARE_400;             
			Log.d(TAG, url);
			File target = new File(fileDir, cEntity.getServerID()+".jpg");              

			aq.download(url, target, new AjaxCallback<File>(){
			        
			        public void callback(String url, File file, AjaxStatus status) {
			                
			        	Bitmap bm = LoadingImageTools.loadingNative(activity.getApplicationContext(),filePathDir + cEntity.getServerID()+".jpg");
			        	if (bm != null && view != null)
						{
							view.setImageBitmap(bm);
							bitHashMap.put(position, bm);
						}
			        }
			        
			});
		}
	}
}
