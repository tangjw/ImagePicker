package cn.tangjunwei.imagelibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/3/14.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public abstract class ILBaseFragment extends Fragment {
    
    protected FragmentActivity mActivity;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
        } else {
            throw new RuntimeException(context.toString() + "must extends FragmentActivity!");
        }
        
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            initArguments(args);
        }
    }
    
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        
        View mRootView = inflater.inflate(getContentLayoutId(), container, false);
        
        init(mRootView);
        
        return mRootView;
    }
    
    /**
     * @return {@code R.layout.fra_content}
     */
    protected abstract int getContentLayoutId();
    
    /**
     * 初始化参数
     */
    protected void initArguments(@NonNull Bundle args) {
    }
    
    /**
     * 初始化其他
     * @param rootView 填充的view
     */
    protected void init(View rootView) {
    }
    
}
