module com.banking.system.bankingsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.banking.system.bankingsystem to javafx.fxml;
    exports com.banking.system.bankingsystem;
}