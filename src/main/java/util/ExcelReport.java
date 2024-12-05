package main.java.util;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import queries.Clientes;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelReport {

    public static void clientReport (Connection conn) {
        try {
            String excelFilePath = "ClientesReport.xlsx";

            Workbook workbook = new XSSFWorkbook();

            //Obtener los datos de los clientes
            StringBuilder clientData = Clientes.fillClients(conn);

            //Crear la hoja en el archivo

            Sheet sheet = workbook.createSheet("Clientes");

            //Crear fila para encabezado
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Cedula", "Alias", "Nombre", "Apellido"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            //Dividimos los datos en líneas para cada cliente
            String[] clients = clientData.toString().split("\n");
            int rowIndex = 1;
            for (String client : clients) {
                //Divide cada línea en sus componentes de datos
                String[] details = client.split(", ");
                Row dataRow = sheet.createRow(rowIndex++);

                //Escribir cada atributo en una celda correspondiente
                for (int i = 0; i < details.length; i++) {
                    String[] keyValue = details[i].split(": ");
                    Cell cell = dataRow.createCell(i);
                    cell.setCellValue(keyValue[1].trim());
                }
            }

            //Auto-ajustar el tamaño de las columnas
            for (int i = 0; i < headers.length;  i++)
                sheet.autoSizeColumn(i);
            
                //Guardar el archivo

                try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                    workbook.write(fileOut);
                    System.out.println("Creado en " + excelFilePath);
                    fileOut.close();
                }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getSQLState());
        } catch (IOException e) {
            System.out.println("Error al guardar archivo");
        }
        

    }

}
