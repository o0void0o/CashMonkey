package com.koostech.cashmonkey.dummy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koostech.cashmonkey.AddTransaction;
import com.koostech.cashmonkey.R;
import com.koostech.cashmonkey.db.FakeDataProvider;
import com.koostech.cashmonkey.model.Tran;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.PersonViewHolder> {
    List<Tran> transactions;
    HashMap<String, List<Tran>> hMap;
    ArrayList<String> listy = new ArrayList<String>();
    Context con;


    public TransactionRecyclerAdapter(ArrayList<Tran> transactions, Context con) {
        this.transactions = transactions;
        this.con = con;

        Collections.sort(transactions, new Comparator<Tran>() {
            @Override
            public int compare(Tran o1, Tran o2) {
                if (o1.getDate() > o2.getDate()) {
                    return -1;
                } else {
                    return 1;
                }

            }
        });
        hMap = new HashMap();

        for (Tran t : transactions) {


            if (hMap.get(t.getIndexDate()) == null) {
                List<Tran> transactionsThatDay = new ArrayList<Tran>();
                transactionsThatDay.add(t);
                listy.add(t.getIndexDate());
                hMap.put(t.getIndexDate(), transactionsThatDay);
            } else {
                hMap.get(t.getIndexDate()).add(t);
            }
        }
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_daily_card, parent, false);


        PersonViewHolder pvh = new PersonViewHolder(v, parent);
        return pvh;
    }


    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        if (position == 0) {
            if (holder.ll != null) {
                holder.ll.removeAllViews();


            }
        }

       Double total = 0.00d;

        for (final Tran t : hMap.get(listy.get(position))) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(t.getDate()));
            SimpleDateFormat daynumverForm = new SimpleDateFormat("dd");
            SimpleDateFormat dayForm = new SimpleDateFormat("EEEE");
            SimpleDateFormat monthYear = new SimpleDateFormat("MMMM yyyy");

            holder.transactionDailyDayNum.setText(daynumverForm.format(cal.getTime()));
            holder.transactionDailyDayName.setText(dayForm.format(cal.getTime()));
            holder.transactionDailyDayYearAndMonth.setText(monthYear.format(cal.getTime()));
            if (t.isDebit()) {
                total -= t.getAmount();


                View koos = LayoutInflater.from(holder.p.getContext()).inflate(R.layout.transaction_card_item_expense, holder.p, false);
                TextView desc = (TextView) koos.findViewById(R.id.transaction_card_desc);
                desc.setText(t.getDescription());


                holder.ll.addView(koos);
                TextView amount = (TextView) koos.findViewById(R.id.expense_amount);
                amount.setText("R " + String.format("%.2f", t.getAmount()));
                ImageView icon = (ImageView) koos.findViewById(R.id.overview_card_item_expense_icon);

//new Resources().get"R.drawable.icon_34"
                //   koos.getResources().

                icon.setImageResource(koos.getResources().getIdentifier(FakeDataProvider.getCategories().get(t.getCategoryId()).getIcon(), "drawable", con.getPackageName()));

                TextView category = (TextView) koos.findViewById(R.id.overview_card_item_expense_category_name);
                category.setText(FakeDataProvider.getCategories().get(t.getCategoryId()).getName());

                LinearLayout ll = (LinearLayout) koos.findViewById(R.id.poep);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent starter = new Intent(view.getContext(), AddTransaction.class);
                        starter.putExtra("p1", t);
                        starter.putExtra("p2", t.getId());
                        ((Activity) con).startActivityForResult(starter, 1111);
                    }

                });
            } else {
                total += t.getAmount();
                View koos = LayoutInflater.from(holder.p.getContext()).inflate(R.layout.overview_card_item_income, holder.p, false);
                holder.ll.addView(koos);
                TextView amount = (TextView) koos.findViewById(R.id.income_amount);
                amount.setText("R " + String.format("%.2f", t.getAmount()));
                ImageView icon = (ImageView) koos.findViewById(R.id.overview_card_item_income_icon);


                icon.setImageResource(koos.getResources().getIdentifier(FakeDataProvider.getCategories().get(t.getCategoryId()).getIcon(), "drawable", con.getPackageName()));
                TextView category = (TextView) koos.findViewById(R.id.overview_card_item_income_category_name);
                category.setText(FakeDataProvider.getCategories().get(t.getCategoryId()).getName());
                category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Snackbar.make(view, "clicked", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                });
            }


            TextView totalTop = (TextView) holder.itemView.findViewById(R.id.transaction_daily_amount);
            if (total < 0) {
                totalTop.setText("-R " + String.format("%.2f", total * -1));
                totalTop.setTextColor(ContextCompat.getColor(holder.p.getContext(), R.color.expense_Red_Desaturaed));

            } else {
                totalTop.setText("R " + String.format("%.2f", total));
                totalTop.setTextColor(ContextCompat.getColor(holder.p.getContext(), R.color.income_Green_Desaturaed));

            }
        }


//        holder.catname.setText(bills.get(position).getName());
//        holder.b.setText("PAY R "+String.valueOf(bills.get(position).getAmount()));
//        setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return hMap.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView transactionDailyDayNum;
        TextView transactionDailyDayName;
        TextView transactionDailyDayYearAndMonth;
        Button b;
        LinearLayout ll;
        ViewGroup p;
        View itemView;
        ArrayList<Tran> transactions = new ArrayList<Tran>();

        PersonViewHolder(View itemView, ViewGroup p) {
            super(itemView);
            this.itemView = itemView;
            this.p = p;
            this.ll = (LinearLayout) itemView.findViewById(R.id.lll);
            this.transactionDailyDayNum = (TextView) itemView.findViewById(R.id.transaction_daily_day);
            this.transactionDailyDayName = (TextView) itemView.findViewById(R.id.transaction_daily_day_name);
            this.transactionDailyDayYearAndMonth = (TextView) itemView.findViewById(R.id.transaction_daily_monthAndYear);


        }
    }
}


