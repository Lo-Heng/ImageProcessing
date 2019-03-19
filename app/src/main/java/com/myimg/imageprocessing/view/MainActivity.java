package com.myimg.imageprocessing.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.myimg.imageprocessing.R;
import com.myimg.imageprocessing.view.model.FunctionDrawView;

public class MainActivity extends AppCompatActivity {
    private byte[] rawData;
    private ImageView imageView;
    FunctionDrawView mFunctionDrawView;
    private float[] mPoints = new float[500];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.iv_example_image);
        //将bitmap转换成一个伪灰度图，再转化成一个byte[]
        //将byte[]转换为bitmap
//		Bitmap bitmapOrg = BitmapFactory.decodeByteArray(rawData, 0, rawData.length);
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.example);
        Bitmap bitmapNew = bitmapOrg.copy(Bitmap.Config.ARGB_8888, true);
        mFunctionDrawView = findViewById(R.id.fv_function);
        mFunctionDrawView.getWidth();
        mFunctionDrawView.getHeight();
        mFunctionDrawView.invalidate();
        if (bitmapNew == null)
            Log.i("TAG", "null");
        Log.i("TAG", "copy end");
//        float[] pts = {50, 50, 400, 50,
//                400, 50, 400, 600,
//                400, 600, 50, 600,
//                60, 600, 50, 50};
//        mFunctionDrawView.setPts(pts);


        /**
         * 一种简单的灰度化算法，但是这种方法需要修改每个点的像素，如果一张图片比较大，则这个处理速度就堪忧了
         * 往往需要创建一个线程来处理
         */
        for (int i = 0, k = 0,times = 0; i < bitmapNew.getWidth(); i++) {
            for (int j = 0; j < bitmapNew.getHeight(); j++) {
                int col = bitmapNew.getPixel(i, j);//获取点（i，j）处的像素
                //分别获取ARGB的值
                int alpha = col & 0xFF000000;
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);

                //500可设置，就是某一行
                if (j == 300) {
                    //用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
                    int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);

                    //100为间隔可调整
                    if (i % 100 == 0) {
                        float spacingX = 100;//x坐标的间隔
                        mPoints[k++] = spacingX * times;//x坐标
                        mPoints[k++] = gray;//y坐标
                        times++;
                        if (k > 3) {
                            mPoints[k] = mPoints[k-2];
                            k++;
                            mPoints[k] = mPoints[k-2];
                            k++;
                        }
                        Log.d("MainActivity", "gray " + " i= " + i + "gray=" + gray + "k= " + k);
                    }
                    //新的ARGB
//                    int newColor = alpha | (gray << 16) | (gray << 8) | gray;
                    //在点（i,j）处设置新的像素
//                    bitmapNew.setPixel(i, j, newColor);
                } else {
//                    bitmapNew.setPixel(i, j, col);
                }

                //Log.v("tag",  Integer.toHexString(col));
            }
        }
        mFunctionDrawView.setPts(mPoints);
        mFunctionDrawView.invalidate();
        Log.i("TAG", "pro end");
//        sendMsg(bitmapNew);

    }

    private void sendMsg(Bitmap bitmapNew) {
        imageView.setImageBitmap(bitmapNew);
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
