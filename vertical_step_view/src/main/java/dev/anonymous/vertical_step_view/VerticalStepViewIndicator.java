package dev.anonymous.vertical_step_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class VerticalStepViewIndicator extends View {
    private final String TAG_NAME = this.getClass().getSimpleName();

    private final int defaultDP =
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

    // default width = 66dp
    private final int defaultWidth =
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 66, getResources().getDisplayMetrics());
    private int height; // this view dynamic height

    private float completedLineWidth;
    private float circleRadius;

    private Drawable completeIcon;
    private Drawable attentionIcon; // definition default underway icon
    private Drawable defaultIcon; // definition default unCompleted icon
    private float centerX;
    private float leftY;
    private float rightY;

    private int stepNum = 0;
    private float linePadding;

    private List<Float> circleCenterPointPositionList;
    private Paint unCompletedPaint;
    private Paint completedPaint;
    private int unCompletedLineColor = ContextCompat.getColor(getContext(), android.R.color.darker_gray);
    private int completedLineColor = Color.BLACK;

    private int completingPosition;// underway position
    private Path path;

    private OnDrawIndicatorListener onDrawListener;
    private boolean isReverseDraw;


    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener) {
        this.onDrawListener = onDrawListener;
    }

    public float getCircleRadius() {
        return circleRadius;
    }

    public VerticalStepViewIndicator(Context context) {
        this(context, null);
    }

    public VerticalStepViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepViewIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();
        circleCenterPointPositionList = new ArrayList<>();

        PathEffect effects = new DashPathEffect(new float[]{15, 10, 15, 10}, 1);

        unCompletedPaint = new Paint();
        unCompletedPaint.setAntiAlias(true);
        unCompletedPaint.setColor(unCompletedLineColor);
        unCompletedPaint.setStyle(Paint.Style.STROKE);
        unCompletedPaint.setStrokeWidth(5);
        unCompletedPaint.setPathEffect(effects);

        completedPaint = new Paint();
        completedPaint.setAntiAlias(true);
        completedPaint.setColor(completedLineColor);
        completedPaint.setStyle(Paint.Style.FILL);

        completedLineWidth = 5;
        circleRadius = 22f * defaultDP;
        linePadding = 44f * defaultDP;

        completeIcon = ContextCompat.getDrawable(getContext(), R.drawable.check_circle);
        attentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.radio_checked_circle);
        defaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.unchecked_circle);

        isReverseDraw = false;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i(TAG_NAME, "onMeasure");

        int width = defaultWidth;
        height = 0;
        if (stepNum > 0) {
            //dynamic measure VerticalStepViewIndicator height
            // height view = 0 + 0 + (num icon * (radius * 2)) + (num Line * line padding)
            height = (int) (getPaddingTop() + getPaddingBottom() + circleRadius * 2 * stepNum + (stepNum - 1) * linePadding);
        }
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = Math.min(width, MeasureSpec.getSize(widthMeasureSpec));
        }
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        Log.i(TAG_NAME, "onSizeChanged");

        centerX = getWidth() / 2f;
        // start line position
        leftY = centerX - (completedLineWidth / 2);
        // end line position
        rightY = centerX + (completedLineWidth / 2);

        for (int i = 0; i < stepNum; i++) {
            if (isReverseDraw) {
                circleCenterPointPositionList.add(height - (circleRadius + i * circleRadius * 2 + i * linePadding));
            } else {
                // circle center point position = icon radius + (num icon * (icon radius * 2)) + (num line * line padding)
                // in the first = icon radius + 0 + 0
                circleCenterPointPositionList.add(circleRadius + i * circleRadius * 2 + i * linePadding);
            }
        }

        if (onDrawListener != null) {
            onDrawListener.onDrawIndicator();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i(TAG_NAME, "onDraw");

        if (onDrawListener != null) {
            onDrawListener.onDrawIndicator();
        }

        //-------------- draw line --------------
        for (int i = 0; i < circleCenterPointPositionList.size() - 1; i++) {
            // pre Complected X Position
            final float preComplectedXPosition = circleCenterPointPositionList.get(i);
            // after Complected X Position
            final float afterComplectedXPosition = circleCenterPointPositionList.get(i + 1);

            if (i < completingPosition) {
                if (isReverseDraw) {
                    canvas.drawRect(leftY, afterComplectedXPosition + circleRadius - 10, rightY, preComplectedXPosition - circleRadius + 10, completedPaint);
                } else {
                    canvas.drawRect(leftY, preComplectedXPosition + circleRadius - 10, rightY, afterComplectedXPosition - circleRadius + 10, completedPaint);
                }
            } else {
                if (isReverseDraw) {
                    path.moveTo(centerX, afterComplectedXPosition + circleRadius);
                    path.lineTo(centerX, preComplectedXPosition - circleRadius);
                    canvas.drawPath(path, unCompletedPaint);
                } else {
                    path.moveTo(centerX, preComplectedXPosition + circleRadius);
                    path.lineTo(centerX, afterComplectedXPosition - circleRadius);
                    canvas.drawPath(path, unCompletedPaint);
                }

            }
        }
        //-------------- draw line --------------

        //-------------- draw icon --------------
        for (int i = 0; i < circleCenterPointPositionList.size(); i++) {
            final float currentComplectedXPosition = circleCenterPointPositionList.get(i);

            int rLeft = (int) (centerX - circleRadius);
            int rTop = (int) (currentComplectedXPosition - circleRadius);
            int rRight = (int) (centerX + circleRadius);
            int rBottom = (int) (currentComplectedXPosition + circleRadius);

            Rect rect = new Rect(rLeft, rTop, rRight, rBottom);

            if (i < completingPosition) {
                completeIcon.setBounds(rect);
                completeIcon.draw(canvas);
            } else if (i == completingPosition && circleCenterPointPositionList.size() != 1) {
                // white circle around icon
//                completedPaint.setColor(Color.WHITE);
//                canvas.drawCircle(centerX, currentComplectedXPosition, circleRadius * 1.1f, completedPaint);
                attentionIcon.setBounds(rect);
                attentionIcon.draw(canvas);
            } else {
                defaultIcon.setBounds(rect);
                defaultIcon.draw(canvas);
            }
        }
        //-------------- draw icon --------------
    }

    public List<Float> getCircleCenterPointPositionList() {
        return circleCenterPointPositionList;
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
        requestLayout();
    }

    public void setIndicatorLinePaddingProportion(float linePaddingProportion) {
        this.linePadding = linePaddingProportion * defaultDP;
    }

    public void setCompletingPosition(int completingPosition) {
        this.completingPosition = completingPosition;
        requestLayout();
    }

    public void setUnCompletedLineColor(int unCompletedLineColor) {
        this.unCompletedLineColor = unCompletedLineColor;
    }

    public void setCompletedLineColor(int completedLineColor) {
        this.completedLineColor = completedLineColor;
    }

    public void reverseDraw(boolean isReverseDraw) {
        this.isReverseDraw = isReverseDraw;
        invalidate();
    }

    public void setDefaultIcon(Drawable defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public void setCompleteIcon(Drawable completeIcon) {
        this.completeIcon = completeIcon;
    }

    public void setAttentionIcon(Drawable attentionIcon) {
        this.attentionIcon = attentionIcon;
    }

    public interface OnDrawIndicatorListener {
        void onDrawIndicator();
    }
}