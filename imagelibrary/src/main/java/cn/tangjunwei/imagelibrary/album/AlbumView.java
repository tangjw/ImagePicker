package cn.tangjunwei.imagelibrary.album;

import java.util.List;

import cn.tangjunwei.imagelibrary.album.bean.AlbumBean;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/13.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public interface AlbumView {
    
    void showLoadingView();
    
    void HideLoadingView();
    
    void showEmptyView();
    
    void showErrorView();
    
    void showAlbum(List<AlbumBean> list);
    
    void showImage(List<ImageBean> list);
    
    
}
