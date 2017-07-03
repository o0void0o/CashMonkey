package com.koostech.cashmonkey;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.koostech.cashmonkey.model.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eswanepo1 on 11/10/2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PersonViewHolder> {
    List<Bill> bills;

    RecyclerAdapter(List bills) {
        this.bills = bills;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_card, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }



    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.catname.setText(bills.get(position).getName());
        holder.b.setText("PAY R "+String.valueOf(bills.get(position).getAmount()));
      
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView catname;
        Button b;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.bill_card);
            catname = (TextView) itemView.findViewById(R.id.bill_name);
            b = (Button) itemView.findViewById(R.id.pay_bill_button);


        }
    }
}


