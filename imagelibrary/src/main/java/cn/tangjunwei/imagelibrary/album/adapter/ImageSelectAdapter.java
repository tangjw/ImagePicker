package cn.tangjunwei.imagelibrary.album.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import cn.tangjunwei.imagelibrary.GlideApp;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;

public class ImageSelectAdapter extends CustomGenericAdapter<ImageBean> {
    
    public ImageSelectAdapter(Context context, ArrayList<ImageBean> images) {
        super(context, images);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_view_item_image_select, parent, false);
            
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.image_view_image_select);
            viewHolder.view = convertView.findViewById(R.id.view_alpha);
            
            convertView.setTag(viewHolder);
            
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.imageView.getLayoutParams().width = size;
        viewHolder.imageView.getLayoutParams().height = size;
        
        viewHolder.view.getLayoutParams().width = size;
        viewHolder.view.getLayoutParams().height = size;
        
        if (arrayList.get(position).isSelected) {
            viewHolder.view.setAlpha(0.5f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                convertView.setForeground(context.getResources().getDrawable(R.drawable.ic_done_white));
            }
    
        } else {
            viewHolder.view.setAlpha(0.0f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                convertView.setForeground(null);
            }
        }
        
        if (arrayList.get(position).path.endsWith(".gif") || arrayList.get(position).path.contains(".gif")) {
            // TODO: 11/14 gif
            GlideApp.with(context)
                    .load(arrayList.get(position).path)
                    .placeholder(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(viewHolder.imageView);
        } else {
            GlideApp.with(context)
                    .load(arrayList.get(position).path)
                    .placeholder(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.imageView);
        }
        
        return convertView;
    }
    
    private static class ViewHolder {
        ImageView imageView;
        private View view;
    }
}
