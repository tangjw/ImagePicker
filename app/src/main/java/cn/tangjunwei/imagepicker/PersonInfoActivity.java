package cn.tangjunwei.imagepicker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.tangjunwei.imagelibrary.album.AlbumActivity;
import cn.tangjunwei.imagelibrary.core.ImagePicker;
import cn.tangjunwei.imagelibrary.core.Picker;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/16.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class PersonInfoActivity extends AppCompatActivity implements Picker.OnImageSelectListener {
    
    private static final String TAG = PersonInfoActivity.class.getSimpleName();
    private ImageView mImageView;
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        mImageView = findViewById(R.id.imageView);
        GlideApp.with(this)
                .load(R.drawable.ic_avatar_placeholder)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(mImageView);
        if (savedInstanceState != null) {
            System.out.println("PersonInfoActivity savedInstanceState != null");
            ImagePicker.getInstance().initListener(this, this);
        }
        ImagePicker.getInstance().initImageLoader(new MyImageLoaderImpl());
        
        System.out.println("PersonInfoActivity onCreate end");
    }
    
    @SuppressWarnings("all")
    private void showSelectAvatarDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(PersonInfoActivity.this);
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
                        if (XXPermissions.isHasPermission(PersonInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            openAlbumAct();
                        } else {
                            XXPermissions.with(PersonInfoActivity.this)
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
                        
                        if (XXPermissions.isHasPermission(PersonInfoActivity.this, Manifest.permission.CAMERA)) {
                            openCamera();
                        } else {
                            XXPermissions.with(PersonInfoActivity.this)
                                    .permission(Manifest.permission.CAMERA)
                                    .request(new OnPermission() {
                                        @Override
                                        public void hasPermission(List<String> granted, boolean isAll) {
                                            openCamera();
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
    
    private void openCamera() {
        ImagePicker.getInstance().takeAvatar(PersonInfoActivity.this, null, PersonInfoActivity.this);
    }
    
    private void openAlbumAct() {
        //startActivityForResult(new Intent(this, TestActivity.class), 1234);
        startActivity(new Intent(this, AlbumActivity.class));
    }
    
    public void back(View view) {
        finish();
    }
    
    public void selectAvatar(View view) {
       /* if (XXPermissions.isHasPermission(PersonInfoActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ImagePicker.getInstance().takeAvatar(this, null, this);
        } else {
            XXPermissions.with(PersonInfoActivity.this)
                    .permission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request(new OnPermission() {
                        @Override
                        public void hasPermission(List<String> granted, boolean isAll) {
                            ImagePicker.getInstance().takeAvatar(PersonInfoActivity.this, null, PersonInfoActivity.this);
                        }
                    
                        @Override
                        public void noPermission(List<String> denied, boolean quick) {
                            System.out.println("CAMERA denied: ");
                        }
                    });
        }*/
        showSelectAvatarDialog();

//        ImagePicker.getInstance().takePicture(this, this);
    }
    
    @Override
    public void onSelectSuccess(String avatarPath) {
        Log.w(TAG, avatarPath);
        GlideApp.with(this)
                .load(avatarPath)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(mImageView);
        checkFragment();
    }
    
    @Override
    public void onSelectSuccess(String[] paths) {
        Log.w(TAG, Arrays.toString(paths));
        checkFragment();
    }
    
    @Override
    public void onSelectFail(String msg) {
        Log.e(TAG, msg);
        checkFragment();
    }
    
    private void checkFragment() {
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "find CoreFragment: " + getSupportFragmentManager().findFragmentByTag(CoreFragment.class.getSimpleName()));
                CropDialogFragment fragment = (CropDialogFragment) getSupportFragmentManager().findFragmentByTag(CropDialogFragment.class.getSimpleName());
                Log.d(TAG, "find CropDialogFragment: " + fragment);
                if (fragment != null) {
                    fragment.show(getSupportFragmentManager(), CropDialogFragment.class.getSimpleName());
                }
            }
        }, 5000L);*/
    }
}