package com.github.popoy.topreminder;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple text label view that can be show as a message tip,
 * and the tip`s theme can be customized
 *
 * @author rockliu
 */
public class TopReminder extends RelativeLayout {

    private static final String TAG = TopReminder.class.getSimpleName();
    private static final int THEME_DEFAULT = 0;
    private static final int THEME_WARNING = 1;
    private static final int THEME_ERROR = 2;
    private int iconRes;
    private boolean showIcon;
    private int maxLines;
    private boolean swipeToDismiss;
    private String text;
    private int theme;

    private LinearLayout content;
    private ImageView icon;
    private TextView textView;
    float x_tmp1;
    float y_tmp1;
    float x_tmp2;
    float y_tmp2;

    public TopReminder(Context context) {
        super(context);
        initView(context);
    }

    public TopReminder(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.topReminder);
        iconRes = a.getResourceId(R.styleable.topReminder_leftIcon, R.drawable.notification_icon_network);
        showIcon = a.getBoolean(R.styleable.topReminder_showIcon, false);
        maxLines = a.getInt(R.styleable.topReminder_maxLines, 2);
        text = a.getString(R.styleable.topReminder_reminderText);
        swipeToDismiss = a.getBoolean(R.styleable.topReminder_swipeToDismiss, false);
        theme = a.getInt(R.styleable.topReminder_reminderTheme, 0);
        a.recycle();

        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_top_reminder, this);
        content = (LinearLayout) view.findViewById(R.id.top_reminder_content);
        content.setVisibility(View.GONE);
        icon = (ImageView) view.findViewById(R.id.top_reminder_icon);
        textView = (TextView) view.findViewById(R.id.top_reminder_text);
        if (theme == THEME_DEFAULT) {
            content.setBackgroundResource(R.color.default_theme_background_color);
            if (iconRes > 0) {
                icon.setImageResource(iconRes);
            } else {
                icon.setImageResource(R.drawable.notification_icon_network);
            }
        } else if (theme == THEME_WARNING) {
            content.setBackgroundResource(R.color.warning_theme_background_color);
            if (iconRes > 0) {
                icon.setImageResource(iconRes);
            } else {
                icon.setImageResource(R.drawable.notification_icon_network);
            }
        } else if (theme == THEME_ERROR) {
            content.setBackgroundResource(R.color.error_theme_background_color);
            if (iconRes > 0) {
                icon.setImageResource(iconRes);
            } else {
                icon.setImageResource(R.drawable.notification_icon_network);
            }
        }
        if (showIcon) {
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }
        textView.setText(text);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (swipeToDismiss) {
            //get current x,y
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x_tmp1 = x;
                    y_tmp1 = y;
                    break;
                case MotionEvent.ACTION_UP:
                    x_tmp2 = x;
                    y_tmp2 = y;
                    if (x_tmp1 != 0 && y_tmp1 != 0) {
                        if (x_tmp1 - x_tmp2 > 26 && Math.abs(y_tmp1 - y_tmp2) < 26) {
                            Log.i(TAG, "swipe to left");
                            dismiss();
                        }
                        if (x_tmp2 - x_tmp1 > 26 && Math.abs(y_tmp1 - y_tmp2) < 26) {
                            Log.i(TAG, "swipe to right");
                            dismiss();
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }

    }

    public void show() {
        content.setVisibility(View.VISIBLE);
    }

    public void dismiss() {
        content.setVisibility(View.GONE);
    }
}
