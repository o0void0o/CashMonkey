package com.koostech.cashmonkey.model;

/**
 * Created by eswanepo1 on 11/10/2016.
 */

public class Bill {

    String id;
    int dueDate;
    String name;
    double amount;
    double arrears;

    public String getId() {
        return id;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getArrears() {
        return arrears;
    }

    public void setArrears(double arrears) {
        this.arrears = arrears;
    }

    public void setId(String id) {
        this.id = id;
    }
}
