package cn.tangjunwei.imagelibrary.album.adapter;

import android.content.Context;
import android.util.SparseArray;
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
    private int mMaxCount;
    private SparseArray<ImageBean> mSparseArray;
    private OnImageCheckedChangeListener mListener;
    
    public void refreshData(List<ImageBean> list) {
        mList = list;
        notifyDataSetChanged();
    }
    
    public void refreshData(List<ImageBean> list, SparseArray<ImageBean> sparseArray) {
        mList = list;
        mSparseArray = sparseArray;
        if (mMaxCount != 0 && mSparseArray == null) {
            mSparseArray = new SparseArray<>();
        }
       
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
    
    public ImageSelectAdapter(List<ImageBean> list, ImageLoader imageLoader, int maxCount, SparseArray<ImageBean> sparseArray) {
        mList = list;
        mImageLoader = imageLoader;
        mMaxCount = maxCount;
        mSparseArray = sparseArray;
        if (maxCount != 0 && mSparseArray == null) {
            mSparseArray = new SparseArray<>();
        }
    }
    
    private static class ViewHolder {
        ImageView imageView;
        TextView tvType;
        MyCheckableView checkableView;
    }
    
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ImageBean imageBean = mList.get(position);
        final ViewHolder viewHolder;
        final Context context = parent.getContext();
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.grid_view_item_image_select, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.image_view_image_select);
            viewHolder.tvType = convertView.findViewById(R.id.tv_image_type);
            viewHolder.checkableView = convertView.findViewById(R.id.cv_index);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
    
        if (mMaxCount > 0 && mSparseArray != null) {
            viewHolder.checkableView.setVisibility(View.VISIBLE);
            viewHolder.checkableView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int size = mSparseArray.size();
                    ImageBean imageBean = mList.get(position);
                    if (!imageBean.isSelected()) {
                        if (size < 9) {
                            viewHolder.checkableView.setIndex(size + 1);
                            viewHolder.checkableView.switchState();
                            imageBean.setIndex(size + 1);
                            mSparseArray.append(imageBean.getId(), imageBean);
                        } else {
                            Toast.makeText(context.getApplicationContext(), "最多选9张", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (imageBean.getIndex() == size) { //取消选中，如果是最后一个移除即可
                            viewHolder.checkableView.setIndex(0);
                            viewHolder.checkableView.switchState();
                            imageBean.setIndex(0);
                            mSparseArray.remove(imageBean.getId());
                        } else {
                            //取消选中，如果是最后一个之前的，需要遍历降低索引
                            mSparseArray.remove(imageBean.getId());
                            for (int i = 0; i < mSparseArray.size(); i++) {
                                ImageBean imageBean1 = mSparseArray.valueAt(i);
                                if (imageBean1.getIndex() > imageBean.getIndex()) {
                                    imageBean1.setIndex(imageBean1.getIndex() - 1);
                                }
                            }
                            viewHolder.checkableView.setIndex(0);
                            viewHolder.checkableView.switchState();
                            imageBean.setIndex(0);
                            notifyDataSetChanged();
                        }
                    }
                    if (mListener != null) {
                        mListener.onCheckedImageChange(mSparseArray);
                    }
                }
            });
        } else {
            viewHolder.checkableView.setVisibility(View.GONE);
        }
    
        if (mSparseArray != null) {
            ImageBean imageBeanTemp = mSparseArray.get(imageBean.getId());
            if (mSparseArray.size() > 0 && imageBeanTemp != null && imageBeanTemp.getIndex() != imageBean.getIndex()) {
                imageBean.setIndex(imageBeanTemp.getIndex());
            }
            viewHolder.checkableView.setChecked(imageBean.getIndex());
        }
        
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
    
    public void setListener(OnImageCheckedChangeListener listener) {
        mListener = listener;
    }
    
    public interface OnImageCheckedChangeListener {
        void onCheckedImageChange(SparseArray<ImageBean> sparseArray);
    }
}
