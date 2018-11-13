package cn.tangjunwei.imagepicker;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import cn.tangjunwei.imagelibrary.ImageLoaderImpl;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/12.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class MyImageLoaderImpl extends ImageLoaderImpl {
    @Override
    public void loadImage(Context context, String path, ImageView imageView) {
        loadImage(GlideApp.with(context), path, imageView);
    }
    
    @Override
    public void loadImage(FragmentActivity activity, String path, ImageView imageView) {
        loadImage(GlideApp.with(activity), path, imageView);
    }
    
    @Override
    public void loadImage(Fragment fragment, String path, ImageView imageView) {
        loadImage(GlideApp.with(fragment), path, imageView);
    }
    
    
    private void loadImage(GlideRequests glideRequests, String path, ImageView imageView) {
        glideRequests
                .load(path)
                .placeholder(cn.tangjunwei.imagelibrary.R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
    
}
