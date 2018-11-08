package cn.tangjunwei.imagelibrary.core;

import androidx.appcompat.app.AppCompatActivity;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/7.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ImagePickerImpl implements ImagePicker {
    
    private AppCompatActivity mActivity;
    
    public ImagePickerImpl(AppCompatActivity activity) {
        mActivity = activity;
    }
    
    @Override
    public void openCamera() {
        
    }
    
    @Override
    public void openAlbum() {
        //mActivity.startAc;
    }
    
    @Override
    public void openAlbum(int maxCount) {
        
    }
}
