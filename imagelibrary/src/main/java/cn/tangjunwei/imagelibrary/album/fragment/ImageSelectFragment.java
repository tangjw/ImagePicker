package cn.tangjunwei.imagelibrary.album.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.widget.ContentLoadingProgressBar;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.AlbumPresenter;
import cn.tangjunwei.imagelibrary.album.AlbumView;
import cn.tangjunwei.imagelibrary.album.adapter.AlbumSelectAdapter;
import cn.tangjunwei.imagelibrary.album.adapter.ImageSelectAdapter;
import cn.tangjunwei.imagelibrary.album.bean.AlbumBean;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;
import cn.tangjunwei.imagelibrary.base.ILBaseFragment;
import cn.tangjunwei.imagelibrary.core.PickerConfig;
import cn.tangjunwei.imagelibrary.crop.CropDialogFragment;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/13.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ImageSelectFragment extends ILBaseFragment implements AlbumView/*, CropDialogFragment.OnCropListener*/ {
    
    private final static String ARG_PARAM1 = "ImageLoader";
    
    private ContentLoadingProgressBar mProgressBar;
    private ImageLoader mImageLoader;
    private TextView mTvError;
    private RelativeLayout mRlContent;
    private GridView mGridView;
    private ImageSelectAdapter mAdapter;
    private AlbumPresenter mPresenter;
    private CheckBox mTvSelectAlbum;
    private LinearLayout mLlSelectAlbum;
    private ListPopupWindow mListPopupWindow;
    private List<AlbumBean> mAlbumList;
    private AlbumSelectAdapter mAlbumSelectAdapter;
    private int mCurAlbumIndex = 0;
    private List<ImageBean> mList;
    private CropDialogFragment mCropDialogFragment;
    private PickerConfig mPickerConfig;
    private int mMaxCount;
    private int mCropSize;
    
    
    public static ImageSelectFragment newInstance(@Nullable PickerConfig pickerConfig) {
        
        Bundle args = new Bundle();
        args.putParcelable("config", pickerConfig);
        ImageSelectFragment fragment = new ImageSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    /*public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
        if (getFragmentManager() != null) {
            mCropDialogFragment = (CropDialogFragment) getFragmentManager().findFragmentByTag("crop");
            if (mCropDialogFragment != null) {
                mCropDialogFragment.setImageLoader(mImageLoader);
                //mCropDialogFragment.setOnCropListener(ImageSelectFragment.this);
            }
        }
    }*/
    
    @Override
    protected void initArguments(@NonNull Bundle args) {
        mPickerConfig = args.getParcelable("config");
        if (mPickerConfig == null) {
            mPickerConfig = new PickerConfig();
        }
        mMaxCount = mPickerConfig.getMaxCount();
    }
    
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_image_select;
    }
    
    
    @Override
    protected void init(View rootView, @Nullable Bundle savedInstanceState) {
        
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mTvError = rootView.findViewById(R.id.tv_error);
        mRlContent = rootView.findViewById(R.id.rl_content);
        mGridView = rootView.findViewById(R.id.grid_view);
        mLlSelectAlbum = rootView.findViewById(R.id.ll_select_album);
        mTvSelectAlbum = rootView.findViewById(R.id.tv_select_album);
        mPresenter = new AlbumPresenter(this);
        
        mPresenter.loadAllImage(mActivity);
        
        mTvSelectAlbum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showAlbumDialog();
                }
            }
        });
        
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //toggleSelection(position);
                // 打开头像剪切界面
                String path = mList.get(position).path;
                mCropDialogFragment = CropDialogFragment.newInstance(path, mPickerConfig.getCropSize());
                // mCropDialogFragment.setImageLoader(mImageLoader);
                //mCropDialogFragment.setOnCropListener(ImageSelectFragment.this);
                mCropDialogFragment.show(mActivity.getSupportFragmentManager(), "crop");
            }
        });
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //outState.putInt("current", mCurAlbumIndex);
    }
    
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
    
    @Override
    public void showLoadingView() {
        mProgressBar.show();
        mTvError.setVisibility(View.GONE);
    }
    
    @Override
    public void HideLoadingView() {
        mRlContent.setVisibility(View.VISIBLE);
        mProgressBar.hide();
        mTvError.setVisibility(View.GONE);
    }
    
    @Override
    public void showEmptyView() {
        mRlContent.setVisibility(View.GONE);
        mProgressBar.hide();
        mTvError.setText(getText(R.string.error_empty));
        mTvError.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void showErrorView() {
        mRlContent.setVisibility(View.GONE);
        mProgressBar.hide();
        mTvError.setText(getText(R.string.error_null_cursor));
        mTvError.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void showAlbum(List<AlbumBean> list) {
        mAlbumList = list;
    }
    
    @Override
    public void showImage(List<ImageBean> list) {
        mList = list;
        if (mAdapter == null) {
            mAdapter = new ImageSelectAdapter(list, mImageLoader);
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.refreshData(list);
        }
    }
    
    /**
     * 显示相册选择 dialog
     */
    private void showAlbumDialog() {
        if (mListPopupWindow == null) {
            mListPopupWindow = new ListPopupWindow(mActivity);
            mListPopupWindow.setAnchorView(mLlSelectAlbum);
            mListPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
            mListPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            mListPopupWindow.setModal(true);
            if (mAlbumList.size() * dip2px(120) > getScreenHeight()) {
                mListPopupWindow.setHeight((int) (getScreenHeight() * 0.7));
            }
            mListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mTvSelectAlbum.setChecked(false);
                }
            });
            
            mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    
                    if (mCurAlbumIndex == position) {
                        mListPopupWindow.dismiss();
                        return;
                    }
                    // 先取消选中原来所选的相册
                    mAlbumList.get(mCurAlbumIndex).setSelected(false);
                    // 选中现在所选的相册
                    AlbumBean albumBean = mAlbumList.get(position);
                    albumBean.setSelected(true);
                    mAlbumSelectAdapter.notifyDataSetChanged();
                    mTvSelectAlbum.setText(albumBean.getName());
                    mCurAlbumIndex = position;
                    mPresenter.loadAlbumImage(albumBean.getId());
                    mListPopupWindow.dismiss();
                }
            });
            
        }
        
        if (mAlbumSelectAdapter == null) {
            mAlbumSelectAdapter = new AlbumSelectAdapter(mAlbumList, mImageLoader);
            mListPopupWindow.setAdapter(mAlbumSelectAdapter);
        } else {
            mAlbumSelectAdapter.setAlbums(mAlbumList);
        }
        
        mListPopupWindow.show();
    }
    
    private int dip2px(float dp) {
        final float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
    
    private int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        return dm.heightPixels;
    }
    
    /*@Override
    public void onCropSuccess(String avatarPath) {
        if (mOnSelectImageListener != null) {
            mOnSelectImageListener.onSelectAvatarSuccess(avatarPath);
        }
    }*/
    
    private OnSelectImageListener mOnSelectImageListener;
    
    public void setOnSelectImageListener(OnSelectImageListener onSelectImageListener) {
        mOnSelectImageListener = onSelectImageListener;
    }
    
    public interface OnSelectImageListener {
        void onSelectAvatarSuccess(String avatarPath);
        
        void onSelectImageSuccess(String[] imagePaths);
        
        void onSelectError(String message);
    }
    
}
