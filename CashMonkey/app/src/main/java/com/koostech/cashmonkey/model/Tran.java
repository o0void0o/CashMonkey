package com.koostech.cashmonkey.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.koostech.cashmonkey.db.FakeDataProvider;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Table(name = "Tran")
public class Tran extends Model implements Serializable {

    @Column
    double amount;
    @Column
    Long categoryId;
    @Column
    long walletId;
    @Column
    long date;
    @Column
    String receiptId;
    @Column
    boolean isDebit;
    @Column
    String description;
    @Column
    Long budget;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public long getWalletId() {
        return walletId;
    }

    public void setWalletId(long walletId) {
        this.walletId = walletId;
    }

    public long getDate() {
        return date;
    }

    public String getIndexDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(date));
        return c.get(Calendar.DAY_OF_MONTH) + "" + c.get(Calendar.MONTH);

    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public boolean isDebit() {
        return isDebit;
    }

    public void setDebit(boolean debit) {
        isDebit = debit;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public void deleteTransaction() {
        Wallet w = FakeDataProvider.getWallet(String.valueOf(walletId));
        w.setBalance(w.getBalance() + amount);
        w.save();
        delete();
    }


}
