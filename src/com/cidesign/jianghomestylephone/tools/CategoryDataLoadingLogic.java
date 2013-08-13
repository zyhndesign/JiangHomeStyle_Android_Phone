package com.cidesign.jianghomestylephone.tools;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.entity.ArticleEntity;
import com.cidesign.jianghomestylephone.entity.LayoutEntity;

public class CategoryDataLoadingLogic
{
	private static LoadingImageTools loadingImg = new LoadingImageTools();

	/**
	 * 根据数据布局头条面板控件
	 * 
	 * @param articleList
	 * @param headlineLayout
	 * @param screen_width
	 * @param inflater
	 */
	public static void loadHeadLineData(List<ArticleEntity> articleList, LinearLayout headlineLayout, int screen_width,
			LayoutInflater inflater)
	{

		headlineLayout.removeAllViews();
		ArticleEntity aEntity = null;
		int view_width = (screen_width - 40);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 15;
		lp.rightMargin = 15;
		lp.width = view_width;

		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(view_width / 2 - 100);
		layoutEntity.setHeight(view_width / 2 - 100);
		layoutEntity.setLeftMargin(15);
		layoutEntity.setRightMargin(15);
		layoutEntity.setTopMargin(15);
		layoutEntity.setBottomMargin(15);
		RelativeLayout.LayoutParams imageViewLayout = LayoutMarginSetting.getRelativeLayoutParams(layoutEntity);

		for (int i = 1; i < articleList.size(); i++)
		{
			View view = inflater.inflate(R.layout.recommand_layout, null);

			view.setLayoutParams(lp);

			aEntity = articleList.get(i);
			ImageView iv = (ImageView) view.findViewById(R.id.recommandImg);
			loadingImg.loadingImage(iv, StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getProfile_path());
			iv.setPadding(0, 0, 0, 25);
			iv.setLayoutParams(imageViewLayout);
			TextView tv1 = (TextView) view.findViewById(R.id.recommandTitle);
			tv1.setText(aEntity.getTitle());
			TextView tv2 = (TextView) view.findViewById(R.id.recommandTime);
			tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
			view.setTag(aEntity);
			headlineLayout.addView(view);

		}
	}
}
