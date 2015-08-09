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
import android.widget.SeekBar;

/**
 * A sample activity showing some of the functions of the progress bar 
 */
public class main extends Activity {
	private boolean isRunning;
	private ProgressWheel pwOne;
    private SeekBar seekBarProgress;
    private Button btnSpin, btnIncrement;
	int progress = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_wheel_activity);
        pwOne = (ProgressWheel) findViewById(R.id.progressBarTwo);
        seekBarProgress = (SeekBar) findViewById(R.id.progressAmount);
        btnSpin = (Button) findViewById(R.id.btn_spin);
        btnIncrement = (Button) findViewById(R.id.btn_increment);

        seekBarProgress.setOnSeekBarChangeListener(new ProgressUpdater(pwOne));
        btnSpin.setOnClickListener(new SpinListener(pwOne, btnSpin, seekBarProgress));
        btnIncrement.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pwOne.incrementProgress(36);
            }
        });
	}
	
	@Override
	public void onPause() {
		super.onPause();
		pwOne.stopSpinning();
		pwOne.resetCount();
		pwOne.setText("Click\none of the\nbuttons");
	}

    private static final class SpinListener implements OnClickListener {

        private boolean isRunning = false;
        private final ProgressWheel wheel;
        private final Button spinButton;
        private final SeekBar seekBar;
        private int cachedProgress = 0;

        SpinListener(ProgressWheel wheel, Button spinButton, SeekBar seekBar) {
            this.wheel = wheel;
            this.spinButton = spinButton;
            this.seekBar = seekBar;
        }

        @Override
        public void onClick(View button) {
            isRunning = !isRunning;
            if(isRunning) {
                cachedProgress = wheel.getProgress();
                wheel.resetCount();
                wheel.setText("Spinning...");
                wheel.spin();
                spinButton.setText("Stop spinning");
            } else {
                spinButton.setText("Start spinning");
                wheel.setText("");
                wheel.stopSpinning();
                wheel.setProgress(cachedProgress);
            }
            seekBar.setEnabled(!isRunning);
        }
    }

            /*ShapeDrawable bg = new ShapeDrawable(new RectShape());
        int[] pixels = new int[] { 0xFF2E9121, 0xFF2E9121, 0xFF2E9121,
            0xFF2E9121, 0xFF2E9121, 0xFF2E9121, 0xFFFFFFFF, 0xFFFFFFFF};
        Bitmap bm = Bitmap.createBitmap(pixels, 8, 1, Bitmap.Config.ARGB_8888);
        Shader shader = new BitmapShader(bm,
            Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);*/

    private static class ProgressUpdater implements SeekBar.OnSeekBarChangeListener {

        private final ProgressWheel wheel;

        ProgressUpdater(ProgressWheel wheel) {
            this.wheel = wheel;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            double progress = 360.0 * (seekBar.getProgress() / 100.0);
            wheel.setProgress((int) progress);
        }

        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}
