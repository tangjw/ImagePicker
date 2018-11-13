package cn.tangjunwei.imagelibrary;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/12.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public interface ImageLoader {
//    GlideApp.with(context)
//            .load(mList.get(position).path)
//            .placeholder(R.drawable.image_placeholder)
//            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(viewHolder.imageView);
    
    void loadImage(Context context, String path, ImageView imageView);
    void loadImage(Activity activity, String path, ImageView imageView);
    
    void loadImage(Fragment fragment, String path, ImageView imageView);
}
