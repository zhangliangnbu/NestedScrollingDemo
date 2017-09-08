package com.liang.nestedscrollingdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
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

public class OuterVerticalViewGroup extends FrameLayout implements NestedScrollingParent {

	private final NestedScrollingParentHelper helper = new NestedScrollingParentHelper(this);
	private static final String TAG = "outer";

	public OuterVerticalViewGroup(@NonNull Context context) {
		super(context);
	}

	public OuterVerticalViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public OuterVerticalViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
	}

	@Override
	public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//		Log.d(TAG, "--onStartNestedScroll:" + (child == target) + "/" + nestedScrollAxes);
		return true;
	}

	@Override
	public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
//		Log.d(TAG, "--onNestedScrollAccepted:" + (child == target) + "/" + nestedScrollAxes);
		helper.onNestedScrollAccepted(child, target, nestedScrollAxes);
	}

	@Override
	public void onStopNestedScroll(View target) {
//		Log.d(TAG, "--onStopNestedScroll:" + target.getClass().getSimpleName());
		helper.onStopNestedScroll(target);
	}

	@Override
	public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
		Log.d(TAG, "--onNestedScroll:" + target.getClass().getSimpleName() + "/" + dxConsumed + "/" + dyConsumed + "/" + dxUnconsumed + "/" + dyUnconsumed);
//		setY(getY() + dyUnconsumed);
	}

	@Override
	public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
		Log.d(TAG, "--onNestedPreScroll:" + target.getClass().getSimpleName() + "/" + dx + "/" + dy + "/" + Arrays.toString(consumed));
		handleTranslation(dy, consumed);
//		handleSrcoll(dy, consumed);

	}

	private void handleTranslation(int dy, int[] consumed) {
		// 应该移动的Y距离
		final float shouldMoveY = getY() + dy;

		// 获取到父View的容器的引用，这里假定父View容器是View
		final View parent = (View) getParent();

		int consumedY;
		// 如果超过了父View的上边界，只消费子View到父View上边的距离
		if (shouldMoveY <= 0) {
			consumedY = - (int) getY();
		} else if (shouldMoveY >= parent.getHeight() - getHeight()) {
			// 如果超过了父View的下边界，只消费子View到父View
			consumedY = (int) (parent.getHeight() - getHeight() - getY());
		} else {
			// 其他情况下全部消费
			consumedY = dy;
		}

		// 对父View进行移动
		setY(getY() + consumedY);

		// 将父View消费掉的放入consumed数组中
		consumed[1] = consumedY;
	}

	//移动内容 如果内容在顶部或者底部就移动就消费部分或者不消费
	private void handleSrcoll(int dy, int[] consumed) {
		View child = getChildAt(0);
		if(-getScrollY() + dy <= 0) {
			dy = -child.getTop();
		} else if(-getScrollY() + dy >= getHeight() - child.getHeight()) {
			dy = getHeight() - child.getHeight() - (-getScrollY());
		}
//		Log.d(TAG, "dy:" + dy);
		scrollBy(0, -dy);
		consumed[1] = dy;
	}

	@Override
	public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//		Log.d(TAG, "--onNestedFling:" + target.getClass().getSimpleName() + "/" + velocityX + "/" + velocityY + "/" + consumed);
		return false;
	}

	@Override
	public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
//		Log.d(TAG, "--onNestedPreFling:" + target.getClass().getSimpleName() + "/" + velocityX + "/" + velocityY);
		return false;
	}

	@Override
	public int getNestedScrollAxes() {
		int flag = helper.getNestedScrollAxes();
//		Log.d(TAG, "--getNestedScrollAxes:" + flag);
		return flag;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		Log.d(TAG, "dispatchTouchEvent" + "/" + ev.getX() + "/" + ev.getY());
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		Log.d(TAG, "onInterceptTouchEvent" + "/" + ev.getX() + "/" + ev.getY());
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		Log.d(TAG, "onInterceptTouchEvent" + "/" + ev.getX() + "/" + ev.getY());
		return super.onTouchEvent(ev);
	}


}
