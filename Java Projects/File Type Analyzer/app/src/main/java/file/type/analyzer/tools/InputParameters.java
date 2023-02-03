package file.type.analyzer.tools;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class InputParameters {
    private String filepath = "";
    private String patternPath = "";
    private List<Pattern> patternList;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getPatternPath() {
        return patternPath;
    }

    public void setPatternPath(String patternPath) {
        this.patternPath = patternPath;
    }

    public List<Pattern> getPatternList() {
        return patternList;
    }

    public void setPatternList(List<Pattern> patternList) {
        this.patternList = patternList;
    }

    public InputParameters(String[] args) throws IOException {
        this.filepath = args[0];
        this.patternPath = args[1];
        this.patternList = this.readPatterns();
    }

    private List<Pattern> readPatterns() throws IOException {
        List<Pattern> result = new LinkedList<>();
        FileReader
                .readLines(patternPath)
                .forEach(
                        line -> {
                            String[] params = line.split(";");
                            result.add(new Pattern(
                                    Integer.parseInt(params[0]),
                                    params[1].replaceAll("\"", ""),
                                    params[2].replaceAll("\"", ""))
                            );
                        });
        result.sort((p1, p2) -> p2.getPriority() - p1.getPriority());

        return result;
    }
}