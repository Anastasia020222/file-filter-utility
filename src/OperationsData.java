import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public class OperationsData {

    private final Config config;

    public OperationsData(Config config) {
        this.config = config;
    }

    public void printFullStatistics() {
        if (config.isFullStats()) {
            operationsInt();
            operationsFloat();
            operationsString();
        }
    }

    public void printBriefStatistics(Map<String, Integer> statistics) {
        if (config.isBriefStats()) {
            System.out.println("Количество элементов записанных в исходящие файлы:");
            for (Map.Entry<String, Integer> c : statistics.entrySet()) {
                System.out.println(c.getKey() + ": " + c.getValue());
            }
        }
    }

    public void operationsInt() {
        int sum = getStream(DataType.FILE_INTS.getDataType()).mapToInt(Integer::parseInt).sum();
        int max = getStream(DataType.FILE_INTS.getDataType()).mapToInt(Integer::parseInt).max().getAsInt();
        int min = getStream(DataType.FILE_INTS.getDataType()).mapToInt(Integer::parseInt).min().getAsInt();
        double average = getStream(DataType.FILE_INTS.getDataType()).mapToInt(Integer::parseInt).average().getAsDouble();
        System.out.println("\nСумма всех Integer в файле равна: " + sum);
        System.out.println("Среднее значение всех Integer равна: " + average);
        System.out.println("Максимально значение Integer: " + max);
        System.out.println("Минимальное значение Integer: " + min);
    }

    public void operationsFloat() {
        double sum = getStream(DataType.FILE_FLOATS.getDataType()).mapToDouble(Double::parseDouble).sum();
        double max = getStream(DataType.FILE_FLOATS.getDataType()).mapToDouble(Double::parseDouble).max().getAsDouble();
        double min = getStream(DataType.FILE_FLOATS.getDataType()).mapToDouble(Double::parseDouble).min().getAsDouble();
        double average = getStream(DataType.FILE_FLOATS.getDataType()).mapToDouble(Double::parseDouble).average().getAsDouble();
        System.out.println("\nСумма всех Float в файле равна: " + sum);
        System.out.println("Среднее значение всех Float равна: " + average);
        System.out.println("Максимально значение Float: " + max);
        System.out.println("Минимальное значение Float: " + min);
    }

    public void operationsString() {
        int countMin = getStream(DataType.FILE_STRINGS.getDataType()).mapToInt(String::length).min().getAsInt();
        int countMax = getStream(DataType.FILE_STRINGS.getDataType()).mapToInt(String::length).max().getAsInt();
        System.out.println("\nМинимальная длина записанной строки: " + countMin);
        System.out.println("Максимальная длина записанной строки: " + countMax);
    }

    private Stream<String> getStream(String file) {
        try {
            Stream<String> stream = Files.lines(Paths.get(config.getResultDir() + "\\" + config.getPrefix() + file));
            return stream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
