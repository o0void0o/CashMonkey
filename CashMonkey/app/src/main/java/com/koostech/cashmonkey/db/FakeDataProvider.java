package com.koostech.cashmonkey.db;

import com.activeandroid.query.Select;
import com.koostech.cashmonkey.R;
import com.koostech.cashmonkey.model.Budget;
import com.koostech.cashmonkey.model.Category;
import com.koostech.cashmonkey.model.Setting;
import com.koostech.cashmonkey.model.Tran;
import com.koostech.cashmonkey.model.Wallet;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by eswanepo1 on 11/13/2016.
 */

public class FakeDataProvider {

    static ArrayList<Tran> transactions = new ArrayList();

    public static Category getCategoryById(Long id) {

        for (Category cat : FakeDataProvider.getCategoriesList()) {
            if (cat.getId().equals(id)) {
                return cat;
            }

        }
        return null;
    }

    public static String getSetting(String key) {

        ArrayList kk = ((ArrayList) new Select()
                .from(Setting.class)
                .where("Key = ?", key)
//               // .orderBy("Name ASC")
                .execute());

        if (kk.size() == 1) {
            return ((Setting) kk.get(0)).getValue();
        } else {
            return null;
        }
    }

    public static void saveSetting(String key, String value) {
        Setting setting = new Setting();
        setting.setKey(key);
        setting.setValue(value);
        setting.save();


    }


    public static double getWalletBalanceAtStartofPeriod(long start) {
//long time=interval.getStart().toDate().getTime();
        Wallet w = getAciveWallet();
        if (w.getId()==null) {

            return w.getBalance();
        }
ArrayList<Tran> koos = ((ArrayList) new Select()
                .from(Tran.class)
                .where("walletId = ?", w.getId()).and("date>=?", start)
//               // .orderBy("Name ASC")
                .execute());

        Double amount = 0d;
        for (Tran t : koos) {
            amount += t.getAmount();
        }


        return w.getBalance() + amount;

    }


    public static ArrayList<Tran> getTransactions(long start, long end) {
//long time=interval.getStart().toDate().getTime();
        Wallet w = getAciveWallet();
        if (w.getId() == null) {
            return ((ArrayList) new Select()
                    .from(Tran.class)
                    .where("date>=?", start).and("date<=?", end)
                    .execute());
        } else {
            return ((ArrayList) new Select()
                    .from(Tran.class)
                    .where("walletId = ?", w.getId()).and("date>=?", start).and("date<=?", end)
//               // .orderBy("Name ASC")
                    .execute());
        }

    }

    public static ArrayList<Tran> getBudgetsTransactionsByCat(Budget b) {


        return ((ArrayList) new Select()
                .from(Tran.class)
                .where("date>=?", b.getStart()).and("date<=?", b.getEnd()).and("categoryId=?", b.getCat())
//               // .orderBy("Name ASC")
                .execute());


    }

    public static Wallet getDefaultWallet() {
        //todo fix this
        return getWallets().get(0);


    }

    static {
        ArrayList<Category> kk = ((ArrayList) new Select()
                .from(Category.class)

                .execute());

        if (kk.size() == 0) {
            Category c0 = new Category();
            c0.setDebit(true);
            c0.setName("Rent");


            c0.setIcon("ic_category_family");
            c0.save();

            Category c1 = new Category();
            c1.setDebit(true);
            c1.setName("food");

            c1.setIcon("icon_1");
            c1.save();

            Category c2 = new Category();
            c2.setDebit(true);
            c2.setName("Shopping");

            c2.setIcon("icon_2");
            c2.save();

            Category c3 = new Category();
            c3.setDebit(true);
            c3.setName("Petrol");

            c3.setIcon("icon_34");
            c3.save();

            Category c4 = new Category();
            c4.setDebit(true);
            c4.setName("Electricity");

            c4.setIcon("icon_125");
            c4.save();
        }
    }

    public static HashMap<Long, Category> getCategories() {
        HashMap<Long, Category> categories = new HashMap<Long, Category>();

        ArrayList<Category> kk = ((ArrayList) new Select()
                .from(Category.class)

                .execute());

        if (kk.size() == 0) {
            Category c0 = new Category();
            c0.setDebit(true);
            c0.setName("Rent");


            c0.setIcon("ic_category_family");
            c0.save();
            categories.put(c0.getId(), c0);
            Category c1 = new Category();
            c1.setDebit(true);
            c1.setName("food");

            c1.setIcon("icon_1");
            c1.save();
            categories.put(c1.getId(), c1);
            Category c2 = new Category();
            c2.setDebit(true);
            c2.setName("Shopping");

            c2.setIcon("icon_2");
            c2.save();
            categories.put(c2.getId(), c2);
            Category c3 = new Category();
            c3.setDebit(true);
            c3.setName("Petrol");

            c3.setIcon("icon_34");
            c3.save();
            categories.put(c3.getId(), c3);
            Category c4 = new Category();
            c4.setDebit(true);
            c4.setName("Electricity");

            c4.setIcon("icon_125");
            c4.save();
            categories.put(c4.getId(), c4);
        } else {

            for (Category cat : kk) {
                categories.put(cat.getId(), cat);
            }
        }
        return categories;
    }

    public static ArrayList<Category> getCategoriesList() {
        ArrayList<Category> kk = ((ArrayList) new Select()
                .from(Category.class)

                .execute());
        return kk;
    }

    public static ArrayList<Category> getTop5Categories() {
        ArrayList<Category> kk = ((ArrayList) new Select()
                .from(Category.class)

                .execute());
        return kk;

    }


