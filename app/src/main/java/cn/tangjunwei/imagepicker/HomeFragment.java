package cn.tangjunwei.imagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import cn.tangjunwei.imagelibrary.core.ImagePicker;
import cn.tangjunwei.imagelibrary.core.Picker;


public class HomeFragment extends Fragment implements Picker.OnImageSelectListener {
    
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    
    private String mParam1;
    private String mParam2;
    
    public HomeFragment() {
        // Required empty public constructor
    }
    
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (savedInstanceState != null) {
            System.out.println("PersonInfoActivity savedInstanceState != null");
            ImagePicker.getInstance().initListener(getActivity(), this);
        }
        ImagePicker.getInstance().initImageLoader(new MyImageLoaderImpl());
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
    
        view.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PostActivity.class));
            }
        });
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.getInstance().selectPicture(getActivity(), 9, HomeFragment.this);
            }
        });
        return view;
    }
    
    @Override
    public void onHiddenChanged(boolean hidden) {
        System.out.println("home: " + (hidden ? "invisible" : "visible"));
    }
    
    @Override
    public void onSelectSuccess(String avatarPath) {
        
    }
    
    @Override
    public void onSelectSuccess(String[] paths) {
        
    }
    
    @Override
    public void onSelectFail(String msg) {
        
    }
}
