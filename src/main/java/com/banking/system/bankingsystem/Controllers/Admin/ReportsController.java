package com.banking.system.bankingsystem.Controllers.Admin;

import com.banking.system.bankingsystem.Models.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import java.io.FileWriter;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import com.banking.system.bankingsystem.Models.TransactionReport;
public class ReportsController implements Initializable {
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private ComboBox<String> reportType;
    @FXML private Button generateBtn;
    @FXML private Button exportBtn;
    @FXML private TableView<TransactionReport> reportTable;
    @FXML private TableColumn<TransactionReport, String> dateCol;
    @FXML private TableColumn<TransactionReport, String> typeCol;
    @FXML private TableColumn<TransactionReport, Double> amountCol;
    @FXML private TableColumn<TransactionReport, String> descriptionCol;
    @FXML private Text totalTransactions;
    @FXML private Text totalAmount;

    private ObservableList<TransactionReport> reportData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupControls();
        setupTable();
        setupListeners();
    }

    private void setupControls() {
        reportType.getItems().addAll("All Transactions", "Deposits Only", "Withdrawals Only");
        reportType.setValue("All Transactions");
        startDate.setValue(LocalDate.now().minusMonths(1));
        endDate.setValue(LocalDate.now());
    }

    private void setupTable() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        dateCol.setPrefWidth(150);
        typeCol.setPrefWidth(100);
        amountCol.setPrefWidth(100);
        descriptionCol.setPrefWidth(200);
        
        reportTable.setItems(reportData);
    }

    private void setupListeners() {
        generateBtn.setOnAction(e -> generateReport());
        exportBtn.setOnAction(e -> exportToCSV());
    }

    private void generateReport() {
        reportData.clear();
        String sql = buildReportQuery();
        
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();
            
            String startStr = start.toString() + " 00:00:00";
            String endStr = end.toString() + " 23:59:59";
            stmt.setString(1, startStr);
            stmt.setString(2, endStr);
            
            ResultSet rs = stmt.executeQuery();
            double total = 0;
            int count = 0;
            
            while (rs.next()) {
                reportData.add(new TransactionReport(
                    rs.getTimestamp("transaction_date").toLocalDateTime().toString(),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description")
                ));
                total += rs.getDouble("amount");
                count++;
            }
            
            totalTransactions.setText(String.valueOf(count));
            totalAmount.setText(String.format("$%.2f", Math.abs(total)));
            reportTable.refresh();
            
        } catch (SQLException e) {
            showError("Error generating report: " + e.getMessage());
        }
    }

    private String buildReportQuery() {
        String baseQuery = """
            SELECT t.transaction_date, t.transaction_type, t.amount, t.description
            FROM transactions t
            JOIN accounts a ON t.account_id = a.account_id
            WHERE CAST(t.transaction_date AS VARCHAR) BETWEEN ? AND ?
            """;
            
        return switch(reportType.getValue()) {
            case "Deposits Only" -> baseQuery + " AND t.transaction_type IN ('DEPOSIT', 'RECEIVE')";
            case "Withdrawals Only" -> baseQuery + " AND t.transaction_type IN ('WITHDRAWAL', 'SEND')";
            default -> baseQuery;
        } + " ORDER BY t.transaction_date DESC";
    }

    private void exportToCSV() {
        try (FileWriter writer = new FileWriter("transaction_report.csv")) {
            writer.write("Date,Type,Amount,Description\n");
            
            for (TransactionReport report : reportData) {
                writer.write(String.format("%s,%s,%.2f,%s\n",
                    report.getDate(),
                    report.getType(),
                    report.getAmount(),
                    report.getDescription().replace(",", ";")
                ));
            }
            
            showSuccess("Report exported successfully to transaction_report.csv");
        } catch (Exception e) {
            showError("Error exporting report: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}