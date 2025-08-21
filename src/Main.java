import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.*;

public class Main {

    private static String result_dir = "";
    private static String prefix = "";
    private static boolean addExistingFiles = false;
    private static boolean briefStatisticsOutput = false;
    private static boolean fullStatisticsOutput = false;

    private static Map<String, Integer> statistics = new HashMap<>();

    public static void main(String[] args) {
        parseArgs(args);
    }

    public static void parseArgs(String[] args) {
        List<String> files = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o")) {
                if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
                    throw new IllegalArgumentException("-o требует указать путь к директории");
                }
                result_dir = args[++i];
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
                files.add(args[i]);
            } else {
                throw new IllegalArgumentException("Неизвестный аргумент: " + args[i]);
            }
        }

        if (result_dir.isEmpty()) {
            throw new IllegalArgumentException("Аргумент -o отсутствует");
        }
        if (prefix.isEmpty()) {
            throw new IllegalArgumentException("Аргумент -p отсутствует");
        }
        fileProcessing(files);
    }

    public static void fileProcessing(List<String> files) {
        Path outputPath = Paths.get(result_dir).toAbsolutePath();
        int integerCount = 1;
        int floatCount = 1;
        int strCount = 1;

        StandardOpenOption[] option = addExistingFiles
                ? new StandardOpenOption[]{APPEND}
                : new StandardOpenOption[]{CREATE, TRUNCATE_EXISTING};

        try (PrintWriter pwInt = new PrintWriter(Files.newBufferedWriter(outputPath.resolve(prefix + Constants.FILE_INTS), option));
             PrintWriter pwFloat = new PrintWriter(Files.newBufferedWriter(outputPath.resolve(prefix + Constants.FILE_FLOATS), option));
             PrintWriter pwStr = new PrintWriter(Files.newBufferedWriter(outputPath.resolve(prefix + Constants.FILE_STRINGS), option))) {

            for (String file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
        printBriefStatistics();
        printFullStatistics();
    }

    public static void printFullStatistics() {
        if (fullStatisticsOutput) {
            operationsInt();
            operationsFloat();
            operationsString();
        }
    }

    public static void operationsInt() {
        int sum = getStream(Constants.FILE_INTS).mapToInt(Integer::parseInt).sum();
        int max = getStream(Constants.FILE_INTS).mapToInt(Integer::parseInt).max().getAsInt();
        int min = getStream(Constants.FILE_INTS).mapToInt(Integer::parseInt).min().getAsInt();
        double average = getStream(Constants.FILE_INTS).mapToInt(Integer::parseInt).average().getAsDouble();
        System.out.println("\nСумма всех Integer в файле равна: " + sum);
        System.out.println("Среднее значение всех Integer равна: " + average);
        System.out.println("Максимально значение Integer: " + max);
        System.out.println("Минимальное значение Integer: " + min);
    }

    public static void operationsFloat() {
        double sum = getStream(Constants.FILE_FLOATS).mapToDouble(Double::parseDouble).sum();
        double max = getStream(Constants.FILE_FLOATS).mapToDouble(Double::parseDouble).max().getAsDouble();
        double min = getStream(Constants.FILE_FLOATS).mapToDouble(Double::parseDouble).min().getAsDouble();
        double average = getStream(Constants.FILE_FLOATS).mapToDouble(Double::parseDouble).average().getAsDouble();
        System.out.println("\nСумма всех Float в файле равна: " + sum);
        System.out.println("Среднее значение всех Float равна: " + average);
        System.out.println("Максимально значение Float: " + max);
        System.out.println("Минимальное значение Float: " + min);
    }

    public static void operationsString() {
        int countMin = getStream(Constants.FILE_STRINGS).mapToInt(String::length).min().getAsInt();
        int countMax = getStream(Constants.FILE_STRINGS).mapToInt(String::length).max().getAsInt();
        System.out.println("\nМинимальная длина записанной строки: " + countMin);
        System.out.println("Максимальная длина записанной строки: " + countMax);
    }

    public static Stream<String> getStream(String file) {
        try {
            Stream<String> stream = Files.lines(Paths.get(result_dir + "\\" + prefix + file));
            return stream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printBriefStatistics() {
        if (briefStatisticsOutput) {
            System.out.println("Количество элементов записанных в исходящие файлы:");
            for (Map.Entry<String, Integer> c : statistics.entrySet()) {
                System.out.println(c.getKey() + ": " + c.getValue());
            }
        }
    }

    public static boolean checkingDataTypeInteger(String line) {
        try {
            Integer.parseInt(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean checkingDataTypeFloat(String line) {
        try {
            Double.parseDouble(line);
            return line.contains(".") || line.toLowerCase().contains("e");
        } catch (NumberFormatException e) {
            return false;
        }
    }
}