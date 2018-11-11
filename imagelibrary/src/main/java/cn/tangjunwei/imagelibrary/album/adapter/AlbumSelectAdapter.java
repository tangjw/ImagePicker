package cn.tangjunwei.imagelibrary.album.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.tangjunwei.imagelibrary.GlideApp;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.bean.AlbumBean;

/**
 * ^-^
 * Created by tang-jw on 2017/3/21.
 */

public class AlbumSelectAdapter extends BaseAdapter {
    
    private List<AlbumBean> albums;
    
    public AlbumSelectAdapter(List<AlbumBean> albums) {
        this.albums = albums;
    }
    
    
    public void setAlbums(List<AlbumBean> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return albums.size();
    }
    
    @Override
    public AlbumBean getItem(int position) {
        return albums.get(position);
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
        
        
        viewHolder.textView.setText(albums.get(position).getName());
        viewHolder.textView1.setText(String.format(parent.getContext().getString(R.string.format_count), albums.get(position).getCount()));
        viewHolder.radioButton.setChecked(albums.get(position).isSelected());
        
        if (albums.get(position).getCover().endsWith(".gif") || albums.get(position).getCover().contains(".gif")) {
            // TODO: 11/14 gif
            GlideApp.with(parent.getContext())
                    .load(albums.get(position).getCover())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(viewHolder.imageView);
        } else {
            GlideApp.with(parent.getContext())
                    .load(albums.get(position).getCover())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(viewHolder.imageView);
        }
        
        return convertView;
    }
    
    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView textView1;
        RadioButton radioButton;
    }
}
