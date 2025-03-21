package com.blackpanthers.service;

import com.blackpanthers.model.Cricketer;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.HorizontalAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;

@Service
public class PDFGeneratorService {

    public byte[] generateRegistrationPDF(Cricketer cricketer) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        
        // Add background image with reduced opacity
        addBackgroundImage(pdf);
        
        // Add header
        document.add(new Paragraph("BLACK PANTHERS CRICKET TEAM")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(0, 0, 0)));
        
        document.add(new Paragraph("Registration Confirmation")
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(0, 0, 0)));
        
        document.add(new Paragraph("\n"));
        
        // Create table for player details
        Table table = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
        table.setWidth(UnitValue.createPercentValue(100));
        
        // Add player photo in the table if available
        if (cricketer.getPhoto() != null) {
            Cell photoLabelCell = new Cell().add(new Paragraph("Player Photo").setBold());
            
            Cell photoCell = new Cell();
            ImageData imageData = ImageDataFactory.create(cricketer.getPhoto());
            Image playerImage = new Image(imageData);
            playerImage.setWidth(100);
            playerImage.setHeight(120);
            photoCell.add(playerImage);
            
            table.addCell(photoLabelCell);
            table.addCell(photoCell);
        }
        
        // Add player details to table
        addTableRow(table, "Registration ID", String.valueOf(cricketer.getId()));
        addTableRow(table, "Name", cricketer.getName());
        addTableRow(table, "Date of Birth", cricketer.getDob().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        addTableRow(table, "Email", cricketer.getEmail());
        addTableRow(table, "Phone", cricketer.getPhone());
        addTableRow(table, "Address", cricketer.getAddress());
        addTableRow(table, "Jersey Number", String.valueOf(cricketer.getJerseyNumber()));
        addTableRow(table, "Jersey Size", cricketer.getJerseySize().toString());
        addTableRow(table, "Batting Style", cricketer.getBattingStyle().getDisplayName());
        addTableRow(table, "Bowling Style", cricketer.getBowlingStyle().getDisplayName());
        addTableRow(table, "Player Type", cricketer.getPlayerType().getDisplayName());
        addTableRow(table, "Years of Experience", String.valueOf(cricketer.getYearsOfExperience()));
        
        if (cricketer.getEmergencyContactName() != null) {
            addTableRow(table, "Emergency Contact", cricketer.getEmergencyContactName() + " (" + cricketer.getEmergencyContactPhone() + ")");
        }
        
        if (cricketer.getHasMedicalCondition() != null && cricketer.getHasMedicalCondition()) {
            addTableRow(table, "Medical Condition", cricketer.getMedicalDetails());
        }
        
        addTableRow(table, "Registration Date", cricketer.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        
        document.add(table);
        
        // Add footer
        document.add(new Paragraph("\n\nThank you for registering with Black Panthers Cricket Team. We are excited to have you on board!")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(0, 0, 0)));
        
        document.close();
        
        return baos.toByteArray();
    }
    
    private void addTableRow(Table table, String label, String value) {
        Cell labelCell = new Cell().add(new Paragraph(label).setBold());
        Cell valueCell = new Cell().add(new Paragraph(value));
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    private void addBackgroundImage(PdfDocument pdfDocument) throws IOException {
        // Load the Black Panther image from resources
        ClassPathResource resource = new ClassPathResource("/static/images/black-panther-bg.jpg");
        byte[] imageBytes = resource.getInputStream().readAllBytes();
        ImageData imageData = ImageDataFactory.create(imageBytes);
        
        // Add the image as a background with reduced opacity
        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
            Rectangle pageSize = pdfDocument.getPage(i).getPageSize();
            PdfCanvas canvas = new PdfCanvas(pdfDocument.getPage(i));
            
            // Create a separate canvas for the image with transparency
            canvas.saveState();
        
            // Create a transparency group
            PdfExtGState gs = new PdfExtGState();
            gs.setFillOpacity(0.2f);  // Adjust opacity as needed (0.1 = 10% opacity)
            canvas.setExtGState(gs);
        
            // Calculate dimensions to cover the entire page
            float imageWidth = imageData.getWidth();
            float imageHeight = imageData.getHeight();
            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();
        
            // Scale image to cover the entire page
            float scale = Math.max(pageWidth / imageWidth, pageHeight / imageHeight);
            float scaledWidth = imageWidth * scale;
            float scaledHeight = imageHeight * scale;
        
            // Center the image on the page
            float x = (pageWidth - scaledWidth) / 2;
            float y = (pageHeight - scaledHeight) / 2;
        
            // Add the image to cover the entire page
            canvas.addImageFittedIntoRectangle(imageData, new Rectangle(x, y, scaledWidth, scaledHeight), false);

            // Restore the graphics state
            canvas.restoreState();
        }
    }
}

