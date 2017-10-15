package info.techienotes.toprepos.utils;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class TextViewLight extends AppCompatTextView {

    public TextViewLight(Context context) {
        super(context);
        setTypeface(Utils.getTypeface(context, Constants.FONT_LIGHT));
        setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.HINTING_ON);
    }

    public TextViewLight(Context context, AttributeSet attr) {
        super(context,attr);
        setTypeface(Utils.getTypeface(context, Constants.FONT_LIGHT));
        setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.HINTING_ON);
    }

    public TextViewLight(Context context, AttributeSet attr, int i) {
        super(context,attr,i);
        setTypeface(Utils.getTypeface(context, Constants.FONT_LIGHT));
        setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.HINTING_ON);
    }
}