/*
 * CalendarViewBase.java
 * Copyright 2013 Burke.Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.common.calendar;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import net.sarangnamu.common.DimTool;
import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.common.ui.LpInst;
import net.sarangnamu.common.ui.calendar.R;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class CalendarViewBase extends LinearLayout implements View.OnClickListener {
    protected LinearLayout view, calendar, monthBar;
    protected TextView monthLabel;
    protected Button prevMonth, nextMonth;

    protected int minWidth, minHeight;
    protected int headerHeight, headerTextSize, headerBgColor;
    protected int dayMargin, dayTextMargin;
    protected int satColor, sunColor, notInMonthColor, todayBgColor, dayBgColor;
    protected int normalColor;
    protected boolean mini;
    protected int thisMonth, thisYear;
    protected Locale locale;

    public CalendarViewBase(Context context) {
        super(context);
        initLayout();
    }

    public CalendarViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public CalendarViewBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    protected void setCalendarSize() {
        headerHeight    = dpToPixelInt(32);
        headerTextSize  = dpToPixelInt(5);
        dayMargin       = dpToPixelInt(1);
        dayTextMargin   = dpToPixelInt(3);
    }

    protected void setCalendarColor() {
        headerBgColor   = 0xffd5d5d5;
        satColor        = 0xff7d7d7d;
        sunColor        = 0xff7d7d7d;
        notInMonthColor = 0xffd2d2d2;
        dayBgColor      = 0xffefefef;
        todayBgColor    = 0xffe8e8e8;
        normalColor     = 0;
    }

    protected void initLayout() {
        setCalendarSize();
        setCalendarColor();

        LinearLayout.LayoutParams lp = LpInst.linearMm();
        view        = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.calendar, null);
        monthBar    = (LinearLayout) view.findViewById(R.id.monthBar);
        monthLabel  = (TextView) view.findViewById(R.id.monthLabel);
        calendar    = (LinearLayout) view.findViewById(R.id.calendar);
        prevMonth   = (Button) view.findViewById(R.id.prevMonth);
        nextMonth   = (Button) view.findViewById(R.id.nextMonth);

        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
        calendar.setOrientation(VERTICAL);
        view.setLayoutParams(lp);

        addView(view);

        Calendar cal = Calendar.getInstance(Locale.US);
        thisMonth    = cal.get(Calendar.MONTH);
        thisYear     = cal.get(Calendar.YEAR);

        drawCalendar();
    }

    private void drawCalendar() {
        int numWeeks;

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.MONTH, thisMonth);
        cal.set(Calendar.YEAR, thisYear);

        monthLabel.setText(DateFormat.format("MM.yyyy", cal));

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int monthStartDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int lastDay = cal.getActualMaximum(Calendar.DATE);

        numWeeks = (int) Math.ceil(((double) (lastDay + monthStartDayOfWeek - 1)) / 7f);
        cal.add(Calendar.DAY_OF_MONTH, 1 - monthStartDayOfWeek);

        calendar.removeAllViews();

        setHeader();
        setCalendar(numWeeks, cal, thisMonth);
    }

    protected void setHeader() {
        LinearLayout.LayoutParams lp = LpInst.linear(LinearLayout.LayoutParams.MATCH_PARENT, headerHeight);
        LinearLayout header = new LinearLayout(getContext());
        header.setLayoutParams(lp);
        header.setOrientation(HORIZONTAL);
        header.setBackgroundColor(headerBgColor);

        DateFormatSymbols dfs = DateFormatSymbols.getInstance(Locale.US);
        String[] weekdays = dfs.getShortWeekdays();

        for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
            lp = LpInst.linearMm();
            lp.weight = 1;

            TextView dayView = new TextView(getContext());
            //dayView.setTextAppearance(getContext(), R.style.calendarCell);
            dayView.setText(weekdays[day]);
            dayView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            dayView.setLayoutParams(lp);

            if (day == Calendar.SATURDAY) {
                dayView.setTextColor(satColor);
            } else if (day == Calendar.SUNDAY) {
                dayView.setTextColor(sunColor);
            }

            header.addView(dayView);
        }

        calendar.addView(header);
    }

    protected void setCalendar(int numWeeks, Calendar cal, int thisMonth) {
        int today       = Calendar.getInstance(Locale.US).get(Calendar.DAY_OF_YEAR);
        int todayYear   = cal.get(Calendar.YEAR);
        LinearLayout weekLayout;

        for (int week=0; week<numWeeks; ++week) {
            LinearLayout.LayoutParams lp = LpInst.linearMw();
            lp.weight = 1;
            lp.topMargin = dayMargin;

            weekLayout = new LinearLayout(getContext());
            weekLayout.setLayoutParams(lp);
            weekLayout.setOrientation(HORIZONTAL);

            for (int day = 0; day < 7; day++) {
                boolean inMonth = cal.get(Calendar.MONTH) == thisMonth;
                boolean inYear  = cal.get(Calendar.YEAR) == todayYear;
                boolean isToday = inYear && inMonth && (cal.get(Calendar.DAY_OF_YEAR) == today);
                //boolean isFirstOfMonth = cal.get(Calendar.DAY_OF_MONTH) == 1;

                lp = LpInst.linearMm();
                lp.weight = 1;
                lp.rightMargin = dayMargin;
                FrameLayout dayLayout = new FrameLayout(getContext());
                dayLayout.setLayoutParams(lp);

                FrameLayout.LayoutParams flp = LpInst.frameWw();
                flp.gravity = Gravity.RIGHT;
                flp.rightMargin = dayTextMargin;

                TextView dayTxt = new TextView(getContext());
                dayTxt.setText("" + cal.get(Calendar.DAY_OF_MONTH));
                dayTxt.setTextColor(0xff202020);
                dayTxt.setTypeface(FontLoader.getInstance(getContext()).getRobotoLight());

                if (day == 6) {                      // sat
                    dayTxt.setTextColor(satColor);
                } else if (day == 0) {              // sun
                    dayTxt.setTextColor(sunColor);
                } else {
                    if (normalColor != 0) {
                        dayTxt.setTextColor(normalColor);
                    }
                }

                if (!inMonth) {
                    dayTxt.setTextColor(notInMonthColor);
                } else {
                    setDayLayout(dayLayout);
                }

                setTodayLayout(isToday, dayLayout, dayTxt);

                dayTxt.setLayoutParams(flp);
                dayLayout.addView(dayTxt);

                weekLayout.addView(dayLayout);

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }

            calendar.addView(weekLayout);
            setWeekLine();
        }
    }

    protected void setWeekLine() {

    }

    protected void setTodayLayout(boolean isToday, FrameLayout dayLayout, TextView dayTxt) {
        if (isToday) {
            dayLayout.setBackgroundColor(todayBgColor);
        } else {
            dayLayout.setBackgroundColor(dayBgColor);
        }
    }

    protected int dpToPixelInt(int dp) {
        return DimTool.dpToPixelInt(getContext(), dp);
    }

    protected float dpToPixel(float dp) {
        return DimTool.dpToPixel(getContext(), dp);
    }

    public void setMinSize(int width, int height) {
        minWidth  = dpToPixelInt(width);
        minHeight = dpToPixelInt(height);
    }

    public void setMiniMode() {
        mini = true;
    }

    public void hideMonthBar() {
        monthBar.setVisibility(View.GONE);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // View.OnClickListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {
        if (v.getId() == prevMonth.getId() || v.getId() == nextMonth.getId()) {
            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, v.getId() == prevMonth.getId() ? -1 : 1);

            thisMonth = cal.get(Calendar.MONTH);
            thisYear  = cal.get(Calendar.YEAR);

            drawCalendar();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract void setDayLayout(FrameLayout dayLayout);
}
