package cn.tangjunwei.imagelibrary.album.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/07.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public abstract class CustomGenericAdapter<T> extends BaseAdapter {
    protected ArrayList<T> arrayList;
    protected Context context;
    protected LayoutInflater layoutInflater;
    
    protected int size;
    
    public CustomGenericAdapter(Context context, ArrayList<T> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
    }
    
    public void setArrayList(ArrayList<T> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return arrayList.size();
    }
    
    public T getItem(int position) {
        return arrayList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void setLayoutParams(int size) {
        this.size = size;
    }
    
    public void releaseResources() {
        arrayList = null;
        context = null;
    }
}
