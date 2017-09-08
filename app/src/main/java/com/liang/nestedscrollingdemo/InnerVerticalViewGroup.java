package com.liang.nestedscrollingdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Arrays;

/**
 *
 * Created by zhangliang on 07/09/2017.
 */

public class InnerVerticalViewGroup extends FrameLayout implements NestedScrollingChild {
	private NestedScrollingChildHelper helper = new NestedScrollingChildHelper(this);
	private static final String TAG = "inner";

	public InnerVerticalViewGroup(Context context) {
		super(context);
		init(context, null, 0);
	}

	public InnerVerticalViewGroup(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public InnerVerticalViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		setNestedScrollingEnabled(true);
	}

	@Override
	public void setNestedScrollingEnabled(boolean enabled) {
		Log.d(TAG, "--setNestedScrollingEnabled:" + enabled);
		helper.setNestedScrollingEnabled(enabled);
	}

	@Override
	public boolean isNestedScrollingEnabled() {
		boolean flag = helper.isNestedScrollingEnabled();
		Log.d(TAG, "--isNestedScrollingEnabled:" + flag);
		return flag;
	}

	@Override
	public boolean startNestedScroll(int axes) {
		boolean flag = helper.startNestedScroll(axes);
//		Log.d(TAG, "--startNestedScroll:" + flag + "/" + axes);
		return flag;
	}

	@Override
	public boolean hasNestedScrollingParent() {
		boolean flag = helper.hasNestedScrollingParent();
//		Log.d(TAG, "--hasNestedScrollingParent:" + flag);
		return flag;
	}

	//  // 通知父View, 子View想滑动 dy 个偏移量，父View要不要先滑一下，然后把父View滑了多少，告诉子View一下
	@Override
	public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
		boolean flag = helper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
		Log.d(TAG, "--dispatchNestedPreScroll:" + flag);
		Log.d(TAG, dx + "/" + dy + "/" + Arrays.toString(consumed) + "/" + Arrays.toString(offsetInWindow));
		return flag;
	}

	@Override
	public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
		boolean flag = helper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
		Log.d(TAG, "--dispatchNestedScroll:" + flag);
		Log.d(TAG, dxConsumed + "/" + dyConsumed + "/" + dxUnconsumed + "/" + dyUnconsumed + "/" + Arrays.toString(offsetInWindow));
		return flag;
	}

	@Override
	public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
		boolean flag = helper.dispatchNestedPreFling(velocityX, velocityY);
//		Log.d(TAG, "--dispatchNestedPreFling:" + flag);
//		Log.d(TAG, velocityX + "/" + velocityY);
		return flag;
	}

	@Override
	public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
		boolean flag = helper.dispatchNestedFling(velocityX, velocityY, consumed);
//		Log.d(TAG, "--dispatchNestedFling:" + flag);
//		Log.d(TAG, velocityX +"/" + velocityY + "/" + consumed);
		return flag;
	}

	@Override
	public void stopNestedScroll() {
//		Log.d(TAG, "--stopNestedScroll");
		helper.stopNestedScroll();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		Log.d(TAG, "dispatchTouchEvent" + "/" + ev.getX() + "/" + ev.getY());
		return super.dispatchTouchEvent(ev);
	}

	private int[] parentConsume = new int[2];
	private int[] offset = new int[2];
	private float lastY;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		Log.d(TAG, "onTouchEvent" + "/" + ev.getX() + "/" + ev.getY());
//		Log.d(TAG, "xy:" + getX() + "/" + getY() + "/" + getTop());
//		Log.d(TAG, "sy:" + getScrollY());
		float y = ev.getRawY();// 需要考虑到父控件的移动
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
				break;
			case MotionEvent.ACTION_MOVE:
				float dy = y - lastY;
//				Log.d(TAG, "before dispatchNestedPreScroll dy:" + dy);
				if(dispatchNestedPreScroll(0, (int) dy, parentConsume, offset)) {
					dy -= parentConsume[1];
				}
				if(Math.floor(Math.abs(dy)) == 0) {
					dy = 0;
				}
//				Log.d(TAG, "after dispatchNestedPreScroll dy:" + dy);
//				handleScroll(dy);
				handleTranslation(dy);
				dispatchNestedScroll(0, (int) dy, 0, parentConsume[1], offset);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				helper.stopNestedScroll();
				break;
			default:
				break;
		}
		lastY = y;
		return true;
	}

	private void handleTranslation(float dy) {
		setY(Math.min(Math.max(getY() + dy, 0), ((View) getParent()).getHeight() - getHeight()));
	}

	private void handleScroll(float dy) {
		// 这里移动子View，下面的min,max是为了控制边界，避免子View越界
		View child = getChildAt(0);
		if(-getScrollY() + dy <= 0) {
			dy = getScrollY();
		} else if(-getScrollY() + dy >= getHeight() - child.getHeight()) {
			dy = getHeight() - child.getHeight() - (-getScrollY());
		}
//				Log.d(TAG, "after dispatchNestedPreScroll dy1:" + dy);
//
		scrollBy(0, (int) -dy);
	}
}
