package cn.tangjunwei.imagelibrary;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * ^-^
 * Created by tang-jw on 11/7.
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
    
}
