package com.banking.system.bankingsystem.Controllers.Admin;

import com.banking.system.bankingsystem.Models.DatabaseConnection;
import com.banking.system.bankingsystem.Models.UserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;

public class ManageUsersController implements Initializable {
    @FXML
    private TableView<UserData> usersTable;
    @FXML
    private TableColumn<UserData, Integer> idCol;
    @FXML
    private TableColumn<UserData, String> usernameCol;
    @FXML
    private TableColumn<UserData, String> emailCol;
    @FXML
    private TableColumn<UserData, String> roleCol;
    @FXML
    private TableColumn<UserData, Void> actionsCol;
    @FXML
    private ComboBox<String> userTypeFilter;
    @FXML
    private TextField searchField;
    @FXML
    private Button addUserBtn;

    private ObservableList<UserData> usersList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupFilters();
        loadUsers();

        addUserBtn.setOnAction(e -> showAddUserDialog());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterUsers());
        userTypeFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterUsers());
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        setupActionsColumn();
    }

    private void setupActionsColumn() {
        actionsCol.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("  edit");
            private final Button deleteButton = new Button("delete");

            {
                editButton.setOnAction(event -> {
                    UserData user = getTableView().getItems().get(getIndex());
                    showEditUserDialog(user);
                });

                deleteButton.setOnAction(event -> {
                    UserData user = getTableView().getItems().get(getIndex());
                    showDeleteConfirmation(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    private void loadUsers() {
        usersList.clear();
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM users WHERE role != 'ADMIN'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                usersList.add(new UserData(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role")));
            }
            usersTable.setItems(usersList);
        } catch (SQLException e) {
            showError("load users failed: " + e.getMessage());
        }
    }

    private void showAddUserDialog() {
        Dialog<UserData> dialog = new Dialog<>();
        dialog.setTitle("add new user");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        TextField email = new TextField();
        PasswordField password = new PasswordField();
        ComboBox<String> role = new ComboBox<>();
        role.getItems().addAll("CLIENT", "EMPLOYEE");

        grid.add(new Label("username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("email:"), 0, 1);
        grid.add(email, 1, 1);
        grid.add(new Label("password:"), 0, 2);
        grid.add(password, 1, 2);
        grid.add(new Label("role:"), 0, 3);
        grid.add(role, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try (Connection conn = DatabaseConnection.connect()) {
                    String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, username.getText());
                    stmt.setString(2, email.getText());
                    stmt.setString(3, password.getText());
                    stmt.setString(4, role.getValue());
                    stmt.executeUpdate();

                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        return new UserData(
                                rs.getInt(1),
                                username.getText(),
                                email.getText(),
                                role.getValue());
                    }
                } catch (SQLException e) {
                    showError("Failed to add user: " + e.getMessage());
                }
            }
            return null;
        });

        Optional<UserData> result = dialog.showAndWait();
        result.ifPresent(userData -> {
            usersList.add(userData);
            loadUsers();
        });
    }

    private void showDeleteConfirmation(UserData user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("confirm delete");
        alert.setHeaderText("delete user");
        alert.setContentText("confirm to delete user " + user.getUsername() + " ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteUser(user);
        }
    }

    private void deleteUser(UserData user) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();

            usersList.remove(user);
        } catch (SQLException e) {
            showError("delete user failed: " + e.getMessage());
        }
    }

    private void filterUsers() {
        String searchText = searchField.getText().toLowerCase();
        String selectedType = userTypeFilter.getValue();

        ObservableList<UserData> filteredList = usersList
                .filtered(user -> (searchText.isEmpty() || user.getUsername().toLowerCase().contains(searchText)
                        || user.getEmail().toLowerCase().contains(searchText))
                        && (selectedType == null || selectedType.equals("ALL") || user.getRole().equals(selectedType)));

        usersTable.setItems(filteredList);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupFilters() {
        userTypeFilter.getItems().addAll("ALL", "CLIENT", "EMPLOYEE", "EMPLOYEE_PLUS");
        userTypeFilter.setValue("ALL");
    }

    private void showEditUserDialog(UserData user) {
        Dialog<UserData> dialog = new Dialog<>();
        dialog.setTitle("Edit User");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField(user.getUsername());
        TextField email = new TextField(user.getEmail());
        PasswordField password = new PasswordField();
        ComboBox<String> role = new ComboBox<>();
        role.getItems().addAll("CLIENT", "EMPLOYEE");
        role.setValue(user.getRole());

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(email, 1, 1);
        grid.add(new Label("New Password:"), 0, 2);
        grid.add(password, 1, 2);
        grid.add(new Label("Role:"), 0, 3);
        grid.add(role, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try (Connection conn = DatabaseConnection.connect()) {
                    String sql;
                    PreparedStatement stmt;

                    if (password.getText().isEmpty()) {
                        // If password is empty, don't update it
                        sql = "UPDATE users SET username = ?, email = ?, role = ? WHERE user_id = ?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, username.getText());
                        stmt.setString(2, email.getText());
                        stmt.setString(3, role.getValue());
                        stmt.setInt(4, user.getId());
                    } else {
                        // If new password provided, update it
                        sql = "UPDATE users SET username = ?, email = ?, password = ?, role = ? WHERE user_id = ?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, username.getText());
                        stmt.setString(2, email.getText());
                        stmt.setString(3, password.getText());
                        stmt.setString(4, role.getValue());
                        stmt.setInt(5, user.getId());
                    }

                    int affected = stmt.executeUpdate();
                    if (affected > 0) {
                        return new UserData(
                                user.getId(),
                                username.getText(),
                                email.getText(),
                                role.getValue());
                    }
                } catch (SQLException e) {
                    showError("Failed to update user: " + e.getMessage());
                }
            }
            return null;
        });

        Optional<UserData> result = dialog.showAndWait();
        result.ifPresent(updatedUser -> {
            int index = usersList.indexOf(user);
            if (index >= 0) {
                usersList.set(index, updatedUser);
            }
            loadUsers();
        });
    }
}