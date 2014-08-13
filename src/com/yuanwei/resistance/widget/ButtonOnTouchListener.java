package com.yuanwei.resistance.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;

public class ButtonOnTouchListener implements OnTouchListener{
	Context context;
	
	AlphaAnimation alphaDown = new AlphaAnimation(1.0f, 0.5f);
	AlphaAnimation alphaUp = new AlphaAnimation(0.5f, 1.0f);
	TranslateAnimation leftAnim;
	TranslateAnimation rightAnim;
	

	public ButtonOnTouchListener(Context context){
		this.context=context;
		alphaDown.setDuration(100);
		alphaUp.setDuration(100);
		alphaDown.setFillAfter(true);
		alphaUp.setFillAfter(true);
		 leftAnim = new TranslateAnimation(0f, -5f, 0f, -5f);
		leftAnim.setDuration(10);
		
		leftAnim.setFillAfter(true);
		
		         
		rightAnim = new TranslateAnimation(0f, 5f, 0f, 5f);
		
		rightAnim.setDuration(10);
		rightAnim.setFillAfter(true);
		
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {

			//((Button) v).setBackgroundColor(Color.parseColor("#619883"));
			//((Button) v).setTextColor(context.getResources().getColor(
					//android.R.color.darker_gray));
			//Animation shake = AnimationUtils.loadAnimation(
					//context, R.anim.translate);
			//v.startAnimation(alphaDown);
			v.setAlpha(0.5f);
			v.setTranslationX(-5f);
			v.setTranslationY(-5f);
			//v.startAnimation(leftAnim);
			//v.invalidate();
			

			break;
		}
		case MotionEvent.ACTION_UP: {

			//((Button) v).setBackgroundColor(context.getResources().getColor(
					//android.R.color.transparent));
			//((Button) v).setTextColor(context.getResources().getColor(
					//android.R.color.white));
			//Animation shake = AnimationUtils.loadAnimation(
			//		context, R.anim.backtranslate);
			//v.startAnimation(alphaUp);
			v.setAlpha(1.0f);
			v.setTranslationX(5f);
			v.setTranslationY(5f);
			//v.startAnimation(rightAnim);
			//v.invalidate();
			//v.invalidate();

			
			break;
		}
		case MotionEvent.ACTION_CANCEL: {

			//((Button) v).setBackgroundColor(context.getResources().getColor(
			//		android.R.color.transparent));
			//((Button) v).setTextColor(context.getResources().getColor(
			//		android.R.color.white));
			//v.invalidate();
			//Animation shake = AnimationUtils.loadAnimation(
					//context, R.anim.backtranslate);
			//v.startAnimation(alphaUp);
			v.setAlpha(1.0f);
			v.setTranslationX(5f);
			v.setTranslationY(5f);
			//v.startAnimation(rightAnim);
			//v.invalidate();

			break;
		}
		}
		return false;
	}
	
}
