package com.ne.boot.poi;

/**
 * Created by xiezhouyan on 16-5-13.
 */
public class CelllMerge {

    private MergeType type;

    private int rowIndex;
    private int cellIndex;

    public CelllMerge(MergeType type) {
        this.type = type;
    }

    public CelllMerge(MergeType type, int rowIndex, int cellIndex) {
        this.type = type;
        this.rowIndex = rowIndex;
        this.cellIndex = cellIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public MergeType getType() {
        return type;
    }

    public void setType(MergeType type) {
        this.type = type;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public enum MergeType {
        START, END
    }


}
