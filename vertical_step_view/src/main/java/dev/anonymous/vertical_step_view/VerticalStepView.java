package dev.anonymous.vertical_step_view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class VerticalStepView extends LinearLayout
        implements VerticalStepViewIndicator.OnDrawIndicatorListener {
    private RelativeLayout textContainer;
    private VerticalStepViewIndicator stepsViewIndicator;
    private List<String> texts;
    private int completingPosition;
    private int unComplectedTextColor = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
    private int complectedTextColor = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);

    private int textSize = 14; // default textSize


    public VerticalStepView(Context context) {
        this(context, null);
    }

    public VerticalStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_vertical_steps_view, this);
        stepsViewIndicator = rootView.findViewById(R.id.steps_indicator);
        stepsViewIndicator.setOnDrawListener(this);
        textContainer = rootView.findViewById(R.id.rl_text_container);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public VerticalStepView setStepViewTexts(List<String> texts) {
        this.texts = texts;
        stepsViewIndicator.setStepNum(texts.size());
        return this;
    }

    public VerticalStepView setStepsViewIndicatorCompletingPosition(int completingPosition) {
        this.completingPosition = completingPosition;
        stepsViewIndicator.setCompletingPosition(completingPosition);
        return this;
    }

    public VerticalStepView setStepViewUnComplectedTextColor(int unComplectedTextColor) {
        this.unComplectedTextColor = unComplectedTextColor;
        return this;
    }

    public VerticalStepView setStepViewComplectedTextColor(int complectedTextColor) {
        this.complectedTextColor = complectedTextColor;
        return this;
    }

    public VerticalStepView setStepsViewIndicatorUnCompletedLineColor(int unCompletedLineColor) {
        stepsViewIndicator.setUnCompletedLineColor(unCompletedLineColor);
        return this;
    }

    public VerticalStepView setStepsViewIndicatorCompletedLineColor(int completedLineColor) {
        stepsViewIndicator.setCompletedLineColor(completedLineColor);
        return this;
    }

    public VerticalStepView setStepsViewIndicatorDefaultIcon(Drawable defaultIcon) {
        stepsViewIndicator.setDefaultIcon(defaultIcon);
        return this;
    }

    public VerticalStepView setStepsViewIndicatorCompleteIcon(Drawable completeIcon) {
        stepsViewIndicator.setCompleteIcon(completeIcon);
        return this;
    }

    public VerticalStepView setStepsViewIndicatorAttentionIcon(Drawable attentionIcon) {
        stepsViewIndicator.setAttentionIcon(attentionIcon);
        return this;
    }

    public VerticalStepView reverseDraw(boolean isReverse) {
        this.stepsViewIndicator.reverseDraw(isReverse);
        return this;
    }

    public VerticalStepView setLinePaddingProportion(float linePaddingProportion) {
        this.stepsViewIndicator.setIndicatorLinePaddingProportion(linePaddingProportion);
        return this;
    }

    public VerticalStepView setTextSize(int textSize) {
        if (textSize > 0) {
            this.textSize = textSize;
        }
        return this;
    }

    @Override
    public void onDrawIndicator() {
        if (textContainer != null) {
            textContainer.removeAllViews();
            List<Float> completedXPosition = stepsViewIndicator.getCircleCenterPointPositionList();

            if (texts != null && completedXPosition != null && !completedXPosition.isEmpty()) {

                for (int i = 0; i < texts.size(); i++) {
                    TextView textView = new TextView(getContext());
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                    textView.setText(texts.get(i));

                    // - stepsViewIndicator.getCircleRadius() / 2  => text start from (half / 2)icon
                    textView.setY(completedXPosition.get(i) - stepsViewIndicator.getCircleRadius() / 2);
                    textView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));

                    // all text is bold except unComplected
                    if (i <= completingPosition) {
                        textView.setTypeface(null, Typeface.BOLD);
                        textView.setTextColor(complectedTextColor);
                    } else {
                        textView.setTextColor(unComplectedTextColor);
                    }

                    textContainer.addView(textView);
                }
            }
        }
    }
}
