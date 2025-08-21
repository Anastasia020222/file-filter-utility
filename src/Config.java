import java.nio.file.Path;
import java.util.List;

public class Config {

    private Path resultDir;
    private String prefix;
    private boolean addExistingFiles;
    private boolean briefStats;
    private boolean fullStats;
    private List<Path> inputFiles;

    Config(Path resultDir, String prefix, boolean addExistingFiles, boolean briefStats, boolean fullStats, List<Path> inputFiles) {
        this.resultDir = resultDir;
        this.prefix = prefix;
        this.addExistingFiles = addExistingFiles;
        this.briefStats = briefStats;
        this.fullStats = fullStats;
        this.inputFiles = inputFiles;
    }

    public Path getResultDir() {
        return resultDir;
    }

    public void setResultDir(Path resultDir) {
        this.resultDir = resultDir;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean getAddExistingFiles() {
        return addExistingFiles;
    }

    public void setAddExistingFiles(boolean addExistingFiles) {
        this.addExistingFiles = addExistingFiles;
    }

    public boolean isBriefStats() {
        return briefStats;
    }

    public void setBriefStats(boolean briefStats) {
        this.briefStats = briefStats;
    }

    public boolean isFullStats() {
        return fullStats;
    }

    public void setFullStats(boolean fullStats) {
        this.fullStats = fullStats;
    }

    public List<Path> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(List<Path> inputFiles) {
        this.inputFiles = inputFiles;
    }
}
