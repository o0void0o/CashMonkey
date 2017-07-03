package com.koostech.cashmonkey.model;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import java.io.Serializable;

@Table(name = "Setting")
public class Setting extends Model implements Serializable {
    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String key;
    @Column
    String value;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
