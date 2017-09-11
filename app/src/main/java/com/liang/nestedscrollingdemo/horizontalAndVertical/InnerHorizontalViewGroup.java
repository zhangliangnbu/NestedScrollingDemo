package com.liang.nestedscrollingdemo.horizontalAndVertical;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.util.Arrays;

/**
 *
 * Created by zhangliang on 07/09/2017.
 */

public class InnerHorizontalViewGroup extends FrameLayout implements NestedScrollingChild {
	private NestedScrollingChildHelper helper = new NestedScrollingChildHelper(this);
	private static final String TAG = "inner";

	public InnerHorizontalViewGroup(Context context) {
		super(context);
		init(context, null, 0);
	}

	public InnerHorizontalViewGroup(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public InnerHorizontalViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		setNestedScrollingEnabled(true);
	}

	@Override
	public void setNestedScrollingEnabled(boolean enabled) {
		helper.setNestedScrollingEnabled(enabled);
	}

	@Override
	public boolean isNestedScrollingEnabled() {
		return helper.isNestedScrollingEnabled();
	}

	@Override
	public boolean startNestedScroll(int axes) {
		return helper.startNestedScroll(axes);
	}

	@Override
	public boolean hasNestedScrollingParent() {
		return helper.hasNestedScrollingParent();
	}

	//  // 通知父View, 子View想滑动 dy 个偏移量，父View要不要先滑一下，然后把父View滑了多少，告诉子View一下
	@Override
	public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
		Log.d(TAG, "--dispatchNestedPreScroll:" + dx + "/" + dy + "/" + Arrays.toString(consumed) + "/" + Arrays.toString(offsetInWindow));
		return helper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
	}

	@Override
	public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
		Log.d(TAG, "--dispatchNestedScroll:" + dxConsumed + "/" + dyConsumed + "/" + dxUnconsumed + "/" + dyUnconsumed + "/" + Arrays.toString(offsetInWindow));
		return helper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
	}

	@Override
	public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
		return helper.dispatchNestedPreFling(velocityX, velocityY);
	}

	@Override
	public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
		return helper.dispatchNestedFling(velocityX, velocityY, consumed);
	}

	@Override
	public void stopNestedScroll() {
		helper.stopNestedScroll();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	private int[] parentConsume = new int[2];
	private int[] offset = new int[2];
	private float lastY, lastX;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		Log.d(TAG, "onTouchEvent" + "/" + ev.getX() + "/" + ev.getY());
//		Log.d(TAG, "xy:" + getX() + "/" + getY() + "/" + getTop());
//		Log.d(TAG, "sy:" + getScrollY());
		float y = ev.getRawY();// 需要考虑到父控件的移动
		float x = ev.getRawX();
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL);
				break;
			case MotionEvent.ACTION_MOVE:
				// dx > dy child 移动， dx < dy parent 移动
				float dy = y - lastY;
				float dx = x - lastX;
				boolean isHorizontalScroll = Math.abs(dx) >= Math.abs(dy);
				Log.d(TAG, "dx-dy:" + dx + "/" + dy);
				if(isHorizontalScroll) {
					setX(getX() + dx);
				} else {
					dispatchNestedScroll(0, 0, (int)dx, (int)dy, offset);
				}
				break;
			case MotionEvent.ACTION_UP:
				helper.stopNestedScroll();
				break;
			case MotionEvent.ACTION_CANCEL:
				helper.stopNestedScroll();
				break;
			default:
				break;
		}
		lastX = x;
		lastY = y;
		return true;
	}
}
