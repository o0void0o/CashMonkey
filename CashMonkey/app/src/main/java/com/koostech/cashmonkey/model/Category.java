package com.koostech.cashmonkey.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Category")
public class Category extends Model {

    @Column
    String name;
    @Column
    String description;
    @Column
    String icon;
    Category parent;
    @Column
    Long parentId;
    @Column
    boolean isDebit;

    Long categoryTypeId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public Category setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isDebit() {
        return isDebit;
    }

    public void setDebit(boolean debit) {
        isDebit = debit;
    }

    public Long getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(Long categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Category category = (Category) o;

        return getId() != null ? getId().equals(category.getId()) : category.getId() == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
