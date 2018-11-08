package cn.tangjunwei.imagelibrary.album.bean;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/7.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class AlbumBean {
    private String id;
    private String name;
    private String cover;
    private int count;
    private boolean selected;
    
    public AlbumBean(String id, String name, String cover, int count, boolean selected) {
        this.id = id;
        this.name = name;
        this.cover = cover;
        this.count = count;
        this.selected = selected;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCover() {
        return cover;
    }
    
    public void setCover(String cover) {
        this.cover = cover;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
