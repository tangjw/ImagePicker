package cn.tangjunwei.imagelibrary.album.bean;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/7.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ImageBean {
    private long id;
    private String name;
    private String path;
    private int index;
    private boolean isSelected;
    
    public ImageBean(long id, String name, String path, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isSelected = isSelected;
    }
    
    public ImageBean(long id, String name, String path, int index, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.index = index;
        this.isSelected = isSelected;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
        this.isSelected = index > 0;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public boolean isSelected() {
        return isSelected;
    }
}
