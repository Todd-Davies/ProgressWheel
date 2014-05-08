package com.todddavies.components.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;


/**
 * An indicator of progress, similar to Android's ProgressBar.
 * Can be used in 'spin mode' or 'increment mode'
 *
 * @author Todd Davies
 *         <p/>
 *         Licensed under the Creative Commons Attribution 3.0 license see:
 *         http://creativecommons.org/licenses/by/3.0/
 */
public class ProgressWheel extends View {

    //Sizes (with defaults)
    private int layout_height = 0;
    private int layout_width = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 20;
    private int rimWidth = 20;
    private int textSize = 20;
    private float contourSize = 0;

    //Padding (with defaults)
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;

    //Colors (with defaults)
    private int barColor = 0xAA000000;
    private int contourColor = 0xAA000000;
    private int circleColor = 0x00000000;
    private int rimColor = 0xAADDDDDD;
    private int textColor = 0xFF000000;

    //Paints
    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint contourPaint = new Paint();

    //Rectangles
    @SuppressWarnings("unused")
    private RectF rectBounds = new RectF();
    private RectF circleBounds = new RectF();
    private RectF circleOuterContour = new RectF();
    private RectF circleInnerContour = new RectF();

    //Animation
    //The amount of pixels to move the bar by on each draw
    private int spinSpeed = 2;
    //The number of milliseconds to wait inbetween each draw
    private int delayMillis = 0;
    private Handler spinHandler = new Handler() {
        /**
         * This is the code that will increment the progress variable
         * and so spin the wheel
         */
        @Override
        public void handleMessage(Message msg) {
            invalidate();
            if (isSpinning) {
                progress += spinSpeed;
                if (progress > 360) {
                    progress = 0;
                }
                spinHandler.sendEmptyMessageDelayed(0, delayMillis);
            }
            //super.handleMessage(msg);
        }
    };
    int progress = 0;
    boolean isSpinning = false;

    //Other
    private String text = "";
    private String[] splitText = {};

