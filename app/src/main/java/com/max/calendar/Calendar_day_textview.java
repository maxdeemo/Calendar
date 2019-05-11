package com.max.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class Calendar_day_textview extends android.support.v7.widget.AppCompatTextView {

    public boolean isToday = false;
    private Paint paint = new Paint();

    public Calendar_day_textview(Context context) {
        super(context);
    }

    public Calendar_day_textview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl();
    }

    public Calendar_day_textview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl();
    }

    private void initControl() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#ff0000"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isToday) {
            canvas.translate(getWidth() / 2, getHeight() / 2);
            canvas.drawCircle(0, 0, getWidth() / 2, paint);
        }
    }
}