//package com.blackpanthers.service;
//
//import com.blackpanthers.model.Cricketer;
//import com.itextpdf.io.image.ImageData;
//import com.itextpdf.io.image.ImageDataFactory;
//import com.itextpdf.kernel.colors.ColorConstants;
//import com.itextpdf.kernel.colors.DeviceRgb;
//import com.itextpdf.kernel.geom.PageSize;
//import com.itextpdf.kernel.geom.Rectangle;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Cell;
//import com.itextpdf.layout.element.Image;
//import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.layout.element.Table;
//import com.itextpdf.layout.properties.TextAlignment;
//import com.itextpdf.layout.properties.UnitValue;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.time.format.DateTimeFormatter;
//import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
//
//@Service
//public class PDFGeneratorService {
//
//    public byte[] generateRegistrationPDF(Cricketer cricketer) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PdfWriter writer = new PdfWriter(baos);
//        PdfDocument pdf = new PdfDocument(writer);
//        Document document = new Document(pdf, PageSize.A4);
//        
//        // Add background image with reduced opacity
//        addBackgroundImage(pdf);
//        
//        // Add header
//        document.add(new Paragraph("BLACK PANTHERS CRICKET TEAM")
//                .setFontSize(24)
//                .setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setFontColor(new DeviceRgb(0, 0, 0)));
//        
//        document.add(new Paragraph("Registration Confirmation")
//                .setFontSize(18)
//                .setTextAlignment(TextAlignment.CENTER)
//                .setFontColor(new DeviceRgb(0, 0, 0)));
//        
//        document.add(new Paragraph("\n"));
//        
//        // Add player photo if available
//        if (cricketer.getPhoto() != null) {
//            ImageData imageData = ImageDataFactory.create(cricketer.getPhoto());
//            Image playerImage = new Image(imageData);
//            playerImage.setWidth(100);
//            playerImage.setHeight(120);
//            document.add(playerImage);
//        }
//        
//        // Create table for player details
//        Table table = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
//        table.setWidth(UnitValue.createPercentValue(100));
//        
//        // Add player details to table
//        addTableRow(table, "Registration ID", String.valueOf(cricketer.getId()));
//        addTableRow(table, "Name", cricketer.getName());
//        addTableRow(table, "Date of Birth", cricketer.getDob().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//        addTableRow(table, "Email", cricketer.getEmail());
//        addTableRow(table, "Phone", cricketer.getPhone());
//        addTableRow(table, "Address", cricketer.getAddress());
//        addTableRow(table, "Jersey Number", String.valueOf(cricketer.getJerseyNumber()));
//        addTableRow(table, "Jersey Size", cricketer.getJerseySize().toString());
//        addTableRow(table, "Batting Style", cricketer.getBattingStyle().getDisplayName());
//        addTableRow(table, "Bowling Style", cricketer.getBowlingStyle().getDisplayName());
//        addTableRow(table, "Player Type", cricketer.getPlayerType().getDisplayName());
//        addTableRow(table, "Years of Experience", String.valueOf(cricketer.getYearsOfExperience()));
//        
//        if (cricketer.getEmergencyContactName() != null) {
//            addTableRow(table, "Emergency Contact", cricketer.getEmergencyContactName() + " (" + cricketer.getEmergencyContactPhone() + ")");
//        }
//        
//        if (cricketer.getHasMedicalCondition() != null && cricketer.getHasMedicalCondition()) {
//            addTableRow(table, "Medical Condition", cricketer.getMedicalDetails());
//        }
//        
//        addTableRow(table, "Registration Date", cricketer.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
//        
//        document.add(table);
//        
//        // Add footer
//        document.add(new Paragraph("\n\nThank you for registering with Black Panthers Cricket Team. We are excited to have you on board!")
//                .setTextAlignment(TextAlignment.CENTER)
//                .setFontColor(new DeviceRgb(0, 0, 0)));
//        
//        document.close();
//        
//        return baos.toByteArray();
//    }
//    
//    private void addTableRow(Table table, String label, String value) {
//        Cell labelCell = new Cell().add(new Paragraph(label).setBold());
//        Cell valueCell = new Cell().add(new Paragraph(value));
//        table.addCell(labelCell);
//        table.addCell(valueCell);
//    }
//    
//    private void addBackgroundImage(PdfDocument pdfDocument) throws IOException {
//        // Load the Black Panther image from resources
//        ClassPathResource resource = new ClassPathResource("static/images/black-panther-bg.jpg");
//        byte[] imageBytes = resource.getInputStream().readAllBytes();
//        ImageData imageData = ImageDataFactory.create(imageBytes);
//        
//        // Add the image as a background with reduced opacity
//        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
//            Rectangle pageSize = pdfDocument.getPage(i).getPageSize();
//            PdfCanvas canvas = new PdfCanvas(pdfDocument.getPage(i));
//            
//            // Create a separate canvas for the image with transparency
//            canvas.saveState();
//        
//            // Create a transparency group
//            PdfExtGState gs = new PdfExtGState();
//            gs.setFillOpacity(0.1f);
//            canvas.setExtGState(gs);
//        
//            // Calculate dimensions to fit the page
//            float imageWidth = imageData.getWidth();
//            float imageHeight = imageData.getHeight();
//            float pageWidth = pageSize.getWidth();
//            float pageHeight = pageSize.getHeight();
//        
//            // Scale image to fit the page
//            float scale = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);
//            float scaledWidth = imageWidth * scale;
//            float scaledHeight = imageHeight * scale;
//        
//            // Center the image on the page
//            float x = (pageWidth - scaledWidth) / 2;
//            float y = (pageHeight - scaledHeight) / 2;
//        
//            // Add the image
//            canvas.addImageAt(imageData, x, y,false);
//        
//            // Restore the graphics state
//            canvas.restoreState();
//        }
//    }
//}
