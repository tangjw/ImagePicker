package cn.tangjunwei.imagelibrary.album.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.tangjunwei.imagelibrary.ImageLoader;
import cn.tangjunwei.imagelibrary.R;
import cn.tangjunwei.imagelibrary.album.bean.ImageBean;
import cn.tangjunwei.imagelibrary.widget.MyCheckableView;

public class ImageSelectAdapter extends BaseAdapter {
    private List<ImageBean> mList;
    private ImageLoader mImageLoader;
    
    private int mCount;
    
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
        return getItem(position).getId();
    }
    
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ImageBean imageBean = mList.get(position);
        final ViewHolder viewHolder;
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.grid_view_item_image_select, null);
            
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.image_view_image_select);
            viewHolder.tvType = convertView.findViewById(R.id.tv_image_type);
            viewHolder.checkableView = convertView.findViewById(R.id.cv_index);
            viewHolder.checkableView.setOnCheckedChangeListener(new MyCheckableView.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(MyCheckableView checkableView, boolean isChecked) {
            
                }
            });
    
            viewHolder.checkableView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            
                    if (mCount < 9) {
                        ImageBean imageBean = mList.get(position);
                        if (!imageBean.isSelected()) {
                            mCount = mCount + 1;
                            imageBean.setIndex(mCount);
                            viewHolder.checkableView.setIndex(mCount);
                        } else {
                            if (imageBean.getIndex() < mCount) {
                                // TODO: 2018/11/26 更新已选择的索引 
                            } else {
                                mCount = mCount - 1;
                                imageBean.setIndex(0);
                                viewHolder.checkableView.setIndex(0);
                            }
                        }
                
                        viewHolder.checkableView.switchState();
                    } else {
                        Toast.makeText(parent.getContext(), "最多选9张", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            
            convertView.setTag(viewHolder);
            
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        System.out.println(imageBean.getIndex());
        System.out.println(imageBean.getPath());
        viewHolder.checkableView.setChecked(imageBean.getIndex());
    
    
        String path = imageBean.getPath();
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
        MyCheckableView checkableView;
    }
    
    private OnSelectedCountChangeListener mListener;
    
    interface OnSelectedCountChangeListener {
        void onCountChange(int count);
    }
    
    
}
