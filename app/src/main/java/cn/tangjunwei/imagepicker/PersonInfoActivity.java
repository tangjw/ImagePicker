package cn.tangjunwei.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/11/16.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class PersonInfoActivity extends AppCompatActivity {
    
    private String mAvatar;
    private ImageView mImageView;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_person_info);
        
        mImageView = findViewById(R.id.imageView);
        GlideApp.with(this)
                .load(mAvatar)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .into(mImageView);
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
                            //openAlbumAct();
                        } else {
                            XXPermissions.with(PersonInfoActivity.this)
                                    .permission(Manifest.permission.CAMERA)
                                    .request(new OnPermission() {
                                        @Override
                                        public void hasPermission(List<String> granted, boolean isAll) {
                                            //openAlbumAct();
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
        startActivityForResult(new Intent(this, TestActivity.class), 1234);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1234) {
                if (data != null) {
                    mAvatar = data.getStringExtra("avatar");
                    GlideApp.with(this)
                            .load(mAvatar)
                            .placeholder(mImageView.getDrawable())
                            .error(mImageView.getDrawable())
                            .into(mImageView);
                }
            }
        }
    }
    
    public void back(View view) {
        finish();
    }
    
    public void selectAvatar(View view) {
        showSelectAvatarDialog();
    }
}
