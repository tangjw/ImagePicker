package cn.tangjunwei.imagepicker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/3/14.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public abstract class BaseFragment extends Fragment {
    
    protected AppCompatActivity mActivity;
    
    /**
     * 根view
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            mActivity = (AppCompatActivity) context;
        } else {
            throw new RuntimeException(context.toString() + "must extends AppCompatActivity!");
        }
        
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            initArguments(args);
        } else {
            Log.i(BaseFragment.class.getSimpleName(), "getArguments() === null");
        }
    }
    
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(getContentLayoutId(), container, false);
        
        initView(mRootView);
        
        return mRootView;
    }
    
    
    /**
     * 初始化参数
     */
    protected void initArguments(@NonNull Bundle args) {
    }
    
    /**
     * @return {@code R.layout.fra_content}
     */
    protected abstract int getContentLayoutId();
    
    /**
     * 初始化其他
     */
    protected void initView(View rootView) {
    }
    
}
