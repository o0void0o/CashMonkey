package com.koostech.cashmonkey;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class ScrollOffBottomBehaviour extends CoordinatorLayout.Behavior<View> {

    private int mViewHeight;
    private ObjectAnimator mAnimator;

    public ScrollOffBottomBehaviour(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {

        mViewHeight = child.getHeight();

        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);

       // ((FloatingActionButton)child).show();


    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {


        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,dxUnconsumed, dyUnconsumed);

        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            ((FloatingActionButton)child).hide();
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            ((FloatingActionButton)child).show();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;


    }


}