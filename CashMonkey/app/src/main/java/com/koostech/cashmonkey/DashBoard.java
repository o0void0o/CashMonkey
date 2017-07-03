package com.koostech.cashmonkey;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.koostech.cashmonkey.db.FakeDataProvider;
import com.koostech.cashmonkey.model.Budget;
import com.koostech.cashmonkey.model.Category;
import com.koostech.cashmonkey.model.Tran;
import com.koostech.cashmonkey.model.Wallet;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        ImageView icIcon =(ImageView)toolbar.getChildAt(1);
        Toolbar.LayoutParams lp=(Toolbar.LayoutParams)(icIcon.getLayoutParams());
        lp.topMargin=0;
        lp.leftMargin=0;
        icIcon.setLayoutParams(lp);
        DateTime now = new DateTime();
        DateTime end=new DateTime();
        end= end.withYear(2017).withDayOfMonth(24).withMonthOfYear(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
        DateTime begin = null;

       now = now.withTimeAtStartOfDay();





        Wallet activeWalet = FakeDataProvider.getAllWallets();
        ArrayList<Budget> budgets = FakeDataProvider.getBudgets(now,end);

        HashMap<Long, Double> monthsTransHM = new HashMap<>();
        HashMap<Long, ArrayList<Budget> >bugetForCats = new HashMap<>();


        Double totalBudgetamount = 0d;
        Double totalBudgetRemaining = 0d;
        Double otherSpent = 0d;


        Collections.sort(budgets, new Comparator<Budget>() {
            @Override
            public int compare(Budget o1, Budget o2) {
                if (o1.getDateTimeStart().isBefore(o2.getDateTimeStart())) {
                    return -1;
                } else {
                    return 1;
                }

            }
        });

        double plexCarryOver = 0d;
        for (Budget b : budgets) {

                totalBudgetRemaining += b.getAmountRemaining();

                if (bugetForCats.get(b.getCat()) == null) {

                    ArrayList<Budget>bugets4car=new ArrayList<Budget>();
                    bugets4car.add(b);
                    bugetForCats.put(b.getCat(),bugets4car);
                }else{
                    bugetForCats.get(b.getCat()).add(b);
                }
            }

     //if weekly
        ArrayList<Budget> weeklyBudgets = FakeDataProvider.getBudgets(now,now.withDayOfWeek(7).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59));

        Double budgetRunner = 0d;
        for ( Budget b :weeklyBudgets) {


            int days = Days.daysBetween(b.getDateTimeStart(), b.getDateTimeEnd().plusDays(1).withTimeAtStartOfDay()).getDays();
            Double daylyTarget = (b.getAmount() / days);
            int currentNumDays = Days.daysBetween(b.getDateTimeStart(), now).getDays();


            Double shouldHaveused = daylyTarget * currentNumDays;

            Double tolatBudgetUsed = b.getAmount() - b.getAmountRemaining();
            budgetRunner += (shouldHaveused - tolatBudgetUsed);

        }
        System.out.print(weeklyBudgets);
        ArrayList<Tran> weeksTrans = FakeDataProvider.getTransactions(now.withDayOfWeek(1).withTimeAtStartOfDay().toDate().getTime(), now.withDayOfWeek(7).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999).toDate().getTime());

        Double expenses = 0d;
        Double incomes = 0d;

        for (Tran t : weeksTrans) {
            if (t.isDebit()) {
                expenses += t.getAmount();


            } else {
                incomes += t.getAmount();
            }

        }


        TextView expensesTextView = (TextView) findViewById(R.id.dash_totalExpenses);
        expensesTextView.setText("R " + String.valueOf(expenses));

//
        TextView budgettingFlex = (TextView) findViewById(R.id.dash_bugdet_flex);
        budgettingFlex.setText("R " + String.valueOf(budgetRunner));
//
//
        TextView incomeTextView = (TextView) findViewById(R.id.dash_total_income);
        incomeTextView.setText("R " + String.valueOf(incomes));

        TextView oping = (TextView) findViewById(R.id.dash_openingBalance);
        oping.setText("R " + String.valueOf(activeWalet.getBalance() + expenses - incomes));

        TextView ending = (TextView) findViewById(R.id.dash_end_balance);
        ending.setText("R " + String.valueOf(activeWalet.getBalance()));
        TextView flex = (TextView) findViewById(R.id.dash_flex);
        flex.setText("R " + String.valueOf((activeWalet.getBalance() - (totalBudgetRemaining))));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the budgetMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
