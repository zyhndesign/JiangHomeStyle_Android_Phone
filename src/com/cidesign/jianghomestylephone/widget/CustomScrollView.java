package com.cidesign.jianghomestylephone.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView
{
	private static final String TAG = CustomScrollView.class.getSimpleName();

	private RelativeLayout landscapeAnimPanel;
	private RelativeLayout humanityAnimPanel;
	private RelativeLayout storyAnimPanel;
	private RelativeLayout communityAnimPanel;

	// �������뼰����
	private float xDistance, yDistance, xLast, yLast;

	private boolean landscapeIsAnimation = true;
	private boolean humanityIsAnimation = true;
	private boolean storyIsAnimation = true;
	private boolean communityIsAnimation = true;

	private int animHeight = 0;

	public void setLandscapeAnimPanel(RelativeLayout landscapeAnimPanel)
	{
		this.landscapeAnimPanel = landscapeAnimPanel;
	}

	public void setHumanityAnimPanel(RelativeLayout humanityAnimPanel)
	{
		this.humanityAnimPanel = humanityAnimPanel;
	}

	public void setStoryAnimPanel(RelativeLayout storyAnimPanel)
	{
		this.storyAnimPanel = storyAnimPanel;
	}

	public void setCommunityAnimPanel(RelativeLayout communityAnimPanel)
	{
		this.communityAnimPanel = communityAnimPanel;
	}

	public CustomScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;
			if (xDistance > yDistance)
			{
				return false;
			}
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (animHeight == 0)
		{
			animHeight = 100;
		}

		if (landscapeIsAnimation && landscapeAnimPanel != null && (this.getScrollY() + 800 >= landscapeAnimPanel.getY()))
		{
			landscapeIsAnimation = false;
			Animation animation = new TranslateAnimation(0, 0, 0, -animHeight);
			animation.setDuration(1000);
			animation.setAnimationListener(new UpPushAnimation(landscapeAnimPanel, animHeight));
			landscapeAnimPanel.startAnimation(animation);
		}

		if (humanityIsAnimation && humanityAnimPanel != null && (this.getScrollY() + 800 >= humanityAnimPanel.getY()))
		{
			humanityIsAnimation = false;
			Animation animation = new TranslateAnimation(0, 0, 0, -animHeight);
			animation.setDuration(1000);
			animation.setAnimationListener(new UpPushAnimation(humanityAnimPanel, animHeight));
			humanityAnimPanel.startAnimation(animation);
		}

		if (storyIsAnimation && storyAnimPanel != null && (this.getScrollY() + 800 >= storyAnimPanel.getY()))
		{
			storyIsAnimation = false;
			Animation animation = new TranslateAnimation(0, 0, 0, -animHeight);
			animation.setDuration(1000);
			animation.setAnimationListener(new UpPushAnimation(storyAnimPanel, animHeight));
			storyAnimPanel.startAnimation(animation);
		}

		if (communityIsAnimation && communityAnimPanel != null && (this.getScrollY() + 800 >= communityAnimPanel.getY()))
		{
			communityIsAnimation = false;
			Animation animation = new TranslateAnimation(0, 0, 0, -animHeight);
			animation.setDuration(1000);
			animation.setAnimationListener(new UpPushAnimation(communityAnimPanel, animHeight));
			communityAnimPanel.startAnimation(animation);
		}

		return super.onTouchEvent(ev);
	}

	class UpPushAnimation implements AnimationListener
	{
		private View view;
		private int animHeight;

		public UpPushAnimation(View view, int animHeight)
		{
			this.view = view;
			this.animHeight = animHeight;
		}

		@Override
		public void onAnimationEnd(Animation animation)
		{
			view.clearAnimation();
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
			lp.setMargins(lp.leftMargin, lp.topMargin - animHeight, lp.rightMargin, lp.bottomMargin);
			view.setLayoutParams(lp);
		}

		@Override
		public void onAnimationRepeat(Animation animation)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation)
		{
			// TODO Auto-generated method stub

		}

	}
}
