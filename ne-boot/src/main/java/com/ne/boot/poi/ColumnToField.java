package com.ne.boot.poi;

import java.util.List;

/**
 * Created by xiezhouyan on 15-12-10.
 */
public class ColumnToField {
    private String colname;

    private String fieldName;

    private CellValue cellValue = new DefaultCellValue();

    private List<ColumnToField> fields;

    private Boolean isShow = true;


    public ColumnToField(String fieldName, List<ColumnToField> fields, boolean isShow) {
        this.fieldName = fieldName;
        this.fields = fields;
        this.isShow = isShow;
    }

    public ColumnToField(String fieldName, List<ColumnToField> fields) {
        this.fieldName = fieldName;
        this.fields = fields;
    }

    public ColumnToField(String fieldName, String colname) {
        this.colname = colname;
        this.fieldName = fieldName;
    }

    public ColumnToField(String fieldName, String colname, String pattern) {
        this.colname = colname;
        this.fieldName = fieldName;
        this.cellValue = new DefaultCellValue(pattern);
    }

    public ColumnToField(String fieldName, String colname, CellValue cellValue) {
        this.colname = colname;
        this.fieldName = fieldName;
        this.cellValue = cellValue;
    }

    public ColumnToField(String fieldName, String colname, Boolean isShow) {
        this.colname = colname;
        this.fieldName = fieldName;
        this.isShow = isShow;
    }

    public String getColname() {
        return colname;
    }

    public void setColname(String colname) {
        this.colname = colname;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public CellValue getCellValue() {
        return cellValue;
    }

    public void setCellValue(CellValue cellValue) {
        this.cellValue = cellValue;
    }

    public List<ColumnToField> getFields() {
        return fields;
    }

    public void setFields(List<ColumnToField> fields) {
        this.fields = fields;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }
}
