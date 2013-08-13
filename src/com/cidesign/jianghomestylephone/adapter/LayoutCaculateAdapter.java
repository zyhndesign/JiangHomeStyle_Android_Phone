package com.cidesign.jianghomestylephone.adapter;

import com.cidesign.jianghomestylephone.entity.LayoutEntity;
import com.cidesign.jianghomestylephone.entity.RelativeLayoutRulesEntity;
import com.cidesign.jianghomestylephone.tools.LayoutMarginSetting;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LayoutCaculateAdapter
{
	public static LinearLayout.LayoutParams getLinearLayout(int screenWidth, int moiety)
	{
		int columnWidth = (screenWidth) / moiety;
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth);
		layoutEntity.setHeight(columnWidth);
		return LayoutMarginSetting.getLinearLayoutParams(layoutEntity);
	}

	public static RelativeLayout.LayoutParams getRelativeRectangleLayout(int screenWidth, int moiety,
			RelativeLayoutRulesEntity rulesEntity)
	{
		int columnWidth = (screenWidth) / moiety;
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth);
		layoutEntity.setHeight(columnWidth * 2);
		layoutEntity.setLeftMargin(15);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(5);
		layoutEntity.setBottomMargin(5);
		return LayoutMarginSetting.getRelativeLayoutDetailParams(layoutEntity, rulesEntity);
	}

	public static RelativeLayout.LayoutParams getRelativeLayout(int screenWidth, int moiety)
	{
		int columnWidth = (screenWidth) / moiety;
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth * 2);
		layoutEntity.setHeight(columnWidth * 2);
		layoutEntity.setLeftMargin(5);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(10);
		layoutEntity.setBottomMargin(5);
		return LayoutMarginSetting.getRelativeLayoutParams(layoutEntity);
	}

	public static RelativeLayout.LayoutParams getBigRelativeLayoutOfParam(int screenWidth, int moiety,
			RelativeLayoutRulesEntity rulesEntity)
	{
		int columnWidth = (screenWidth) / moiety;
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth * 2);
		layoutEntity.setHeight(columnWidth * 2);
		layoutEntity.setLeftMargin(5);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(10);
		layoutEntity.setBottomMargin(5);
		return LayoutMarginSetting.getRelativeLayoutDetailParams(layoutEntity, rulesEntity);
	}

	public static RelativeLayout.LayoutParams getRelativeLayoutOfParam(int screenWidth, int moiety,
			RelativeLayoutRulesEntity rulesEntity)
	{
		int columnWidth = (screenWidth) / moiety;
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth - 8);
		layoutEntity.setHeight(columnWidth - 8);
		layoutEntity.setLeftMargin(5);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(10);
		layoutEntity.setBottomMargin(5);
		return LayoutMarginSetting.getRelativeLayoutDetailParams(layoutEntity, rulesEntity);
	}
	
	public static RelativeLayout.LayoutParams getStoryHorRelativeLayout(int screenWidth, int moiety)
	{
		int columnWidth = (screenWidth) / moiety;
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setHeight(columnWidth * 2 + 20);
		layoutEntity.setWidth(screenWidth);
		return LayoutMarginSetting.getRelativeLayoutParams(layoutEntity);
	}
}
