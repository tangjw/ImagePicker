package cn.tangjunwei.imagelibrary.album.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.LongSparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.adapter.AlbumSelectAdapter;
import cn.tangjunwei.imagelibrary.album.adapter.ImageSelectAdapter;
import cn.tangjunwei.imagelibrary.album.bean.AlbumBean;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;
import cn.tangjunwei.imagelibrary.crop.CropActivity;

public class ImageSelectActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    
    private static final String BUCKET_ID = "bucket_id";
    
    private final String[] projection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
    
    private TextView errorDisplay;
    private ProgressBar progressBar;
    private GridView gridView;
    private ImageSelectAdapter adapter;
    private AlbumSelectAdapter mAlbumSelectAdapter;
    
    //private ContentObserver observer;
    
    private TextView mTvPreview;
    private CheckBox mTvAlbum;
    private ListPopupWindow mListPopupWindow;
    /**
     * 默认 0，选中的相册是所有图片
     */
    private int mCurrentSelectedAlbum = 0;
    private ArrayList<ImageBean> mImageList;
    /**
     * 全局选中的Image集合
     */
    private ArrayList<ImageBean> mSelectedImageList;
    /**
     * 全局选中的Image id 集合
     */
    private HashSet<Long> mSelectedIdSet;
    /**
     * 当前选中Image在当前Album所有Image集合中的位置
     */
    private LongSparseArray<Integer> mSparseArray;
    private ArrayList<ImageBean> mPreviewImages;
    private LoaderManager mLoaderManager;
    
    /**
     * 相册数组
     */
    private LinkedHashMap<String, AlbumBean> mAlbumMap;
    private List<AlbumBean> mAlbumList;
    //private ImagePreviewFragment mImagePreviewFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_image_select);
        
        initView();
        
        initData();
        
        setViewActionListener();
    }
    
    /**
     * 初始化数据, 查询图片和相册
     */
    private void initData() {
        
        mSelectedIdSet = new HashSet<>();
        mSelectedImageList = new ArrayList<>();
        
        mLoaderManager = getSupportLoaderManager();
        LoadAllImage();
        
    }
    
    private void initView() {
        mTvPreview = findViewById(R.id.tv_preview);
        
        //mTvPreview.setVisibility(Constants.isCrop ? View.GONE : View.VISIBLE);
        
        mTvAlbum = findViewById(R.id.tv_select_album);
        
        errorDisplay = findViewById(R.id.text_view_error);
        errorDisplay.setVisibility(View.INVISIBLE);
        
        progressBar = findViewById(R.id.progress_bar_image_select);
        gridView = findViewById(R.id.grid_view_image_select);
        
    }
    
    @Override
    protected void onPause() {
        if (mListPopupWindow != null) {
            mListPopupWindow.dismiss();
        }
        super.onPause();
        
    }
    
    /**
     * 设置控件的监听
     */
    private void setViewActionListener() {
        
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //toggleSelection(position);
                // 打开头像剪切界面
                String path = mImageList.get(position).path;
                System.out.println(path);
                Intent intent = new Intent(ImageSelectActivity.this, CropActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });
        
        mTvAlbum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && mAlbumList.size() > 1) {
                    showAlbumDialog();
                }
            }
        });
        
        mTvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedImageList != null && mSelectedImageList.size() > 0) {
                    if (mPreviewImages == null) {
                        mPreviewImages = new ArrayList<>();
                    } else {
                        mPreviewImages.clear();
                    }
                    
                    mPreviewImages.addAll(mSelectedImageList);
                    
                    //mImagePreviewFragment = ImagePreviewFragment.newInstance(mPreviewImages);
                    //mImagePreviewFragment.show(getSupportFragmentManager(), "preview");
                }
                
            }
        });
    }
    
    /**
     * 显示相册选择 dialog
     */
    private void showAlbumDialog() {
        if (mListPopupWindow == null) {
            mListPopupWindow = new ListPopupWindow(this);
            mListPopupWindow.setAnchorView(findViewById(R.id.rl_select_album));
            mListPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
            mListPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            mListPopupWindow.setModal(true);
            if (mAlbumList.size() * dip2px(120) > getScreenHeight()) {
                mListPopupWindow.setHeight((int) (getScreenHeight() * 0.7));
            }
            mListPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mTvAlbum.setChecked(false);
                }
            });
            
            mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    
                    if (mCurrentSelectedAlbum == position) {
                        mListPopupWindow.dismiss();
                        return;
                    }
                    // 先取消选中原来所选的相册
                    mAlbumList.get(mCurrentSelectedAlbum).setSelected(false);
                    // 选中现在所选的相册
                    AlbumBean albumBean = mAlbumList.get(position);
                    albumBean.setSelected(true);
                    mTvAlbum.setText(albumBean.getName());
                    mAlbumSelectAdapter.notifyDataSetChanged();
                    mListPopupWindow.dismiss();
                    
                    mCurrentSelectedAlbum = position;
                    
                    loadAlbumImage(albumBean.getId());
                }
            });
            
        }
        
        if (mAlbumSelectAdapter == null) {
            mAlbumSelectAdapter = new AlbumSelectAdapter(mAlbumList);
            mListPopupWindow.setAdapter(mAlbumSelectAdapter);
        } else {
            mAlbumSelectAdapter.setAlbums(mAlbumList);
        }
        
        mListPopupWindow.show();
    }
    
    
    /**
     * 先加载所有的图片
     */
    private void LoadAllImage() {
        mAlbumMap = new LinkedHashMap<>();
        mAlbumMap.put(null, new AlbumBean(null, getResources().getString(R.string.album_name_all), null, 0, true));
        loadAlbumImage(null);
    }
    
    /**
     * 根据相册加载图片
     *
     * @param albumId 相册的 BUCKET_ID {@link MediaStore.Images.Media#BUCKET_ID}
     */
    private void loadAlbumImage(@Nullable String albumId) {
        Bundle args = new Bundle();
        args.putString(BUCKET_ID, albumId);
        mLoaderManager.restartLoader(0, args, this);
    }
    
    
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader;
        String albumId = args.getString(BUCKET_ID);
        
        if (albumId == null) {  // 加载全部
            loader = new CursorLoader(this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    null, null,
                    MediaStore.Images.Media.DATE_ADDED);
        } else {
            loader = new CursorLoader(this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    MediaStore.Images.Media.BUCKET_ID + "=?",
                    new String[]{albumId},
                    MediaStore.Images.Media.DATE_ADDED);
        }
        return loader;
    }
    
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null) return;
        if (!data.moveToLast()) return;
        
        if (mImageList == null) {
            mImageList = new ArrayList<>();
        } else {
            mImageList.clear();
        }
        
        if (mSparseArray == null) {
            mSparseArray = new LongSparseArray<>();
        } else {
            mSparseArray.clear();
        }
        
        for (int i = 0; i < data.getCount(); i++) {
            long img_id = data.getLong(data.getColumnIndex(projection[0]));
            String img_name = data.getString(data.getColumnIndex(projection[1]));
            String img_path = data.getString(data.getColumnIndex(projection[2]));
            mImageList.add(new ImageBean(img_id, img_name, img_path, mSelectedIdSet.contains(img_id)));
            
            String bucket_id = data.getString(data.getColumnIndex(projection[3]));
            String bucket_name = data.getString(data.getColumnIndex(projection[4]));
            
            if (mAlbumMap != null) {
                if (i == 0) {
                    AlbumBean albumBean = mAlbumMap.get(null);
                    if (albumBean != null) {
                        albumBean.setCover(img_path);
                        albumBean.setCount(data.getCount());
                    }
                }
                AlbumBean albumBean = mAlbumMap.get(bucket_id);
                if (albumBean == null) {    //没有匹配的相册, 新增
                    mAlbumMap.put(bucket_id, new AlbumBean(bucket_id, bucket_name, img_path, 1, false));
                } else {                    //匹配到图片数量+1
                    albumBean.setCount(albumBean.getCount() + 1);
                }
            }
            
            if (!data.moveToPrevious()) {
                break;
            }
        }
        
        /*Set<Map.Entry<Long, AlbumBean>> entries = mAlbumMap.entrySet();
    
        for (Map.Entry<Long, AlbumBean> entry : entries) {
            AlbumBean albumBean = entry.getValue();
            System.out.println(albumBean.getName() + ": " + albumBean.getCount());
        }*/
        
        if (mAlbumMap != null && mAlbumMap.size() > 0) {
            mAlbumList = new ArrayList<>(mAlbumMap.values());
            mAlbumMap.clear();
            mAlbumMap = null;
        }
        
        if (adapter == null) {
            adapter = new ImageSelectAdapter(mImageList);
            gridView.setAdapter(adapter);
        } else {
            adapter.refreshData(mImageList);
        }
        
        
        progressBar.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
        //orientationBasedUI(getResources().getConfiguration().orientation);
        
        
    }
    
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        
    }
    
    private void toggleSelection(int position) {
        
       /* if (Constants.isCrop) {
            mSelectedImageList.add(mImageList.get(position));
            sendIntent();
            return;
        }*/
        
        if (!mImageList.get(position).isSelected && mSelectedImageList.size() > 9) {
            Toast.makeText(
                    getApplicationContext(),
                    String.format(getString(R.string.limit_exceeded), 9),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        
        
        if (!mImageList.get(position).isSelected) {
            mSelectedImageList.add(mImageList.get(position));
            mSelectedIdSet.add(mImageList.get(position).id);
            mSparseArray.put(mImageList.get(position).id, position);
            
        } else {
            mSelectedImageList.remove(mImageList.get(position));
            mSelectedIdSet.remove(mImageList.get(position).id);
            mSparseArray.remove(mImageList.get(position).id);
        }
        
        mImageList.get(position).isSelected = !mImageList.get(position).isSelected;
        
        adapter.notifyDataSetChanged();
        
        setActionBarText();
        
    }
    
    /**
     * 设置工具栏的文字
     */
    private void setActionBarText() {
        int selectedSize = mSelectedImageList.size();
        if (selectedSize > 0) {
            mTvPreview.setTextColor(Color.WHITE);
            mTvPreview.setText("预览(" + selectedSize + ")");
            //mActionSelectDone.setTitle("完成(" + selectedSize + "/9)");
        } else {
            //mActionSelectDone.setTitle("完成");
            mTvPreview.setTextColor(getResources().getColor(R.color.colorTextGray));
            mTvPreview.setText("预览");
        }
    }
    
    private void sendIntent() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("www", mSelectedImageList);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    
    public int dip2px(float dp) {
        final float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
    
    private int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        return dm.heightPixels;
    }
}
