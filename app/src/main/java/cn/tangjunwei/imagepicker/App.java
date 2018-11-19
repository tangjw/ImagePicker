package cn.tangjunwei.imagepicker;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/17.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
