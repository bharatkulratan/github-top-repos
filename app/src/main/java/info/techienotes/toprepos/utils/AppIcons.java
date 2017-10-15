package info.techienotes.toprepos.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class AppIcons extends AppCompatTextView {
    public AppIcons(Context context) {
        super(context);
        setTypeface(Utils.getTypeface(context, Constants.ICONS_PATH));
    }

    public AppIcons(Context context, AttributeSet attr) {
        super(context, attr);
        setTypeface(Utils.getTypeface(context, Constants.ICONS_PATH));
    }

    public AppIcons(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        setTypeface(Utils.getTypeface(context, Constants.ICONS_PATH));
    }
}