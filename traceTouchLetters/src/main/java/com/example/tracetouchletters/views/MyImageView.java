package com.example.tracetouchletters.views;

import com.example.tracetouchletters.constants.AppCtx;
import com.example.tracetouchletters.R;
import com.example.tracetouchletters.constants.HapticPlacement;
import com.example.tracetouchletters.service.MyTPadService;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import nxr.tpad.lib.TPad;

import static com.example.tracetouchletters.constants.HapticPlacement.BACKGROUND;
import static com.example.tracetouchletters.constants.HapticPlacement.DIRECTION;
import static com.example.tracetouchletters.constants.HapticPlacement.LETTER;

public class MyImageView extends ImageView {
    private static final String TAG = "MyImageView";
    final int paintTextLetterColor = Color.BLACK;
    final int paintTextBackgroundColor = Color.WHITE;
    final int paintTextDirectionColor = Color.RED;
    final int letterBackgroundColor = Color.WHITE;
    final int backgroundBackgroundColor = Color.BLACK;
    final int directionBackgroundColor = Color.GRAY;
    protected Paint paint;
    protected Rect bounds;
    Bitmap mBmp;
    Canvas myCanvas;
    int viewHeight;
    int viewWidth;
    TPad mTpad;
    MyTPadService myTpadSevice;
    boolean noChar, isPreview;
    Integer hapticType;
    private boolean log = false;
    private Character ch = 'A';


