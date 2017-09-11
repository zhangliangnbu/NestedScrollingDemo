package com.liang.nestedscrollingdemo.scrollTranslatePosition;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.liang.nestedscrollingdemo.R;

/**
 * parent scroll and translation 不影响 child的 position/translation/scroll的数值
 * parent scroll 移动的是子控件的坐标系，因此子控件的相对于它的参数不变。
 * parent translation 移动的是自己的坐标系，因此它相对于父控件的参数不变。
 */
public class ScrollTranslatePositionActivity extends AppCompatActivity {
	private static final String TAG = "STPActivity";
	private View tv;
	private View layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll_translate_position);

		tv = findViewById(R.id.tv_rec);
		layout = findViewById(R.id.frame_layout);

		tv.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					Log.d(TAG, "tv-ev_y:" + event.getY());
					Log.d(TAG, "tv-ev_rawY:" + event.getRawY());
				}
				return false;
			}
		});

		layout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					Log.d(TAG, "layout-ev_y:" + event.getY());
					Log.d(TAG, "layout-ev_rawY:" + event.getRawY());
				}
				return false;
			}
		});

		findViewById(R.id.btn_scroll_tv).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv.scrollBy(0, -20);
				Log.d(TAG, "----------tv_scrollY_-20----------");
				printPTS();
			}
		});
		findViewById(R.id.btn_scroll_frame).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layout.scrollBy(0, -20);
				Log.d(TAG, "----------layout_scrollY_-20----------");
				printPTS();
			}
		});
		findViewById(R.id.btn_translate_tv).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv.setTranslationY(tv.getTranslationY() + 20);
				Log.d(TAG, "----------tv_translateY_20----------");
				printPTS();
			}
		});
		findViewById(R.id.btn_translate_frame).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layout.setTranslationY(layout.getTranslationY() + 20);
				Log.d(TAG, "----------layout_translateY_20----------");
				printPTS();
			}
		});



	}

	private void printPTS() {
		printView(TAG+"--tv", tv);
		printView(TAG+"--layout", layout);
	}

	private void printView(String tag, View view) {
		Log.d(tag, "position-ltrb:" + view.getLeft() + "/" + view.getTop() + "/" + view.getRight() + "/" + view.getBottom());
		Log.d(tag, "translation-xy:" + view.getTranslationX() + "/" + view.getTranslationY());
		Log.d(tag, "xy:" + view.getX() + "/" + view.getY());
		Log.d(tag, "scroll-xy:" + view.getScrollX() + "/" + view.getScrollY());
	}

}