    public static Wallet getAciveWallet() {


        String walletId = FakeDataProvider.getSetting("monkey.activeWallet");


        if (walletId != null && !walletId.equals("null")) {
            return getWallet(walletId);

        }
        Wallet w = new Wallet();
        w.setName("All Wallets");
        w.setCurrencyId("1");
        w.setBalance(1639d);
        w.setIcon(R.drawable.all_wallets);
        w.setExcludeFromTotal(false);
        Double amount = 0d;
        ArrayList<Wallet> wallets = getWallets();
        for (Wallet wallet : wallets) {
            if (!wallet.isExcludeFromTotal()) {
                amount += wallet.getBalance();
            }
        }
        w.setBalance(amount);
        return w;


    }

    public static Wallet getAllWallets() {

        Wallet w = new Wallet();
        w.setName("All Wallets");
        w.setCurrencyId("1");

        w.setIcon(R.drawable.all_wallets);
        w.setExcludeFromTotal(false);
        Double amount = 0d;
        ArrayList<Wallet> wallets = getWallets();
        for (Wallet wallet : wallets) {
            if (!wallet.isExcludeFromTotal()) {
                amount += wallet.getBalance();
            }
        }
        w.setBalance(amount);
        return w;


    }

    public static HashMap<Category, ArrayList<Budget>> getActiveBudgets() {

        DateTime now = new DateTime().withTimeAtStartOfDay();
        HashMap<Category, ArrayList<Budget>> retval = new HashMap<>();
        ArrayList<Budget> kk = ((ArrayList) new Select()
                .from(Budget.class).where("end>=?", now.getMillis())

//               // .orderBy("Name ASC")
                .execute());

        if (kk.size() > 0) {

            for (Budget b : kk) {
                Category c = getCategoryById(b.getCat());

                if (retval.get(c) != null) {
                    retval.get(c).add(b);

                } else {
                    ArrayList<Budget> bb = new ArrayList<>();
                    bb.add(b);
                    retval.put(c, bb);
                }
            }
        }
        return retval;
    }

    public static Budget getBudgetByID(Long id) {


        ArrayList<Budget> kk = ((ArrayList) new Select()
                .from(Budget.class).where("id>=?", id)

//               // .orderBy("Name ASC")
                .execute());

        if (kk.size() > 0) {
            return kk.get(0);
        } else {
            return null;
        }
    }

    public static ArrayList<Budget> getBudgets(DateTime from, DateTime end) {


        ArrayList kk = ((ArrayList) new Select()
                .from(Budget.class).where("end>=?", from.getMillis()).and("start<=?", end.getMillis())

//               // .orderBy("Name ASC")
                .execute());

        if (kk.size() > 0) {
            return kk;
        } else {


            return null;
        }
    }

    public static ArrayList<Wallet> getToolbarWallets() {
        ArrayList<Wallet> wallets = new ArrayList();

        Wallet w0 = new Wallet();
        w0.setName("All Wallets");
        w0.setCurrencyId("0");
        w0.setBalance(2000d + 1639d);
        w0.setIcon(R.drawable.all_wallets);
        w0.setExcludeFromTotal(true);
        wallets.add(w0);


        ArrayList<Wallet> kk = getWallets();
        wallets.addAll(kk);

        return wallets;
    }


    public static Budget getActiveBudgetForCatAndWallet(Long catId, long time, long walletID) {


        ArrayList<Budget> kk = ((ArrayList) new Select()
                .from(Budget.class)
                .where("cat = ?", catId).and("start<=?", time).and("end>=?", time).and("wallet=?", walletID)
//               // .orderBy("Name ASC")
                .execute());

        if (kk.size() > 0) {
            return kk.get(0);
        } else {
            return null;
        }
    }


    public static Wallet getWallet(String id) {

        ArrayList kk = ((ArrayList) new Select()
                .from(Wallet.class)
                .where("id = ?", id)
//               // .orderBy("Name ASC")
                .execute());

        if (kk.size() == 1) {
            return ((Wallet) kk.get(0));
        } else {
            return null;
        }

    }


    public static ArrayList<Wallet> getWallets() {
        ArrayList kk = ((ArrayList) new Select()
                .from(Wallet.class)

//               // .orderBy("Name ASC")
                .execute());

        if (kk.size() == 0) {


            Wallet w1 = new Wallet();
            w1.setName("Fnb checking");

            w1.setCurrencyId("0");
            w1.setBalance(1639d);
            w1.setIcon(R.drawable.ic_income_teal_card_pink);
            w1.setExcludeFromTotal(false);
            w1.save();
            Wallet w2 = new Wallet();
            w2.setName("Satrix");

            w2.setCurrencyId("0");
            w2.setBalance(-50d);
            w2.setIcon(R.drawable.ic_income_teal_card_gold);
            w2.setExcludeFromTotal(true);
            w2.save();

            Wallet w3 = new Wallet();
            w3.setName("Cash");

            w3.setCurrencyId("0");
            w3.setBalance(0d);
            w3.setIcon(R.drawable.ic_income_teal_cash);
            w3.setExcludeFromTotal(false);
            w3.save();

            Wallet w4 = new Wallet();
            w4.setName("Absa Savings");

            w4.setCurrencyId("0");
            w4.setBalance(2000d);
            w4.setIcon(R.drawable.ic_income_teal_card_silver);
            w4.setExcludeFromTotal(false);
            w4.save();
            return getWallets();
        }
        return kk;
    }
}
