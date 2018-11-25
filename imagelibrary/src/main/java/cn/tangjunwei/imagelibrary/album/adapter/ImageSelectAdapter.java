package cn.tangjunwei.imagelibrary.album.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;

public class ImageSelectAdapter extends BaseAdapter {
    private List<ImageBean> mList;
    private ImageLoader mImageLoader;
    
    public ImageSelectAdapter(List<ImageBean> list, ImageLoader imageLoader) {
        mList = list;
        mImageLoader = imageLoader;
    }
    
    public void refreshData(List<ImageBean> list) {
        mList = list;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }
    
    @Override
    public ImageBean getItem(int position) {
        return mList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.grid_view_item_image_select, null);
            
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.image_view_image_select);
            viewHolder.tvType = convertView.findViewById(R.id.tv_image_type);
            convertView.setTag(viewHolder);
            
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        if (mList.get(position).isSelected) {
            // viewHolder.view.setAlpha(0.5f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // convertView.setForeground(context.getDrawable(R.drawable.ic_done_white));
            }
        } else {
            //viewHolder.view.setAlpha(0.0f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                convertView.setForeground(null);
            }
        }
    
        String path = mList.get(position).path;
        if (path.endsWith(".gif")) {
            viewHolder.tvType.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvType.setVisibility(View.GONE);
        }
        
        if (mImageLoader != null) {
            mImageLoader.loadImage(context, path, viewHolder.imageView);
        }
        
        return convertView;
    }
    
    private static class ViewHolder {
        ImageView imageView;
        TextView tvType;
    }
}
