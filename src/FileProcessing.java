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

        try (PrintWriter pwInt = writer(DataType.FILE_INTS.getDataType());
             PrintWriter pwFloat = writer(DataType.FILE_FLOATS.getDataType());
             PrintWriter pwStr = writer(DataType.FILE_STRINGS.getDataType())) {

            for (Path file : config.getInputFiles()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (checkingDataTypeInteger(line)) {
                            statistics.put("Integer", integerCount);
                            integerCount++;
                            pwInt.println(line);
                        } else if (checkingDataTypeFloat(line)) {
                            statistics.put("Float", floatCount);
                            floatCount++;
                            pwFloat.println(line);
                        } else {
                            statistics.put("String", strCount);
                            strCount++;
                            pwStr.println(line);
                        }
                    }
                } catch (FileNotFoundException e) {
                    throw new FileNotFoundException("Файл " + file + " не найден: " + e);
                } catch (IOException e) {
                    throw new IOException("Ошибка при чтении файла " + file + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        try {
            Integer.parseInt(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkingDataTypeFloat(String line) {
        try {
            Double.parseDouble(line);
            return line.contains(".") || line.toLowerCase().contains("e");
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
