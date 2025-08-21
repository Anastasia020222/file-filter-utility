import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArgsParser {

    public static Config parseArgs(String[] args) {
        Path result_dir = null;
        String prefix = "";
        boolean addExistingFiles = false;
        boolean briefStatisticsOutput = false;
        boolean fullStatisticsOutput = false;

        List<Path> files = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o")) {
                if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
                    throw new IllegalArgumentException("-o требует указать путь к директории");
                }
                result_dir = Path.of(args[++i]);
            } else if (args[i].equals("-p")) {
                if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
                    throw new IllegalArgumentException("-p требует указать префикс");
                }
                prefix = args[++i];
            } else if (args[i].equals("-a")) {
                addExistingFiles = true;
            } else if (args[i].equals("-s")) {
                briefStatisticsOutput = true;
            } else if (args[i].equals("-f")) {
                fullStatisticsOutput = true;
            } else if (args[i].endsWith(".txt")) {
                files.add(Path.of(args[i]));
            } else {
                throw new IllegalArgumentException("Неизвестный аргумент: " + args[i]);
            }
        }

        if (result_dir == null) {
            throw new IllegalArgumentException("Аргумент -o отсутствует");
        }
        if (prefix.isEmpty()) {
            throw new IllegalArgumentException("Аргумент -p отсутствует");
        }

        return new Config(result_dir, prefix, addExistingFiles, briefStatisticsOutput, fullStatisticsOutput, files);
    }
}
