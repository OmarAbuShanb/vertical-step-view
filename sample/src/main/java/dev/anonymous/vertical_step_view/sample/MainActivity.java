package dev.anonymous.vertical_step_view.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import dev.anonymous.vertical_step_view.StepItem;
import dev.anonymous.vertical_step_view.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int currentStep = 3;
    private final List<StepItem> steps = getOrderTrackingSteps();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupStepper();
        setupButtons();
        updateUI();
    }

    private void setupStepper() {
        int activeColor = ContextCompat.getColor(this, R.color.step_active);
        int titleActiveColor = ContextCompat.getColor(this, R.color.step_title_active);
        int descActiveColor = ContextCompat.getColor(this, R.color.step_desc_active);
        int inactiveColor = ContextCompat.getColor(this, R.color.step_inactive);
        int descInactiveColor = ContextCompat.getColor(this, R.color.step_desc_inactive);
        int lineInactiveColor = ContextCompat.getColor(this, R.color.step_line_inactive);

        binding.stepView
                .setStepItems(steps)
                // Title Styling
                .setTitleTextSize(15)
                .setCompletedTitleTextColor(titleActiveColor)
                .setUnCompletedTitleTextColor(inactiveColor)
                .setTitleBoldForCompleted(true)
                // Description Styling
                .setDescriptionTextSize(13)
                .setCompletedDescriptionTextColor(descActiveColor)
                .setUnCompletedDescriptionTextColor(descInactiveColor)
                .setDescriptionBold(false)
                .setTitleDescriptionGapDp(3)
                // Spacing & Dimensions
                .setLinePaddingDp(20)
                .setIndicatorCircleRadiusDp(12)
                .setTextContainerMarginStartDp(14)
                // Line & Icon Colors
                .setStepsViewIndicatorCompletedLineColor(activeColor)
                .setStepsViewIndicatorUnCompletedLineColor(lineInactiveColor)
                .setCompleteIconTint(activeColor)
                .setAttentionIconTint(activeColor)
                .setDefaultIconTint(inactiveColor)
                .setStepsViewIndicatorCompletingPosition(currentStep);
    }

    private void setupButtons() {
        binding.btnPrev.setOnClickListener(v -> {
            if (currentStep > 0) {
                currentStep--;
                updateUI();
            }
        });

        binding.btnNext.setOnClickListener(v -> {
            if (currentStep < steps.size() - 1) {
                currentStep++;
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (currentStep == steps.size() - 1) {
            // When all steps are finished, mark all icons as completed
            binding.stepView.setAllStepsCompleted();
        } else {
            binding.stepView.setStepsViewIndicatorCompletingPosition(currentStep);
        }

        binding.btnPrev.setEnabled(currentStep > 0);
        binding.btnNext.setEnabled(currentStep < steps.size() - 1);

        String[] statuses = {
                "Order Placed",
                "Payment Confirmed",
                "Processing",
                "In Transit",
                "Out for Delivery",
                "Delivered"
        };
        if (currentStep < statuses.length) {
            binding.tvStatusBadge.setText(statuses[currentStep]);
        }
    }

    private static List<StepItem> getOrderTrackingSteps() {
        List<StepItem> list = new ArrayList<>();
        list.add(new StepItem(
                "Order Placed",
                "25 Sep 2026, 09:30 AM — Order received by merchant"
        ));
        list.add(new StepItem(
                "Payment Confirmed",
                "25 Sep 2026, 09:32 AM — Paid via Apple Pay ($120.00)"
        ));
        list.add(new StepItem(
                "Order Packed & Ready",
                "25 Sep 2026, 11:15 AM — Warehouse Riyadh Logistics Hub"
        ));
        list.add(new StepItem(
                "Handed to Courier",
                "25 Sep 2026, 02:45 PM — Tracking #TRK-98214"
        ));
        list.add(new StepItem(
                "Out for Delivery",
                "Driver: Ahmed Hassan (+966 50 123 4567)"
        ));
        list.add(new StepItem(
                "Delivered",
                "Recipient signature received at destination address"
        ));
        return list;
    }
}