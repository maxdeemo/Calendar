package com.max.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewCalendar extends LinearLayout {

    private Button btnPre;
    private Button btnNext;
    private TextView txtdate;
    private GridView gv_calendar;

    private Calendar caldate = Calendar.getInstance();
    private String displayFormat;

    public NewCalendarListener listener;

    public NewCalendar(Context context) {
        super(context);
    }

    public NewCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public NewCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    private void initControl(Context context, AttributeSet attrs) {
        bindControl(context);
        bindControlEvent();

        TypedArray ta = getContext().obtainStyledAttributes(attrs,R.styleable.NewCalendar);

        try {
            String format = ta.getString(R.styleable.NewCalendar_dataFormat);
            displayFormat = format;
            if (displayFormat == null)
            {
                displayFormat = "MMM yyyy";
            }
        }
        finally {
            ta.recycle();
        }

        renderCalendar();
    }

    private void bindControl(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view, this);

        btnPre = findViewById(R.id.btn_pre);
        btnNext = findViewById(R.id.btn_next);
        txtdate = findViewById(R.id.date);
        gv_calendar = findViewById(R.id.calendar_view);
    }

    private void bindControlEvent() {
        btnPre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                caldate.add(Calendar.MONTH, -1);
                renderCalendar();
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                caldate.add(Calendar.MONTH, 1);
                renderCalendar();
            }
        });
    }

    private void renderCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
        txtdate.setText(sdf.format(caldate.getTime()));

        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) caldate.clone();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int preDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -preDays);

        int maxCellCount = 6 * 7;
        while (cells.size() < maxCellCount) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        gv_calendar.setAdapter(new CalendarAdapter(getContext(), cells));
        gv_calendar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener == null)
                {
                    return false;
                }else{
                    listener.onItemLongPress((Date)parent.getItemAtPosition(position));
                    return true;
                }

            }
        });
    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days) {
            super(context, R.layout.calendar_test, (List<Date>) days);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Date date = getItem(position);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendar_test, parent, false);
            }

            int day = date.getDate();
            ((TextView) convertView).setText(String.valueOf(day));


            Date now = new Date();
            Boolean isTheSameMonth = false;
            if (date.getMonth() == now.getMonth()) {
                isTheSameMonth = true;
            }
            if (isTheSameMonth) {
                ((TextView) convertView).setTextColor(Color.parseColor("#000000"));
            } else {
                ((TextView) convertView).setTextColor(Color.parseColor("#666666"));
            }

            if (now.getDate() == date.getDate() && now.getMonth() == date.getMonth()
                    && now.getYear() == date.getYear()) {
                ((TextView) convertView).setTextColor(Color.parseColor("#ff0000"));
                ((Calendar_day_textview) convertView).isToday = true;
            }

            return convertView;
        }
    }

    public interface NewCalendarListener
    {

        void onItemLongPress(Date day);
    }
}
