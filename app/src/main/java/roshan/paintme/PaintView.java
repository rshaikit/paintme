package roshan.paintme;
/**
 * Created by Roshan Shaik on 6/4/2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Roshan on 6/4/2017.
 */

public class PaintView extends View {

    public static int BRUSH_SIZE =10;
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    public static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath>paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    public PaintView(Context context) {
        this (context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Cap.Round);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[]{1,1,1},0.4f,6,3.5f);
        mBlur = new BlurMaskFilter(5,BlurMaskFilter.Blur.NORMAL);
    }
        public void init(DisplayMetrics metrics){
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

            mBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARG8_8888);
            mCanvas = new Canvas(mBitmap);

            currentColor = DEFAULT_COLOR;
            strokeWidth = BRUSH_SIZE;
        }
        public void normal(){
        emboss =false;
        blur = false;
    }
    public void emboss(){
        emboss =true;
        blur = false;
    }
    public void blur(){
        emboss =false;
        blur = true;
    }
    public void clear(){
        backgroundColor =DEFAULT_BG_COLOR;
        paths.clear();
        normal();
        invalidate();
    }

    public ArrayList<FingerPath> getPaths() {
        return paths;
    }
    @Overrride
    protected void onDraw(Canvas canvas){
        canvas.save();
        mCanvas.drawColor(backgroundColor);
        for (FingerPath fp : paths){
            mpaint.selectColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);

            if(fp.emboss)
                mPaint.setmaskFilter(mEmboss);
            else if (fp.blur)
                mpaint.setMaskFilter(mblur);
            mCanvas.drawPath(fp.path,mPaint);
        }
        canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
        canvas.restore();
    }
    private void touchStart(float x,float y){
        mPath = new Path();
        FingerPath fp = new FingerPath (currentColor,emboss,strokeWidth,blur,mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x,y);
        mX=X;
        mY=Y;

    }
    private void touchMOve(float x, float y){
        float dx = Math.abs(x-mX);
        float dy = Math.abs(y-mY);

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            mPath.quadTo(mX,mY,(x + mX)/2 , (y+mY)/2);
            mX=x;
            mY=y;
        }

        public void touchup(){
        mPath.lineTo(mX,mY);

    }
    @Override
    public boolean onTouchEvent (MotionEvent){
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.Action_DOWN:
                touchStart(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                tocuchMove(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;


    }
    }
}
