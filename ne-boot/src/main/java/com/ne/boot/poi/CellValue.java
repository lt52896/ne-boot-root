package com.ne.boot.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by xiezhouyan on 15-12-11.
 */
public interface CellValue {
    void setValue(ExcelExport export, Workbook wokbook, Cell cell, Object o);
}
