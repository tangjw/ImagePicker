package cn.tangjunwei.imagepicker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import cn.tangjunwei.imagelibrary.album.fragment.ImageSelectFragment;

public class TestActivity extends AppCompatActivity implements ImageSelectFragment.OnSelectImageListener {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("activity onCreate");
        setContentView(R.layout.activity_test);
        FragmentManager fragmentManager = getSupportFragmentManager();
    
        ImageSelectFragment fragment = (ImageSelectFragment) fragmentManager.findFragmentByTag("test");
        if (fragment == null) {
            MyImageLoaderImpl imageLoader = new MyImageLoaderImpl();
            System.out.println(imageLoader);
            fragment = ImageSelectFragment.newInstance(null);
        }
        fragment.setImageLoader(new MyImageLoaderImpl());
        fragment.setOnSelectImageListener(this);
        fragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment, "test")
                .commit();
    
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("activity onSaveInstanceState");
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("activity onRestoreInstanceState");
    }
    
    @Override
    public void onSelectAvatarSuccess(String avatarPath) {
        System.out.println("onSelectAvatarSuccess: "+ avatarPath);
    }
    
    @Override
    public void onSelectImageSuccess(String[] imagePaths) {
        
    }
    
    @Override
    public void onSelectError(String message) {
        
    }
}
