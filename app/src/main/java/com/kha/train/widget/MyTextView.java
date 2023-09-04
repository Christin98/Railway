package com.kha.train.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.kha.train.R;

public class MyTextView extends AppCompatTextView {
    String txtType = null;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.MyTextView, 0, 0);
        try {
            this.txtType = obtainStyledAttributes.getString(0);
            setMyTypeface(context, this.txtType);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public MyTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.MyTextView, 0, 0);
        try {
            this.txtType = obtainStyledAttributes.getString(0);
            setMyTypeface(context, this.txtType);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public void setMyTypeface(Context context, String str) {
        if (str != null) {
            if (str.equalsIgnoreCase(context.getString(R.string.medium))) {
                setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto.medium.ttf"));
            } else if (str.equalsIgnoreCase(context.getString(R.string.regular))) {
                setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto.regular.ttf"));
            } else if (str.equalsIgnoreCase(context.getString(R.string.light))) {
                setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto.light.ttf"));
            } else if (str.equalsIgnoreCase(context.getString(R.string.thin))) {
                setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto.thin.ttf"));
            }
        }
    }
}
