package com.example.mobilebankinglicenta;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.highlight.PieHighlighter;
import com.github.mikephil.charting.renderer.PieChartRenderer;

public class CustomPieChart extends PieChart {
    public CustomPieChart(Context context) {
        super(context);
    }

    public CustomPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public float getDiameter() {
        return super.getDiameter()*0.6f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new CustomRenderer(this, mAnimator, mViewPortHandler);
        mXAxis = null;

        mHighlighter = new PieHighlighter(this);
    }
}
