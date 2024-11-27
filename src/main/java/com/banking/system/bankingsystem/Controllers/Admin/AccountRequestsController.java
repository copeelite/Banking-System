package com.banking.system.bankingsystem.Controllers.Admin;

import com.banking.system.bankingsystem.Models.RequestData;
import com.banking.system.bankingsystem.Models.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
public class AccountRequestsController implements Initializable {
    @FXML private TableView<RequestData> requestsTable;
    @FXML private TableColumn<RequestData, String> customerEmailCol;
    @FXML private TableColumn<RequestData, String> accountTypeCol;
    @FXML private TableColumn<RequestData, Double> depositCol;
    @FXML private TableColumn<RequestData, String> employeeCol;
    @FXML private TableColumn<RequestData, LocalDateTime> createdAtCol;
    @FXML private TableColumn<RequestData, Void> actionsCol;

    private ObservableList<RequestData> requestsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadRequests();
    }

    private void setupTable() {
        customerEmailCol.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        accountTypeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        depositCol.setCellValueFactory(new PropertyValueFactory<>("initialDeposit"));
        employeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeEmail"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        
        setupActionsColumn();
        
        requestsTable.setItems(requestsList);
    }

    private void setupActionsColumn() {
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button approveButton = new Button("Approve");
            private final Button rejectButton = new Button("Reject");
            {
                approveButton.setOnAction(e -> {
                    RequestData request = getTableRow().getItem();
                    if (request != null) {
                        handleApprove(request);
                    }
                });
                
                rejectButton.setOnAction(e -> {
                    RequestData request = getTableRow().getItem();
                    if (request != null) {
                        handleReject(request);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, approveButton, rejectButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    private void loadRequests() {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM account_requests WHERE status = 'PENDING'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            requestsList.clear();
            boolean hasData = false;
            
            while (rs.next()) {
                hasData = true;
                requestsList.add(new RequestData(
                    rs.getInt("request_id"),
                    rs.getString("customer_email"),
                    rs.getString("account_type"),
                    rs.getDouble("initial_deposit"),
                    rs.getString("employee_email"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
            
            if (!hasData) {
                showNoDataMessage();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to load requests: " + e.getMessage());
        }
    }

    private void showNoDataMessage() {
        Label noDataLabel = new Label("No pending requests found");
        noDataLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");
        requestsTable.setPlaceholder(noDataLabel);
    }

    private void handleApprove(RequestData request) {
        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false);
            try {
                createAccount(conn, request);
                updateRequestStatus(conn, request.getRequestId(), "APPROVED");
                conn.commit();
                loadRequests();
                showSuccess("Account created successfully");
            } catch (Exception e) {
                conn.rollback();
                showError("Failed to approve request: " + e.getMessage());
            }
        } catch (SQLException e) {
            showError("Database connection failed");
        }
    }

    private void handleReject(RequestData request) {
        try (Connection conn = DatabaseConnection.connect()) {
            updateRequestStatus(conn, request.getRequestId(), "REJECTED");
            loadRequests();
            showSuccess("Request rejected");
        } catch (SQLException e) {
            showError("Failed to reject request: " + e.getMessage());
        }
    }

    private void createAccount(Connection conn, RequestData request) throws SQLException {
        // Generate account number
        String accountNumber = generateAccountNumber();
        
        String sql = """
            INSERT INTO accounts (customer_id, account_type, account_number, balance)
            SELECT user_id, ?, ?, ?
            FROM users WHERE email = ?
        """;
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, request.getAccountType());
        pstmt.setString(2, accountNumber);
        pstmt.setDouble(3, request.getInitialDeposit());
        pstmt.setString(4, request.getCustomerEmail());
        pstmt.executeUpdate();
    }

    private void updateRequestStatus(Connection conn, int requestId, String status) throws SQLException {
        String sql = "UPDATE account_requests SET status = ?, processed_at = CURRENT_TIMESTAMP WHERE request_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, status);
        pstmt.setInt(2, requestId);
        pstmt.executeUpdate();
    }

    private String generateAccountNumber() {
        return String.format("%010d", (long) (Math.random() * 10000000000L));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}