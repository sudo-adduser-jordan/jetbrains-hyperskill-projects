package file.type.analyzer.tools;


public class Pattern {
    private int priority;
    private String pattern;
    private String fileType;

    public Pattern(int priority, String pattern, String fileType) {
        this.priority = priority;
        this.pattern = pattern;
        this.fileType = fileType;
    }

    public Pattern() {
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
