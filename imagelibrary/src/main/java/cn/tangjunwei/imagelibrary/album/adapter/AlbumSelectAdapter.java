package cn.tangjunwei.imagelibrary.album.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.bean.AlbumBean;

/**
 * ^-^
 * Created by tang-jw on 2017/3/21.
 */

public class AlbumSelectAdapter extends BaseAdapter {
    
    private List<AlbumBean> mList;
    private ImageLoader mImageLoader;
    
    public AlbumSelectAdapter(List<AlbumBean> albums, ImageLoader imageLoader) {
        mList = albums;
        mImageLoader = imageLoader;
    }
    
    
    public void setAlbums(List<AlbumBean> albums) {
        mList = albums;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mList.size();
    }
    
    @Override
    public AlbumBean getItem(int position) {
        return mList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        ViewHolder viewHolder;
        
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_album_select, parent, false);
            
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.image_view_album_image);
            viewHolder.textView = convertView.findViewById(R.id.text_view_album_name);
            viewHolder.textView1 = convertView.findViewById(R.id.tv_image_count);
            viewHolder.radioButton = convertView.findViewById(R.id.radiobutton);
            
            convertView.setTag(viewHolder);
            
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.textView.setText(mList.get(position).getName());
        viewHolder.textView1.setText(String.format(parent.getContext().getString(R.string.format_count), mList.get(position).getCount()));
        viewHolder.radioButton.setChecked(mList.get(position).isSelected());
        
        mImageLoader.loadImage(parent.getContext(), mList.get(position).getCover(), viewHolder.imageView);
        
        return convertView;
    }
    
    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView textView1;
        RadioButton radioButton;
    }
}
