package function;

public class ExcelEntity {
    private String id;
    private String tableName;
    private String field;
    private String fieldName;
    private String fieldType;
    private String fieldLength;

    public ExcelEntity() {
    }

    public ExcelEntity(String id, String tableName, String field, String fieldName, String fieldType, String fieldLength) {
        this.id = id;
        this.tableName = tableName;
        this.field = field;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldLength = fieldLength;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(String fieldLength) {
        this.fieldLength = fieldLength;
    }

    @Override
    public String toString() {
        return "ExcelEntity{" +
                "id='" + id + '\'' +
                ", tableName='" + tableName + '\'' +
                ", field='" + field + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", fieldLength='" + fieldLength + '\'' +
                '}';
    }
}
