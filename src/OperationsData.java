import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
        System.out.println("\nСтатистика по числам:");
        if (checkDirectory(config.getResultDir(), config.getPrefix() + DataType.FILE_INTS.getDataType())) {
            List<BigInteger> numbers = getStream(DataType.FILE_INTS.getDataType())
                    .map(BigInteger::new)
                    .toList();

            BigInteger sum = numbers.stream().reduce(BigInteger.ZERO, BigInteger::add);
            BigInteger max = numbers.stream().max(BigInteger::compareTo).get();
            BigInteger min = numbers.stream().min(BigInteger::compareTo).get();

            double average = sum.divide(BigInteger.valueOf(numbers.size())).doubleValue();

            System.out.println("Сумма всех чисел в файле равна: " + sum);
            System.out.println("Среднее значение равно: " + average);
            System.out.println("Максимальное значение: " + max);
            System.out.println("Минимальное значение: " + min);
        } else {
            System.out.println("Статистика по целым числам отсутствует.");
        }
    }

    public void operationsFloat() {
        System.out.println("\nСтатистика по вещественным числам:");
        if (checkDirectory(config.getResultDir(), config.getPrefix() + DataType.FILE_FLOATS.getDataType())) {
            double sum = getStream(DataType.FILE_FLOATS.getDataType()).mapToDouble(Double::parseDouble).sum();
            double max = getStream(DataType.FILE_FLOATS.getDataType()).mapToDouble(Double::parseDouble).max().getAsDouble();
            double min = getStream(DataType.FILE_FLOATS.getDataType()).mapToDouble(Double::parseDouble).min().getAsDouble();
            double average = getStream(DataType.FILE_FLOATS.getDataType()).mapToDouble(Double::parseDouble).average().getAsDouble();
            System.out.println("Сумма всех Float в файле равна: " + sum);
            System.out.println("Среднее значение всех Float равна: " + average);
            System.out.println("Максимально значение Float: " + max);
            System.out.println("Минимальное значение Float: " + min);
        } else {
            System.out.println("Статистика по вещественным числам отсутствует.");
        }
    }

    public void operationsString() {
        System.out.println("\nСтатистика по строкам:");
        if (checkDirectory(config.getResultDir(), config.getPrefix() + DataType.FILE_STRINGS.getDataType())) {
            int countMin = getStream(DataType.FILE_STRINGS.getDataType()).mapToInt(String::length).min().getAsInt();
            int countMax = getStream(DataType.FILE_STRINGS.getDataType()).mapToInt(String::length).max().getAsInt();
            System.out.println("Минимальная длина записанной строки: " + countMin);
            System.out.println("Максимальная длина записанной строки: " + countMax);
        } else {
            System.out.println("Статистика по строкам отсутствует.");
        }
    }

    private Stream<String> getStream(String file) {
        try {
            Stream<String> stream = Files.lines(Paths.get(config.getResultDir() + "\\" + config.getPrefix() + file));
            return stream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkDirectory(Path dir, String file) {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(dir)) {
            for (Path pathNew : files) {
                String path = String.valueOf(pathNew.getFileName());
                if (path.equals(file)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
