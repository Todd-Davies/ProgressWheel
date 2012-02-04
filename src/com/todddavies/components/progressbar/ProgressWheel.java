package com.todddavies.components.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class ProgressWheel extends View {
	
	//Sizes (with defaults)
	private int fullRadius = 100;
	private int circleRadius = 80;
	private int barLength = 60;
	private int barWidth = 20;
	private int textSize = 20;
	
	//Padding (with defaults)
	private int paddingTop = 5;
	private int paddingBottom = 5;
	private int paddingLeft = 5;
	private int paddingRight = 5;
	
	//Colors (with defaults)
	private int barColor = 0xAA000000;
	private int circleColor = 0x00000000;
	private int rimColor = 0xAADDDDDD;
	private int textColor = 0xFF000000;
	
	//Paints
	private Paint barPaint = new Paint();
	private Paint circlePaint = new Paint();
	private Paint rimPaint = new Paint();
	private Paint textPaint = new Paint();
	
	//Rectangles
	@SuppressWarnings("unused")
	private RectF rectBounds = new RectF();
	private RectF circleBounds = new RectF();
	
	//Animation
	private Handler spinHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			invalidate();
			if(isSpinning) {
				progress+=2;
				if(progress>360) {
					progress = 0;
				}
				spinHandler.sendEmptyMessage(0);
			}
			super.handleMessage(msg);
		}
	};
	int progress = 0;
	boolean isSpinning = false;
	
	//Other
	private String text = "";
	private String[] splitText = {};
	
	/**
	 * The constructor for the ProgressWheel
	 * @param context
	 * @param attrs
	 */
	public ProgressWheel(Context context, AttributeSet attrs) {
		super(context, attrs);

		parseAttributes(context.obtainStyledAttributes(attrs, 
				R.styleable.ProgressWheel));
	}
	
	/**
	 * Puts the view on spin mode
	 */
	public void spin() {
		isSpinning = true;
		spinHandler.sendEmptyMessage(0);
	}
	
	/**
	 * Increment the progress by 1 (of 360)
	 */
	public void incrementProgress() {
		isSpinning = false;
		progress++;
		setText(Math.round(((float)progress/360)*100) + "%");
		spinHandler.sendEmptyMessage(0);
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		setupBounds();
		setupPaints();
		invalidate();
	}
	
	/**
	 * Set the text in the progress bar
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
		splitText = this.text.split("\n");
	}
	
	
	private void setupPaints() {
		barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth);
        
        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Style.STROKE);
        rimPaint.setStrokeWidth(barWidth);
        
        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);
        
        textPaint.setColor(textColor);
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
	}

	private void setupBounds() {
		paddingTop = this.getPaddingTop();
	    paddingBottom = this.getPaddingBottom();
	    paddingLeft = this.getPaddingLeft();
	    paddingRight = this.getPaddingRight();
		
		rectBounds = new RectF(paddingLeft,
				paddingTop,
                this.getLayoutParams().width - paddingRight,
                this.getLayoutParams().height - paddingBottom);
		
		circleBounds = new RectF(paddingLeft + barWidth,
				paddingTop + barWidth,
                this.getLayoutParams().width - paddingRight - barWidth,
                this.getLayoutParams().height - paddingBottom - barWidth);
		
		fullRadius = (this.getLayoutParams().width - paddingRight - barWidth)/2;
	    circleRadius = (fullRadius - barWidth) + 1;
	}

	/**
	 * Parse the attributes passed to the view from the XML
	 * @param a
	 */
	private void parseAttributes(TypedArray a) {
		barWidth = (int) a.getDimension(R.styleable.ProgressWheel_barWidth, barWidth);
	    
	    barColor = a.getColor(R.styleable.ProgressWheel_barColor, barColor);
	    
	    barLength = (int) a.getDimension(R.styleable.ProgressWheel_barLength, barLength);
	    
	    textSize = (int) a.getDimension(R.styleable.ProgressWheel_textSize, textSize);
	    
	    textColor = (int) a.getColor(R.styleable.ProgressWheel_textColor, textColor);
	    
	    setText(a.getString(R.styleable.ProgressWheel_text));
	    
	    rimColor = (int) a.getColor(R.styleable.ProgressWheel_rimColor, rimColor);
	    
	    circleColor = (int) a.getColor(R.styleable.ProgressWheel_circleColor, circleColor);
	}


	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//Draw the rim
		canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
		//Draw the bar
		if(isSpinning) {
			canvas.drawArc(circleBounds, progress - 90, barLength, false, barPaint);
		} else {
			canvas.drawArc(circleBounds, -90, progress, false, barPaint);
		}
		//Draw the inner circle
		canvas.drawCircle(circleBounds.width()/2 + barWidth, 
				circleBounds.height()/2 + barWidth, 
				circleRadius, 
				circlePaint);
		//Draw the text
		int offsetNum = 0;
		for(String s : splitText) {
			float offset = textPaint.measureText(s) / 2;
			canvas.drawText(s, this.getWidth() / 2 - offset, 
				this.getHeight() / 2 + (textSize*(offsetNum)) - ((splitText.length-1)*(textSize/2)), textPaint);
			offsetNum++;
		}
	}

	/**
	 * Reset the count (in increment mode)
	 */
	public void resetCount() {
		progress = 0;
		setText("0%");
		invalidate();
	}

	/**
	 * Turn off spin mode
	 */
	public void stopSpinning() {
		isSpinning = false;
		progress = 0;
		spinHandler.removeMessages(0);
	}
}
