package cn.tangjunwei.imagelibrary.album.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.LongSparseArray;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class ImageSelectActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    
    private static final String ALBUM_NAME = "albumName";
    private static final int IMAGE_ALL = 220;
    private static final int ALBUM_ALL = 221;
    private static final int IMAGE_ALBUM = 222;
    
    private final String[] projection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,};
    private final String[] projection2 = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA};
    
    /**
     * 当前选中的相册名,默认为 null
     */
    private String mSelectedAlbumName;
    private ArrayList<AlbumBean> mAllAlbumLists;
    private TextView errorDisplay;
    private ProgressBar progressBar;
    private GridView gridView;
    private ImageSelectAdapter adapter;
    private AlbumSelectAdapter mAlbumSelectAdapter;
    
    //private ContentObserver observer;
    
    private TextView mTvPreview;
    private TextView mTvAlbum;
    private ListPopupWindow mListPopupWindow;
    private int mCurrentSelectedAlbum;
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
    private LinkedHashMap<Long, AlbumBean> mAlbumMap;
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
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        mImageList = null;
        if (adapter != null) {
            //adapter.releaseResources();
        }
        gridView.setOnItemClickListener(null);
    }
    
    /**
     * 初始化数据, 查询图片和相册
     */
    private void initData() {
        
        mSelectedIdSet = new HashSet<>();
        mSelectedImageList = new ArrayList<>();
        
        
        mLoaderManager = getSupportLoaderManager();
        LoadAllImage();
        // loadImages(null);
        // loadAlbums();
    }
    
    private void initView() {
        mTvPreview = findViewById(R.id.tv_preview);
        
        //mTvPreview.setVisibility(Constants.isCrop ? View.GONE : View.VISIBLE);
        
        mTvAlbum = findViewById(R.id.tv_select_album);
        
        errorDisplay = findViewById(R.id.text_view_error);
        errorDisplay.setVisibility(View.INVISIBLE);
        
        progressBar = findViewById(R.id.progress_bar_image_select);
        gridView = findViewById(R.id.grid_view_image_select);
        
        
        mListPopupWindow = new ListPopupWindow(this);
        mListPopupWindow.setAnchorView(findViewById(R.id.rl_select_album));
        mListPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        mListPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        mListPopupWindow.setModal(true);
    }
    
    /**
     * 设置控件的监听
     */
    private void setViewActionListener() {
        
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toggleSelection(position);
            }
        });
        
        mTvAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAllAlbumLists != null && mAllAlbumLists.size() > 0) {
                    mListPopupWindow.show();
                }
            }
        });
        
        mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                mAllAlbumLists.get(mCurrentSelectedAlbum).setSelected(false);
                mAllAlbumLists.get(position).setSelected(true);
                
                mAlbumSelectAdapter.notifyDataSetChanged();
                
                mListPopupWindow.dismiss();
                
                mCurrentSelectedAlbum = position;
                
                if (position != 0) {
                    mSelectedAlbumName = mAllAlbumLists.get(position).getName();
                    //mToolbar.setTitle(mSelectedAlbumName);
                } else {
                    mSelectedAlbumName = null;
                    ///mToolbar.setTitle("所有图片");
                }
                
                loadImages(mSelectedAlbumName);
                
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
     * 根据相册名加载图片
     *
     * @param albumName null 加载所有图片
     */
    private void loadImages(@Nullable String albumName) {
        
        System.out.println("loadImages => " + albumName);
        
        LoaderManager loaderManager = getSupportLoaderManager();
        Bundle args = new Bundle();
        args.putString(ALBUM_NAME, albumName);
        if (TextUtils.isEmpty(albumName)) {
            loaderManager.initLoader(IMAGE_ALL, args, this);
        } else {
            loaderManager.restartLoader(IMAGE_ALBUM, args, this);
            
        }
        
        
    }
    
    /**
     * 先加载所有的图片
     */
    @SuppressLint("UseSparseArrays")    //使用SparseArray会按照key数值顺序排列, 还是有HashMap吧
    private void LoadAllImage() {
        mAlbumMap = new LinkedHashMap<>();
        mAlbumMap.put(0L, new AlbumBean(0, "所有图片", null, 0, true));
        loadAlbumImage(null);
    }
    
    /**
     * 根据相册加载图片
     *
     * @param albumName 相册名
     */
    private void loadAlbumImage(@Nullable String albumName) {
        Bundle args = new Bundle();
        args.putString(ALBUM_NAME, albumName);
        mLoaderManager.initLoader(albumName == null ? IMAGE_ALL : IMAGE_ALBUM, args, this);
    }
    
    
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader;
        switch (id) {
            case IMAGE_ALBUM:
                loader = new CursorLoader(
                        this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?",
                        new String[]{args.getString(ALBUM_NAME)},
                        MediaStore.Images.Media.DATE_ADDED);
                break;
            case IMAGE_ALL:
            default:
                loader = new CursorLoader(
                        this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        MediaStore.Images.Media.DATE_ADDED);
                break;
            
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
            System.out.println(img_id+": "+img_name + ": " + img_path);
            mImageList.add(new ImageBean(img_id, img_name, img_path, mSelectedIdSet.contains(img_id)));
            
            long BUCKET_ID = data.getLong(data.getColumnIndex(projection[3]));
            String BUCKET_NAME = data.getString(data.getColumnIndex(projection[4]));
            
            if (mAlbumMap != null && mAlbumMap.size() > 0) {
                if (i == 0) {
                    AlbumBean albumBean = mAlbumMap.get(0L);
                    if (albumBean != null) {
                        albumBean.setCover(img_path);
                        albumBean.setCount(mImageList.size());
                    }
                }
                AlbumBean albumBean = mAlbumMap.get(BUCKET_ID);
                if (albumBean == null) {    //没有匹配的相册
                    mAlbumMap.put(BUCKET_ID, new AlbumBean(BUCKET_ID, BUCKET_NAME, img_path, 1, false));
                } else {
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
            adapter = new ImageSelectAdapter(getApplicationContext(), mImageList);
            gridView.setAdapter(adapter);
        } else {
            adapter.setArrayList(mImageList);
        }
        
        
        progressBar.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
        orientationBasedUI(getResources().getConfiguration().orientation);
        
        
    }
    
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        
    }
    
    
    /*private void loadAlbums() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(ALBUM_ALL, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                
                return new CursorLoader(getApplicationContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection2, null, null,
                        MediaStore.Images.Media.DATE_ADDED);
            }
            
            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                if (data == null) {
                    System.out.println("Cursor data 为 null");
                    return;
                }
                
                if (!data.moveToLast()) {
                    System.out.println("Cursor data 不能已到最后, 即cursor is empty");
                    return;
                }
                
                if (mAllAlbumLists == null) {
                    mAllAlbumLists = new ArrayList<>();
                } else {
                    mAllAlbumLists.clear();
                }
                
                
                HashMap<Long, Integer> albumIdPositionMap = new HashMap<>();
                
                System.out.println("album count " + data.getCount());
                
                int position = 0;
                
                ArrayList<AlbumBean> tempAlbumList = new ArrayList<>();
                
                do {
                    long albumId = data.getLong(data.getColumnIndex(projection2[0]));
                    String albumName = data.getString(data.getColumnIndex(projection2[1]));
                    String albumImage = data.getString(data.getColumnIndex(projection2[2]));
                    
                    if (albumIdPositionMap.containsKey(albumId)) {
                        AlbumBean album = tempAlbumList.get(albumIdPositionMap.get(albumId));
                        album.setCount(album.getCount() + 1);
                    } else {
                        tempAlbumList.add(new AlbumBean(albumName, albumImage, 1, false));
                        albumIdPositionMap.put(albumId, position++);
                    }
                    
                } while (data.moveToPrevious());
                
                mAllAlbumLists.add(new AlbumBean("所有图片", tempAlbumList.get(0).getCover(), data.getCount(), false));
                mAllAlbumLists.addAll(tempAlbumList);
                
                
                mAllAlbumLists.get(mCurrentSelectedAlbum).setSelected(true);
                if (mAlbumSelectAdapter == null) {
                    mAlbumSelectAdapter = new AlbumSelectAdapter(mAllAlbumLists);
                    if (mAllAlbumLists.size() * dip2px(110) > getScreenHeight()) {
                        mListPopupWindow.setHeight((int) (getScreenHeight() * 0.7));
                    }
                    mListPopupWindow.setAdapter(mAlbumSelectAdapter);
                } else {
                    mAlbumSelectAdapter.setAlbums(mAllAlbumLists);
                }
                
            }
            
            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                
            }
        });
    }*/
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationBasedUI(newConfig.orientation);
    }
    
    private void orientationBasedUI(int orientation) {
        final WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        
        if (adapter != null) {
            int size = orientation == Configuration.ORIENTATION_PORTRAIT ? metrics.widthPixels / 4 : metrics.widthPixels / 5;
            adapter.setLayoutParams(size);
        }
        gridView.setNumColumns(orientation == Configuration.ORIENTATION_PORTRAIT ? 4 : 5);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
       /* MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_select_toolbar, menu);
        mActionSelectDone = menu.getItem(0);
        if (Constants.isCrop) {
            mActionSelectDone.setVisible(false);
        }*/
        return super.onCreateOptionsMenu(menu);
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
