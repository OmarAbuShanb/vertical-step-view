package dev.anonymous.vertical_step_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class VerticalStepView extends LinearLayout {
    private LinearLayout textContainer;
    private VerticalStepViewIndicator stepsViewIndicator;

    private List<StepItem> stepItems = new ArrayList<>();
    private int completingPosition;

    // Title Styling Properties
    private int completedTitleColor = ContextCompat.getColor(
            getContext(), android.R.color.black
    );
    private int unCompletedTitleColor = ContextCompat.getColor(
            getContext(), android.R.color.darker_gray
    );
    private float titleTextSizePx;
    private Typeface titleTypeface;
    private boolean isTitleBoldForCompleted = true;

    // Description Styling Properties
    private int completedDescriptionColor = ContextCompat.getColor(
            getContext(), android.R.color.darker_gray
    );
    private int unCompletedDescriptionColor = ContextCompat.getColor(
            getContext(), android.R.color.darker_gray
    );
    private float descriptionTextSizePx;
    private Typeface descriptionTypeface;
    private boolean isDescriptionBold = false;
    private float titleDescriptionGapPx;

    private float linePaddingPx;

    public VerticalStepView(Context context) {
        this(context, null);
    }

    public VerticalStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(HORIZONTAL);

        titleTextSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                15,
                getResources().getDisplayMetrics()
        );
        descriptionTextSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                13,
                getResources().getDisplayMetrics()
        );
        linePaddingPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics()
        );
        titleDescriptionGapPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4,
                getResources().getDisplayMetrics()
        );

        View rootView = LayoutInflater.from(getContext()).inflate(
                R.layout.widget_vertical_steps_view,
                this,
                true
        );
        stepsViewIndicator = rootView.findViewById(R.id.steps_indicator);
        textContainer = rootView.findViewById(R.id.rl_text_container);

        if (attrs != null) {
            try (TypedArray ta = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.VerticalStepView,
                    defStyleAttr,
                    0
            )) {
                if (ta.hasValue(R.styleable.VerticalStepView_titleTextSize)) {
                    titleTextSizePx = ta.getDimensionPixelSize(
                            R.styleable.VerticalStepView_titleTextSize,
                            (int) titleTextSizePx
                    );
                }

                completedTitleColor = ta.getColor(
                        R.styleable.VerticalStepView_completedTitleColor,
                        completedTitleColor
                );
                unCompletedTitleColor = ta.getColor(
                        R.styleable.VerticalStepView_unCompletedTitleColor,
                        unCompletedTitleColor
                );
                isTitleBoldForCompleted = ta.getBoolean(
                        R.styleable.VerticalStepView_titleBoldForCompleted,
                        isTitleBoldForCompleted
                );

                if (ta.hasValue(R.styleable.VerticalStepView_descriptionTextSize)) {
                    descriptionTextSizePx = ta.getDimensionPixelSize(
                            R.styleable.VerticalStepView_descriptionTextSize,
                            (int) descriptionTextSizePx
                    );
                }

                completedDescriptionColor = ta.getColor(
                        R.styleable.VerticalStepView_completedDescriptionColor,
                        completedDescriptionColor
                );
                unCompletedDescriptionColor = ta.getColor(
                        R.styleable.VerticalStepView_unCompletedDescriptionColor,
                        unCompletedDescriptionColor
                );
                isDescriptionBold = ta.getBoolean(
                        R.styleable.VerticalStepView_descriptionBold,
                        isDescriptionBold
                );
                titleDescriptionGapPx = ta.getDimension(
                        R.styleable.VerticalStepView_titleDescriptionGap,
                        titleDescriptionGapPx
                );

                linePaddingPx = ta.getDimension(
                        R.styleable.VerticalStepView_linePadding,
                        linePaddingPx
                );

                if (ta.hasValue(R.styleable.VerticalStepView_circleRadius)) {
                    float radiusPx = ta.getDimension(R.styleable.VerticalStepView_circleRadius, 0);
                    stepsViewIndicator.setCircleRadius(radiusPx);
                }

                if (ta.hasValue(R.styleable.VerticalStepView_textContainerMarginStart)) {
                    int marginPx = (int) ta.getDimension(R.styleable.VerticalStepView_textContainerMarginStart, 0);
                    MarginLayoutParams lp = (MarginLayoutParams) textContainer.getLayoutParams();
                    if (lp != null) {
                        lp.setMarginStart(marginPx);
                        textContainer.setLayoutParams(lp);
                    }
                }

                if (ta.hasValue(R.styleable.VerticalStepView_completedLineColor)) {
                    stepsViewIndicator.setCompletedLineColor(
                            ta.getColor(R.styleable.VerticalStepView_completedLineColor, Color.BLACK)
                    );
                }
                if (ta.hasValue(R.styleable.VerticalStepView_unCompletedLineColor)) {
                    stepsViewIndicator.setUnCompletedLineColor(
                            ta.getColor(R.styleable.VerticalStepView_unCompletedLineColor, Color.GRAY)
                    );
                }

                if (ta.hasValue(R.styleable.VerticalStepView_completeIconTint)) {
                    stepsViewIndicator.setCompleteIconTint(
                            ta.getColor(R.styleable.VerticalStepView_completeIconTint, 0)
                    );
                }
                if (ta.hasValue(R.styleable.VerticalStepView_attentionIconTint)) {
                    stepsViewIndicator.setAttentionIconTint(
                            ta.getColor(R.styleable.VerticalStepView_attentionIconTint, 0)
                    );
                }
                if (ta.hasValue(R.styleable.VerticalStepView_defaultIconTint)) {
                    stepsViewIndicator.setDefaultIconTint(
                            ta.getColor(R.styleable.VerticalStepView_defaultIconTint, 0)
                    );
                }

                if (ta.hasValue(R.styleable.VerticalStepView_completeIcon)) {
                    Drawable icon = ta.getDrawable(R.styleable.VerticalStepView_completeIcon);
                    if (icon != null) stepsViewIndicator.setCompleteIcon(icon);
                }
                if (ta.hasValue(R.styleable.VerticalStepView_attentionIcon)) {
                    Drawable icon = ta.getDrawable(R.styleable.VerticalStepView_attentionIcon);
                    if (icon != null) stepsViewIndicator.setAttentionIcon(icon);
                }
                if (ta.hasValue(R.styleable.VerticalStepView_defaultIcon)) {
                    Drawable icon = ta.getDrawable(R.styleable.VerticalStepView_defaultIcon);
                    if (icon != null) stepsViewIndicator.setDefaultIcon(icon);
                }

                if (ta.hasValue(R.styleable.VerticalStepView_reverseDraw)) {
                    stepsViewIndicator.reverseDraw(
                            ta.getBoolean(R.styleable.VerticalStepView_reverseDraw, false)
                    );
                }

                completingPosition = ta.getInt(
                        R.styleable.VerticalStepView_completingPosition,
                        0
                );
                stepsViewIndicator.setCompletingPosition(completingPosition);
            }
        }

        if (isInEditMode()) {
            setupEditModePreview();
        }
    }

    private void setupEditModePreview() {
        List<StepItem> dummySteps = new ArrayList<>();
        dummySteps.add(new StepItem("First Step Title", "This is the description for the first step."));
        dummySteps.add(new StepItem("Second Step Title", "This is the description for the second step."));
        dummySteps.add(new StepItem("Third Step Title", "This is the description for the third step."));

        setStepItems(dummySteps);
        setStepsViewIndicatorCompletingPosition(1); // Position set to 2nd step (0-indexed 1)
    }

    public VerticalStepView setStepItems(List<StepItem> items) {
        this.stepItems = items != null ? items : new ArrayList<>();
        rebuildViews();
        return this;
    }

    public VerticalStepView setStepViewTexts(List<String> texts) {
        List<StepItem> items = new ArrayList<>();
        if (texts != null) {
            for (String text : texts) {
                items.add(new StepItem(text));
            }
        }
        return setStepItems(items);
    }

    private void rebuildViews() {
        if (textContainer == null) return;
        textContainer.removeAllViews();

        if (stepItems != null) {
            for (int i = 0; i < stepItems.size(); i++) {
                StepItem item = stepItems.get(i);

                LinearLayout itemContainer = new LinearLayout(getContext());
                itemContainer.setOrientation(VERTICAL);
                LayoutParams containerLp = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                if (i < stepItems.size() - 1) {
                    containerLp.bottomMargin = (int) linePaddingPx;
                }
                itemContainer.setLayoutParams(containerLp);

                // Title
                TextView tvTitle = new TextView(getContext());
                tvTitle.setLayoutParams(new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSizePx);
                tvTitle.setText(item.getTitle());
                tvTitle.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);

                boolean isCompleted = i <= completingPosition;

                // Title Style
                int titleColor = isCompleted ? completedTitleColor : unCompletedTitleColor;
                tvTitle.setTextColor(titleColor);

                int titleStyle = (isCompleted && isTitleBoldForCompleted) ? Typeface.BOLD : Typeface.NORMAL;
                if (titleTypeface != null) {
                    tvTitle.setTypeface(titleTypeface, titleStyle);
                } else {
                    tvTitle.setTypeface(null, titleStyle);
                }

                itemContainer.addView(tvTitle);

                // Description (if present)
                if (item.getDescription() != null && !item.getDescription().isEmpty()) {
                    TextView tvDescription = new TextView(getContext());
                    LayoutParams descLp = new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    descLp.topMargin = (int) titleDescriptionGapPx;
                    tvDescription.setLayoutParams(descLp);
                    tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, descriptionTextSizePx);
                    tvDescription.setText(item.getDescription());
                    tvDescription.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);

                    int descColor = isCompleted ? completedDescriptionColor : unCompletedDescriptionColor;
                    tvDescription.setTextColor(descColor);

                    int descStyle = isDescriptionBold ? Typeface.BOLD : Typeface.NORMAL;
                    if (descriptionTypeface != null) {
                        tvDescription.setTypeface(descriptionTypeface, descStyle);
                    } else {
                        tvDescription.setTypeface(null, descStyle);
                    }

                    itemContainer.addView(tvDescription);
                }

                textContainer.addView(itemContainer);
            }
        }
        stepsViewIndicator.setStepNum(stepItems != null ? stepItems.size() : 0);
    }

    public VerticalStepView setStepsViewIndicatorCompletingPosition(int completingPosition) {
        this.completingPosition = completingPosition;
        stepsViewIndicator.setCompletingPosition(completingPosition);
        rebuildViews();
        return this;
    }

    public VerticalStepView setAllStepsCompleted() {
        if (stepItems != null) {
            this.completingPosition = stepItems.size();
            stepsViewIndicator.setAllStepsCompleted();
            rebuildViews();
        }
        return this;
    }

    // Title Setters
    public VerticalStepView setTitleTextSize(int sp) {
        this.titleTextSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics()
        );
        rebuildViews();
        return this;
    }

    public VerticalStepView setCompletedTitleTextColor(int color) {
        this.completedTitleColor = color;
        rebuildViews();
        return this;
    }

    public VerticalStepView setUnCompletedTitleTextColor(int color) {
        this.unCompletedTitleColor = color;
        rebuildViews();
        return this;
    }

    public VerticalStepView setTitleTypeface(Typeface typeface) {
        this.titleTypeface = typeface;
        rebuildViews();
        return this;
    }

    public VerticalStepView setTitleBoldForCompleted(boolean isBold) {
        this.isTitleBoldForCompleted = isBold;
        rebuildViews();
        return this;
    }

    // Description Setters
    public VerticalStepView setDescriptionTextSize(int sp) {
        this.descriptionTextSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics()
        );
        rebuildViews();
        return this;
    }

    public VerticalStepView setCompletedDescriptionTextColor(int color) {
        this.completedDescriptionColor = color;
        rebuildViews();
        return this;
    }

    public VerticalStepView setUnCompletedDescriptionTextColor(int color) {
        this.unCompletedDescriptionColor = color;
        rebuildViews();
        return this;
    }

    public VerticalStepView setDescriptionTypeface(Typeface typeface) {
        this.descriptionTypeface = typeface;
        rebuildViews();
        return this;
    }

    public VerticalStepView setDescriptionBold(boolean isBold) {
        this.isDescriptionBold = isBold;
        rebuildViews();
        return this;
    }

    public VerticalStepView setTitleDescriptionGapDp(float gapDp) {
        this.titleDescriptionGapPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                gapDp,
                getResources().getDisplayMetrics()
        );
        rebuildViews();
        return this;
    }

    // Backwards Compatibility Aliases
    public VerticalStepView setStepViewComplectedTextColor(int color) {
        return setCompletedTitleTextColor(color);
    }

    public VerticalStepView setStepViewCompletedTextColor(int color) {
        return setCompletedTitleTextColor(color);
    }

    public VerticalStepView setStepViewUnComplectedTextColor(int color) {
        return setUnCompletedTitleTextColor(color);
    }

    public VerticalStepView setStepViewUnCompletedTextColor(int color) {
        return setUnCompletedTitleTextColor(color);
    }

    public VerticalStepView setTextSize(int sp) {
        return setTitleTextSize(sp);
    }

    // Indicator Line & Icon Methods
    public VerticalStepView setStepsViewIndicatorUnCompletedLineColor(int unCompletedLineColor) {
        stepsViewIndicator.setUnCompletedLineColor(unCompletedLineColor);
        return this;
    }

    public VerticalStepView setStepsViewIndicatorCompletedLineColor(int completedLineColor) {
        stepsViewIndicator.setCompletedLineColor(completedLineColor);
        return this;
    }

    public VerticalStepView setCompleteIconTint(int tintColor) {
        stepsViewIndicator.setCompleteIconTint(tintColor);
        return this;
    }

    public VerticalStepView setAttentionIconTint(int tintColor) {
        stepsViewIndicator.setAttentionIconTint(tintColor);
        return this;
    }

    public VerticalStepView setDefaultIconTint(int tintColor) {
        stepsViewIndicator.setDefaultIconTint(tintColor);
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
        this.linePaddingPx = linePaddingProportion * TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics()
        );
        rebuildViews();
        return this;
    }

    public VerticalStepView setLinePaddingDp(float paddingDp) {
        this.linePaddingPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                paddingDp,
                getResources().getDisplayMetrics()
        );
        rebuildViews();
        return this;
    }

    public VerticalStepView setIndicatorCircleRadiusDp(float radiusDp) {
        float radiusPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                radiusDp,
                getResources().getDisplayMetrics()
        );
        stepsViewIndicator.setCircleRadius(radiusPx);
        return this;
    }

    public VerticalStepView setTextContainerMarginStartDp(int marginDp) {
        if (textContainer != null) {
            int marginPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    marginDp,
                    getResources().getDisplayMetrics()
            );
            MarginLayoutParams lp = (MarginLayoutParams) textContainer.getLayoutParams();
            if (lp != null) {
                lp.setMarginStart(marginPx);
                textContainer.setLayoutParams(lp);
            }
        }
        return this;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        updateCirclePositions();
    }

    private void updateCirclePositions() {
        if (textContainer == null || stepsViewIndicator == null || stepItems == null || stepItems.isEmpty()) {
            return;
        }
        int childCount = textContainer.getChildCount();
        if (childCount == 0) return;

        List<Float> positions = new ArrayList<>(childCount);
        float circleRadius = stepsViewIndicator.getCircleRadius();

        for (int i = 0; i < childCount; i++) {
            View child = textContainer.getChildAt(i);
            int itemTop = child.getTop();

            float centerY;
            if (child instanceof ViewGroup && ((ViewGroup) child).getChildCount() > 0) {
                View titleView = ((ViewGroup) child).getChildAt(0);
                if (titleView instanceof TextView) {
                    TextView tvTitle = (TextView) titleView;
                    if (tvTitle.getLayout() != null && tvTitle.getLineCount() > 0) {
                        int line0Top = tvTitle.getLayout().getLineTop(0);
                        int line0Bottom = tvTitle.getLayout().getLineBottom(0);
                        float firstLineCenter = (itemTop + tvTitle.getTop()) + (line0Top + line0Bottom) / 2f;
                        centerY = Math.max(itemTop + circleRadius, firstLineCenter);
                    } else {
                        centerY = itemTop + Math.max(circleRadius, titleView.getHeight() / 2f);
                    }
                } else {
                    centerY = itemTop + Math.max(circleRadius, child.getHeight() / 2f);
                }
            } else {
                centerY = itemTop + Math.max(circleRadius, child.getHeight() / 2f);
            }
            positions.add(centerY);
        }

        stepsViewIndicator.setCircleCenterPointPositionList(positions);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.completingPosition = this.completingPosition;
        ss.titleTextSizePx = this.titleTextSizePx;
        ss.completedTitleColor = this.completedTitleColor;
        ss.unCompletedTitleColor = this.unCompletedTitleColor;
        ss.descriptionTextSizePx = this.descriptionTextSizePx;
        ss.completedDescriptionColor = this.completedDescriptionColor;
        ss.unCompletedDescriptionColor = this.unCompletedDescriptionColor;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.completingPosition = ss.completingPosition;
        this.titleTextSizePx = ss.titleTextSizePx;
        this.completedTitleColor = ss.completedTitleColor;
        this.unCompletedTitleColor = ss.unCompletedTitleColor;
        this.descriptionTextSizePx = ss.descriptionTextSizePx;
        this.completedDescriptionColor = ss.completedDescriptionColor;
        this.unCompletedDescriptionColor = ss.unCompletedDescriptionColor;
        rebuildViews();
    }

    static class SavedState extends BaseSavedState {
        int completingPosition;
        float titleTextSizePx;
        int completedTitleColor;
        int unCompletedTitleColor;
        float descriptionTextSizePx;
        int completedDescriptionColor;
        int unCompletedDescriptionColor;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            completingPosition = in.readInt();
            titleTextSizePx = in.readFloat();
            completedTitleColor = in.readInt();
            unCompletedTitleColor = in.readInt();
            descriptionTextSizePx = in.readFloat();
            completedDescriptionColor = in.readInt();
            unCompletedDescriptionColor = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(completingPosition);
            out.writeFloat(titleTextSizePx);
            out.writeInt(completedTitleColor);
            out.writeInt(unCompletedTitleColor);
            out.writeFloat(descriptionTextSizePx);
            out.writeInt(completedDescriptionColor);
            out.writeInt(unCompletedDescriptionColor);
        }

        public static final Creator<SavedState> CREATOR = new Creator<>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}