module com.example.proyekadsi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;      // Wajib untuk database
    requires java.desktop;  // Kadang diperlukan untuk beberapa komponen UI

    // 1. Izin untuk Main Class (HelloApplication)
    opens com.example.proyekadsi to javafx.fxml;
    exports com.example.proyekadsi;

    // 2. Izin untuk Controller (INI YANG BIKIN ERROR KAMU)
    // "opens" membolehkan FXML menyuntikkan data ke private field
    opens com.example.proyekadsi.controller to javafx.fxml;
    exports com.example.proyekadsi.controller;

    // 3. Izin untuk Model (Agar bisa dibaca TableView)
    opens com.example.proyekadsi.model to javafx.base;
    exports com.example.proyekadsi.model;

    // 4. Izin untuk DAO/Database (Opsional tapi aman ditambahkan)
    exports com.example.proyekadsi.DAO;
}