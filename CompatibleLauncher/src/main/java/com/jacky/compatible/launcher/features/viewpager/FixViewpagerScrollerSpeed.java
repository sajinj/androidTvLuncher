package com.jacky.compatible.launcher.features.viewpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

import androidx.viewpager.widget.ViewPager;

public class FixViewpagerScrollerSpeed extends Scroller {

    private int mDuration = 1000;

    public FixViewpagerScrollerSpeed(Context context) {
        super(context);
    }

    public FixViewpagerScrollerSpeed(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixViewpagerScrollerSpeed(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    public static void setViewPagerScrollSpeed(ViewPager viewPager){
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixViewpagerScrollerSpeed scroller = new FixViewpagerScrollerSpeed(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
