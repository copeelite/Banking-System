package com.banking.system.bankingsystem.Controllers.Employee;

import com.banking.system.bankingsystem.Models.Model;
import com.banking.system.bankingsystem.Views.AdminMenuOptions;
import com.banking.system.bankingsystem.Views.EmployeeMenuOptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMenuController implements Initializable {
    @FXML
    private Button logout_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model.getInstance().getViewFactory().getEmployeeSelectedMenuItem().set(EmployeeMenuOptions.MANAGE_ACCOUNTS);

    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
