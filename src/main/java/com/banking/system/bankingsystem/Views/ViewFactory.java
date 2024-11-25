package com.banking.system.bankingsystem.Views;
import com.banking.system.bankingsystem.BankApplication;
import com.banking.system.bankingsystem.Controllers.Admin.AdminController;
import com.banking.system.bankingsystem.Controllers.Client.ClientController;
import com.banking.system.bankingsystem.Controllers.Employee.EmployeeController;
import com.banking.system.bankingsystem.cof.AppConfig;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.banking.system.bankingsystem.cof.AppConfig;

public class ViewFactory {
    private AccountType signinAccountType;
    //Client
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private AnchorPane dashbaordView;
    private AnchorPane transactionsView;
    private AnchorPane accountsView;

    //Employee
    private final ObjectProperty<EmployeeMenuOptions> employeeSelectedMenuItem;
    //Admin
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;



    public ViewFactory() {
        this.signinAccountType = AccountType.CLIENT;
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
        this.employeeSelectedMenuItem = new SimpleObjectProperty<>();
    }
    public AccountType getSigninAccountType() {
        return signinAccountType;
    }
    public void setSigninAccountType(AccountType signinAccountType) {
        this.signinAccountType = signinAccountType;
    }
    public ObjectProperty<ClientMenuOptions> getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }
    public AnchorPane getDashboardView() {
        if( dashbaordView == null) {

            try{
                dashbaordView = new FXMLLoader(getClass().getResource("/fxml/Client/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashbaordView;
    }
    public AnchorPane getTransactionsView() {
        if( transactionsView == null) {
            try{
                transactionsView = new FXMLLoader(getClass().getResource("/fxml/Client/Transactions.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return transactionsView;
    }
    public AnchorPane getAccountsView() {
        if( accountsView == null) {
            try{
                accountsView = new FXMLLoader(getClass().getResource("/fxml/Client/Accounts.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return accountsView;
    }





    public void showSinginWindow() {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Signin.fxml"));
            createStage(loader);
    }

    public void showClientWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        createStage(loader);
    }
    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }
    public void showEmployeeWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Employee/Employee.fxml"));
        EmployeeController employeeController = new EmployeeController();
        loader.setController(employeeController);
        createStage(loader);
    }
    //Admin
    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    //Employee
    public ObjectProperty<EmployeeMenuOptions> getEmployeeSelectedMenuItem() {
        return employeeSelectedMenuItem;
    }




    private void createStage(FXMLLoader loader){
        AppConfig.init();

        Scene scene = null;

        try {
            scene = new Scene(loader.load());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(AppConfig.title);
        stage.setResizable(AppConfig.stageResizable);
        stage.getIcons().add(new Image(BankApplication.class.getResourceAsStream(AppConfig.icon)));
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}
