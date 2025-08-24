import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class FileProcessing {

    private final Config config;
    private final OperationsData operationsData;
    private Map<String, Integer> statistics = new HashMap<>();

    public FileProcessing(Config config) {
        this.config = config;
        operationsData = new OperationsData(config);
    }

    public void fileProcessing() {
        int integerCount = 1;
        int floatCount = 1;
        int strCount = 1;

        PrintWriter pwInt = null;
        PrintWriter pwFloat = null;
        PrintWriter pwStr = null;

        try {
            for (Path file : config.getInputFiles()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (checkingDataTypeInteger(line)) {
                            if (pwInt == null) {
                                pwInt = writer(DataType.FILE_INTS.getDataType());
                            }
                            statistics.put("Integer", integerCount);
                            integerCount++;
                            pwInt.println(line);
                        } else if (checkingDataTypeFloat(line)) {
                            if (pwFloat == null) {
                                pwFloat = writer(DataType.FILE_FLOATS.getDataType());
                            }
                            statistics.put("Float", floatCount);
                            floatCount++;
                            pwFloat.println(line);
                        } else {
                            if (pwStr == null) {
                                pwStr = writer(DataType.FILE_STRINGS.getDataType());
                            }
                            statistics.put("String", strCount);
                            strCount++;
                            pwStr.println(line);
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("\u001b[31mФайл " + file + " не найден: " + e + "\u001b[0m");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (pwInt != null) pwInt.close();
            if (pwFloat != null) pwFloat.close();
            if (pwStr != null) pwStr.close();
        }
        operationsData.printBriefStatistics(statistics);
        operationsData.printFullStatistics();
    }

    private PrintWriter writer(String suffix) throws IOException {
        StandardOpenOption[] option = config.getAddExistingFiles()
                ? new StandardOpenOption[]{StandardOpenOption.APPEND}
                : new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
        return new PrintWriter(Files.newBufferedWriter(config.getResultDir().resolve(config.getPrefix() + suffix), option));
    }

    private boolean checkingDataTypeInteger(String line) {
        if (line == null || line.isEmpty()) {
            return false;
        }
        return line.trim().matches("[+-]?\\d+");
    }

    private boolean checkingDataTypeFloat(String line) {
        if (line == null || line.isEmpty()) {
            return false;
        }
        return line.trim().matches(
                "^[-+]?([0-9]+\\.?[0-9]*|\\.[0-9]+)([eE][-+]?[0-9]+)?$"
        );
    }
}
