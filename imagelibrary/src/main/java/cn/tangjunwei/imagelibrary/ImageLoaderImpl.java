package cn.tangjunwei.imagelibrary;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import androidx.fragment.app.Fragment;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/12.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ImageLoaderImpl implements ImageLoader {
    
    @Override
    public void loadImage(Context context, String path, ImageView imageView) {
        loadImage(GlideApp.with(context), path, imageView);
    }
    
    @Override
    public void loadImage(Activity activity, String path, ImageView imageView) {
        loadImage(GlideApp.with(activity), path, imageView);
    }
    
    @Override
    public void loadImage(Fragment fragment, String path, ImageView imageView) {
        loadImage(GlideApp.with(fragment), path, imageView);
    }
    
    private void loadImage(GlideRequests glideRequests, String path, ImageView imageView) {
        glideRequests
                .load(path)
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
}
