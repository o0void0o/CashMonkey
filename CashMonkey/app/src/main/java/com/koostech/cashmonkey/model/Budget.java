package com.koostech.cashmonkey.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by eswanepo1 on 12/1/2016.
 */
@Table(name = "Budget")
public class Budget extends Model implements Serializable {

    @Column
    long cat;
    @Column
    double amount;
    @Column
    double amountRemaining;
    @Column
    long wallet;
    @Column
    long start;
    @Column
    long end;
    @Column
    boolean ballanceCarryOver;
    public boolean isInclude() {
        return include;
    }

    public void setInclude(boolean include) {
        this.include = include;
    }

    @Column
    boolean include;

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    @Column
    boolean repeat;

    public boolean isBallanceCarryOver() {
        return ballanceCarryOver;
    }

    public void setBallanceCarryOver(boolean ballanceCarryOver) {
        this.ballanceCarryOver = ballanceCarryOver;
    }

    public double getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(double amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public long getCat() {
        return cat;
    }

    public void setCat(long cat) {
        this.cat = cat;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getWallet() {
        return wallet;
    }

    public void setWallet(long wallet) {
        this.wallet = wallet;
    }

    public long getStart() {
        return start;
    }

    public DateTime getDateTimeStart() {
        return new DateTime(new Date(start));
    }

    public DateTime getDateTimeEnd() {
        return new DateTime(new Date(end));
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
