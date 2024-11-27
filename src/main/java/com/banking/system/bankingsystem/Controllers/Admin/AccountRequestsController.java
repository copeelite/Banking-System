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
    @FXML private TableView<RequestData> completedRequestsTable;
    @FXML private TableColumn<RequestData, String> completedCustomerEmailCol;
    @FXML private TableColumn<RequestData, String> completedAccountTypeCol;
    @FXML private TableColumn<RequestData, Double> completedDepositCol;
    @FXML private TableColumn<RequestData, String> completedEmployeeCol;
    @FXML private TableColumn<RequestData, LocalDateTime> completedCreatedAtCol;
    @FXML private TableColumn<RequestData, String> completedStatusCol;

    private ObservableList<RequestData> requestsList = FXCollections.observableArrayList();
    private ObservableList<RequestData> completedRequestsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupCompletedTable();
        loadRequests();
        loadCompletedRequests();
    }

    private void setupTable() {
        customerEmailCol.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        accountTypeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        depositCol.setCellValueFactory(new PropertyValueFactory<>("initialDeposit"));
        employeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeEmail"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        
        customerEmailCol.setPrefWidth(150);
        accountTypeCol.setPrefWidth(100);
        depositCol.setPrefWidth(100);
        employeeCol.setPrefWidth(150);
        createdAtCol.setPrefWidth(150);
        actionsCol.setPrefWidth(150);  
        
        setupActionsColumn();
        
        requestsTable.setItems(requestsList);
    }

    private void setupCompletedTable() {
        completedCustomerEmailCol.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        completedAccountTypeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        completedDepositCol.setCellValueFactory(new PropertyValueFactory<>("initialDeposit"));
        completedEmployeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeEmail"));
        completedCreatedAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        completedStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        completedRequestsTable.setItems(completedRequestsList);
    }

    private void setupActionsColumn() {
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button approveButton = new Button("Approve");
            private final Button rejectButton = new Button("Reject");
            {
                approveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-min-width: 60px;");
                rejectButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-min-width: 60px;");
                
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
                    buttons.setStyle("-fx-alignment: center;");
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
            requestsTable.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to load requests: " + e.getMessage());
        }
    }

    private void loadCompletedRequests() {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM account_requests WHERE status IN ('APPROVED', 'REJECTED') ORDER BY created_at DESC LIMIT 50";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            completedRequestsList.clear();
            
            while (rs.next()) {
                RequestData request = new RequestData(
                    rs.getInt("request_id"),
                    rs.getString("customer_email"),
                    rs.getString("account_type"),
                    rs.getDouble("initial_deposit"),
                    rs.getString("employee_email"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                request.setStatus(rs.getString("status"));
                completedRequestsList.add(request);
            }
            
            completedRequestsTable.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to load completed requests: " + e.getMessage());
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
                String checkSql = "SELECT status FROM account_requests WHERE request_id = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, request.getRequestId());
                    ResultSet rs = checkStmt.executeQuery();
                    if (!rs.next() || !"PENDING".equals(rs.getString("status"))) {
                        throw new SQLException("request has been processed or does not exist");
                    }
                }
                
                createAccount(conn, request);
                updateRequestStatus(conn, request.getRequestId(), "APPROVED");
                conn.commit();
                showSuccess("Account created successfully");
                loadRequests();
                loadCompletedRequests();
            } catch (Exception e) {
                conn.rollback();
                showError("Failed to approve request: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            showError("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleReject(RequestData request) {
        try (Connection conn = DatabaseConnection.connect()) {
            updateRequestStatus(conn, request.getRequestId(), "REJECTED");
            loadRequests();
            loadCompletedRequests();
            showSuccess("Request rejected");
        } catch (SQLException e) {
            showError("Failed to reject request: " + e.getMessage());
        }
    }

    private void createAccount(Connection conn, RequestData request) throws SQLException {
        String checkUserSql = "SELECT user_id FROM users WHERE email = ? AND role = 'CLIENT'";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
            checkStmt.setString(1, request.getCustomerEmail());
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Customer does not exist: " + request.getCustomerEmail());
            }
            int userId = rs.getInt("user_id");
            
            String checkAccountSql = "SELECT COUNT(*) FROM accounts WHERE customer_id = ? AND account_type = ?";
            try (PreparedStatement accStmt = conn.prepareStatement(checkAccountSql)) {
                accStmt.setInt(1, userId);
                accStmt.setString(2, request.getAccountType());
                ResultSet accRs = accStmt.executeQuery();
                if (accRs.next() && accRs.getInt(1) > 0) {
                    throw new SQLException("Customer already has this type of account");
                }
            }
            
            String accountNumber = generateAccountNumber();
            
            String insertSql = """
                INSERT INTO accounts (customer_id, account_type, account_number, balance, status)
                VALUES (?, ?, ?, ?, 'ACTIVE')
            """;
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, request.getAccountType());
                insertStmt.setString(3, accountNumber);
                insertStmt.setDouble(4, request.getInitialDeposit());
                insertStmt.executeUpdate();
            }
            
             if (request.getInitialDeposit() > 0) {
                String transSql = """
                    INSERT INTO transactions (account_id, transaction_type, amount, description)
                    SELECT account_id, 'DEPOSIT', ?, 'Initial Deposit'
                    FROM accounts WHERE account_number = ?
                """;
                try (PreparedStatement transStmt = conn.prepareStatement(transSql)) {
                    transStmt.setDouble(1, request.getInitialDeposit());
                    transStmt.setString(2, accountNumber);
                    transStmt.executeUpdate();
                }
            }
        }
    }

    private void updateRequestStatus(Connection conn, int requestId, String status) throws SQLException {
        String sql = "UPDATE account_requests SET status = ? WHERE request_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, requestId);
            pstmt.executeUpdate();
        }
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