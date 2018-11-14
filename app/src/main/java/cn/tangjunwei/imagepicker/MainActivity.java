package cn.tangjunwei.imagepicker;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    
    private FrameLayout mFrameLayout;
    private HomeFragment home;
    private PersonFragment person;
    private FragmentManager mFragmentManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mFragmentManager.beginTransaction().show(home).hide(person).commit();
                    return true;
                case R.id.navigation_person:
                    mFragmentManager.beginTransaction().show(person).hide(home).commit();
                    return true;
            }
            return false;
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mFrameLayout = findViewById(R.id.fl_container);
        
        mFragmentManager = getSupportFragmentManager();
        home = (HomeFragment) mFragmentManager.findFragmentByTag(HomeFragment.class.getSimpleName());
        person = (PersonFragment) mFragmentManager.findFragmentByTag(PersonFragment.class.getSimpleName());
        
        if (person == null) {
            person = PersonFragment.newInstance();
            mFragmentManager.beginTransaction().add(R.id.fl_container, person, PersonFragment.class.getSimpleName()).hide(person).commit();
        }
        if (home == null) {
            home = HomeFragment.newInstance("Home", "#440");
            mFragmentManager.beginTransaction().add(R.id.fl_container, home, HomeFragment.class.getSimpleName()).commit();
        }
        
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("main activity onSave");
    }
}
