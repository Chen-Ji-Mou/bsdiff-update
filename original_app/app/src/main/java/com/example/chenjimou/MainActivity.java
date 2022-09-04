package com.example.chenjimou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

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
            new Thread(() -> {
                boolean result = FtpUtils.getInstance().downloadFile("patch", getExternalCacheDir().getAbsolutePath());
                if (result) {
                    runOnUiThread(this :: patch);
                }
            }).start();
        });
    }

    public void patch() {
        File newApk = new File(getExternalCacheDir(), "new-app-"+ BuildConfig.BUILD_TYPE +".apk");
        File patchFile = new File(getExternalCacheDir(),"patch");
        int result = PatchUtils.patch(getApplicationInfo().sourceDir, newApk.getAbsolutePath(), patchFile.getAbsolutePath());
        if (result == 0)
        {
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
        }
    }

}