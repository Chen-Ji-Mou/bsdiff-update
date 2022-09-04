package com.example.chenjimou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chenjimou.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.button.setOnClickListener((view) -> {
            Toast.makeText(this,"开始下载patch文件",Toast.LENGTH_SHORT).show();
            new Thread(() -> {
                Result result = FTPUtils.getInstance()
                        .downloadFile("patch", getExternalCacheDir().getAbsolutePath());
                if (result.status == ResultStatus.success) {
                    runOnUiThread(this :: patch);
                } else {
                    runOnUiThread(() -> Toast.makeText(this,result.msg,Toast.LENGTH_LONG).show());
                }
            }).start();
        });
    }

    public void patch() {
        Toast.makeText(this,"开始合并patch生成新apk",Toast.LENGTH_SHORT).show();
        File newApk = new File(getExternalCacheDir(), "new-app-"+ BuildConfig.BUILD_TYPE +".apk");
        File patchFile = new File(getExternalCacheDir(),"patch");
        int result = PatchUtils.patch(
                getApplicationInfo().sourceDir, newApk.getAbsolutePath(), patchFile.getAbsolutePath());
        if (result == 0)
        {
            Toast.makeText(this,"新apk生成完成，开始安装",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                Uri apkUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", newApk);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            }
            else
            {
                intent.setDataAndType(Uri.fromFile(newApk), "application/vnd.android.package-archive");
            }
            startActivity(intent);
        } else {
            Toast.makeText(this,"新apk生成失败",Toast.LENGTH_SHORT).show();
        }
    }

}