package com.banking.system.bankingsystem.Controllers.Employee;

import com.banking.system.bankingsystem.Models.Model;
import com.banking.system.bankingsystem.Views.EmployeeMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getEmployeeSelectedMenuItem().addListener((observableValue, oldValue, newValue) -> {
            switch(newValue) {
                case PROFILE -> {
                    Model.getInstance().getViewFactory().setProfileView(null);
                    client_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                }
                case MANAGE_ACCOUNTS -> {
                    Model.getInstance().getViewFactory().setCreateAccountView(null);
                    client_parent.setCenter(Model.getInstance().getViewFactory().getCreateAccountView());
                }
                case CLIENTS -> {
                    
                }
            }
        });
    }
}