package dev.anonymous.vertical_step_view;

public class StepItem {
    private String title;
    private String description;

    public StepItem(String title) {
        this(title, null);
    }

    public StepItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}