package cn.tangjunwei.imagelibrary;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/12.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public interface ImageLoader {
    
    void loadImage(Context context, String path, ImageView imageView);
    
    void loadImage(FragmentActivity activity, String path, ImageView imageView);
    
    void loadImage(Fragment fragment, String path, ImageView imageView);
    
    void loadCropImage(FragmentActivity activity, String path, ImageView imageView);
    
    void loadCropImage(Fragment fragment, String path, ImageView imageView);
    
}
