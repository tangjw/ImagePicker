package cn.tangjunwei.imagelibrary.core;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntRange;

/**
 * 如果仅为选取头像，仅需设置cropSize即可
 * <p>
 * Created by tangjunwei on 2018/11/15.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class PickerConfig implements Parcelable {
    /**
     * 默认0，则当前模式为选区头像
     */
    private int maxCount = 0;
    
    private boolean useCrop = true;
    
    /**
     * 裁剪后的宽高，图片为正方形
     */
    private int cropSize = 240;
    
    public int getCropSize() {
        return cropSize;
    }
    
    /**
     * 设置裁剪后的宽高 {@link PickerConfig#setMaxCount}
     *
     * @param cropSize 建议不要过大 默认240x240
     */
    public void setCropSize(int cropSize) {
        this.cropSize = cropSize;
    }
    
    public int getMaxCount() {
        return maxCount;
    }
    
    /**
     * 设置选区图片的数量 默认0，则当前模式为选区头像
     *
     * @param maxCount 最大9张
     */
    public void setMaxCount(@IntRange(from = 0, to = 9) int maxCount) {
        this.maxCount = maxCount;
    }
    
    public boolean isUseCrop() {
        return useCrop;
    }
    
    /**
     * 默认true，使用裁剪，仅选头像模式即可用 {@link PickerConfig#setMaxCount}
     */
    public void setUseCrop(boolean useCrop) {
        this.useCrop = useCrop;
    }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxCount);
        dest.writeByte(this.useCrop ? (byte) 1 : (byte) 0);
        dest.writeInt(this.cropSize);
    }
    
    public PickerConfig() {
    }
    
    protected PickerConfig(Parcel in) {
        this.maxCount = in.readInt();
        this.useCrop = in.readByte() != 0;
        this.cropSize = in.readInt();
    }
    
    public static final Creator<PickerConfig> CREATOR = new Creator<PickerConfig>() {
        @Override
        public PickerConfig createFromParcel(Parcel source) {
            return new PickerConfig(source);
        }
        
        @Override
        public PickerConfig[] newArray(int size) {
            return new PickerConfig[size];
        }
    };
}
