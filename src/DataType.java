public enum DataType {

    FILE_INTS("_integers.txt"),
    FILE_FLOATS("_floats.txt"),
    FILE_STRINGS("_strings.txt");

    private final String dataType;

    DataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }
}
