package com.myimg.imageprocessing.view.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

//自定义画图函数的view
public class FunctionDrawView extends View {

    private float pts[];
    private Paint mPaint;// 创建一个画笔
    private int width, height;
    private final float AXIS_OFFSET = 7.5f;//坐标轴的偏移量

    public FunctionDrawView(Context context) {
        super(context);

    }

    public FunctionDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FunctionDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        mPaint = new Paint();
        //设置画笔颜色
        mPaint.setColor(Color.BLACK);
        //STROKE                //描边
        //FILL                  //填充
        //FILL_AND_STROKE       //描边加填充
        //设置画笔模式
        mPaint.setStyle(Paint.Style.FILL);
        //设置画笔宽度为30px
        mPaint.setStrokeWidth(4f);

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
        initPaint();
//        canvas.drawPoint(200, 200, mPaint);

        //画坐标轴
        drawAxis(canvas);

        //绘制直线
        drawLines(canvas);

    }

    private void drawAxis(Canvas canvas) {
        Paint paint = new Paint();
        //设置画笔颜色
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5f);
        //X轴
        canvas.drawLine(AXIS_OFFSET, height - AXIS_OFFSET, width - 2 * AXIS_OFFSET, height - AXIS_OFFSET, paint);
        // 绘制箭头
        Path path = new Path();
        path.moveTo(width, height - AXIS_OFFSET);
        path.lineTo(width - AXIS_OFFSET * 2, height - AXIS_OFFSET * 2);
        path.lineTo(width - AXIS_OFFSET * 2, height);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);
        //y轴
        canvas.drawLine(AXIS_OFFSET, height - AXIS_OFFSET, AXIS_OFFSET, AXIS_OFFSET * 2, paint);
        // 绘制箭头
        Path path1 = new Path();
        path1.moveTo(AXIS_OFFSET, 0);
        path1.lineTo(AXIS_OFFSET * 2, AXIS_OFFSET * 2);
        path1.lineTo(0, AXIS_OFFSET * 2);
        path1.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path1, paint);

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
            if (maxX < ptsTemp[i]) {
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
                    ptsTemp[i] += AXIS_OFFSET;
                    break;
                case 1:
                    //开始点的Y坐标
                case 3:
                    //结束点的Y坐标
                    ptsTemp[i] = 255 - ptsTemp[i];//先反过来
                    ptsTemp[i] = ptsTemp[i] * (height - AXIS_OFFSET) / maxY;//按比例转化
                    ptsTemp[i] -= AXIS_OFFSET;
                    break;
            }

        }
        canvas.drawLines(ptsTemp, mPaint);
    }
}
