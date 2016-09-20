package com.github.popoy.topreminder;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
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
    public static final int THEME_DEFAULT = 0;
    public static final int THEME_WARNING = 1;
    public static final int THEME_ERROR = 2;
    private int iconRes;
    private boolean showIcon;
    private int maxLines;
    private boolean swipeToDismiss;
    private String text;
    private int reminderTheme;

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
        reminderTheme = a.getInt(R.styleable.topReminder_reminderTheme, 0);
        a.recycle();

        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_top_reminder, this);
        content = (LinearLayout) view.findViewById(R.id.top_reminder_content);
        content.setVisibility(View.GONE);
        icon = (ImageView) view.findViewById(R.id.top_reminder_icon);
        textView = (TextView) view.findViewById(R.id.top_reminder_text);
        textView.setText(text);
        textView.setMaxLines(maxLines);
        if (showIcon) {
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }
        updateTheme();
    }

    private void updateTheme() {
        if (reminderTheme == THEME_DEFAULT) {
            content.setBackgroundResource(R.color.default_theme_background_color);
            if (iconRes > 0) {
                icon.setImageResource(iconRes);
            } else {
                icon.setImageResource(R.drawable.notification_icon_network);
            }
        } else if (reminderTheme == THEME_WARNING) {
            content.setBackgroundResource(R.color.warning_theme_background_color);
            if (iconRes > 0) {
                icon.setImageResource(iconRes);
            } else {
                icon.setImageResource(R.drawable.notification_icon_network);
            }
        } else if (reminderTheme == THEME_ERROR) {
            content.setBackgroundResource(R.color.error_theme_background_color);
            if (iconRes > 0) {
                icon.setImageResource(iconRes);
            } else {
                icon.setImageResource(R.drawable.notification_icon_network);
            }
        }

        invalidate();
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

    public void setText(int stringRes) {
        textView.setText(stringRes);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setTheme(int theme) {
        reminderTheme = theme;
        updateTheme();
    }

    public void show() {
        show(text);
    }

    public void show(String text) {
        show(text, swipeToDismiss, showIcon);
    }

    public void show(String text, boolean swipeToDismiss, boolean showIcon) {
        this.swipeToDismiss = swipeToDismiss;
        content.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }
        this.showIcon = showIcon;
        if (showIcon) {
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }
    }

    public void dismiss() {
        content.setVisibility(View.GONE);
    }
}
