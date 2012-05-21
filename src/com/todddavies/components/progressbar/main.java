package com.todddavies.components.progressbar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * A sample activity showing some of the functions of the progress bar 
 */
public class main extends Activity {
	boolean running;
	ProgressWheel pw_two;
	ProgressWheel pw_three;
	ProgressWheel pw_four;
	//ProgressWheel pw_five;
	int progress = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_wheel_activity);
        pw_two = (ProgressWheel) findViewById(R.id.progressBarTwo);
        pw_three = (ProgressWheel) findViewById(R.id.progressBarThree);
        pw_four = (ProgressWheel) findViewById(R.id.progressBarFour);
        //pw_five = (ProgressWheel) findViewById(R.id.progressBarFive);
        
        ShapeDrawable bg = new ShapeDrawable(new RectShape());
        int[] pixels = new int[] { 0xFF2E9121, 0xFF2E9121, 0xFF2E9121,
            0xFF2E9121, 0xFF2E9121, 0xFF2E9121, 0xFFFFFFFF, 0xFFFFFFFF};
        Bitmap bm = Bitmap.createBitmap(pixels, 8, 1, Bitmap.Config.ARGB_8888);
        Shader shader = new BitmapShader(bm,
            Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        pw_three.setRimShader(shader);
        
        pw_three.spin();
        pw_four.spin();
        
        final Runnable r = new Runnable() {
			public void run() {
				running = true;
				while(progress<361) {
					pw_two.incrementProgress();
					progress++;
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				running = false;
			}
        };
        
        Button spin = (Button) findViewById(R.id.btn_spin);
        spin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(!running) {
					if(pw_two.isSpinning) {
						pw_two.stopSpinning();
					}
					pw_two.resetCount();
					pw_two.setText("Loading...");
					pw_two.spin();
				}
			}
        });
        
        Button increment = (Button) findViewById(R.id.btn_increment);
        increment.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(!running) {
					progress = 0;
					pw_two.resetCount();
					Thread s = new Thread(r);
					s.start();
				}
			}
        });
	}
	
	@Override
	public void onPause() {
		super.onPause();
		progress = 361;
		pw_two.stopSpinning();
		pw_two.resetCount();
		pw_two.setText("Click\none of the\nbuttons");
	}
}
