package cn.tangjunwei.imagelibrary;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/12.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public interface ImageLoader extends Serializable {
    
    void loadImage(Context context, String path, ImageView imageView);
    
    void loadImage(FragmentActivity activity, String path, ImageView imageView);
    
    void loadImage(Fragment fragment, String path, ImageView imageView);
    
    void loadCropImage(FragmentActivity activity, String path, ImageView imageView);
    
    void loadCropImage(Fragment fragment, String path, ImageView imageView);
    
    void loadCropImage(Fragment fragment, String path, ImageView imageView, String mimeType, int width, int height);
    
}
