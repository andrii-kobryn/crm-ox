package com.akobryn.crm.controllers.api;

import com.akobryn.crm.dto.ClientDTO;
import com.akobryn.crm.service.ClientService;
import com.akobryn.crm.service.ExcelService;
import com.akobryn.crm.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportApiController {

    private final ClientService clientService;
    private final ExcelService excelService;
    private final PdfService pdfService;

    @GetMapping("/{clientId}/excel")
    public ResponseEntity<byte[]> exportClientToExcel(@PathVariable Long clientId) throws IOException {
        ClientDTO client = clientService.getClientById(clientId);
        byte[] excelData = excelService.exportToExcel(client);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=client.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @GetMapping("/{clientId}/pdf")
    public ResponseEntity<byte[]> exportClientToPdf(@PathVariable Long clientId) throws IOException {
        ClientDTO client = clientService.getClientById(clientId);
        byte[] pdfData = pdfService.exportToPdf(client);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=client.pdf")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(pdfData);
    }
}
