
package com.yuanwei.resistance.ui.widget;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * A special {@link Button} that does not turn into the pressed state when when
 * the parent is already pressed.
 * 
 * @author Yuanwei Chen
 */

public class TouchAnimationButton extends Button {

    public TouchAnimationButton(Context context) {
        super(context);
    }

    public TouchAnimationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchAnimationButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
   public void setOnTouchListener(OnTouchListener l) {
    	
    	super.setOnTouchListener(buttonOnTouchListener);
    	
    }; 
    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l){
    	
    	super.setOnFocusChangeListener(buttonOnFocusChangeListener);
    };
   
   
    private final static float[] BT_SELECTED=new float[] {      
        2, 0, 0, 0, 2,      
        0, 2, 0, 0, 2,      
        0, 0, 2, 0, 2,      
        0, 0, 0, 1, 0 };     
         
   
    private final static float[] BT_NOT_SELECTED=new float[] {      
        1, 0, 0, 0, 0,      
        0, 1, 0, 0, 0,      
        0, 0, 1, 0, 0,      
        0, 0, 0, 1, 0 };     
    
    
   
    private  OnFocusChangeListener buttonOnFocusChangeListener=new OnFocusChangeListener() {     
         
    @Override    
    public void onFocusChange(View v, boolean hasFocus) {     
     if (hasFocus) {     
      v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));     
      v.invalidate();
     }     
     else    
     {     
      v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));     
      v.invalidate();    
     }     
    }     
   };     
        
  
   private OnTouchListener buttonOnTouchListener=new OnTouchListener() {     
    @Override    
    public boolean onTouch(View v, MotionEvent event) {     
     if(event.getAction() == MotionEvent.ACTION_DOWN){     
      v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));     
      v.invalidate();
      System.out.print("Pressed");
      v.performClick();
      }     
      else if(event.getAction() == MotionEvent.ACTION_UP){     
       v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));     
       v.invalidate();    
      }else if(event.getAction()==MotionEvent.ACTION_CANCEL){
          v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));     
          v.invalidate(); 
      }
     return false;     
    }     
   };

   
}
