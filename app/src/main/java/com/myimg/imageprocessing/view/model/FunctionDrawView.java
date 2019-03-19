package com.myimg.imageprocessing.view.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

//自定义画图函数的view
public class FunctionDrawView extends View {

    private float pts[];
    private Paint mPaint;// 创建一个画笔
    private int width, height;

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
        this.pts = pts;
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
        mPaint.setStrokeWidth(5f);
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
//        draw();

        //绘制直线
        drawLines(canvas);

        //绘制一组点，坐标一一对应
//        canvas.drawPoints(new float[]{
//                900, 100,
//                500, 200,
//                500, 300
//        }, mPaint);

    }

    /**
     * 画线
     * 坐标轴的范围最大值为255 最小值为0
     * 但是左边和下边留5px话坐标轴,所以所有坐标+5
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        float maxX, maxY = 255f;
        maxX = 0;
        for (int i = 0; i < pts.length; i++) {
            if(maxX < pts[i]){
                maxX = pts[i];
            }
        }

        for (int i = 0; i < pts.length; i++) {
            switch (i % 4) {
                case 0:
                    //起始点的X坐标
                case 2:
                    //结束点的X坐标
                    pts[i] = pts[i] * maxX / width;//按比例转化
//                    pts[i] += 5;
                    break;
                case 1:
                    //开始点的Y坐标
                case 3:
                    //结束点的Y坐标
                    pts[i] = 255 - pts[i];//先反过来
                    pts[i] = pts[i] * maxY / height;//按比例转化
//                    pts[i] -= 5;
                    break;
            }

        }
        canvas.drawLines(pts, mPaint);
    }
}
