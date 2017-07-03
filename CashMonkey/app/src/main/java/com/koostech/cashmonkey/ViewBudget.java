package com.koostech.cashmonkey;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koostech.cashmonkey.db.FakeDataProvider;
import com.koostech.cashmonkey.dummy.DummyContent;
import com.koostech.cashmonkey.model.Budget;
import com.koostech.cashmonkey.model.Tran;
import com.koostech.cashmonkey.model.Wallet;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ViewBudget extends AppCompatActivity {
    Budget budget;
    TextView budgetAmountRemaing;
    TextView budgetAmount;
    TextView budgetPeriodAmount;
    TextView view_budget_period_remaining;
    ProgressBar budgetProgressDays;
    ProgressBar budgetProgress;
    ArrayList<Tran> transactions;
    CardView card;
    LinearLayout poepies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budget);
        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().get("p1") != null) {
            budget = FakeDataProvider.getBudgetByID((Long) (this.getIntent().getExtras().get("p1")));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        transactions = FakeDataProvider.getBudgetsTransactionsByCat(budget);
        Double spent = 0d;
        for (Tran t : transactions) {
            spent += t.getAmount();
        }


        view_budget_period_remaining = (TextView) findViewById(R.id.view_budget_period_remaining);
        budgetAmountRemaing = (TextView) findViewById(R.id.budget_view_amount_remaining);
        budgetAmountRemaing.setText("R " + String.format("%.2f", (budget.getAmountRemaining())));
        if (budget.getAmountRemaining() < 0) {
            budgetAmountRemaing.setTextColor(getResources().getColor(R.color.expense_Red_Desaturaed));
        }
        LinearLayout topLL = (LinearLayout) findViewById(R.id.budget_list_ll);
        card = (CardView) findViewById(R.id.budget_trans_card);
//poepies=(ListView) findViewById(R.id.poepies);
//        if (transactions.size() >= 2) {
//
//            poepies.setMinimumHeight(300);
//
//
//        } else if (transactions.size() <= 1) {
//            poepies.setMinimumHeight(100);
//
//        } else {
//            poepies.setVisibility(View.GONE);
//
//        }
        String startDate = budget.getDateTimeStart().getDayOfMonth() + " " + new SimpleDateFormat("MMM").format(budget.getDateTimeStart().toDate());
        String endDate = budget.getDateTimeEnd().getDayOfMonth() + " " + new SimpleDateFormat("MMM").format(budget.getDateTimeEnd().toDate());
        int daysmax = (Days.daysBetween(budget.getDateTimeStart(), budget.getDateTimeEnd()).getDays()) + 1;
        Days days = Days.daysBetween(budget.getDateTimeStart(), budget.getDateTimeEnd());

        getSupportActionBar().setTitle("");
        budgetProgressDays = (ProgressBar) findViewById(R.id.progressBar2);
        budgetProgress = (ProgressBar) findViewById(R.id.progressBar);
        budgetProgress.setMax(100);
        Long progress = Math.round(100 - budget.getAmountRemaining() / budget.getAmount() * 100);

        budgetProgress.setProgress(progress.intValue());

        budgetProgressDays.setMax(daysmax);
        budgetProgressDays.setProgress((Days.daysBetween(budget.getDateTimeStart(), new DateTime()).getDays()));

        int daysBetween = Days.daysBetween(budget.getDateTimeStart(), new DateTime()).getDays();
        int days2 = daysmax - daysBetween;

        if (budget.getDateTimeStart().isBefore(new DateTime())) {

            if (days2 == 1) {
                view_budget_period_remaining.setText("Last Day");

            } else {
                view_budget_period_remaining.setText(days2 + " Days Remaining");
            }


            Double dailyUse = (spent / (daysBetween + 1));
            TextView view_budget_current_daily_use = (TextView) findViewById(R.id.view_budget_current_daily_use);
            view_budget_current_daily_use.setText("R " + String.format("%.2f", dailyUse));
            TextView view_budget_safe_amount = (TextView) findViewById(R.id.view_budget_safe_amount);
            TextView budget_status = (TextView) findViewById(R.id.budget_status);
            if (budget.getAmountRemaining() >= 0) {
                Double safeAmount = (budget.getAmountRemaining() / days2);
                if (dailyUse > (safeAmount)) {
                    view_budget_current_daily_use.setTextColor(getResources().getColor(R.color.expense_Red_Desaturaed));
                    budget_status.setText("CAUTION");
                    budget_status.setTextColor(getResources().getColor(R.color.yellow_caution));

                } else {

                    budget_status.setText("GOOD");
                    budget_status.setTextColor(getResources().getColor(R.color.income_Green_Desaturaed));
                }
                view_budget_safe_amount.setText("R " + String.format("%.2f", safeAmount));
            } else {
                view_budget_safe_amount.setText("R 0.00");
                view_budget_current_daily_use.setTextColor(getResources().getColor(R.color.expense_Red_Desaturaed));
                budget_status.setText("NOT GOOD");
                budget_status.setTextColor(getResources().getColor(R.color.expense_Red_Desaturaed));


            }

        } else {
            view_budget_period_remaining.setText(daysmax + " Day Period");
            TextView view_budget_safe_amount = (TextView) findViewById(R.id.view_budget_safe_amount);
            if (budget.getAmountRemaining() > 0) {
                view_budget_safe_amount.setText("R " + String.format("%.2f", (budget.getAmountRemaining() / daysmax)));
            } else {
                view_budget_safe_amount.setText("R 0,00");
            }
            TextView view_budget_current_daily_use = (TextView) findViewById(R.id.view_budget_current_daily_use);
            view_budget_current_daily_use.setText("R 0.00");
        }


        TextView view_budget_peroid_amount = (TextView) findViewById(R.id.view_budget_peroid_amount);
        view_budget_peroid_amount.setText("R " + String.format("%.2f", (budget.getAmount())));

        if (transactions.size() > 0) {
            LinearLayout recyclerView = (LinearLayout) findViewById(R.id.poepies);


            for (Tran tran : transactions) {


                final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View koos = inflater.inflate(R.layout.budget_expenses_per_cat_listview_item, recyclerView, false);
                TextView desc = (TextView) koos.findViewById(R.id.bugdeted_cat_amount);
                desc.setText(String.valueOf(tran.getAmount()));
                recyclerView.addView(koos);

//            convertView = inflater.inflate(R.layout.budget_expenses_per_cat_listview_item, null);
//            holder = new ViewHolder();
//            holder.icon = (ImageView) convertView.findViewById(R.id.picture);
//            holder.title = (TextView) convertView.findViewById(R.id.title);
//            holder.bugdetedCatAmount = (TextView) convertView.findViewById(R.id.bugdeted_cat_amount);
//            holder.budget_cat_tran_date = (TextView) convertView.findViewById(R.id.budget_cat_tran_date);
//            holder.budget_cat_tran_desc = (TextView) convertView.findViewById(R.id.budget_cat_tran_desc);
//            holder.budget_cat_tran_fullname = (TextView) convertView.findViewById(R.id.budget_cat_tran_fullname);
//            holder.lll = (LinearLayout) convertView.findViewById(R.id.megshit);


            }
        }else{
            topLL.removeView(card);
        }

//        ListView grootpoep = (ListView) findViewById(R.id.grootpoep);
//        grootpoep.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the budgetMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_budget_actions, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1111:
                if (resultCode == RESULT_OK) {
                    Intent i = getIntent();
                    i.putExtra("p1", budget);
                    i.putExtra("p2", "delete");
                    setResult(Activity.RESULT_OK, i);

                    finish();
                }
        }

        System.out.print("kk");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent starter = new Intent(this.getBaseContext(), CreateBudget.class);
        starter.putExtra("p1", budget.getId());
        starter.putExtra("p2", true);
        startActivityForResult(starter, 1111);

        return true;
    }
}
