package cn.tangjunwei.imagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class PersonFragment extends BaseFragment {
    
    private final int permissionCode = 3999;
    
    public PersonFragment() {
        // Required empty public constructor
    }
    
    public static PersonFragment newInstance() {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_person;
    }
    
    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.cl_info_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, PersonInfoActivity.class));
            }
        });
    
        rootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(mActivity, PersonInfoActivity.class));
                //System.out.println(ImagePicker.getInstance().getOnImageCaptureListener());
            }
        });
    
    
        MyImageLoaderImpl imageLoader = new MyImageLoaderImpl();
//        imageLoader.loadImage(this,?);
        ImageView iv = rootView.findViewById(R.id.imageView);
        GlideApp.with(this)
                .load(R.drawable.ic_avatar_placeholder)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(iv);
        
    }
    
    @Override
    public void onHiddenChanged(boolean hidden) {
        System.out.println("person: " + (hidden ? "invisible" : "visible"));
        
    }
}
