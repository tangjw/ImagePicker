package cn.tangjunwei.imagepicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Locale;

/**
 * ^-^
 * Created by tang-jw on 11/12.
 */

public class CommonUtils {
    
    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            try {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void showSoftKeyboard(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dip) {
        return (int) (0.5f + dip * Resources.getSystem().getDisplayMetrics().density);
    }
    
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dip(float px) {
        return (px / Resources.getSystem().getDisplayMetrics().density);
    }
    
    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight1 = 0;
        //获取status_bar_height资源的ID  
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值  
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }
    
    public static int getStatusBarHeight() {
        int statusBarHeight1 = 0;
        //获取status_bar_height资源的ID  
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值  
            statusBarHeight1 = Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }
    
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    
    
    public static boolean is16v9() {
        
        return false;
    }
    
    
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceIdss(Context context) {
        //IMEI（imei）
        String imei;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            imei = tm.getDeviceId();
        } else {
            imei = "imei";
        }
        
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        
        String serial;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception e) {
            serial = "serial"; // some value
        }
        
        //LogUtils.d("imei", imei);
        //LogUtils.d("serial", serial);
        //LogUtils.d("android_id", android_id);
        
        return MD5(imei + serial + android_id);
    }
    
    public static String MD5(String string) {
        //用于加密的字符
        char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = string.getBytes();
            
            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            
            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);
            
            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();
            
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {   //  i = 0
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }
            
            //返回经过加密后的字符串
            return new String(str);
            
        } catch (Exception e) {
            return string;
        }
    }
    
    @SuppressWarnings("all")
    public static int getNavBarHeight() {
        int result = 0;
        boolean hasNavigationBar = false;
        
        Resources rs = Resources.getSystem();
        
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception ignored) {
            
        }
        
        int resourceId = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (hasNavigationBar && resourceId > 0) {
            result = rs.getDimensionPixelSize(resourceId);
        }
        return result;
    }
    
    public static String getFormatMeter(double distance) {
        
        if (distance > 0 && distance < 1000) {
            return String.format(Locale.CHINA, "%.0f米", distance);
        }
        
        if (distance >= 1000 && distance < 100 * 1000) {
            return String.format(Locale.CHINA, "%.1f公里", distance / 1000);
        }
        
        if (distance >= 100 * 1000) {
            return String.format(Locale.CHINA, "%.0f公里", distance / 1000);
        }
        
        return "";
    }
    
    public static void writeObject(String path, Object map) {
        File f = new File(path);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            ObjectOutputStream objwrite = new ObjectOutputStream(out);
            objwrite.writeObject(map);
            objwrite.flush();
            objwrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // read the object from the file
    public static Object readObject(String path) {
        Object map = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            ObjectInputStream objread = new ObjectInputStream(in);
            map = objread.readObject();
            objread.close();
        } catch (IOException e) {
            //LogUtils.e("本地没有搜索记录");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }
    
}
