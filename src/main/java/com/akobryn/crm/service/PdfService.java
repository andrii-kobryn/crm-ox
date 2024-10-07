package com.akobryn.crm.service;

import com.akobryn.crm.dto.ClientDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {
    /**
     * Generate PDF with Client fields
     * @param clientDTO
     * @return
     */
    public byte[] exportToPdf(ClientDTO clientDTO) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

            document.add(new Paragraph("Company name: " + clientDTO.getCompanyName()));
            document.add(new Paragraph("Industry: " + clientDTO.getIndustry()));
            document.add(new Paragraph("Address: " + clientDTO.getAddress()));

        document.close();
        return outputStream.toByteArray();
    }
}
