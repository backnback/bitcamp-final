package bitcamp.project.parsingcode;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.Properties;

public class ExcelToDatabase {
    public static void main(String[] args) {
//        String jdbcUrl = "jdbc:mysql://localhost/finaldb";
//        String username = "study";
//        String password = "1111";

        String jdbcUrl;
        String username;
        String password;

        String excelFilePath = "app/temp/location.xlsx";

        try (InputStream inputStream = new FileInputStream("app/src/main/resources/application-dev.properties")) {
            Properties prop = new Properties();
            prop.load(inputStream);

            jdbcUrl = prop.getProperty("spring.datasource.url");
            username = prop.getProperty("spring.datasource.username");
            password = prop.getProperty("spring.datasource.password");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             FileInputStream file = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            String insertQuery = "INSERT INTO location (location_id, first_name, second_name) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    // 첫 번째 행은 헤더라고 가정하고 넘깁니다.
                    continue;
                }

                Cell cell1 = row.getCell(0);
                Cell cell2 = row.getCell(1);
                Cell cell3 = row.getCell(2);

                preparedStatement.setString(1, String.valueOf(cell1.getNumericCellValue()));
                preparedStatement.setString(2, cell2.getStringCellValue());
                preparedStatement.setString(3, cell3.getStringCellValue());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            System.out.println("Data has been inserted successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
