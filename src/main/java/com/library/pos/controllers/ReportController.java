package com.library.pos.controllers;

import com.library.pos.models.User;
import com.library.pos.services.ReportService;

import javax.swing.*;
import java.io.File;

public class ReportController {

    private final ReportService reportService;

    public ReportController() {
        this.reportService = new ReportService();
    }

    public boolean exportToPDF(JFrame parent, User currentUser) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Laporan PDF");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            String userHome = System.getProperty("user.home");
            fileChooser.setCurrentDirectory(new File(userHome + "/Documents"));

            int userSelection = fileChooser.showSaveDialog(parent);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = fileChooser.getSelectedFile();
                String outputPath = selectedDirectory.getAbsolutePath();

                SwingUtilities.invokeLater(() -> {
                    JOptionPane pane = new JOptionPane(
                        "Generating PDF report... Please wait.",
                        JOptionPane.INFORMATION_MESSAGE,
                        JOptionPane.DEFAULT_OPTION,
                        null,
                        new Object[]{},
                        null
                    );
                    JDialog dialog = pane.createDialog(parent, "Exporting");
                    dialog.setModal(false);
                    dialog.setVisible(true);

                    new Thread(() -> {
                        try {
                            String fileName = reportService.generatePDFReport(currentUser, outputPath);

                            SwingUtilities.invokeLater(() -> {
                                dialog.dispose();
                                JOptionPane.showMessageDialog(
                                    parent,
                                    "Laporan berhasil diekspor!\n\nFile: " + fileName,
                                    "Export Berhasil",
                                    JOptionPane.INFORMATION_MESSAGE
                                );
                            });
                        } catch (Exception e) {
                            SwingUtilities.invokeLater(() -> {
                                dialog.dispose();
                                JOptionPane.showMessageDialog(
                                    parent,
                                    "Gagal mengekspor laporan!\n\nError: " + e.getMessage(),
                                    "Export Gagal",
                                    JOptionPane.ERROR_MESSAGE
                                );
                            });
                            e.printStackTrace();
                        }
                    }).start();
                });

                return true;
            }

            return false;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                parent,
                "Gagal mengekspor laporan!\n\nError: " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            return false;
        }
    }
}

