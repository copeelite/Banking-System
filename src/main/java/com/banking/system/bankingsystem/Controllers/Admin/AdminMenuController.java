package com.banking.system.bankingsystem.Controllers.Admin;

import com.banking.system.bankingsystem.Models.Model;
import com.banking.system.bankingsystem.Views.AdminMenuOptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button create_client_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button clients_btn;
    public Button deposit_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.MANAGE_ACCOUNTS);
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
