public enum DataType {

    FILE_INTS("integers.txt"),
    FILE_FLOATS("floats.txt"),
    FILE_STRINGS("strings.txt");

    private final String dataType;

    DataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }
}
