package com.banking.system.bankingsystem.Models;

import com.banking.system.bankingsystem.Views.ViewFactory;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private String currentUserEmail;

    private Model() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }
    public String getCurrentUserEmail() {
        return currentUserEmail;
    }
    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }
}
