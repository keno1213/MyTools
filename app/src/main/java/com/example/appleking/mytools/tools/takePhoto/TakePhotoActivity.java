package com.example.appleking.mytools.tools.takePhoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appleking.mytools.MainActivity;
import com.example.appleking.mytools.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by appleking on 2017/2/20.
 */

public class TakePhotoActivity extends AppCompatActivity {

    //自定义变量
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Uri imageUri; //图片路径
    private String filename; //图片名称
    private ImageView showImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo_layout);
        showImage = (ImageView) findViewById(R.id.photo);
    }

    public void takePhoto(View view){
        //图片名称 时间命名
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        filename = format.format(date);
        //创建File对象用于存储拍照的图片 SD卡根目录
        //File outputImage = new File(Environment.getExternalStorageDirectory(),"test.jpg");
        //存储至DCIM文件夹
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File outputImage = new File(path,filename+".jpg");

            if(outputImage.exists()) {
                outputImage.delete();
            }

        //将File对象转换为Uri并启动照相程序
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //照相
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //指定图片输出地址
        startActivityForResult(intent,TAKE_PHOTO); //启动照相
        //拍完照startActivityForResult() 结果返回onActivityResult()函数
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "ActivityResult resultCode error", Toast.LENGTH_SHORT).show();
            return;
        }
        switch(requestCode) {
            case TAKE_PHOTO:
                Intent intent = new Intent("com.android.camera.action.CROP"); //剪裁
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("scale", true);
                //设置宽高比例
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                //设置裁剪图片宽高
                intent.putExtra("outputX", 340);
                intent.putExtra("outputY", 340);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                Toast.makeText(this, "剪裁图片", Toast.LENGTH_SHORT).show();
                //广播刷新相册
                Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intentBc.setData(imageUri);
                this.sendBroadcast(intentBc);
                startActivityForResult(intent, CROP_PHOTO); //设置裁剪参数显示图片至ImageView
                break;
            case CROP_PHOTO:
                try {
                    //图片解析成Bitmap对象
                    Bitmap bitmap = BitmapFactory.decodeStream(
                            getContentResolver().openInputStream(imageUri));
                    Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();
                    showImage.setImageBitmap(bitmap); //将剪裁后照片显示出来
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
//大图片加载防止OOM操作
//if(resultCode==RESULT_OK) {
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels; //宽度
//        int height = dm.heightPixels ; //高度
//        加载图像尺寸而不是图像本身
//        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//        bmpFactoryOptions.inJustDecodeBounds = true; //bitmap为null 只是把图片的宽高放在Options里
//        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.toString(), bmpFactoryOptions);
//        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
//        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
//        设置图片压缩比例 如果两个比例大于1 图像一边将大于屏幕
//        if(heightRatio>1&&widthRatio>1) {
//        if(heightRatio>widthRatio) {
//        bmpFactoryOptions.inSampleSize = heightRatio;
//        }
//        else {
//        bmpFactoryOptions.inSampleSize = widthRatio;
//        }
//        }
//        图像真正解码
//        bmpFactoryOptions.inJustDecodeBounds = false;
//        bitmap = BitmapFactory.decodeFile(imageUri.toString(), bmpFactoryOptions);
//        showImage.setImageBitmap(bitmap); //将剪裁后照片显示出来
//        }  if