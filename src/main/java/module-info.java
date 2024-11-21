module com.banking.system.bankingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;


    opens com.banking.system.bankingsystem to javafx.fxml;
    exports com.banking.system.bankingsystem;
    exports com.banking.system.bankingsystem.Controllers;
    exports com.banking.system.bankingsystem.Controllers.Admin;
    exports com.banking.system.bankingsystem.Controllers.Client;
    exports com.banking.system.bankingsystem.Models;
    exports com.banking.system.bankingsystem.Views;

}