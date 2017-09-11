package com.liang.nestedscrollingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.liang.nestedscrollingdemo.horizontalAndVertical.HVScrollActivity;
import com.liang.nestedscrollingdemo.scrollTranslatePosition.ScrollTranslatePositionActivity;

public class HomeActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		findViewById(R.id.btn_to_main).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
			}
		});
		findViewById(R.id.btn_to_vh).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), HVScrollActivity.class));
			}
		});

		findViewById(R.id.btn_to_pts).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ScrollTranslatePositionActivity.class));
			}
		});

	}

}
