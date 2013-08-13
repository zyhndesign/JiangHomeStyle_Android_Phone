package com.cidesign.jianghomestylephone.widget;

import java.util.HashMap;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cidesign.jianghomestylephone.R;
import com.cidesign.jianghomestylephone.tools.WidgetCache;

public class PopMenu
{
	public static void initPopuWindow(Context ctx, View showLocation, int menuViewID,int screenWidth)
	{
		LayoutInflater mLayoutInflater = LayoutInflater.from(ctx);
		/* 设置显示menu布局 view子VIEW */
		View sub_view = mLayoutInflater.inflate(menuViewID, null);
		/* 第一个参数弹出显示view 后两个是窗口大小 */
		final PopupWindow mPopupWindow = new PopupWindow(sub_view, screenWidth - 180, LayoutParams.WRAP_CONTENT);
		/* 设置背景显示 */
		mPopupWindow.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.popbg));
		/* 设置触摸外面时消失 */
		mPopupWindow.setOutsideTouchable(true);
		/* 设置系统动画 */
		// mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

		mPopupWindow.update();
		mPopupWindow.setTouchable(true);
		/* 设置点击menu以外其他地方以及返回键退出 */
		mPopupWindow.setFocusable(true);

		/**
		 * 1.解决再次点击MENU键无反应问题 2.sub_view是PopupWindow的子View
		 */
		sub_view.setFocusableInTouchMode(true);
		sub_view.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				// TODO Auto-generated method stub
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (mPopupWindow.isShowing()))
				{
					mPopupWindow.dismiss();// 这里写明模拟menu的PopupWindow退出就行
					return true;
				}
				return false;
			}
		});

		/* 监听MENU事件 */
		View[] menu = new View[4];
		menu[0] = sub_view.findViewById(R.id.topLandscapeMenuBtn);
		menu[1] = sub_view.findViewById(R.id.topHumanityMenuBtn);
		menu[2] = sub_view.findViewById(R.id.topStoryMenuBtn);
		menu[3] = sub_view.findViewById(R.id.topCommunityMenuBtn);

		final HashMap<Integer, View> hashMap = WidgetCache.getInstance().getViewCache();

		menu[0].setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				((CustomScrollView) hashMap.get(R.id.mainScrollView)).smoothScrollTo(0,
						(int) ((ImageView) hashMap.get(R.id.landscapeBgImg)).getY());
				mPopupWindow.dismiss();
			}
		});

		menu[1].setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				((CustomScrollView) hashMap.get(R.id.mainScrollView)).smoothScrollTo(0,
						(int) ((ImageView) hashMap.get(R.id.humanityBgImg)).getY());
				mPopupWindow.dismiss();
			}
		});

		menu[2].setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				((CustomScrollView) hashMap.get(R.id.mainScrollView)).smoothScrollTo(0,
						(int) ((ImageView) hashMap.get(R.id.storyBgImg)).getY());
				mPopupWindow.dismiss();
			}
		});

		menu[3].setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				((CustomScrollView) hashMap.get(R.id.mainScrollView)).smoothScrollTo(0,
						(int) ((ImageView) hashMap.get(R.id.communityBgImg)).getY());
				mPopupWindow.dismiss();
			}
		});
		mPopupWindow.showAsDropDown(showLocation, 80, 0);
	}
	
}
