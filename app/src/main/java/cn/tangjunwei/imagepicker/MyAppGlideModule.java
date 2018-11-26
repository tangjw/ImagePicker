package cn.tangjunwei.imagepicker;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/12.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
    
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
    
        builder.setDefaultRequestOptions(RequestOptions.formatOf(DecodeFormat.PREFER_RGB_565));
        // RequestOptions requestOptions = RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888);
        
        
    }
    
    
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
    }
    
    
    
}
