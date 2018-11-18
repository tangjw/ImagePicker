package cn.tangjunwei.imagelibrary.album;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.fragment.ImageSelectFragment;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/18.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class AlbumActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // TODO: 2018/11/18
        }
        
        setContentView(R.layout.activity_album);
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        ImageSelectFragment fragment = (ImageSelectFragment) fragmentManager.findFragmentByTag(ImageSelectFragment.class.getSimpleName());
        if (fragment == null) {
            //MyImageLoaderImpl imageLoader = new MyImageLoaderImpl();
            // System.out.println(imageLoader);
            fragment = ImageSelectFragment.newInstance(1, null);
        }
        //fragment.setImageLoader(new MyImageLoaderImpl());
        // fragment.setOnSelectImageListener(this);
        fragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment, ImageSelectFragment.class.getSimpleName())
                .commit();
    }
}
