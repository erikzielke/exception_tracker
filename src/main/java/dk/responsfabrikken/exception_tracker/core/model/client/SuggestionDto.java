package dk.responsfabrikken.exception_tracker.core.model.client;

public class SuggestionDto {
    private int matchStart;
    private int matchEnd;
    private int completionStart;
    private int completionEnd;
    private int caret;
    private String description;
    private String suffix;
    private String option;
    private String styleClass;

    public int getMatchStart() {
        return matchStart;
    }

    public void setMatchStart(int matchStart) {
        this.matchStart = matchStart;
    }

    public int getMatchEnd() {
        return matchEnd;
    }

    public int getCompletionStart() {
        return completionStart;
    }

    public void setCompletionStart(int completionStart) {
        this.completionStart = completionStart;
    }

    public int getCompletionEnd() {
        return completionEnd;
    }

    public void setCompletionEnd(int completionEnd) {
        this.completionEnd = completionEnd;
    }

    public void setMatchEnd(int matchEnd) {
        this.matchEnd = matchEnd;
    }

    public int getCaret() {
        return caret;
    }

    public void setCaret(int caret) {
        this.caret = caret;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
}
