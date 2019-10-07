package helper;

import configure.Config;
import configure.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WebTable {
    public Log logger = Log.getInstance();
    public Config config = Config.getInstance();

    // get the number of rows present
    public int getRowCount(WebElement tableElement) {
        int noOfRows = tableElement.findElements(By.tagName("tr")).size();
        return noOfRows;
    }

    // get the number of columns present
    public int getColumnCount(WebElement tableElement) {
        int noOfCols = tableElement.findElements(By.xpath("//tr[1]/td")).size();
        return noOfCols;
    }

    // get the number of rows and columns and return it as Map
    public Map<String, Integer> getTableSize(WebElement tableElement) {
        Map<String, Integer> tableSize = new HashMap<>();
        tableSize.put("rows", getRowCount(tableElement));
        tableSize.put("columns", getColumnCount(tableElement));
        return tableSize;
    }

    // get row data and return it as list
    public List<String> getRowData(WebElement tableElement, int rowNumber) throws Exception {
        logger.INFO("Get data table at row: " + rowNumber);
        if (rowNumber == 0) {
            throw new Exception("Row number starts from 1");
        }
        List<WebElement> row = null;
        try {
            WaitUtils.waitForTryToFindInElement(
                    tableElement, By.xpath("//tr[" + rowNumber + "]/td"), config.getTimeOut());
            row = tableElement.findElements(By.xpath("//tr[" + rowNumber + "]/td"));
        } catch (Exception e) {
            logger.failedWithMessage(e.toString());
        }

        List rData = new ArrayList();
        for (WebElement webElement : row) {
            rData.add(webElement.getText().trim());
        }
        return rData;
    }

    public List<WebElement> getRowElement(WebElement tableElement, int rowIndex) throws Exception {
        logger.INFO("Get data table at row: " + rowIndex);
        if (rowIndex == 0) {
            throw new Exception("Row number starts from 1");
        }
        List<WebElement> rowElement = null;
        try {
            rowElement = tableElement.findElements(By.xpath("//tr[" + rowIndex + "]/td"));
        } catch (Exception e) {
            logger.failedWithMessage(e.toString());
        }
        return rowElement;
    }

    public List<WebElement> getColumnElement(WebElement tableElement, int columnIndex)
            throws Exception {
        logger.INFO("Get data table at column: " + columnIndex);
        if (columnIndex == 0) {
            throw new Exception("Column number starts from 1");
        }
        int rowTotal = getRowCount(tableElement);
        List<WebElement> rowElement = null;
        List<WebElement> columnElement = null;
        try {
            if (rowTotal > 0) {
                for (int i = 1; i < rowTotal + 1; i++) {
                    rowElement = tableElement.findElements(By.xpath("//tr[" + i + "]/td"));
                    columnElement.add(rowElement.get(columnIndex));
                }
            }
        } catch (Exception e) {
            logger.failedWithMessage(e.toString());
        }

        return columnElement;
    }

    public List<String> getHeader(WebElement webTable) {
        return webTable.findElements(By.tagName("th")).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public List<String> getHeaderScroll(WebElement webTable, WebDriver driver) {
        List<String> header = new ArrayList<>();
        List<WebElement> headerList = webTable.findElements(By.tagName("th"));
        for (WebElement webElement : headerList) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElement);
            header.add(webElement.getText().trim());
        }
        return header;
    }

    public List<WebElement> getHeaderElement(WebElement webTable) {
        return webTable.findElements(By.tagName("th"));
    }

    // get the column data and return as list
    public List columnData(WebElement tableElement, int columnNumber) throws Exception {
        if (columnNumber == 0) {
            throw new Exception("Column number starts from 1");
        }
        List<WebElement> column = tableElement.findElements(By.xpath("//tr/td[" + columnNumber + "]"));
        List cData = new ArrayList<>();
        for (WebElement webElement : column) {
            cData.add(webElement.getText());
        }
        return cData;
    }

    // get all the data from the table
    public List getAllData(WebElement tableElement) {
        // get number of rows
        int noOfRows = tableElement.findElements(By.xpath("//tr")).size() - 1;
        // get number of columns
        int noOfColumns = tableElement.findElements(By.xpath("//tr[2]/td")).size();
        List allData = new ArrayList<>();
        // iterate over the rows, to ignore the headers we have started the i with '1'
        for (int i = 2; i < noOfRows; i++) {
            // reset the row data every time
            List<String> ro = new ArrayList<>();
            // iterate over columns
            for (int j = 1; j < noOfColumns; j++) {
                // get text from the i th row and j th column
                ro.add(tableElement.findElement(By.xpath("//tr[" + i + "]/td[" + j + "]")).getText());
            }
            // add the row data to allData of the table
            allData.add(ro);
        }
        return allData;
    }

    // verify presence of the text/data
    public boolean presenceOfData(WebElement tableElement, String data) {
        // verify the data by getting the size of the element matches based on the text/data passed
        int dataSize =
                tableElement.findElements(By.xpath("//td[normalize-space(text())='" + data + "']")).size();
        boolean presence = false;
        if (dataSize > 0) {
            presence = true;
        }
        return presence;
    }

    // get the data from a specific cell
    public String getCellData(WebElement tableElement, int rowNumber, int columnNumber)
            throws Exception {
        if (rowNumber == 0) {
            throw new Exception("Row number starts from 1");
        }
        rowNumber = rowNumber + 1;
        String cellData =
                tableElement
                        .findElement(By.xpath("//tr[" + rowNumber + "]/td[" + columnNumber + "]"))
                        .getText();
        return cellData;
    }
}
