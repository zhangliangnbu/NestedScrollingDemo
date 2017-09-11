package com.liang.nestedscrollingdemo.horizontalAndVertical;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.liang.nestedscrollingdemo.R;

/**
 * 使用NestedScrollingParent/Child 可防止父控件拿到事件并消费后，之后的事件不再传给子控件的问题
 */
public class HVScrollActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hvscroll);
	}
}
