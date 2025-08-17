package dev.anonymous.vertical_step_view.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import dev.anonymous.vertical_step_view.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<String> list = getStrings();

        binding.stepView
                .setStepViewTexts(list)
//                .setTextSize(15)
//                .setStepsViewIndicatorCompletingPosition(list.size() - 3)
//                .setLinePaddingProportion(0.85f) // space between icon
//                .setStepsViewIndicatorCompletedLineColor(
//                        ContextCompat.getColor(this, android.R.color.black)
//                ).setStepsViewIndicatorUnCompletedLineColor(
//                        ContextCompat.getColor(this, android.R.color.holo_purple)
//                ).setStepViewComplectedTextColor(
//                        ContextCompat.getColor(this, android.R.color.holo_orange_dark)
//                ).setStepViewUnComplectedTextColor(
//                        ContextCompat.getColor(this, android.R.color.holo_blue_bright)
//                ).setStepsViewIndicatorCompleteIcon(
//                        ContextCompat.getDrawable(this, R.drawable.check_circle)
//                ).setStepsViewIndicatorDefaultIcon(
//                        ContextCompat.getDrawable(this, R.drawable.unchecked_circle)
//                ).setStepsViewIndicatorAttentionIcon(
//                        ContextCompat.getDrawable(this, R.drawable.radio_checked_circle)
//                )
//                .reverseDraw(false)
        ;

    }

    private static List<String> getStrings() {
        List<String> list = new ArrayList<>();
        list.add("is simply dummy text of the printing and typesetting industry.");
        list.add("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s");
        list.add("when an unknown printer took a galley of type and scrambled it to make a type specimen book.");
        list.add("has survived not only five centuries, but also the" +
                " leap into electronic typesetting, remaining essentially unchanged."
        );
        list.add("more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        list.add("It was popularised in the 1960s with the release");
        return list;
    }
}