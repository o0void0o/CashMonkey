package com.koostech.cashmonkey;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.koostech.cashmonkey.db.FakeDataProvider;
import com.koostech.cashmonkey.model.Bill;
import com.koostech.cashmonkey.model.Budget;
import com.koostech.cashmonkey.model.Category;
import com.koostech.cashmonkey.model.Wallet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Budgets extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    BudgetRecyclerAdapter adapter;
    private NestedScrollView sv;
    FloatingActionButton fab;
    Wallet wallet;
    HashMap<Category, ArrayList<Budget>> budgets;
    TextView budget_total;
    TextView wallet_budget_total;
    TextView flex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgets);
        wallet = FakeDataProvider.getAciveWallet();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        budget_total = (TextView) findViewById(R.id.budget_total);
        wallet_budget_total = (TextView) findViewById(R.id.budget_wallet_balance);
        wallet_budget_total.setText("R " + String.format("%.2f",wallet.getBalance()));
        flex = (TextView) findViewById(R.id.flex);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent starter = new Intent(Budgets.this, CreateBudget.class);
                startActivityForResult(starter, 3333);
            }
        });
        budgets = FakeDataProvider.getActiveBudgets();
        Double total = 0d;
        for (Category key : budgets.keySet()) {

            for (Budget b : budgets.get(key)) {
                if (b.getAmountRemaining() > 0) {
                    total += b.getAmountRemaining();
                }
            }

        }
        budget_total.setText("- R " + String.format("%.2f", ( total)));
        adapter = new BudgetRecyclerAdapter(budgets, this);
        mRecyclerView.setAdapter(adapter);
        sv = (NestedScrollView) findViewById(R.id.scroll_view);
        double flexamount = (wallet.getBalance() - total);

        if (flexamount >= 0) {
            flex.setText("R " + String.format("%.2f", (flexamount)));
        } else {
            flex.setText("- R " + String.format("%.2f", (-1* flexamount)));
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 2222:
                if (resultCode == RESULT_OK) {
                    budgets = FakeDataProvider.getActiveBudgets();
                    adapter = new BudgetRecyclerAdapter(budgets, this);
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            case 3333:
                if (resultCode == RESULT_OK) {
                    budgets = FakeDataProvider.getActiveBudgets();
                    adapter = new BudgetRecyclerAdapter(budgets, this);
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();
        return true;
    }
}
