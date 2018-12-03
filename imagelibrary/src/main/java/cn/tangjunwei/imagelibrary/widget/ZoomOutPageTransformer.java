package cn.tangjunwei.imagelibrary.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/12/3.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.94f;
    // private static final float MIN_ALPHA = 0.8f;
    
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            System.out.println("position: " + position);
            // view.setAlpha(0f);
            
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            System.out.println("position: " + position);
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }
            
            // Scale the page down (between MIN_SCALE and 1)
            System.out.println("scaleFactor: " + scaleFactor);
            
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            
            // Fade the page relative to its size.
            // view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            System.out.println("position: " + position);
            // view.setAlpha(0f);
        }
    }
}
