package cn.tangjunwei.imagelibrary.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.fragment.ImageSelectFragment;
import cn.tangjunwei.imagelibrary.core.ImagePicker;
import cn.tangjunwei.imagelibrary.core.Picker;
import cn.tangjunwei.imagelibrary.crop.CropDialogFragment;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/18.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class AlbumActivity extends AppCompatActivity implements Picker.OnImageSelectListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (ImagePicker.getInstance().getImageLoader() != null) {
                ImageLoader imageLoader = (ImageLoader) savedInstanceState.getSerializable("ImageLoader");
                if (imageLoader != null) {
                    ImagePicker.getInstance().initImageLoader(imageLoader);
                }
            }
    
            CropDialogFragment fragment = (CropDialogFragment) getSupportFragmentManager().findFragmentByTag(CropDialogFragment.class.getSimpleName());
            if (fragment != null) {
                fragment.setOnImageSelectListener(this);
            }
        }
        
        setContentView(R.layout.activity_album);
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        ImageSelectFragment fragment = (ImageSelectFragment) fragmentManager.findFragmentByTag(ImageSelectFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = ImageSelectFragment.newInstance(1, null);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment, ImageSelectFragment.class.getSimpleName())
                .commit();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("ImageLoader", ImagePicker.getInstance().getImageLoader());
    }
    
    @Override
    public void onSelectSuccess(String avatarPath) {
        Intent intent = new Intent();
        intent.putExtra("path", avatarPath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    
    @Override
    public void onSelectSuccess(String[] paths) {
        
    }
    
    @Override
    public void onSelectFail(String msg) {
        
    }
    
    public void back(View view) {
        finish();
    }
}
