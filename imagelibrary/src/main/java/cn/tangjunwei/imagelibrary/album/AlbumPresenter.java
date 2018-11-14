package cn.tangjunwei.imagelibrary.album;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import cn.tangjunwei.imagelibrary.album.bean.AlbumBean;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/13.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class AlbumPresenter implements LoaderManager.LoaderCallbacks<Cursor> {
    
    private final String BUCKET_ID = "bucket_id";
    private final String[] mProjection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
    private AlbumView mAlbumView;
    private LoaderManager mLoaderManager;
    /**
     * 相册数组
     */
    private LinkedHashMap<String, AlbumBean> mAlbumMap;
    private Activity mActivity;
    
    /**
     * 相册数组
     */
    private ArrayList<ImageBean> mImageList;
    /**
     * 当前选中相册的 bucket_id, 默认null是所有图片
     */
    private String mCurrentSelectedAlbum;
    /**
     * 当前选中Image在当前Album所有Image集合中的位置
     */
    private LongSparseArray<Integer> mSparseArray;
    /**
     * 全局选中的Image id 集合
     */
    private HashSet<Long> mSelectedIdSet;
    private ArrayList<AlbumBean> mAlbumList;
    
    public AlbumPresenter(AlbumView albumView) {
        mAlbumView = albumView;
    }
    
    /**
     * 首次加载，加载所有的图片
     */
    public void loadAllImage(FragmentActivity activity) {
        mAlbumView.showLoadingView();
        mActivity = activity;
        mLoaderManager = LoaderManager.getInstance(activity);
        mSelectedIdSet = new HashSet<>();
        mAlbumMap = new LinkedHashMap<>();
        mAlbumMap.put(null, new AlbumBean(null, "所有图片", null, 0, true));
        mCurrentSelectedAlbum = null;
        loadAlbumImage(mCurrentSelectedAlbum);
    }
    
    /**
     * 根据相册加载图片
     *
     * @param albumId 相册的 BUCKET_ID {@link MediaStore.Images.Media#BUCKET_ID}
     */
    public void loadAlbumImage(@Nullable String albumId) {
        Bundle args = new Bundle();
        args.putString(BUCKET_ID, albumId);
        mLoaderManager.restartLoader(0, args, this);
    
    }
    
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        System.out.println("3333333333333333");
        CursorLoader loader;
        String albumId = args.getString(BUCKET_ID);
        if (albumId == null) {  // 加载全部
            loader = new CursorLoader(mActivity,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mProjection,
                    null, null,
                    MediaStore.Images.Media.DATE_ADDED);
        } else {
            loader = new CursorLoader(mActivity,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mProjection,
                    MediaStore.Images.Media.BUCKET_ID + "=?",
                    new String[]{albumId},
                    MediaStore.Images.Media.DATE_ADDED);
        }
        return loader;
    }
    
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        System.out.println("onLoadFinished");
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
            long img_id = data.getLong(data.getColumnIndex(mProjection[0]));
            String img_name = data.getString(data.getColumnIndex(mProjection[1]));
            String img_path = data.getString(data.getColumnIndex(mProjection[2]));
            mImageList.add(new ImageBean(img_id, img_name, img_path, mSelectedIdSet.contains(img_id)));
            
            String bucket_id = data.getString(data.getColumnIndex(mProjection[3]));
            String bucket_name = data.getString(data.getColumnIndex(mProjection[4]));
            
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
    
        if (mAlbumMap != null && mAlbumMap.size() > 0) {
            mAlbumList = new ArrayList<>(mAlbumMap.values());
            mAlbumMap.clear();
            mAlbumMap = null;
            mAlbumView.HideLoadingView();
        }
    
        if (mAlbumList != null) {
            mAlbumView.showImage(mImageList);
        } else if (mCurrentSelectedAlbum == null) {
            mAlbumView.showErrorView();
        } else {
            mAlbumView.showEmptyView();
        }
        
    }
    
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        
    }
}
