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
    public long id;
    public String name;
    public String path;
    public boolean isSelected;
    
    public ImageBean(long id, String name, String path, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isSelected = isSelected;
    }
    
    
    private ImageBean(Parcel in) {
        id = in.readLong();
        name = in.readString();
        path = in.readString();
        isSelected = in.readByte() != 0;
    }
    
    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            return new ImageBean(in);
        }
        
        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
