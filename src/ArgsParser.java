import java.io.IOException;
import java.nio.file.Files;
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
            switch (args[i]) {
                case "-o":
                    if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
                        result_dir = pathResultDir(result_dir);
                    } else {
                        Path c = Path.of(args[++i]);
                        checkDirectory(c);
                        result_dir = c;
                    }
                    break;
                case "-p":
                    if (i + 1 >= args.length || args[i + 1].startsWith("-") || args[i + 1].endsWith(".txt")) {
                        throw new IllegalArgumentException("-p требует указать префикс");
                    }
                    prefix = args[++i];
                    break;
                case "-a":
                    addExistingFiles = true;
                    break;
                case "-s":
                    briefStatisticsOutput = true;
                    break;
                case "-f":
                    fullStatisticsOutput = true;
                    break;
                default:
                    if (args[i].endsWith(".txt")) {
                        files.add(Path.of(args[i]));
                    } else {
                        throw new IllegalArgumentException("Неизвестная опция: " + args[i]);
                    }
                    break;
            }
        }
        result_dir = pathResultDir(result_dir);
        if (prefix.isEmpty()) {
            prefix = "result_";
            //throw new IllegalArgumentException("Аргумент -p отсутствует");
        }

        if (files.isEmpty()) {
            System.out.println("Входящие файлы отсутствуют.");
        }

        return new Config(result_dir, prefix, addExistingFiles, briefStatisticsOutput, fullStatisticsOutput, files);
    }

    private static Path pathResultDir(Path result_dir) {
        if (result_dir == null) {
            String currentDir = System.getProperty("user.dir");
            return Path.of(currentDir);
        } else {
            return result_dir;
        }
    }

    private static void checkDirectory(Path directory) {
        if (!Files.exists(directory) && !Files.isDirectory(directory)) {
            System.out.println("Указанная директория не существует. Она будет создана.");
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("Не удалось создать директорию " + e);
            }
        }
    }
}
