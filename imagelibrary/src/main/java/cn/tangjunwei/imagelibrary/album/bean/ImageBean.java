package cn.tangjunwei.imagelibrary.album.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/7.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ImageBean implements Parcelable {
    private int id;
    private String name;
    private String path;
    private int direction;
    private int index;
    private boolean isSelected;
    
    public ImageBean(int id, String name, String path, int direction) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.direction = direction;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
        this.isSelected = index > 0;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
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
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeInt(this.direction);
        dest.writeInt(this.index);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }
    
    protected ImageBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.path = in.readString();
        this.direction = in.readInt();
        this.index = in.readInt();
        this.isSelected = in.readByte() != 0;
    }
    
    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel source) {
            return new ImageBean(source);
        }
        
        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };
}
