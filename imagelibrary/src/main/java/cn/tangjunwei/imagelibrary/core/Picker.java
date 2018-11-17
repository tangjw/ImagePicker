package cn.tangjunwei.imagelibrary.core;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import cn.tangjunwei.imagelibrary.ImageLoader;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/16.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public interface Picker {
    
    /**
     * 为了省去手动处理 OnActivityForResult() 的操作并且保证 OnSaveInstanceState 后状态的恢复，
     * 请在 FragmentActivity或者 Fragment被回收重建时在 onCreate()方法中重新初始化监听器。
     * 另外如果你需要处理 OnActivityForResult()请保留 super.OnActivityForResult()
     * <p>示例：</p>
     * <pre><code>
     * protected void onCreate(@Nullable Bundle savedInstanceState) {
     *     super.onCreate(savedInstanceState);
     *     if (savedInstanceState != null) {
     *         ImagePicker.getInstance().initListener(this, this, null);
     *     }
     * }
     * </code></pre>
     *
     * @param onImageSelectListener 选图片的监听
     */
    void initListener(@NonNull FragmentActivity fragmentActivity, @NonNull OnImageSelectListener onImageSelectListener);
    
    /**
     * 使用前必须设置图片加载器
     *
     * @param imageLoader {@link ImageLoader}
     */
    void initImageLoader(@NonNull ImageLoader imageLoader);
    
    void takePicture(@NonNull FragmentActivity fragmentActivity, @NonNull OnImageSelectListener onImageSelectListener);
    
    void selectPicture(@NonNull FragmentActivity fragmentActivity, @IntRange(from = 1, to = 9) int maxCount, @NonNull OnImageSelectListener onImageSelectListener);
    
    void takeAvatar(@NonNull FragmentActivity fragmentActivity, @Nullable CropOption cropOption, @NonNull OnImageSelectListener onImageSelectListener);
    
    void selectAvatar(@NonNull FragmentActivity fragmentActivity, @Nullable CropOption cropOption, @NonNull OnImageSelectListener onImageSelectListener);
    
    
    interface OnImageSelectListener {
        
        void onSelectSuccess(String avatarPath);
        
        void onSelectSuccess(String[] paths);
        
        void onSelectFail(String msg);
    }
    
}
