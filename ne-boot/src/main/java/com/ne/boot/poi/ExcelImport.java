package com.ne.boot.poi;

import com.ne.boot.common.exception.NEError;
import com.ne.boot.common.exception.NEException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;

/**
 * Created by xiezhouyan on 15-12-10.
 */
public class ExcelImport {

    protected final Logger logger = LoggerFactory.getLogger(ExcelImport.class);

    private List<ColumnToField> fields;


    public void read(CellCallBack cellCallback, InputStream is) {
        read(null, cellCallback, is);
    }

    public void read(RowCallBack rowCallBack, InputStream is) {
        read(rowCallBack, null, is);
    }

    public void read(RowCallBack rowCallBack, CellCallBack cellCallback, InputStream is) {
        Workbook workbook = null;
        try {
            if (!is.markSupported()) {
                is = new PushbackInputStream(is, 8);
            }
            if (POIFSFileSystem.hasPOIFSHeader(is)) {
                workbook = new HSSFWorkbook(is);
            } else if (DocumentFactoryHelper.hasOOXMLHeader(is)) {
                workbook = new XSSFWorkbook(OPCPackage.open(is));
            }
        } catch (Throwable e) {
            logger.warn("read excel with  error", e);
            throw new NEException(NEError.READ_EXCEL_ERROR, e);
        }
        int sheetTotalNum = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetTotalNum; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                if (rowCallBack != null) {
                    try {
                        rowCallBack.call(new RowCall(sheet, i, row, rowIndex));
                    } catch (Exception e) {
                        logger.error("call rowcallback error", e);
                        throw new NEException(NEError.READ_EXCEL_ERROR, e);
                    }
                }
                if (cellCallback == null) {
                    continue;
                }
                for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    if (cell == null) {
                        continue;
                    }
                    try {
                        cellCallback.call(new CellCall(sheet, i, row, rowIndex, cell, cellIndex));
                    } catch (Exception e) {
                        logger.error("call cellcallback error", e);
                        throw new NEException(NEError.READ_EXCEL_ERROR, e);
                    }
                }
            }
        }
    }
}

