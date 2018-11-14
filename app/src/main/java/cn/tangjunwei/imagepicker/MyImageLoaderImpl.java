package cn.tangjunwei.imagepicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition;

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
        System.out.println("===============");
        glideRequests
                .load(path)
                //.placeholder(cn.tangjunwei.imagelibrary.R.drawable.image_placeholder)
                //.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(new MyImageRequestListener())
                .into(imageView);
    }
    
    // RequestListener
    private class MyImageRequestListener implements RequestListener<Drawable> {
        
        @Override
        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            System.out.println("onResourceReady isFirstResource: " + isFirstResource);
            System.out.println(e.getMessage());
            return false;
        }
        
        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            System.out.println("onResourceReady isFirstResource: " + isFirstResource);
            System.out.println(dataSource);
            target.onResourceReady(resource, new DrawableCrossFadeTransition(1000, isFirstResource));
            return true;
        }
    }
}
