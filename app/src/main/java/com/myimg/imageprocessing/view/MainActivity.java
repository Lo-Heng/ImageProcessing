package com.myimg.imageprocessing.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myimg.imageprocessing.BuildConfig;

import com.myimg.imageprocessing.R;
import com.myimg.imageprocessing.view.model.FunctionDrawView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.myimg.imageprocessing.view.model.Constants.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private byte[] rawData;
    private float[] points;
    private int mSample = 10;//间隔
    private int mGap = 10;//采样间隔
    private int mRow = -1;//像素行数
    private ImageView ivExampleImage;
    FunctionDrawView mFunctionDrawView;

    List<Float> mFloatArrayList = new ArrayList<Float>();
    private TextView mTvImgWidth;
    private TextView mTvImgHeight;
    private Button mBtnLine;
    private Button mBtnGap;
    private EditText mEtGap;
    private EditText mEtLine;
    private Bitmap mBitmapNew;
    private File tempFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定
        ivExampleImage = findViewById(R.id.iv_example_image);
        mFunctionDrawView = findViewById(R.id.fv_function);
        mTvImgHeight = findViewById(R.id.tv_img_height);
        mTvImgWidth = findViewById(R.id.tv_img_width);
        mBtnLine = findViewById(R.id.btn_confirm_line);
        mBtnGap = findViewById(R.id.btn_confirm_gap);
        mEtLine = findViewById(R.id.et_line);
        mEtGap = findViewById(R.id.et_gap);

        mBtnLine.setOnClickListener(this);
        mBtnGap.setOnClickListener(this);
        ivExampleImage.setOnClickListener(this);
        ivExampleImage.setLongClickable(true);
        ivExampleImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                getPicFromCamera();
                return true;
            }
        });
        //读入bitmap
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.example);
        mBitmapNew = bitmapOrg.copy(Bitmap.Config.ARGB_8888, true);

        //显示图片像素
        mTvImgHeight.setText("图片高（像素）： " + mBitmapNew.getHeight());
        mTvImgWidth.setText("图片宽（像素）： " + mBitmapNew.getWidth());

        //清空点集合
        mFloatArrayList.clear();


        //阻塞计算灰度值
        startCalculate();


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
     * @参数 mRow 输入第几行
     * @创建时间 2019/3/20
     */
    private void startCalculate() {
        //如果没有初始化多少行 默认高度/2向下取整
        if (mRow == -1) {
            mRow = mBitmapNew.getHeight() / 2;
        }
        for (int i = 0, k = 0, times = 0; i < mBitmapNew.getWidth(); i++) {
//            for (int j = 0; j < bitmapNew.getHeight(); j++) {
            int col = mBitmapNew.getPixel(i, mRow);//获取点的像素
            //分别获取ARGB的值
            int alpha = col & 0xFF000000;
            int red = (col & 0x00FF0000) >> 16;
            int green = (col & 0x0000FF00) >> 8;
            int blue = (col & 0x000000FF);

            //用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
            int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);

            //100为间隔可调整
            if (i % mSample == 0) {
                float spacingX = mGap;//x坐标的间隔 最大值为屏幕宽度
                mFloatArrayList.add(spacingX * times);
                mFloatArrayList.add((float) gray);
                times++;
                if (mFloatArrayList.size() > 3) {
                    mFloatArrayList.add(mFloatArrayList.get(mFloatArrayList.size() - 2));
                    mFloatArrayList.add(mFloatArrayList.get(mFloatArrayList.size() - 2));

                }
                Log.d("MainActivity", "gray " + " i= " + i + "gray=" + gray + "k= " + k);
            }

            //新的ARGB
//                    int newColor = alpha | (gray << 16) | (gray << 8) | gray;
            //在点（i,j）处设置新的像素
//                    bitmapNew.setPixel(i, j, newColor);


        }
//        }
        points = new float[mFloatArrayList.size()];
        for (int i = 0; i < mFloatArrayList.size(); i++) {
            points[i] = mFloatArrayList.get(i);
        }

        mFunctionDrawView.setPts(points);

    }

    //从相机获取照片
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uriImage;
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uriImage = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", tempFile);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            uriImage = Uri.fromFile(tempFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    private void sendMsg(Bitmap bitmapNew) {
        ivExampleImage.setImageBitmap(bitmapNew);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            // 调用相机后返回
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    Uri contentUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        contentUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", tempFile);
                    } else {
                        contentUri = intent.getData();
                    }
                    ivExampleImage.setImageURI(contentUri);
                    Bitmap bm = ((BitmapDrawable) ivExampleImage.getDrawable()).getBitmap();
                    mBitmapNew = bm.copy(Bitmap.Config.ARGB_8888, true);
                    mTvImgHeight.setText("图片宽（像素）： " + mBitmapNew.getHeight());
                    mTvImgWidth.setText("图片高（像素）： " + mBitmapNew.getWidth());
                }
                break;
            //调用相册后返回
            case ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    ivExampleImage.setImageURI(uri);
                    Bitmap bm = ((BitmapDrawable) ivExampleImage.getDrawable()).getBitmap();
                    mBitmapNew = bm.copy(Bitmap.Config.ARGB_8888, true);
                    mTvImgHeight.setText("图片宽（像素）： " + mBitmapNew.getHeight());
                    mTvImgWidth.setText("图片高（像素）： " + mBitmapNew.getWidth());
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm_line:
                String tempRow = mEtLine.getText().toString();
                if (!tempRow.isEmpty()) {
                    try {
                        mRow = Integer.valueOf(tempRow);
                        mFloatArrayList.clear();
                        startCalculate();
                        mFunctionDrawView.postInvalidate();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_confirm_gap:
                String tempGap = mEtGap.getText().toString();
                if (!tempGap.isEmpty()) {
                    try {
                        mSample = Integer.valueOf(tempGap);
                        mFloatArrayList.clear();
                        startCalculate();
//                        mFunctionDrawView.clear();
                        mFunctionDrawView.postInvalidate();
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
                break;
            case R.id.iv_example_image:
                getPicFromAlbm();

                break;
        }
    }
}