package cn.tangjunwei.imagepicker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.fragment.ImageSelectFragment;

public class TestActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        Fragment fragment = fragmentManager.findFragmentByTag("test");
        if (fragment == null) {
            fragment = ImageSelectFragment.newInstance(new MyImageLoaderImpl());
        }
        
        fragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment, "test")
                .commit();
        
    }
}
