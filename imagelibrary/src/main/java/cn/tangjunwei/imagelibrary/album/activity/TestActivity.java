package cn.tangjunwei.imagelibrary.album.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import cn.tangjunwei.imagelibrary.ImageLoaderImpl;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.fragment.ImageSelectFragment;

public class TestActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        FrameLayout frameLayout = findViewById(R.id.fl_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        Fragment fragment = fragmentManager.findFragmentByTag("test");
        if (fragment == null) {
            fragment = ImageSelectFragment.newInstance(new ImageLoaderImpl());
        }
        
        fragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment, "test")
                .commit();
        
    }
}
