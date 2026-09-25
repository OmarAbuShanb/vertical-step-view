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
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;
import java.util.List;

public class VerticalStepViewIndicator extends View {

    private final int defaultDP = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1,
            getResources().getDisplayMetrics()
    );

    private float completedLineWidth;
    private float unCompletedLineWidth;
    private float circleRadius;

    private Drawable completeIcon;
    private Drawable attentionIcon;
    private Drawable defaultIcon;

    private Integer completeIconTint = null;
    private Integer attentionIconTint = null;
    private Integer defaultIconTint = null;

    private float centerX;
    private float leftY;
    private float rightY;

    private int stepNum = 0;
    private float linePadding;

    private List<Float> circleCenterPointPositionList;
    private Paint unCompletedPaint;
    private Paint completedPaint;

    private int unCompletedLineColor = ContextCompat.getColor(
            getContext(),
            android.R.color.darker_gray
    );
    private int completedLineColor = Color.BLACK;

    private int completingPosition;
    private final Path path = new Path();
    private final Rect iconRect = new Rect();

    private OnDrawIndicatorListener onDrawListener;
    private boolean isReverseDraw;

    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener) {
        this.onDrawListener = onDrawListener;
    }

    public float getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(float circleRadius) {
        this.circleRadius = circleRadius;
        requestLayout();
        invalidate();
    }

    public VerticalStepViewIndicator(Context context) {
        this(context, null);
    }

    public VerticalStepViewIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepViewIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circleCenterPointPositionList = new ArrayList<>();

        PathEffect effects = new DashPathEffect(
                new float[]{15, 10, 15, 10},
                1
        );

        unCompletedLineWidth = 3 * defaultDP;
        completedLineWidth = 3 * defaultDP;
        circleRadius = 13f * defaultDP;
        linePadding = 24f * defaultDP;

        unCompletedPaint = new Paint();
        unCompletedPaint.setAntiAlias(true);
        unCompletedPaint.setColor(unCompletedLineColor);
        unCompletedPaint.setStyle(Paint.Style.STROKE);
        unCompletedPaint.setStrokeWidth(unCompletedLineWidth);
        unCompletedPaint.setPathEffect(effects);

        completedPaint = new Paint();
        completedPaint.setAntiAlias(true);
        completedPaint.setColor(completedLineColor);
        completedPaint.setStyle(Paint.Style.FILL);

        completeIcon = ContextCompat.getDrawable(getContext(), R.drawable.check_circle);
        attentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.radio_checked_circle);
        defaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.unchecked_circle);

        isReverseDraw = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = (int) (circleRadius * 2 + getPaddingLeft() + getPaddingRight());
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        int desiredHeight = 0;
        if (circleCenterPointPositionList != null && !circleCenterPointPositionList.isEmpty()) {
            float lastPos = circleCenterPointPositionList.get(circleCenterPointPositionList.size() - 1);
            desiredHeight = (int) (lastPos + circleRadius + getPaddingBottom());
        } else if (stepNum > 0) {
            desiredHeight = (int) (getPaddingTop() + getPaddingBottom() + circleRadius * 2 * stepNum + (stepNum - 1) * linePadding);
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.max(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        centerX = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2f;
        leftY = centerX - (completedLineWidth / 2f);
        rightY = centerX + (completedLineWidth / 2f);

        if (circleCenterPointPositionList.isEmpty() && stepNum > 0) {
            for (int i = 0; i < stepNum; i++) {
                if (isReverseDraw) {
                    circleCenterPointPositionList.add(h - (circleRadius + i * circleRadius * 2 + i * linePadding));
                } else {
                    circleCenterPointPositionList.add(circleRadius + i * circleRadius * 2 + i * linePadding);
                }
            }
        }

        if (onDrawListener != null) {
            onDrawListener.onDrawIndicator();
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (circleCenterPointPositionList == null || circleCenterPointPositionList.isEmpty()) {
            return;
        }

        centerX = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2f;
        leftY = centerX - (completedLineWidth / 2f);
        rightY = centerX + (completedLineWidth / 2f);

        // Draw connector lines
        for (int i = 0; i < circleCenterPointPositionList.size() - 1; i++) {
            float prePos = circleCenterPointPositionList.get(i);
            float afterPos = circleCenterPointPositionList.get(i + 1);

            int startStepIndex = isReverseDraw ? (circleCenterPointPositionList.size() - 1 - i) : i;

            if (startStepIndex < completingPosition) {
                if (isReverseDraw) {
                    canvas.drawRect(leftY, afterPos + circleRadius, rightY, prePos - circleRadius, completedPaint);
                } else {
                    canvas.drawRect(leftY, prePos + circleRadius, rightY, afterPos - circleRadius, completedPaint);
                }
            } else {
                path.reset();
                if (isReverseDraw) {
                    path.moveTo(centerX, afterPos + circleRadius);
                    path.lineTo(centerX, prePos - circleRadius);
                } else {
                    path.moveTo(centerX, prePos + circleRadius);
                    path.lineTo(centerX, afterPos - circleRadius);
                }
                canvas.drawPath(path, unCompletedPaint);
            }
        }

        // Draw step icons
        for (int i = 0; i < circleCenterPointPositionList.size(); i++) {
            float currentY = circleCenterPointPositionList.get(i);

            int rLeft = (int) (centerX - circleRadius);
            int rTop = (int) (currentY - circleRadius);
            int rRight = (int) (centerX + circleRadius);
            int rBottom = (int) (currentY + circleRadius);

            iconRect.set(rLeft, rTop, rRight, rBottom);

            int stepIndex = isReverseDraw ? (circleCenterPointPositionList.size() - 1 - i) : i;

            Drawable icon;
            int tint;
            if (stepIndex < completingPosition) {
                icon = completeIcon;
                tint = completeIconTint != null ? completeIconTint : completedLineColor;
            } else if (stepIndex == completingPosition) {
                icon = attentionIcon;
                tint = attentionIconTint != null ? attentionIconTint : completedLineColor;
            } else {
                icon = defaultIcon;
                tint = defaultIconTint != null ? defaultIconTint : unCompletedLineColor;
            }

            if (icon != null) {
                Drawable wrapped = DrawableCompat.wrap(icon.mutate());
                DrawableCompat.setTint(wrapped, tint);
                wrapped.setBounds(iconRect);
                wrapped.draw(canvas);
            }
        }
    }

    public List<Float> getCircleCenterPointPositionList() {
        return circleCenterPointPositionList;
    }

    public void setCircleCenterPointPositionList(List<Float> positions) {
        if (positions == null) return;
        if (this.circleCenterPointPositionList != null && this.circleCenterPointPositionList.equals(positions)) {
            return;
        }
        this.circleCenterPointPositionList = new ArrayList<>(positions);
        invalidate();
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
        requestLayout();
    }

    public void setIndicatorLinePaddingProportion(float linePaddingProportion) {
        this.linePadding = linePaddingProportion * defaultDP;
        invalidate();
    }

    public void setCompletingPosition(int completingPosition) {
        this.completingPosition = completingPosition;
        invalidate();
    }

    public void setAllStepsCompleted() {
        this.completingPosition = this.stepNum;
        invalidate();
    }

    public void setUnCompletedLineColor(int unCompletedLineColor) {
        this.unCompletedLineColor = unCompletedLineColor;
        this.unCompletedPaint.setColor(unCompletedLineColor);
        invalidate();
    }

    public void setCompletedLineColor(int completedLineColor) {
        this.completedLineColor = completedLineColor;
        this.completedPaint.setColor(completedLineColor);
        invalidate();
    }

    public void setCompletedLineWidth(float widthPx) {
        this.completedLineWidth = widthPx;
        this.completedPaint.setStrokeWidth(widthPx);
        invalidate();
    }

    public void setUnCompletedLineWidth(float widthPx) {
        this.unCompletedLineWidth = widthPx;
        this.unCompletedPaint.setStrokeWidth(widthPx);
        invalidate();
    }

    public void setCompleteIconTint(int tintColor) {
        this.completeIconTint = tintColor;
        invalidate();
    }

    public void setAttentionIconTint(int tintColor) {
        this.attentionIconTint = tintColor;
        invalidate();
    }

    public void setDefaultIconTint(int tintColor) {
        this.defaultIconTint = tintColor;
        invalidate();
    }

    public void reverseDraw(boolean isReverseDraw) {
        this.isReverseDraw = isReverseDraw;
        invalidate();
    }

    public void setDefaultIcon(Drawable defaultIcon) {
        this.defaultIcon = defaultIcon;
        invalidate();
    }

    public void setCompleteIcon(Drawable completeIcon) {
        this.completeIcon = completeIcon;
        invalidate();
    }

    public void setAttentionIcon(Drawable attentionIcon) {
        this.attentionIcon = attentionIcon;
        invalidate();
    }

    public interface OnDrawIndicatorListener {
        void onDrawIndicator();
    }
}