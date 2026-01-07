package com.library.pos.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.library.pos.models.Borrowing;
import com.library.pos.models.Fine;
import com.library.pos.models.ReportData;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFReportGenerator {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
    private static final Font HEADING_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.DARK_GRAY);

    public static String generateReport(ReportData reportData, String outputPath) throws Exception {
        Document document = new Document(PageSize.A4);
        String fileName = outputPath + "/Library_Report_" +
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        addHeader(document, reportData);
        addStatistics(document, reportData);
        addTransactions(document, reportData);
        addFines(document, reportData);
        addFooter(document, reportData);

        document.close();

        return fileName;
    }

    private static void addHeader(Document document, ReportData reportData) throws DocumentException {
        Paragraph title = new Paragraph("LIBRARY POS SYSTEM", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(5);
        document.add(title);

        Paragraph subtitle = new Paragraph("LAPORAN SISTEM PERPUSTAKAAN", HEADING_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(20);

        addInfoRow(infoTable, "Tanggal Laporan:", reportData.getGeneratedDate());
        addInfoRow(infoTable, "Dibuat Oleh:", reportData.getGeneratedBy());

        document.add(infoTable);
        document.add(new Paragraph("________________________________________________________________________________", SMALL_FONT));
        document.add(Chunk.NEWLINE);
    }

    private static void addStatistics(Document document, ReportData reportData) throws DocumentException {
        Paragraph statTitle = new Paragraph("STATISTIK SISTEM", HEADING_FONT);
        statTitle.setSpacingBefore(10);
        statTitle.setSpacingAfter(10);
        document.add(statTitle);

        PdfPTable statsTable = new PdfPTable(4);
        statsTable.setWidthPercentage(100);
        statsTable.setSpacingAfter(20);

        addStatHeaderCell(statsTable, "Total Buku");
        addStatHeaderCell(statsTable, "Total Anggota");
        addStatHeaderCell(statsTable, "Peminjaman Aktif");
        addStatHeaderCell(statsTable, "Total Users");

        addStatDataCell(statsTable, String.valueOf(reportData.getTotalBooks()));
        addStatDataCell(statsTable, String.valueOf(reportData.getTotalMembers()));
        addStatDataCell(statsTable, String.valueOf(reportData.getActiveLoans()));
        addStatDataCell(statsTable, String.valueOf(reportData.getTotalUsers()));

        document.add(statsTable);
    }

    private static void addTransactions(Document document, ReportData reportData) throws DocumentException {
        Paragraph transTitle = new Paragraph("TRANSAKSI PEMINJAMAN TERBARU", HEADING_FONT);
        transTitle.setSpacingBefore(10);
        transTitle.setSpacingAfter(10);
        document.add(transTitle);

        if (reportData.getRecentTransactions() == null || reportData.getRecentTransactions().isEmpty()) {
            Paragraph noData = new Paragraph("Tidak ada data transaksi.", NORMAL_FONT);
            noData.setSpacingAfter(20);
            document.add(noData);
            return;
        }

        PdfPTable transTable = new PdfPTable(6);
        transTable.setWidthPercentage(100);
        transTable.setSpacingAfter(20);

        float[] columnWidths = {1f, 2f, 2f, 2f, 2f, 1.5f};
        transTable.setWidths(columnWidths);

        addTableHeader(transTable, "ID");
        addTableHeader(transTable, "Anggota");
        addTableHeader(transTable, "Petugas");
        addTableHeader(transTable, "Tanggal Pinjam");
        addTableHeader(transTable, "Tanggal Kembali");
        addTableHeader(transTable, "Status");

        for (Borrowing borrowing : reportData.getRecentTransactions()) {
            addTableCell(transTable, String.valueOf(borrowing.getId()));
            addTableCell(transTable, borrowing.getMemberName() != null ? borrowing.getMemberName() : "-");
            addTableCell(transTable, borrowing.getUserName() != null ? borrowing.getUserName() : "-");
            addTableCell(transTable, borrowing.getBorrowDate() != null ? borrowing.getBorrowDate().toString() : "-");
            addTableCell(transTable, borrowing.getDueDate() != null ? borrowing.getDueDate().toString() : "-");
            addTableCell(transTable, borrowing.getStatus());
        }

        document.add(transTable);
    }

    private static void addFines(Document document, ReportData reportData) throws DocumentException {
        Paragraph fineTitle = new Paragraph("DENDA TERBARU", HEADING_FONT);
        fineTitle.setSpacingBefore(10);
        fineTitle.setSpacingAfter(10);
        document.add(fineTitle);

        if (reportData.getRecentFines() == null || reportData.getRecentFines().isEmpty()) {
            Paragraph noData = new Paragraph("Tidak ada data denda.", NORMAL_FONT);
            noData.setSpacingAfter(20);
            document.add(noData);
            return;
        }

        PdfPTable fineTable = new PdfPTable(5);
        fineTable.setWidthPercentage(100);
        fineTable.setSpacingAfter(20);

        float[] columnWidths = {1f, 2f, 2f, 1.5f, 1.5f};
        fineTable.setWidths(columnWidths);

        addTableHeader(fineTable, "ID");
        addTableHeader(fineTable, "Anggota");
        addTableHeader(fineTable, "Keterangan");
        addTableHeader(fineTable, "Jumlah");
        addTableHeader(fineTable, "Status");

        for (Fine fine : reportData.getRecentFines()) {
            addTableCell(fineTable, String.valueOf(fine.getId()));
            addTableCell(fineTable, fine.getMemberName() != null ? fine.getMemberName() : "-");
            addTableCell(fineTable, fine.getDaysOverdue() + " hari");
            addTableCell(fineTable, String.format("Rp %.0f", fine.getAmount()));
            addTableCell(fineTable, fine.getPaymentStatus());
        }

        document.add(fineTable);
    }

    private static void addFooter(Document document, ReportData reportData) throws DocumentException {
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("________________________________________________________________________________", SMALL_FONT));

        Paragraph footer = new Paragraph("Laporan ini digenerate secara otomatis oleh Library POS System", SMALL_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(10);
        document.add(footer);

        Paragraph timestamp = new Paragraph("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), SMALL_FONT);
        timestamp.setAlignment(Element.ALIGN_CENTER);
        document.add(timestamp);
    }

    private static void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, NORMAL_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(5);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingBottom(5);
        table.addCell(valueCell);
    }

    private static void addStatHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE)));
        cell.setBackgroundColor(new BaseColor(99, 102, 241));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        table.addCell(cell);
    }

    private static void addStatDataCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(15);
        cell.setBackgroundColor(new BaseColor(245, 247, 250));
        table.addCell(cell);
    }

    private static void addTableHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE)));
        cell.setBackgroundColor(new BaseColor(31, 41, 55));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        table.addCell(cell);
    }

    private static void addTableCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}

