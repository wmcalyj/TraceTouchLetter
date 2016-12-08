package com.example.tracetouchletters.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.tracetouchletters.R;
import com.example.tracetouchletters.constants.AppCtx;
import com.example.tracetouchletters.constants.HapticPlacement;
import com.example.tracetouchletters.letterdirection.AllLettersDirection;
import com.example.tracetouchletters.letterdirection.LetterDirection;
import com.example.tracetouchletters.service.MyTPadService;

import nxr.tpad.lib.TPad;

import static com.example.tracetouchletters.constants.HapticPlacement.BACKGROUND;
import static com.example.tracetouchletters.constants.HapticPlacement.DIRECTION;
import static com.example.tracetouchletters.constants.HapticPlacement.LETTER;

public class DrawDirectionMyImageView extends ImageView {
    private static final String TAG = "DrawDirectionImageView";
    protected Paint paint;
    protected Rect bounds;
    int viewHeight;
    int viewWidth;
    Bitmap mBmp;
    Canvas myCanvas;
    String text = "A";
    boolean log = true;
    // Letter direction
    LetterDirection letterDirection;
    // For drawing direction
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint;
    //initial color
    private int paintColor = 0xFF660000;

    public DrawDirectionMyImageView(Context context) {
        super(context);
        init(context);
    }

    public DrawDirectionMyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public DrawDirectionMyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        paint = new Paint();
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
        bounds = new Rect();

        // For drawing direction
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        // For letter direction
        letterDirection = new LetterDirection(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBmp == null || myCanvas == null) {
            createBitmap();
        }
        if (viewHeight == 0 || viewWidth == 0) {
            viewHeight = this.getHeight();
            viewWidth = this.getWidth();
        }
        if (paint == null) {
            paint = new Paint();
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
        }
        drawChar(myCanvas);
        drawDirection(canvas);
    }

    protected void createBitmap() {
        viewHeight = this.getHeight();
        viewWidth = this.getWidth();
        if (mBmp == null) {
            mBmp = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        }
        if (myCanvas == null) {
            myCanvas = new Canvas(mBmp);
        }
    }


    protected void drawChar(Canvas canvas) {

        float textSize = paint.getTextSize();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int measuredHeight = bounds.height();
        int measuredWidth = bounds.width();
        if (measuredWidth < viewWidth && measuredHeight < viewHeight) {
            while (measuredWidth < viewWidth && measuredHeight < viewHeight) {
                paint.setTextSize(textSize++);
                paint.getTextBounds(text, 0, text.length(), bounds);
                measuredHeight = bounds.height();
                measuredWidth = bounds.width();
            }
            textSize--;
        } else if (measuredWidth > viewWidth || measuredHeight > viewHeight) {
            while (measuredWidth > viewWidth || measuredHeight > viewHeight) {
                paint.setTextSize(textSize--);
                paint.getTextBounds(text, 0, text.length(), bounds);
                measuredHeight = bounds.height();
                measuredWidth = bounds.width();
            }
        }
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), bounds);
//        if (log) {
//            Log.d(TAG, "TextSize: " + paint.getTextSize());
//            Log.d(TAG, "TextSize: " + paint.getTextSize());
//            Log.d(TAG, "measuredWidth: " + measuredWidth + ", measuredHeight: " +
//                    measuredHeight);
//            Log.d(TAG, "viewWidth: " + viewWidth + ", viewHeight: " + viewHeight);
//            Log.d(TAG, "StartX: " + -bounds.left + ", StartY: " + -bounds.top);
//
//        }

        myCanvas.drawText(text, -bounds.left,
                -bounds.top,
                paint);
//        myCanvas.drawBitmap(mBmp, 0, 0, paint);
        this.setImageBitmap(mBmp);
    }

    private void drawDirection(Canvas canvas) {
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                letterDirection.startRecording(touchX, touchY);
                drawPath.moveTo(touchX, touchY);
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                letterDirection.recordingDirection(touchX, touchY);
                drawPath.lineTo(touchX, touchY);
                invalidate();

                break;
            case MotionEvent.ACTION_UP:
                letterDirection.finishRecordingDirection(touchX, touchY);
                myCanvas.drawPath(drawPath, drawPaint);
                invalidate();

//                drawPath.reset();
                break;
        }
        return true;
    }

    public void saveLetterDirection() {
        AllLettersDirection.getInstance().saveLetterDirection(letterDirection);
    }


}
