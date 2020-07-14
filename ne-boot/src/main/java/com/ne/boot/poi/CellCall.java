package com.ne.boot.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Created by xiezhouyan on 15-12-24.
 */
public class CellCall {
    private Sheet sheet;
    private int sheetIndex;
    private Row row;
    private int rowIndex;
    private Cell cell;
    private int cellIndex;

    public CellCall() {
    }

    public CellCall(Sheet sheet, int sheetIndex, Row row, int rowIndex, Cell cell, int cellIndex) {
        this.sheet = sheet;
        this.sheetIndex = sheetIndex;
        this.row = row;
        this.rowIndex = rowIndex;
        this.cell = cell;
        this.cellIndex = cellIndex;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }
}