    public MyImageView(Context context) {
        super(context);
        init(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint = new Paint();
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        bounds = new Rect();
        myTpadSevice = new MyTPadService();
        readCustomAttributes(context, attrs);
    }

    protected void readCustomAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.MyImageView,
                    0, 0);
            try {
                noChar = a.getBoolean(R.styleable.MyImageView_noChar, false);
                isPreview = a.getBoolean(R.styleable.MyImageView_previewMode, false);
                if (noChar) {
                    isPreview = true;
                }
                if (isPreview) {
                    int hapticPlacementType = a.getInt(R.styleable
                            .MyImageView_previewHapticPlacement, 1);
                    if (hapticPlacementType == 0) {
                        // Background
                        hapticType = HapticPlacement.BACKGROUND;
                    } else if (hapticPlacementType == 2) {
                        // Direction
                        hapticType = HapticPlacement.DIRECTION;
                    } else {
                        hapticType = LETTER;
                    }
                } else {
                    hapticType = AppCtx.hapticPlacement;
                }

            } finally {
                a.recycle();
            }
        }
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

    @Override
    protected void onDraw(Canvas canvas) {
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
        configureHapticPlacement();
        if (!noChar) {
            drawCharBitmap(isPreview);
        } else {
            drawNoCharBitmap();
        }
        super.onDraw(canvas);

    }

    protected void configureHapticPlacement() {
        if (hapticType == null) {
            Log.e(TAG, "Haptic Placement is NULL");
            return;
        }
        if (hapticType == LETTER) {
            myCanvas.drawColor(letterBackgroundColor);
            paint.setColor(paintTextLetterColor);
        } else if (hapticType == BACKGROUND) {
            myCanvas.drawColor(backgroundBackgroundColor);
            paint.setColor(paintTextBackgroundColor);
        } else if (hapticType == DIRECTION) {
            // TODO
            // For testing purpose
            myCanvas.drawColor(directionBackgroundColor);
            paint.setColor(paintTextDirectionColor);
        } else {
            Log.e(TAG, "Unknown Haptic Placement: " + hapticType);
        }
    }

    protected void drawCharBitmap(boolean isPreview) {
        String text = isPreview ? "A" : String.valueOf(ch);
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
        if (log) {
            Log.d(TAG, "TextSize: " + paint.getTextSize());
            Log.d(TAG, "TextSize: " + paint.getTextSize());
            Log.d(TAG, "measuredWidth: " + measuredWidth + ", measuredHeight: " +
                    measuredHeight);
            Log.d(TAG, "viewWidth: " + viewWidth + ", viewHeight: " + viewHeight);
        }

        int startX = (int) Math.floor((viewWidth / 2 - measuredWidth / 2));
        int startY = viewHeight / 2 + measuredHeight / 2;
        if (log) {
            Log.d(TAG, "StartX: " + startX + ", StartY: " + startY);
        }
        myCanvas.drawText(String.valueOf(ch), -bounds.left,
                -bounds.top,
                paint);
        this.setImageBitmap(mBmp);
    }

    protected void drawNoCharBitmap() {
        myCanvas.drawColor(Color.GRAY);
        this.setImageBitmap(mBmp);
    }

    protected void resetMyCanvas() {
        myCanvas.drawColor(Color.WHITE);
    }

    public void drawNextChar() {
        resetMyCanvas();
        nextChar();
        this.invalidate();
    }

    public void drawPrevChar() {
        resetMyCanvas();
        prevChar();
        this.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTpad == null || !mTpad.getBound()) {
            Log.e(TAG, "TPad is null or not connected");
            return false;
        }
        generateFriction(isPreview, noChar, event);
        return true;
    }

    protected void generateFriction(boolean isPreview, boolean noChar, MotionEvent event) {
        if (!isPreview) {
            generateCharFriction(AppCtx.hapticPlacement, event, false);
        } else {
            if (!noChar) {
                generateCharFriction(hapticType, event, isPreview);
            } else {
                generateNoCharFriction(event);
            }
        }
    }

    protected void generateCharFriction(int placement,
                                        MotionEvent event, boolean isPreview) {
        if (isPreview && AppCtx.tmpNoHaptic) {
            return;
        }
        if ((int) event.getY() < 0 || (int) event.getY() >= mBmp.getHeight() || (int) event.getX()
                < 0 || (int) event.getX() >= mBmp.getWidth()) {
            return;
        }
        int pixelColor = mBmp.getPixel((int) event.getX(), (int) event.getY());
        switch (placement) {
            case LETTER:
                if (compareColor(paintTextLetterColor, pixelColor)) {
                    Log.i(TAG, "Same color (" + pixelColor + ") at pos: " + event.getX() + "," +
                            " " +
                            event.getY());
                    mTpad.sendFrictionBuffer(myTpadSevice.calculateFrictionBuffer(isPreview));
                } else {
                    mTpad.turnOff();
                }
                break;
            case BACKGROUND:
                if (!compareColor(paintTextBackgroundColor, pixelColor)) {
                    Log.i(TAG, "Different color (" + pixelColor + ") at pos: " + event.getX()
                            + ", "
                            + event.getY());
                    mTpad.sendFrictionBuffer(myTpadSevice.calculateFrictionBuffer(isPreview));
                } else {
                    mTpad.turnOff();
                }
                break;
            case DIRECTION:
                Log.i(TAG, "Haptic Placement is DIRECTION, do nothing");
                break;
            default:
                Log.e(TAG, "Unknown Haptic Placement: " + placement);
                break;
        }


    }

    protected void generateNoCharFriction(MotionEvent event) {
        // No char && preview:
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mTpad.sendFrictionBuffer(myTpadSevice.calculateFrictionBuffer((float)
                        (AppCtx
                                .tmpHapticStrength / 100.00), (float) (AppCtx
                        .tmpHapticRoughness /
                        100.00)));
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTpad.turnOff();
                break;
        }
    }

    public boolean compareColor(int toCompareColor, int pixelColor) {
        return pixelColor == toCompareColor;
    }

    public void setTpad(TPad tpad) {
        if (tpad != null) {
            mTpad = tpad;
        } else {
            Log.e(TAG, "Tpad is null, cannot use tpad");
        }
    }

    public boolean getTPadStatus() {
        if (mTpad == null || !mTpad.getBound()) {
            return false;
        } else {
            return true;
        }
    }

    public void nextChar() {
        if (ch == 'Z') {
            ch = 'A';
        } else if (ch == 'z') {
            ch = 'a';
        } else {
            ch = (char) (Integer.valueOf(ch) + 1);
        }
        invalidate();
    }

    public void prevChar() {
        if (ch == 'A') {
            ch = 'Z';
        } else if (ch == 'a') {
            ch = 'z';
        } else {
            ch = (char) (Integer.valueOf(ch) - 1);
        }
        invalidate();
    }

    public void changeToUpperCase() {
        ch = Character.toUpperCase(ch);
        invalidate();
    }

    public void changeToLowerCase() {
        ch = Character.toLowerCase(ch);
        invalidate();
    }

    public void disconnectTPad() {
        mTpad = null;
    }
}
