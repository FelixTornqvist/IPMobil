package com.example.uppgift31photostore;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * To keep the photogrid_item:s square
 */

public class SquareLinearLayout extends LinearLayout {
    public SquareLinearLayout(Context context) {
        super(context);
    }

    public SquareLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);// getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
