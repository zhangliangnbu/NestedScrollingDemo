package com.liang.nestedscrollingdemo.horizontalAndVertical;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.util.Log;
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
		return true;
	}

	@Override
	public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
		helper.onNestedScrollAccepted(child, target, nestedScrollAxes);
	}

	@Override
	public void onStopNestedScroll(View target) {
		helper.onStopNestedScroll(target);
	}

	@Override
	public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
		Log.d(TAG, "--onNestedScroll:" + target.getClass().getSimpleName() + "/" + dxConsumed + "/" + dyConsumed + "/" + dxUnconsumed + "/" + dyUnconsumed);
		setY(getY() + dyUnconsumed);
	}

	@Override
	public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
		Log.d(TAG, "--onNestedPreScroll:" + target.getClass().getSimpleName() + "/" + dx + "/" + dy + "/" + Arrays.toString(consumed));
	}

	@Override
	public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
		Log.d(TAG, "--onNestedFling:" + target.getClass().getSimpleName() + "/" + velocityX + "/" + velocityY + "/" + consumed);
		return false;
	}

	@Override
	public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
		Log.d(TAG, "--onNestedPreFling:" + target.getClass().getSimpleName() + "/" + velocityX + "/" + velocityY);
		return false;
	}

	@Override
	public int getNestedScrollAxes() {
		return helper.getNestedScrollAxes();
	}
}
