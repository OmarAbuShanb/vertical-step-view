# Vertical Step View

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![API](https://img.shields.io/badge/API-21%2B-blue.svg)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/License-MIT-brightgreen.svg)](LICENSE)

A lightweight, customizable, and smooth Vertical Stepper / Timeline component for Android. Perfect for displaying order tracking, process flows, status timelines, and multi-step forms. Includes automatic **Android Studio Layout Editor Preview** support!

---

## 📱 Demo

<img width="400" height="667" alt="Screenrecorder-2026-09-26-01-05-11-783" src="https://github.com/user-attachments/assets/d5b55f6d-84ba-48d7-90ae-9ae8dbc9ead4" />

---

## ✨ Features

- 👁️ **Android Studio Layout Editor Live Preview**: Shows an automatic 3-step preview directly in the layout editor without running the app.
- 🎨 **XML & Programmatic Customization**: Configure styling via custom XML attributes or programmatically in Java/Kotlin.
- 📱 **RTL & LTR Native Support**: Seamlessly adapts to Arabic and English layout directions based on system or view locale.
- 📝 **Structured Data Models**: Pass structured `StepItem` objects containing both **Title** and **Description**.
- 🎨 **Independent Title & Description Styling**: Customize text size, colors, font typefaces, and bold weights separately for titles and descriptions.
- 🌈 **Dynamic Icon Tinting**: Tint completed, in-progress, and pending icons with custom colors.
- 📐 **Dynamic Multi-line Wrapping**: Automatically adjusts step heights and connecting lines for multi-line text descriptions without clipping or text overlap.
- 🚀 **Smooth Performance**: No memory allocations in `onDraw` and zero unnecessary layout requests during scrolling.
- 🔄 **State Preservation**: Saves and restores state automatically across screen rotations and configuration changes.

---

## 💻 Installation

Add the repository in your root `settings.gradle` or `build.gradle`:

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app module's `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.omarabushanb:vertical-step-view:1.0.0'
}
```

---

## 🚀 Quick Start

### 1. Add `VerticalStepView` to your Layout XML with Custom Attributes

```xml
<dev.anonymous.vertical_step_view.VerticalStepView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/stepView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="16dp"
    app:titleTextSize="15sp"
    app:completedTitleColor="#0F172A"
    app:unCompletedTitleColor="#94A3B8"
    app:descriptionTextSize="13sp"
    app:completedDescriptionColor="#475569"
    app:unCompletedDescriptionColor="#CBD5E1"
    app:completedLineColor="#4F46E5"
    app:unCompletedLineColor="#E2E8F0"
    app:completeIconTint="#4F46E5"
    app:attentionIconTint="#4F46E5"
    app:defaultIconTint="#94A3B8"
    app:circleRadius="12dp"
    app:linePadding="20dp"
    app:textContainerMarginStart="14dp"
    app:completingPosition="1" />
```

### 2. Configure Data in Code

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<StepItem> steps = new ArrayList<>();
        steps.add(new StepItem("Order Placed", "25 Sep 2026, 09:30 AM — Order received by merchant"));
        steps.add(new StepItem("Payment Confirmed", "25 Sep 2026, 09:32 AM — Paid via Apple Pay ($120.00)"));
        steps.add(new StepItem("In Transit", "25 Sep 2026, 02:45 PM — Tracking #TRK-98214"));
        steps.add(new StepItem("Delivered", "Recipient signature received at destination address"));

        VerticalStepView stepView = findViewById(R.id.stepView);
        stepView.setStepItems(steps);
        stepView.setStepsViewIndicatorCompletingPosition(2);
    }
}
```

---

## 📖 XML Attributes Reference

