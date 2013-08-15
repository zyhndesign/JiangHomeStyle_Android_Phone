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
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LandscapeViewpagerAdapter extends PagerAdapter
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
	public void finishUpdate(View view)
	{

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
		View view = inflater.inflate(R.layout.landscape_template, null);
		aq = new AQuery(view);
		try
		{
			RelativeLayout.LayoutParams imageViewLayout = LayoutCaculateAdapter.getRelativeLayout((screenWidth / 2 ), 2);
			RelativeLayoutRulesEntity rulesEntity = new RelativeLayoutRulesEntity();
			rulesEntity.setBelowOfValue(R.id.landscapeThumbImg);
			RelativeLayout.LayoutParams textLayout = LayoutCaculateAdapter.getBigRelativeLayoutOfParam((screenWidth - 100), 4,
					rulesEntity);

			rulesEntity = new RelativeLayoutRulesEntity();
			rulesEntity.setRightOfVlaue(R.id.landscapeThumbImg);
			RelativeLayout.LayoutParams contentLayout = LayoutCaculateAdapter.getRelativeRectangleLayout((screenWidth - 100), 2,
					rulesEntity);

			ContentEntity cEntity = null;

			cEntity = list.get(position);
			ImageView landscapeBgImg = (ImageView) activity.findViewById(R.id.landscapeBgImg);
			Bitmap bitMap = null;
			if (position == 0 && cEntity.getMax_bg_img() != null && !cEntity.getMax_bg_img().equals(""))
			{
				String fileDir = StorageUtils.FILE_ROOT + cEntity.getServerID() + "/" + cEntity.getMax_bg_img();
				File file = new File(fileDir);
				if (file.exists() && fileDir.endsWith(".jpg"))
				{
					bitMap = LoadingImageTools.getImageBitmap(fileDir);
				}
				else
				{
					bitMap = LoadingImageTools.loadingNative(activity.getApplicationContext(), "bg4.jpg");
				}
			}
			else
			{
				bitMap = LoadingImageTools.loadingNative(activity.getApplicationContext(), "bg4.jpg");
			}
			if (bitMap != null)
			{
				landscapeBgImg.setImageBitmap(bitMap);
			}

			ImageView firstImg = (ImageView) view.findViewById(R.id.landscapeThumbImg);
			
			getThumbImage(cEntity,firstImg,R.id.landscapeThumbImg,position);

			firstImg.setLayoutParams(imageViewLayout);

			TextView tv1 = (TextView) view.findViewById(R.id.landscapeTitle);
			tv1.setText(cEntity.getTitle());
			TextView tv2 = (TextView) view.findViewById(R.id.landscapeTime);
			tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(cEntity.getTimestamp())));
			TextView tv3 = (TextView) view.findViewById(R.id.landscapeContent);
			tv3.setText(cEntity.getDescription());
			tv3.setLayoutParams(contentLayout);
			tv3.setTag(cEntity);
			tv3.setOnClickListener(new ClickPop());
			LinearLayout mainContentLayout = ((LinearLayout) view.findViewById(R.id.mainContentLayout));
			mainContentLayout.setLayoutParams(textLayout);
			mainContentLayout.setTag(cEntity);
			mainContentLayout.setOnClickListener(new ClickPop());
			firstImg.setTag(cEntity);
			firstImg.setOnClickListener(new ClickPop());
			((ViewPager) context).addView(view);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return view;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1)
	{

	}

	@Override
	public Parcelable saveState()
	{
		return null;
	}

	@Override
	public void startUpdate(View view)
	{

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
			                
			        	Bitmap bm = LoadingImageTools.getImageBitmap(filePathDir + cEntity.getServerID()+".jpg");
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