    /**
     * The constructor for the ProgressWheel
     *
     * @param context
     * @param attrs
     */
    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.ProgressWheel));
    }

    //----------------------------------
    //Setting up stuff
    //----------------------------------

    /*
     * When this is called, make the view square.
     * From: http://www.jayway.com/2012/12/12/creating-custom-android-views-part-4-measuring-and-how-to-force-a-view-to-be-square/
     * 
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	// The first thing that happen is that we call the superclass 
    	// implementation of onMeasure. The reason for that is that measuring 
    	// can be quite a complex process and calling the super method is a 
    	// convenient way to get most of this complexity handled.
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    	// We can’t use getWidth() or getHight() here. During the measuring 
    	// pass the view has not gotten its final size yet (this happens first 
    	// at the start of the layout pass) so we have to use getMeasuredWidth() 
    	// and getMeasuredHeight().
        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();
        
        // Finally we have some simple logic that calculates the size of the view 
        // and calls setMeasuredDimension() to set that size.
        // Before we compare the width and height of the view, we remove the padding, 
        // and when we set the dimension we add it back again. Now the actual content 
        // of the view will be square, but, depending on the padding, the total dimensions 
        // of the view might not be.
        if (widthWithoutPadding > heigthWithoutPadding) {
            size = heigthWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }
        
        // If you override onMeasure() you have to call setMeasuredDimension(). 
        // This is how you report back the measured size.  If you don’t call
        // setMeasuredDimension() the parent will throw an exception and your 
        // application will crash.        
        // We are calling the onMeasure() method of the superclass so we don’t 
        // actually need to call setMeasuredDimension() since that takes care 
        // of that. However, the purpose with overriding onMeasure() was to 
        // change the default behaviour and to do that we need to call 
        // setMeasuredDimension() with our own values.
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Share the dimensions
        layout_width = w;
        layout_height = h;

        setupBounds();
        setupPaints();
        invalidate();
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);

        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        contourPaint.setColor(contourColor);
        contourPaint.setAntiAlias(true);
        contourPaint.setStyle(Style.STROKE);
        contourPaint.setStrokeWidth(contourSize);
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds() {
        // Width should equal to Height, find the min value to steup the circle
        int minValue = Math.min(layout_width, layout_height);

        // Calc the Offset if needed
        int xOffset = layout_width - minValue;
        int yOffset = layout_height - minValue;

        // Add the offset
        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        int width = getWidth(); //this.getLayoutParams().width;
        int height = getHeight(); //this.getLayoutParams().height;

        rectBounds = new RectF(paddingLeft,
                paddingTop,
                width - paddingRight,
                height - paddingBottom);

        circleBounds = new RectF(paddingLeft + barWidth,
                paddingTop + barWidth,
                width - paddingRight - barWidth,
                height - paddingBottom - barWidth);
        circleInnerContour = new RectF(circleBounds.left + (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.top + (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.right - (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.bottom - (rimWidth / 2.0f) - (contourSize / 2.0f));
        circleOuterContour = new RectF(circleBounds.left - (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.top - (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.right + (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.bottom + (rimWidth / 2.0f) + (contourSize / 2.0f));

        fullRadius = (width - paddingRight - barWidth) / 2;
        circleRadius = (fullRadius - barWidth) + 1;
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        barWidth = (int) a.getDimension(R.styleable.ProgressWheel_barWidth,
                barWidth);

        rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_rimWidth,
                rimWidth);

        spinSpeed = (int) a.getDimension(R.styleable.ProgressWheel_spinSpeed,
                spinSpeed);

        delayMillis = a.getInteger(R.styleable.ProgressWheel_delayMillis,
                delayMillis);
        if (delayMillis < 0) {
            delayMillis = 0;
        }

        barColor = a.getColor(R.styleable.ProgressWheel_barColor, barColor);

        barLength = (int) a.getDimension(R.styleable.ProgressWheel_barLength,
                barLength);

        textSize = (int) a.getDimension(R.styleable.ProgressWheel_textSize,
                textSize);

        textColor = (int) a.getColor(R.styleable.ProgressWheel_textColor,
                textColor);

        //if the text is empty , so ignore it
        if (a.hasValue(R.styleable.ProgressWheel_text)) {
            setText(a.getString(R.styleable.ProgressWheel_text));
        }

        rimColor = (int) a.getColor(R.styleable.ProgressWheel_rimColor,
                rimColor);

        circleColor = (int) a.getColor(R.styleable.ProgressWheel_circleColor,
                circleColor);

        contourColor = a.getColor(R.styleable.ProgressWheel_contourColor, contourColor);
        contourSize = a.getDimension(R.styleable.ProgressWheel_contourSize, contourSize);


        // Recycle
        a.recycle();
    }

    //----------------------------------
    //Animation stuff
    //----------------------------------

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw the inner circle
        canvas.drawArc(circleBounds, 360, 360, false, circlePaint);
        //Draw the rim
        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
        canvas.drawArc(circleOuterContour, 360, 360, false, contourPaint);
        canvas.drawArc(circleInnerContour, 360, 360, false, contourPaint);
        //Draw the bar
        if (isSpinning) {
            canvas.drawArc(circleBounds, progress - 90, barLength, false,
                    barPaint);
        } else {
            canvas.drawArc(circleBounds, -90, progress, false, barPaint);
        }
        //Draw the text (attempts to center it horizontally and vertically)
        float textHeight = textPaint.descent() - textPaint.ascent();
        float verticalTextOffset = (textHeight / 2) - textPaint.descent();

        for (String s : splitText) {
            float horizontalTextOffset = textPaint.measureText(s) / 2;
            canvas.drawText(s, this.getWidth() / 2 - horizontalTextOffset,
                    this.getHeight() / 2 + verticalTextOffset, textPaint);
        }
    }

    /**
    *   Check if the wheel is currently spinning
    */
    
    public boolean isSpinning() {
        if(isSpinning){
            return true;
        } else {
            return false;
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
        if (progress > 360)
            progress = 0;
//        setText(Math.round(((float) progress / 360) * 100) + "%");
        spinHandler.sendEmptyMessage(0);
    }


    /**
     * Set the progress to a specific value
     */
    public void setProgress(int i) {
        isSpinning = false;
        progress = i;
        spinHandler.sendEmptyMessage(0);
    }

    //----------------------------------
    //Getters + setters
    //----------------------------------

    /**
     * Set the text in the progress bar
     * Doesn't invalidate the view
     *
     * @param text the text to show ('\n' constitutes a new line)
     */
    public void setText(String text) {
        this.text = text;
        splitText = this.text.split("\n");
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarLength() {
        return barLength;
    }

    public void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getRimColor() {
        return rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
    }


    public Shader getRimShader() {
        return rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }
}
