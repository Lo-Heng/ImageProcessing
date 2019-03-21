package com.myimg.imageprocessing.view.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

//自定义画图函数的view
public class FunctionDrawView extends View {

    private float pts[];
    private Paint mPaint;// 创建一个画笔
    private int width, height;
    private final float AXIS_OFFSET = 15f;//坐标轴的偏移量
    private Canvas mCanvas;
    private SurfaceHolder sh;
    private Path mPath1 = new Path();
    ;
    private Path mPath2 = new Path();
    ;

    public FunctionDrawView(Context context) {
        super(context);
        mPaint = new Paint();
    }

    public FunctionDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

    }

    public FunctionDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    public void setPts(float[] pts) {
        this.pts = pts.clone();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 2.初始化画笔
     */
    private void initPaint() {

        //设置画笔颜色
        mPaint.setColor(Color.BLACK);
        //STROKE                //描边
        //FILL                  //填充
        //FILL_AND_STROKE       //描边加填充
        //设置画笔模式
        mPaint.setStyle(Paint.Style.FILL);
        //设置画笔宽度为30px
        mPaint.setStrokeWidth(4f);


        mPaint.setTextSize(20);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        initPaint();

        //画坐标轴
        drawAxis(canvas);

        //绘制直线
        drawLines(canvas);

    }


    public void clear() {
//        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }


    private void drawAxis(Canvas canvas) {
        mPath1.reset();
        mPath2.reset();
        Paint paint = new Paint();
        //设置画笔颜色
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5f);
        //X轴
        canvas.drawLine(AXIS_OFFSET, height - AXIS_OFFSET, width - 2 * AXIS_OFFSET, height - AXIS_OFFSET, paint);
        // 绘制箭头

        mPath1.moveTo(width, height - AXIS_OFFSET);
        mPath1.lineTo(width - AXIS_OFFSET * 2, height - AXIS_OFFSET * 2);
        mPath1.lineTo(width - AXIS_OFFSET * 2, height);
        mPath1.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(mPath1, paint);
        //y轴
        canvas.drawLine(AXIS_OFFSET, height - AXIS_OFFSET, AXIS_OFFSET, AXIS_OFFSET * 2, paint);
        // 绘制箭头
        mPath2.moveTo(AXIS_OFFSET, 0);
        mPath2.lineTo(AXIS_OFFSET * 2, AXIS_OFFSET * 2);
        mPath2.lineTo(0, AXIS_OFFSET * 2);
        mPath2.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(mPath2, paint);

    }

    /**
     * 画线
     * 坐标轴的范围最大值为255 最小值为0
     * 但是左边和下边留5px话坐标轴,所以所有坐标+5
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        float[] ptsTemp = pts.clone();
        float maxX, maxY = 255f;
        maxX = 0;
        for (int i = 0; i < ptsTemp.length; i++) {
            if (i % 2 == 0 && maxX < ptsTemp[i]) {
                maxX = ptsTemp[i];
            }
        }

        for (int i = 0; i < ptsTemp.length; i++) {
            switch (i % 4) {
                case 0:
                    //起始点的X坐标
                case 2:
                    //结束点的X坐标
                    ptsTemp[i] = ptsTemp[i] * (width - AXIS_OFFSET) / maxX;//按比例转化
                    ptsTemp[i] += AXIS_OFFSET;//图像往右边移动
                    break;
                case 1:
                    //开始点的Y坐标
                case 3:
                    //结束点的Y坐标
                    ptsTemp[i] = 255 - ptsTemp[i];//先反过来
                    ptsTemp[i] = ptsTemp[i] * (height - AXIS_OFFSET * 2) / maxY;//按比例转化
//                    ptsTemp[i] -= AXIS_OFFSET;
                    break;
            }

        }
        //原点
        canvas.drawText("0", 0, height, mPaint);
        //X最大值
        int max = (int)maxX;
        canvas.drawText(String.valueOf(max), width - AXIS_OFFSET * 4, height, mPaint);
        //y最大值
        canvas.drawText("255", AXIS_OFFSET * 2, 0, mPaint);

        canvas.drawLines(ptsTemp, mPaint);
    }
}
