package cn.tangjunwei.imagelibrary.album.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.AlbumPresenter;
import cn.tangjunwei.imagelibrary.album.AlbumView;
import cn.tangjunwei.imagelibrary.album.adapter.ImageSelectAdapter;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;
import cn.tangjunwei.imagelibrary.base.ILBaseFragment;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/13.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ImageSelectFragment extends ILBaseFragment implements AlbumView {
    
    private final static String ARG_PARAM1 = "ImageLoader";
    private ContentLoadingProgressBar mProgressBar;
    private ImageLoader mImageLoader;
    private TextView mTvError;
    private RelativeLayout mRlContent;
    private GridView mGridView;
    private ImageSelectAdapter mAdapter;
    private AlbumPresenter mPresenter;
    
    public static ImageSelectFragment newInstance(@NonNull ImageLoader imageLoader) {
        
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, imageLoader);
        ImageSelectFragment fragment = new ImageSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected void initArguments(@NonNull Bundle args) {
        mImageLoader = args.getParcelable(ARG_PARAM1);
    }
    
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_image_select;
    }
    
    
    @Override
    protected void init(View rootView) {
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mTvError = rootView.findViewById(R.id.tv_error);
        mRlContent = rootView.findViewById(R.id.rl_content);
        mGridView = rootView.findViewById(R.id.grid_view);
        mPresenter = new AlbumPresenter(this);
        mPresenter.loadAllImage(mActivity);
    }
    
    @Override
    public void showLoadingView() {
        mProgressBar.show();
        mTvError.setVisibility(View.GONE);
        mRlContent.setVisibility(View.GONE);
    }
    
    @Override
    public void HideLoadingView() {
        mRlContent.setVisibility(View.VISIBLE);
        mProgressBar.hide();
        mTvError.setVisibility(View.GONE);
    }
    
    @Override
    public void showErrorView() {
        mRlContent.setVisibility(View.GONE);
        mProgressBar.hide();
        mTvError.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void showImage(List<ImageBean> list) {
        if (mAdapter == null) {
            System.out.println(list.size());
            mAdapter = new ImageSelectAdapter(list, mImageLoader);
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.refreshData(list);
        }
    }
}
