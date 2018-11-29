package cn.tangjunwei.imagelibrary.album.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentManager;
import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.AlbumPresenter;
import cn.tangjunwei.imagelibrary.album.AlbumView;
import cn.tangjunwei.imagelibrary.album.PreviewDialogFragment;
import cn.tangjunwei.imagelibrary.album.adapter.AlbumSelectAdapter;
import cn.tangjunwei.imagelibrary.album.adapter.ImageSelectAdapter;
import cn.tangjunwei.imagelibrary.album.bean.AlbumBean;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;
import cn.tangjunwei.imagelibrary.base.ILBaseFragment;
import cn.tangjunwei.imagelibrary.core.CropOption;
import cn.tangjunwei.imagelibrary.core.ImagePicker;
import cn.tangjunwei.imagelibrary.core.Picker;
import cn.tangjunwei.imagelibrary.crop.CropDialogFragment;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/13.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ImageSelectFragment extends ILBaseFragment implements AlbumView, AdapterView.OnItemClickListener, ImageSelectAdapter.OnCheckedImageChangeListener {
    
    private final static String ARG_PARAM1 = "ImageLoader";
    
    private ContentLoadingProgressBar mProgressBar;
    private ImageLoader mImageLoader;
    private TextView mTvError;
    private TextView mTvPreview;
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
    private List<ImageBean> mImageList;
    
    private CropOption mCropOption;
    private int mMaxCount;
    private int mCropSize;
    private CropDialogFragment mCropDialogFragment;
    
    public SparseArray<ImageBean> mSparseArray;
    
    public static ImageSelectFragment newInstance(int maxCount, @Nullable CropOption cropOption) {
        
        Bundle args = new Bundle();
        args.putInt("MaxCount", maxCount);
        args.putSerializable("CropOption", cropOption);
        ImageSelectFragment fragment = new ImageSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mImageLoader = ImagePicker.getInstance().getImageLoader();
        if (savedInstanceState != null) {
            if (mImageLoader == null) {
                mImageLoader = (ImageLoader) savedInstanceState.getSerializable("ImageLoader");
                if (mImageLoader != null) {
                    ImagePicker.getInstance().initImageLoader(mImageLoader);
                }
            }
            mSparseArray = savedInstanceState.getSparseParcelableArray("ImageArray");
        }
        super.onCreate(savedInstanceState);
    
    }
    
    @Override
    protected void initArguments(@NonNull Bundle args) {
        mCropOption = (CropOption) args.getSerializable("CropOption");
        if (mCropOption == null) {
            mCropOption = new CropOption();
        }
        mMaxCount = args.getInt("MaxCount");
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
        mTvPreview = rootView.findViewById(R.id.tv_preview);
        if (mMaxCount == 0) {
            mTvPreview.setVisibility(View.GONE);
        } else {
            mTvPreview.setVisibility(View.VISIBLE);
            mTvPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSparseArray != null && mSparseArray.size() > 0) {
                        Toast.makeText(mActivity, "预览", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
    
        mGridView.setOnItemClickListener(this);
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("ImageLoader", ImagePicker.getInstance().getImageLoader());
        outState.putSparseParcelableArray("ImageArray", mSparseArray);
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
        mImageList = list;
        if (mAdapter == null) {
            mAdapter = new ImageSelectAdapter(list, mImageLoader, mMaxCount, mSparseArray);
            mAdapter.setListener(this);
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.refreshData(list, mSparseArray);
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (mListPopupWindow != null) {
            mListPopupWindow.dismiss();
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
        return getResources().getDisplayMetrics().heightPixels;
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mMaxCount > 0) {
            // TODO: 2018/11/26 预览
    
            System.out.println(mImageList.get(position).getDirection());
    
            PreviewDialogFragment.newInstance(mImageList.get(position).getPath(), mImageList.get(position).getDirection())
                    .show(mActivity.getSupportFragmentManager(), PreviewDialogFragment.class.getSimpleName());
            
            if (mSparseArray == null) return;
            for (int i = 0; i < mSparseArray.size(); i++) {
                System.out.println(mSparseArray.valueAt(i).getName());
            }
        } else {
    
            FragmentManager fm = mActivity.getSupportFragmentManager();
            CropDialogFragment fragment = (CropDialogFragment) fm.findFragmentByTag(CropDialogFragment.class.getSimpleName());
    
            if (fragment != null) {
                fragment.dismissAllowingStateLoss();
            }
            fragment = CropDialogFragment.newInstance(mImageList.get(position).getPath(), mCropOption.getWith());
            fragment.setOnImageSelectListener((Picker.OnImageSelectListener) mActivity);
            fragment.show(fm, CropDialogFragment.class.getSimpleName());
        }
    }
    
    
    @Override
    public void onCheckedImageChange(SparseArray<ImageBean> sparseArray) {
        mSparseArray = sparseArray;
        if (sparseArray != null && sparseArray.size() > 0) {
            mTvPreview.setTextColor(Color.WHITE);
        } else {
            mTvPreview.setTextColor(Color.GRAY);
        }
    }
}
