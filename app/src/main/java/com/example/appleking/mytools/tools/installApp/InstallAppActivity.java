package com.example.appleking.mytools.tools.installApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by appleking on 2017/2/21.
 */

public class InstallAppActivity extends AppCompatActivity {
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    init();
}

    private void init() {
        Thread thread = new Thread(new Runnable() {

            private File file;

            @Override
            public void run() {
                try {
                    //下载
                    String path = "http://192.168.0.20:8080/game.apk";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("get");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        file = new File("sdcard/apk/game.apk");
                        FileOutputStream fos = new FileOutputStream(file);
                        int len = -1;
                        byte[] buffer = new byte[1024];
                        while((len = is.read())!=-1){
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                        fos.close();
                    }
                    /**
                     *
                     * 安装：
                     String str = "/CanavaCancel.apk";
                     String fileName = Environment.getExternalStorageDirectory() + str;
                     Intent intent = new Intent(Intent.ACTION_VIEW);
                     intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                     startActivity(intent);

                     卸载：
                     Uri packageURI = Uri.parse("package:com.demo.CanavaCancel");
                     Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                     startActivity(uninstallIntent);

                     Environment拥有一些可以获取环境变量的方法
                     package:com.demo.CanavaCancel 这个形式是 package:程序完整的路径 (包名+程序名)
                     * */
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }
}
