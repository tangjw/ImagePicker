package cn.tangjunwei.imagepicker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import cn.tangjunwei.imagelibrary.album.activity.ImageSelectActivity;


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
        ImageView iv = rootView.findViewById(R.id.imageView);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectAvatarDialog();
            }
        });
    }
    
    @Override
    public void onHiddenChanged(boolean hidden) {
        System.out.println("person: " + (hidden ? "invisible" : "visible"));
    }
    
    
    @SuppressWarnings("all")
    private void showSelectAvatarDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        dialog.setContentView(R.layout.dialog_select_image);
        dialog.findViewById(R.id.tv_select_image_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
        
        dialog.findViewById(R.id.tv_select_image_album)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        if (XXPermissions.isHasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            openAlbumAct();
                        } else {
                            XXPermissions.with(mActivity)
                                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .request(new OnPermission() {
                                        @Override
                                        public void hasPermission(List<String> granted, boolean isAll) {
                                            openAlbumAct();
                                        }
                                        
                                        @Override
                                        public void noPermission(List<String> denied, boolean quick) {
                                            System.out.println("STORAGE denied: ");
                                        }
                                    });
                        }
                        
                    }
                });
        
        dialog.findViewById(R.id.tv_select_image_camera)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        if (XXPermissions.isHasPermission(mActivity, Manifest.permission.CAMERA)) {
                            openAlbumAct();
                        } else {
                            XXPermissions.with(mActivity)
                                    .permission(Manifest.permission.CAMERA)
                                    .request(new OnPermission() {
                                        @Override
                                        public void hasPermission(List<String> granted, boolean isAll) {
                                            openAlbumAct();
                                        }
                                        
                                        @Override
                                        public void noPermission(List<String> denied, boolean quick) {
                                            System.out.println("CAMERA denied: ");
                                        }
                                    });
                        }
                    }
                });
        dialog.show();
    }
    
    private void openAlbumAct() {
        startActivity(new Intent(mActivity, ImageSelectActivity.class));
    }
}
