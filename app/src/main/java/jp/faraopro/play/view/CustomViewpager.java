package jp.faraopro.play.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewpager extends ViewPager {
	private boolean enableSwipe = true;

	public CustomViewpager(Context context) {
		super(context);
	}

	public CustomViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (enableSwipe) {
			return super.onTouchEvent(arg0);
		} else {
			return enableSwipe;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (enableSwipe) {
			return super.onInterceptTouchEvent(arg0);
		} else {
			return enableSwipe;
		}
	}
}
