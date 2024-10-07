package com.akobryn.crm.service;

import com.akobryn.crm.dto.ClientDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ExcelService {
    /**
     * Generate Excel with Client fields
     * @param clientDTO
     * @return
     * @throws IOException
     */
    public byte[] exportToExcel(ClientDTO clientDTO) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Company Name");
        headerRow.createCell(1).setCellValue("Industry");
        headerRow.createCell(2).setCellValue("Address");

        int rowNum = 1;
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(clientDTO.getCompanyName());
            row.createCell(1).setCellValue(clientDTO.getIndustry());
            row.createCell(2).setCellValue(clientDTO.getAddress());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}
