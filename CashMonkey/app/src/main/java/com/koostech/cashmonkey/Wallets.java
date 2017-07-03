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

import com.koostech.cashmonkey.db.FakeDataProvider;
import com.koostech.cashmonkey.model.Bill;
import com.koostech.cashmonkey.model.Wallet;

import java.util.ArrayList;
import java.util.List;

public class Wallets extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private NestedScrollView sv;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallets);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Wallets");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent starter = new Intent(Wallets.this, CreateWallet.class);
                startActivity(starter);
            }
        });




        WalletRecyclerAdapter adapter = new WalletRecyclerAdapter(FakeDataProvider.getWallets());
        mRecyclerView.setAdapter(adapter);
        sv = (NestedScrollView) findViewById(R.id.scroll_view);
//        fab.
//        sv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0) {
//                    fab.hide();
//                } else if (dy < 0) {
//                    fab.show();
//                }
//            }
//        });

        sv.smoothScrollTo(0, 0);

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
