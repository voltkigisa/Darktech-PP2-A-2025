package com.library.pos.services;

import com.library.pos.dao.*;
import com.library.pos.models.*;
import com.library.pos.utils.PDFReportGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ReportService {

    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final BorrowingDAO borrowingDAO;
    private final UserDAO userDAO;
    private final FineDAO fineDAO;

    public ReportService() {
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO();
        this.borrowingDAO = new BorrowingDAO();
        this.userDAO = new UserDAO();
        this.fineDAO = new FineDAO();
    }

    public String generatePDFReport(User currentUser, String outputPath) throws Exception {
        ReportData reportData = collectReportData(currentUser);
        String fileName = PDFReportGenerator.generateReport(reportData, outputPath);
        return fileName;
    }

    private ReportData collectReportData(User currentUser) {
        ReportData reportData = new ReportData();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
        reportData.setGeneratedDate(LocalDate.now().format(formatter));
        reportData.setGeneratedBy(currentUser.getName() + " (" + currentUser.getRole() + ")");

        reportData.setTotalBooks(bookDAO.count());

        try {
            reportData.setTotalMembers(memberDAO.count());
        } catch (Exception e) {
            reportData.setTotalMembers(0);
        }

        reportData.setActiveLoans(borrowingDAO.countActive());
        reportData.setTotalUsers(userDAO.count());

        List<Borrowing> allBorrowings = borrowingDAO.getAll();
        int transLimit = Math.min(20, allBorrowings.size());
        reportData.setRecentTransactions(allBorrowings.subList(0, transLimit));

        List<Fine> allFines = fineDAO.getAll();
        int fineLimit = Math.min(20, allFines.size());
        reportData.setRecentFines(allFines.subList(0, fineLimit));

        return reportData;
    }
}

