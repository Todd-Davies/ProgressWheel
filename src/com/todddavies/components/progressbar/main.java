package com.todddavies.components.progressbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class main extends Activity {
	boolean running;
	ProgressWheel pw;
	int progress = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_wheel_activity);
        pw = (ProgressWheel) findViewById(R.id.progressBarTwo);
        //pw.spin();
        
        final Runnable r = new Runnable() {
			public void run() {
				running = true;
				while(progress<361) {
					pw.incrementProgress();
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
					if(pw.isSpinning) {
						pw.stopSpinning();
					}
					pw.resetCount();
					pw.setText("Loading...");
					pw.spin();
				}
			}
        });
        
        Button increment = (Button) findViewById(R.id.btn_increment);
        increment.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(!running) {
					progress = 0;
					pw.resetCount();
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
		pw.stopSpinning();
		pw.resetCount();
		pw.setText("Click\none of the\nbuttons");
	}
}
