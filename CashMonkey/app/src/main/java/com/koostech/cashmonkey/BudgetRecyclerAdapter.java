package com.koostech.cashmonkey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koostech.cashmonkey.db.FakeDataProvider;
import com.koostech.cashmonkey.model.Bill;
import com.koostech.cashmonkey.model.Budget;
import com.koostech.cashmonkey.model.Category;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by eswanepo1 on 11/10/2016.
 */

public class BudgetRecyclerAdapter extends RecyclerView.Adapter<BudgetRecyclerAdapter.BudgetViewHolder> {
    HashMap<Category, ArrayList<Budget>> budgets;
    Activity con;

    BudgetRecyclerAdapter(HashMap<Category, ArrayList<Budget>> budgets, Activity con) {

        this.con = con;
        this.budgets = budgets;

    }

    @Override
    public BudgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_card, parent, false);
        BudgetViewHolder pvh = new BudgetViewHolder(v);
        return pvh;
    }

//    private void setFadeAnimation(View view) {
//        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(1000);
//        view.startAnimation(anim);
//    }

    @Override
    public void onBindViewHolder(BudgetViewHolder holder, int position) {
        ArrayList<Category> clist = new ArrayList<>();
        for (Category c : budgets.keySet()) {
            clist.add(c);
        }
        ArrayList<Budget> bs = budgets.get((clist.get(position)));

        Double total = 0d;
        for (final Budget b : bs) {

            if (b.getAmountRemaining() > 0) {
                total += b.getAmountRemaining();
            }
            View koos = LayoutInflater.from(holder.ll.getContext()).inflate(R.layout.budget_card_item, holder.ll, false);


            holder.budgetIcon.setImageResource(koos.getResources().getIdentifier(clist.get(position).getIcon(), "drawable", con.getPackageName()));

            TextView budgetRemaining = (TextView) koos.findViewById(R.id.budget_remaining);

            budgetRemaining.setText("R " + String.format("%.2f", (b.getAmountRemaining())));
            ProgressBar budgetProgressDays = (ProgressBar) koos.findViewById(R.id.progressBar2);
            ProgressBar budgetProgress = (ProgressBar) koos.findViewById(R.id.progressBar);
            TextView budgetTimespan = (TextView) koos.findViewById(R.id.budget_timespan);
            TextView budgetTimespanRemaining = (TextView) koos.findViewById(R.id.budget_days_left);

//
            holder.budgetName.setText((clist.get(position).getName()));

            String startDate = b.getDateTimeStart().getDayOfMonth() + " " + new SimpleDateFormat("MMM").format(b.getDateTimeStart().toDate());
            String endDate = b.getDateTimeEnd().getDayOfMonth() + " " + new SimpleDateFormat("MMM").format(b.getDateTimeEnd().toDate());
            int daysmax = (Days.daysBetween(b.getDateTimeStart(), b.getDateTimeEnd()).getDays()) + 1;
            Days days = Days.daysBetween(b.getDateTimeStart(), b.getDateTimeEnd());

            budgetTimespan.setText(startDate + " - " + endDate);

//
            budgetProgress.setMax(100);
            Long progress = Math.round(100 - b.getAmountRemaining() / b.getAmount() * 100);

            budgetProgress.setProgress(progress.intValue());

            budgetProgressDays.setMax(daysmax);
            int progresss = Days.daysBetween(b.getDateTimeStart(), new DateTime()).getDays();

            budgetProgressDays.setProgress(progresss);
            //  budgetProgress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            if (b.getDateTimeStart().isBefore(new DateTime())) {

                if ((daysmax - progresss) == 1) {
                    budgetTimespanRemaining.setText("Last Day");
                } else {
                    budgetTimespanRemaining.setText((daysmax - progresss) + " Days Remaining");
                }

            } else {
                ((LinearLayout) budgetTimespanRemaining.getParent()).removeAllViews();
                ((LinearLayout) budgetProgressDays.getParent()).removeView(budgetProgressDays);


            }

            LinearLayout lll = (LinearLayout) (koos.findViewById(R.id.uyg));
            lll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent starter = new Intent(view.getContext(), ViewBudget.class);
                    starter.putExtra("p1", b.getId());

                    (con).startActivityForResult(starter, 2222);
                }

            });
            holder.ll.addView(koos);
        }

        holder.kkk.setText("R " + String.format("%.2f", total));


    }

    @Override
    public int getItemCount() {
        if (budgets == null) {
            return 0;
        } else {
            return budgets.size();
        }
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {

        TextView budgetName;

        TextView budgetTimespan;
        TextView budgetTimespanRemaining;
        ImageView budgetIcon;
        ProgressBar budgetProgress;
        LinearLayout ll;
        TextView kkk;
        LinearLayout topll;

        BudgetViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView.findViewById(R.id.budgetLLhook);
            kkk = (TextView) itemView.findViewById(R.id.kkk);

            budgetIcon = (ImageView) itemView.findViewById(R.id.budget_icon);
            budgetName = (TextView) itemView.findViewById(R.id.budget_name);


        }
    }
}


