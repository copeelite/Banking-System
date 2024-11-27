package com.banking.system.bankingsystem.Controllers.Admin;

import com.banking.system.bankingsystem.Models.Model;
import com.banking.system.bankingsystem.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener((observableValue, oldValue, newValue) -> {
        switch(newValue) {
            case MANAGE_USERS -> admin_parent.setCenter(Model.getInstance().getViewFactory().getManageUsersView());
            case APPROVE_REQUESTS -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAccountRequestsView());
            // case GENERATE_REPORTS -> admin_parent.setCenter(Model.getInstance().getViewFactory().getReportsView());
            // case ASSIGN_ROLES -> admin_parent.setCenter(Model.getInstance().getViewFactory().getRolesView());
        }
    });
    
    Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.MANAGE_USERS);
}
}