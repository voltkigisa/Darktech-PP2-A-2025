# Fix: Package com.itextpdf.text.pdf does not exist

## Problem
Error saat compile PDFReportGenerator.java:
```
D:\PP2LAST\Darktech-PP2-A-2025\src\main\java\com\library\pos\utils\PDFReportGenerator.java:4:29
java: package com.itextpdf.text.pdf does not exist
```

## Root Cause
iText library (itextpdf-5.5.13.3.jar) tidak ada di classpath saat compile.

## Solution

### 1. Pastikan library iText ada di folder lib:
```
lib/itextpdf-5.5.13.3.jar
```

### 2. Compile dengan classpath yang benar:
```batch
cd src\main\java

javac -encoding UTF-8 -cp ".;..\..\..\lib\mysql-connector-j-9.5.0.jar;..\..\..\lib\itextpdf-5.5.13.3.jar;..\..\..\target\classes" -d ..\..\..\target\classes com\library\pos\utils\PDFReportGenerator.java
```

### 3. Compile semua file report:
```batch
javac -encoding UTF-8 -cp ".;..\..\..\lib\mysql-connector-j-9.5.0.jar;..\..\..\lib\itextpdf-5.5.13.3.jar;..\..\..\target\classes" -d ..\..\..\target\classes com\library\pos\models\ReportData.java com\library\pos\utils\PDFReportGenerator.java com\library\pos\services\ReportService.java com\library\pos\controllers\ReportController.java
```

### 4. Compile dashboard views:
```batch
javac -encoding UTF-8 -cp ".;..\..\..\lib\mysql-connector-j-9.5.0.jar;..\..\..\lib\itextpdf-5.5.13.3.jar;..\..\..\target\classes" -d ..\..\..\target\classes com\library\pos\views\admin\AdminDashboardView.java com\library\pos\views\manager\ManagerDashboardView.java
```

### 5. Run aplikasi dengan classpath lengkap:
```batch
java -cp "target\classes;lib\mysql-connector-j-9.5.0.jar;lib\itextpdf-5.5.13.3.jar" com.library.pos.main.Main
```

## ✅ Fixed!
Semua file sudah ter-compile dan aplikasi berjalan dengan baik.

Tombol **"📄 Export PDF"** sekarang berfungsi dengan sempurna di:
- Admin Dashboard
- Manager Dashboard

---
Fixed: 7 Januari 2026

