package com.example.tracetouchletters.views;

import com.example.tracetouchletters.constants.AppCtx;
import com.example.tracetouchletters.R;
import com.example.tracetouchletters.constants.HapticPlacement;
import com.example.tracetouchletters.letterdirection.AllLettersDirection;
import com.example.tracetouchletters.letterdirection.Direction;
import com.example.tracetouchletters.letterdirection.LetterDirection;
import com.example.tracetouchletters.letterdirection.Point;
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

import java.util.LinkedList;
import java.util.List;

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
    private final boolean DEBUG = true;
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
    LetterDirection letterDirection;
    int cacheCount;
    // For direction
    private int directionIdx;
    private int pointIdx;
    private LinkedList<Point> moveCache;
    private double differTolerance;
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
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        bounds = new Rect();
        myTpadSevice = new MyTPadService(context);
        readCustomAttributes(context, attrs);

        letterDirection = AllLettersDirection.getInstance().getLetterDirection(ch);
        directionIdx = 0;
        pointIdx = 0;
        moveCache = new LinkedList<Point>();
        cacheCount = 3;
        differTolerance = 135;

        if (DEBUG) {
            checkLetterDirection(letterDirection);
        }


//        t.start();
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
//        if (DEBUG) {
//            Log.d(TAG, "TextSize: " + paint.getTextSize());
//            Log.d(TAG, "TextSize: " + paint.getTextSize());
//            Log.d(TAG, "measuredWidth: " + measuredWidth + ", measuredHeight: " +
//                    measuredHeight);
//            Log.d(TAG, "viewWidth: " + viewWidth + ", viewHeight: " + viewHeight);
//        Log.d(TAG, "StartX: " + -bounds.left + ", StartY: " + -bounds.top);
//        }

        myCanvas.drawText(text, -bounds.left,
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
    public boolean onTouchEvent(final MotionEvent event) {
        if (mTpad == null || !mTpad.getBound()) {
            Log.e(TAG, "TPad is null or not connected");
            return false;
        }
        generateFriction(isPreview, noChar, event);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }).start();
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
                if (letterDirection == null) {
                    Log.i(TAG, "No letter direction for letter: " + ch);
                } else {
                    generateDirectionCharFriction(isPreview, event, letterDirection);
                }
                break;
            default:
                Log.e(TAG, "Unknown Haptic Placement: " + placement);
                break;
        }
    }

    private void generateDirectionCharFriction(boolean isPreview, MotionEvent event,
                                               LetterDirection letterDirection) {
        // TODO
        Direction currentDirection = letterDirection.directions.get(directionIdx);
        List<Point> oneDirection = currentDirection.oneDirection;
        int totalPoints = oneDirection.size();
        Point currentPoint = oneDirection.get(pointIdx);

        float touchX = event.getX(), touchY = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                moveCache.offer(new Point(touchX, touchY));
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointOutOfBoard(mBmp.getPixel((int) touchX, (int) touchY))) {
                    mTpad.sendFrictionBuffer(myTpadSevice.calculateFrictionBuffer
                            (isPreview));
                    break;
                }
                if (pointIdx >= totalPoints - cacheCount - 1) {
                    pointIdx = totalPoints - cacheCount - 1;
                } else {
                    if (moveCache.size() <= cacheCount) {
                        moveCache.offer(new Point(touchX, touchY));
                    } else {
                        // Cache is full, calculate a
                        double averageAngle = 0;
                        for (int i = 0; i < moveCache.size() - 1; i++) {
                            double vectorSX = (moveCache.get(i + 1).x - moveCache.get(i).x),
                                    vectorSY = (moveCache.get(i + 1).y - moveCache.get(i).y);
                            double vectorTX = (oneDirection.get(i + 1 + pointIdx).x -
                                    oneDirection.get(i + pointIdx).x),
                                    vectorTY = (oneDirection.get(i + 1 + pointIdx).y -
                                            oneDirection.get(i + pointIdx).y);
                            double angle = (float) Math.toDegrees(Math.atan2(vectorTY - vectorSY,
                                    vectorTX - vectorSX));

                            Log.d(TAG, "vectorSX: " + vectorSX + ", vectorSY: " + vectorSY +
                                    ", vectorTX: " + vectorTX + ", vectorTY:" + vectorTY);
                            Log.d(TAG, "Angle is: " + angle);
                            averageAngle += Math.abs(angle);
                        }

                        averageAngle /= moveCache.size() - 1;
                        if (DEBUG) {
                            Log.d(TAG, "angle difference: " + averageAngle);
                        }
                        if (Math.abs(averageAngle) > differTolerance) {
                            mTpad.sendFrictionBuffer(myTpadSevice.calculateFrictionBuffer
                                    (isPreview));
                        } else {
                            mTpad.turnOff();
                        }
                        // Pop out the first element
                        if (!moveCache.isEmpty()) {
                            moveCache.pop();
                        }
                        // Insert the new element
                        moveCache.offer(new Point(touchX, touchY));
                        pointIdx++;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                directionIdx++;
                if (directionIdx == letterDirection.directions.size()) {
                    directionIdx = 0;
                }
                pointIdx = 0;
                moveCache.clear();
                mTpad.turnOff();
                break;
        }
    }

    private boolean pointOutOfBoard(int pixelColor) {
        if (!compareColor(paintTextDirectionColor, pixelColor)) {
            return true;
        } else {
            return false;
        }
    }

    private void checkLetterDirection(LetterDirection letterDirection) {
        if (letterDirection == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        String letter = letterDirection.letter;
        sb.append("For letter: ").append(letter).append("\n");
        int count = 1;
        for (Direction direction : letterDirection.directions) {
            sb.append("Direction ").append(count++).append(" {");
            for (Point p : direction.oneDirection) {
                sb.append("[").append(p.x).append(", ").append(p.y).append("]");
            }
            sb.append("}\n");
        }
        Log.i(TAG, sb.toString());
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
