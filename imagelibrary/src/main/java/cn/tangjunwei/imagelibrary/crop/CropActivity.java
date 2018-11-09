package cn.tangjunwei.imagelibrary.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.tangjunwei.imagelibrary.GlideApp;
import cn.tangjunwei.imagelibrary.R;

/**
 * ^-^
 * Created by tang-jw on 2017/3/27.
 */

public class CropActivity extends AppCompatActivity {
    
    private ClipImageLayout mClipImageLayout;
    private ClipBorderView mClipImageView;
    private ClipZoomImageView mZoomImageView;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crop);
        
        mClipImageLayout = findViewById(R.id.clipimagelayout);
        
        mClipImageView = mClipImageLayout.getClipBorderView();
        
        mZoomImageView = mClipImageLayout.getZoomImageView();
        
        loadImage();
        
    }
    
    private void loadImage() {
        
        String path = getIntent().getStringExtra("path");
        GlideApp.with(this)
                .load(path)
                .fitCenter()
                .into(mZoomImageView);
    }
    
    public void save(View view) {
        
        int rawImageX = getIntent().getIntExtra("outImageX", 640);
        int rawImageY = getIntent().getIntExtra("outImageY", 640);
        Bitmap clip = mClipImageLayout.clip(rawImageX, rawImageY);
        writeToFile(clip);
        Intent data = new Intent();
        setResult(Activity.RESULT_OK, data);
        finish();
    }
    
    public void cancel(View view) {
        finish();
    }
    
    
    public void writeToFile(Bitmap bitmap) {
        if (bitmap == null) return;
        
        Uri uri = getIntent().getParcelableExtra("cropImageUri");
        File file = new File(uri.getPath());
    
        if (file.exists()) {
            file.delete();
            file = new File(uri.getPath());
        }
        
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            bos.flush();
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) try {
                fos.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
}
