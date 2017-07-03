package com.koostech.cashmonkey.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "Wallet")
public class Wallet extends Model implements Serializable {

    @Column
    String name;
    @Column
    String currencyId;
    @Column
    Double balance;
    @Column
    String description;
    @Column
    int icon;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


    public boolean isExcludeFromTotal() {
        return excludeFromTotal;
    }

    public void setExcludeFromTotal(boolean excludeFromTotal) {
        this.excludeFromTotal = excludeFromTotal;
    }

    boolean excludeFromTotal;
}