| XML Attribute | Format | Description |
| :--- | :--- | :--- |
| `app:titleTextSize` | `dimension` | Text size for step titles (e.g. `15sp`). |
| `app:completedTitleColor` | `color` | Color for completed step titles. |
| `app:unCompletedTitleColor` | `color` | Color for pending step titles. |
| `app:titleBoldForCompleted` | `boolean` | Enable/disable bold for completed titles. |
| `app:descriptionTextSize` | `dimension` | Text size for descriptions (e.g. `13sp`). |
| `app:completedDescriptionColor` | `color` | Color for completed step descriptions. |
| `app:unCompletedDescriptionColor` | `color` | Color for pending step descriptions. |
| `app:descriptionBold` | `boolean` | Enable/disable bold for descriptions. |
| `app:titleDescriptionGap` | `dimension` | Vertical spacing between title and description. |
| `app:linePadding` | `dimension` | Vertical padding between steps (e.g. `20dp`). |
| `app:circleRadius` | `dimension` | Radius of step indicator circles (e.g. `12dp`). |
| `app:textContainerMarginStart` | `dimension` | Gap between indicator icons and text. |
| `app:completedLineColor` | `color` | Line color for completed steps. |
| `app:unCompletedLineColor` | `color` | Line color for pending steps. |
| `app:completeIconTint` | `color` | Color tint for completed checkmark icon. |
| `app:attentionIconTint` | `color` | Color tint for in-progress step icon. |
| `app:defaultIconTint` | `color` | Color tint for pending step icons. |
| `app:completeIcon` | `reference` | Custom drawable for completed icon. |
| `app:attentionIcon` | `reference` | Custom drawable for in-progress icon. |
| `app:defaultIcon` | `reference` | Custom drawable for pending icon. |
| `app:completingPosition` | `integer` | Active step index (0-indexed). |
| `app:reverseDraw` | `boolean` | Reverse step rendering order vertically. |

---

## 📖 API Reference (Java / Kotlin)

### Data Methods
| Method | Description |
| :--- | :--- |
| `setStepItems(List<StepItem> items)` | Sets a list of `StepItem` objects (Title + Description). |
| `setStepViewTexts(List<String> texts)` | Backwards-compatible method for a list of string titles. |
| `setStepsViewIndicatorCompletingPosition(int pos)` | Sets the current completed/in-progress step index (0-indexed). |
| `setAllStepsCompleted()` | Marks all steps as completed with checkmarks. |

### Title Customization
| Method | Description |
| :--- | :--- |
| `setTitleTextSize(int sp)` | Sets the text size for step titles in SP. |
| `setCompletedTitleTextColor(int color)` | Sets the title text color for completed steps. |
| `setUnCompletedTitleTextColor(int color)` | Sets the title text color for pending steps. |
| `setTitleTypeface(Typeface typeface)` | Sets a custom typeface/font for titles. |
| `setTitleBoldForCompleted(boolean isBold)` | Enables or disables bold text for completed step titles. |

### Description Customization
| Method | Description |
| :--- | :--- |
| `setDescriptionTextSize(int sp)` | Sets the text size for descriptions in SP. |
| `setCompletedDescriptionTextColor(int color)` | Sets the description text color for completed steps. |
| `setUnCompletedDescriptionTextColor(int color)` | Sets the description text color for pending steps. |
| `setDescriptionTypeface(Typeface typeface)` | Sets a custom typeface/font for descriptions. |
| `setDescriptionBold(boolean isBold)` | Enables or disables bold text for descriptions. |
| `setTitleDescriptionGapDp(float gapDp)` | Sets vertical gap/spacing between title and description in DP. |

### Indicator, Line & Icon Customization
| Method | Description |
| :--- | :--- |
| `setStepsViewIndicatorCompletedLineColor(int color)` | Sets line color for completed connector lines. |
| `setStepsViewIndicatorUnCompletedLineColor(int color)` | Sets line color for pending connector lines. |
| `setCompleteIconTint(int tintColor)` | Tints the checkmark icon for completed steps. |
| `setAttentionIconTint(int tintColor)` | Tints the in-progress step icon. |
| `setDefaultIconTint(int tintColor)` | Tints the pending step icons. |
| `setIndicatorCircleRadiusDp(float radiusDp)` | Sets step circle icon radius in DP. |
| `setLinePaddingDp(float paddingDp)` | Sets vertical padding between step items in DP. |
| `setTextContainerMarginStartDp(int marginDp)` | Sets distance between indicator icons and text in DP. |
| `reverseDraw(boolean isReverse)` | Reverses step drawing order vertically. |

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
