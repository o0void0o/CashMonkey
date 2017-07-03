package com.koostech.cashmonkey.dummy;

;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koostech.cashmonkey.R;
import com.koostech.cashmonkey.db.FakeDataProvider;
import com.koostech.cashmonkey.model.Tran;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;


public class Fragment_one extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private NestedScrollView sv;
    private ArrayList<Tran> transactions;
    TransactionRecyclerAdapter adapter;
    public long start;
    public long end;

    public Fragment_one() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        start = getArguments().getLong("start");
        end = getArguments().getLong("end");
        setRetainInstance(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showSnackBar(String msg) {
        if (sv != null) {
            Snackbar.make(sv, msg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void refresh() {
        if (this.v != null) {
            this.mRecyclerView = ((RecyclerView) this.v.findViewById(R.id.recyclerView));
            this.mLinearLayoutManager = new LinearLayoutManager(this.v.getContext());
            this.mRecyclerView.setLayoutManager(this.mLinearLayoutManager);
            this.mRecyclerView.setNestedScrollingEnabled(false);
            this.sv = ((NestedScrollView) this.v.findViewById(R.id.scroll_view));
            this.adapter = new TransactionRecyclerAdapter(FakeDataProvider.getTransactions(start, end), getContext());
            this.mRecyclerView.setAdapter(this.adapter);
        }
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_one, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);


        // bills.add(b5);
        // bills.add(b3);
        ArrayList<Tran> transactions = FakeDataProvider.getTransactions(start, end);

        if (transactions.size() > 0) {
            adapter = new TransactionRecyclerAdapter(transactions, this.getContext());

            mRecyclerView.setAdapter(adapter);
        } else {
//
        }
        return v;
    }

}