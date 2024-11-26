package com.banking.system.bankingsystem.Controllers.Client;

import com.banking.system.bankingsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {


    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().addListener((observableValue, oldValue, newValue) -> {
            switch(newValue) {
                case PROFILE -> {
                    Model.getInstance().getViewFactory().setProfileView(null);
                    client_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                }
                case TRANSACTIONS -> {
                    Model.getInstance().getViewFactory().setTransactionsView(null);
                    client_parent.setCenter(Model.getInstance().getViewFactory().getTransactionsView());
                }
                case DASHBOARD -> {
                    Model.getInstance().getViewFactory().setDashboardView(null);
                    client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                }
                case ACCOUNTS -> {
                    Model.getInstance().getViewFactory().setAccountsView(null);
                    client_parent.setCenter(Model.getInstance().getViewFactory().getAccountsView());
                }
            }
        });
    }

}
