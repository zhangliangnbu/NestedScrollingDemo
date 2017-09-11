package com.liang.nestedscrollingdemo.fling;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import java.util.Arrays;

/**
 *
 * Created by zhangliang on 07/09/2017.
 */

public class InnerVerticalFlingViewGroup extends FrameLayout implements NestedScrollingChild {
	private NestedScrollingChildHelper helper = new NestedScrollingChildHelper(this);
	private Scroller scroller;
	private VelocityTracker tracker;
	private static final String TAG = "inner";

	public InnerVerticalFlingViewGroup(Context context) {
		super(context);
		init(context, null, 0);
	}

	public InnerVerticalFlingViewGroup(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public InnerVerticalFlingViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		setNestedScrollingEnabled(true);
		tracker = VelocityTracker.obtain();
		scroller = new Scroller(context);
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
		Log.d(TAG, "--dispatchNestedPreFling:" + velocityX + "/" + velocityY);
		return helper.dispatchNestedPreFling(velocityX, velocityY);
	}

	@Override
	public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
		Log.d(TAG, "--dispatchNestedFling:" + velocityX +"/" + velocityY + "/" + consumed);
		return helper.dispatchNestedFling(velocityX, velocityY, consumed);
	}

	@Override
	public void stopNestedScroll() {
		helper.stopNestedScroll();
	}

	private int[] parentConsume = new int[2];
	private int[] offset = new int[2];
	private float lastY;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		Log.d(TAG, "onTouchEvent" + "/" + ev.getX() + "/" + ev.getY());
//		Log.d(TAG, "xy:" + getX() + "/" + getY() + "/" + getTop());
		Log.d(TAG, "sy:" + getScrollY());
		if(tracker == null) {
			tracker = VelocityTracker.obtain();
		}
		tracker.addMovement(ev);
		float y = ev.getRawY();// 需要考虑到父控件的移动
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
				break;
			case MotionEvent.ACTION_MOVE:
				float dy = y - lastY;
				// 当前View在父控件顶部且往上移动后者在父控件底部且往下移动时，才让父控件消费事件
				float childConsumeDy;
				if(getY() + dy <= 0 && dy < 0) {
					childConsumeDy = -getY();
				} else if(getY() + dy >= ((View)getParent()).getHeight() - getHeight() && dy > 0) {
					childConsumeDy = ((View)getParent()).getHeight() - getHeight() - getY();
				} else {
					childConsumeDy = dy;
				}
				parentConsume[1] = (int) (dy - childConsumeDy);

				if(Math.floor(Math.abs(childConsumeDy)) == 0) {
					childConsumeDy = 0;
				}
				Log.d(TAG, "dy-childConsumeDy-parentConsumeDy:" + dy + "/" + childConsumeDy + "/" + parentConsume[1]);
				setY(getY() + childConsumeDy);
				dispatchNestedScroll(0, (int) childConsumeDy, 0, parentConsume[1], offset);

				break;
			case MotionEvent.ACTION_UP:
//				tracker.computeCurrentVelocity(1000);
//				float vy = tracker.getYVelocity();
//				Log.d(TAG,  vy + "");
//				dispatchNestedFling(0, vy, true);
//
//				tracker.recycle();
//				tracker = null;

				helper.stopNestedScroll();
				break;
			case MotionEvent.ACTION_CANCEL:
				helper.stopNestedScroll();
				break;
			default:
				break;
		}
		lastY = y;
		return true;
	}

//	@Override
//	public void computeScroll() {
//		if(scroller.computeScrollOffset()) {
//			invalidate();
//		}
//
//	}
}
