package cn.tangjunwei.imagelibrary;

import android.content.Context;
import android.os.Parcel;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/12.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ImageLoaderImpl implements ImageLoader {
    
    public static final Creator<ImageLoaderImpl> CREATOR = new Creator<ImageLoaderImpl>() {
        @Override
        public ImageLoaderImpl createFromParcel(Parcel source) {
            return new ImageLoaderImpl();
        }
        
        @Override
        public ImageLoaderImpl[] newArray(int size) {
            return new ImageLoaderImpl[size];
        }
    };
    
    public ImageLoaderImpl() {
    }
    
    @Override
    public void loadImage(Context context, String path, ImageView imageView) {
    
    }
    
    @Override
    public void loadImage(FragmentActivity activity, String path, ImageView imageView) {
        
    }
    
    @Override
    public void loadImage(Fragment fragment, String path, ImageView imageView) {
    
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
