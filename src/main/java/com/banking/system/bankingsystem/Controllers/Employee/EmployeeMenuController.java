package com.banking.system.bankingsystem.Controllers.Employee;

import com.banking.system.bankingsystem.Models.Model;
import com.banking.system.bankingsystem.Views.AdminMenuOptions;
import com.banking.system.bankingsystem.Views.EmployeeMenuOptions;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMenuController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model.getInstance().getViewFactory().getEmployeeSelectedMenuItem().set(EmployeeMenuOptions.MANAGE_ACCOUNTS);

    }
}
