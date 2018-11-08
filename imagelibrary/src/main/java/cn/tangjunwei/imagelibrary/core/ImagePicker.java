package cn.tangjunwei.imagelibrary.core;

import androidx.annotation.IntRange;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/7.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public interface ImagePicker {
    /**
     * 打开相机, 拍照获取一张图片
     */
    void openCamera();
    
    /**
     * 打开相册, 选择一张
     */
    void openAlbum();
    
    /**
     * 打开相册, 选择多张
     *
     * @param maxCount 最多选择数量
     */
    void openAlbum(@IntRange(from = 1, to = 9) int maxCount);
}
