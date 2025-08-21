
public class Main {

    public static void main(String[] args) {

        Config config = ArgsParser.parseArgs(args);
        FileProcessing fileProcessing = new FileProcessing(config);
        fileProcessing.fileProcessing();

    }
}