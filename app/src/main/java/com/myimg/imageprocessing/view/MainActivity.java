package com.myimg.imageprocessing.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.myimg.imageprocessing.R;
import com.myimg.imageprocessing.view.model.FunctionDrawView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private byte[] rawData;
    private ImageView ivExampleImage;
    FunctionDrawView mFunctionDrawView;
    private float[] points;
    List<Float> mFloatArrayList = new ArrayList<Float>();
    private TextView mTvImgWidth;
    private TextView mTvImgHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定
        ivExampleImage =  findViewById(R.id.iv_example_image);
        mFunctionDrawView = findViewById(R.id.fv_function);
        mTvImgHeight = findViewById(R.id.tv_img_height);
        mTvImgWidth = findViewById(R.id.tv_img_width);

        //读入
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.example);
        Bitmap bitmapNew = bitmapOrg.copy(Bitmap.Config.ARGB_8888, true);

        mTvImgHeight.append(" " + bitmapNew.getHeight());
        mTvImgWidth.append(" " + bitmapNew.getWidth());

        if (bitmapNew == null)
            Log.i("TAG", "null");

        startCalculate(200, bitmapNew);


        points = new float[mFloatArrayList.size()];
        for (int i = 0; i < mFloatArrayList.size(); i++) {
            points[i] = mFloatArrayList.get(i);
        }

        mFunctionDrawView.setPts(points);

        Log.i("TAG", "pro end");
//        sendMsg(bitmapNew);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新存放数据，以免界面错乱
        mFunctionDrawView.setPts(points);
    }

    /**
     * @创建人 lzh
     * @方法描述
     * @参数 row 输入第几行
     * @创建时间 2019/3/20
     */
    private void startCalculate(int row, Bitmap bitmap) {

        for (int i = 0, k = 0, times = 0; i < bitmap.getWidth(); i++) {
//            for (int j = 0; j < bitmapNew.getHeight(); j++) {
            int col = bitmap.getPixel(i, row);//获取点的像素
            //分别获取ARGB的值
            int alpha = col & 0xFF000000;
            int red = (col & 0x00FF0000) >> 16;
            int green = (col & 0x0000FF00) >> 8;
            int blue = (col & 0x000000FF);

            //500可设置，就是某一行
            //用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
            int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);

            //100为间隔可调整
            if (i % 10 == 0) {
                float spacingX = 10;//x坐标的间隔
                mFloatArrayList.add(spacingX * times);
                mFloatArrayList.add((float) gray);

//                mPoints[k++] = spacingX * times;//x坐标
//                mPoints[k++] = gray;//y坐标
                times++;
                if (mFloatArrayList.size() > 3) {
//                    mPoints[k] = mPoints[k - 2];
                    mFloatArrayList.add(mFloatArrayList.get(mFloatArrayList.size() - 2));
                    mFloatArrayList.add(mFloatArrayList.get(mFloatArrayList.size() - 2));
//                    k++;
//                    mPoints[k] = mPoints[k - 2];
//                    k++;
                }
                Log.d("MainActivity", "gray " + " i= " + i + "gray=" + gray + "k= " + k);
            }
            //新的ARGB
//                    int newColor = alpha | (gray << 16) | (gray << 8) | gray;
            //在点（i,j）处设置新的像素
//                    bitmapNew.setPixel(i, j, newColor);

            //Log.v("tag",  Integer.toHexString(col));
        }
//        }

    }

    private void sendMsg(Bitmap bitmapNew) {
        ivExampleImage.setImageBitmap(bitmapNew);
    }
}

/**
 * drawableToBitmap
 * <p>
 * Bitmap2Bytes
 * <p>
 * Bytes2Bimap
 * <p>
 * Bitmap2Bytes
 * <p>
 * Bytes2Bimap
 * <p>
 * Bitmap2Bytes
 * <p>
 * Bytes2Bimap
 * <p>
 * Bitmap2Bytes
 * <p>
 * Bytes2Bimap
 * <p>
 * Bitmap2Bytes
 * <p>
 * Bytes2Bimap
 * <p>
 * Bitmap2Bytes
 * <p>
 * Bytes2Bimap
 */
/*
 public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap
                        .createBitmap(
                                        drawable.getIntrinsicWidth(),
                                        drawable.getIntrinsicHeight(),
                                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
}
 */

/**
 * Bitmap2Bytes
 */
/*
 private byte[] Bitmap2Bytes(Bitmap bm)
 {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
 	return baos.toByteArray();
 }
*/

/**
 * Bytes2Bimap
 */
/*
 private Bitmap Bytes2Bimap(byte[] b)
 {
    if(b.length ==0)
    {
    	return null;
    }

	return BitmapFactory.decodeByteArray(b, 0, b.length);

  }
*/

//    }
//            }
