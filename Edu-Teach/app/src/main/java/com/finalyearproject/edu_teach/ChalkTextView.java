package com.finalyearproject.edu_teach;

import android.widget.TextView;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by ninad on 12/12/2016.
 */

public class ChalkTextView extends TextView {
    public ChalkTextView(Context context) {
        super(context);
    }

    public ChalkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChalkTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChalkTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setFont(Context context){
        Typeface font=Typeface.createFromAsset(context.getAssets(),"fonts/chalk.ttf");
        this.setTypeface(font);
    }
}
