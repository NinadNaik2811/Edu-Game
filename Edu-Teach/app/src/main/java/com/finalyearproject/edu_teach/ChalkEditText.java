package com.finalyearproject.edu_teach;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ninad on 12/12/2016.
 */

public class ChalkEditText extends EditText {
    public ChalkEditText(Context context) {
        super(context);
    }

    public ChalkEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChalkEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChalkEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setFont(Context context){
        Typeface font=Typeface.createFromAsset(context.getAssets(),"fonts/chalk.ttf");
        this.setTypeface(font);
    }
}
