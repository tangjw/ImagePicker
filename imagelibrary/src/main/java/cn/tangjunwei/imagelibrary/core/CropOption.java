package cn.tangjunwei.imagelibrary.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/17.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class CropOption implements Parcelable {
    
    public static final Creator<CropOption> CREATOR = new Creator<CropOption>() {
        @Override
        public CropOption createFromParcel(Parcel source) {
            return new CropOption(source);
        }
        
        @Override
        public CropOption[] newArray(int size) {
            return new CropOption[size];
        }
    };
    private int with = 240;
    private int height = 240;
    private int quality = 100;
    
    public CropOption() {
    }
    
    public CropOption(int with, int height, int quality) {
        this.with = with;
        this.height = height;
        this.quality = quality;
    }
    
    protected CropOption(Parcel in) {
        this.with = in.readInt();
        this.height = in.readInt();
        this.quality = in.readInt();
    }
    
    public int getWith() {
        return with;
    }
    
    public void setWith(int with) {
        this.with = with;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public int getQuality() {
        return quality;
    }
    
    public void setQuality(int quality) {
        this.quality = quality;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.with);
        dest.writeInt(this.height);
        dest.writeInt(this.quality);
    }
}
