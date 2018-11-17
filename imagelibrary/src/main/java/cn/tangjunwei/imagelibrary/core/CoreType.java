package cn.tangjunwei.imagelibrary.core;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import androidx.annotation.IntDef;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/16.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */

@IntDef({CoreType.TAKE_PIC, CoreType.TAKE_PIC_CROP, CoreType.SELECT_IMAGE, CoreType.SELECT_AVATAR})
@Retention(CLASS)
@Target({PARAMETER})
public @interface CoreType {
    /**
     * 拍一张照片 不裁剪
     */
    int TAKE_PIC = 8102;
    
    /**
     * 选一张头像 裁剪
     */
    int TAKE_PIC_CROP = 8103;
    
    /**
     * 选多张图片 不裁剪
     */
    int SELECT_IMAGE = 8104;
    
    /**
     * 选一张头像 裁剪
     */
    int SELECT_AVATAR = 8105;
    
    
}
