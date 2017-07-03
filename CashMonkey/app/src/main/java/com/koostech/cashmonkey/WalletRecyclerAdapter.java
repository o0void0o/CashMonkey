package com.koostech.cashmonkey;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koostech.cashmonkey.model.Tran;
import com.koostech.cashmonkey.model.Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WalletRecyclerAdapter extends RecyclerView.Adapter<WalletRecyclerAdapter.PersonViewHolder> {
    List<Wallet> wallets;
    HashMap<Boolean, List<Wallet>> hMap;
    ArrayList<Boolean> listy = new ArrayList<Boolean>();


    WalletRecyclerAdapter(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
//        Collections.sort(wallets, new Comparator<Tran>() {
//            @Override
//            public int compare(Tran o1, Tran o2) {
//                if(o1.getDate()>o2.getDate()){
//                    return  -1;
//                }else{
//                    return 1;
//                }
//
//            }
//        });
        hMap = new HashMap();

        listy.add(false);
        listy.add(true);


        for (Wallet t : wallets) {
            if (hMap.get(t.isExcludeFromTotal()) == null) {
                hMap.put(t.isExcludeFromTotal(), new ArrayList<Wallet>());
            }
            hMap.get(t.isExcludeFromTotal()).add(t);

        }
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_card, parent, false);


        PersonViewHolder pvh = new PersonViewHolder(v, parent);
        return pvh;
    }


    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {

        Double total = 0.00d;
        if (hMap.get(listy.get(position)) != null) {
            for (Wallet t : hMap.get(listy.get(position))) {

//            Calendar cal= Calendar.getInstance();
//            cal.setTime(new Date(t.getDate()));
//            SimpleDateFormat daynumverForm=new SimpleDateFormat("dd");
//            SimpleDateFormat dayForm=new SimpleDateFormat("EEEE");
//            SimpleDateFormat monthYear=new SimpleDateFormat("MMMM yyyy");
//
//            holder.transactionDailyDayNum.setText(daynumverForm.format(cal.getTime()));
//            holder.transactionDailyDayName.setText(dayForm.format(cal.getTime()));
//            holder.transactionDailyDayYearAndMonth.setText(monthYear.format(cal.getTime()));
                if (t.isExcludeFromTotal()) {
                    holder.topName.setText("Exluded");
                    total += t.getBalance();

                    View koos = null;
                    if(t.getBalance()<0) {
                        koos = LayoutInflater.from(holder.p.getContext()).inflate(R.layout.wallet_card_item_negative, holder.p, false);
                    }else{
                        koos = LayoutInflater.from(holder.p.getContext()).inflate(R.layout.wallet_card_item_positive, holder.p, false);
                    }
                    holder.ll.addView(koos);
                    TextView amount = (TextView) koos.findViewById(R.id.wallet_card_item_income_amount);
                    amount.setText("R " + String.format("%.2f", t.getBalance()));

                    TextView name = (TextView) koos.findViewById(R.id.wallet_card_item_income_name);
                    name.setText(t.getName());
                    ImageView icon = (ImageView) koos.findViewById(R.id.wallet_card_item_income_icon);
                    icon.setImageResource(t.getIcon());
                    ImageView walletCardIcon = (ImageView) holder.itemView.findViewById(R.id.wallet_card_icon);
                    walletCardIcon.setImageResource(R.drawable.all_wallets_hidden);
//                TextView category=(TextView) koos.findViewById(R.id.overview_card_item_expense_category_name);
//                category.setText(FakeDataProvider.getCategories().get(t.getCategoryId()).getName());
                } else {
                    total += t.getBalance();
                    holder.topName.setText("All Wallets");
                    View koos=null;
if(t.getBalance()<0) {
    koos = LayoutInflater.from(holder.p.getContext()).inflate(R.layout.wallet_card_item_negative, holder.p, false);
}else{
    koos = LayoutInflater.from(holder.p.getContext()).inflate(R.layout.wallet_card_item_positive, holder.p, false);
}
                    holder.ll.addView(koos);
                    TextView name = (TextView) koos.findViewById(R.id.wallet_card_item_income_name);
                    name.setText(t.getName());

                    TextView amount = (TextView) koos.findViewById(R.id.wallet_card_item_income_amount);
                    amount.setText("R " + String.format("%.2f", t.getBalance()));
                    ImageView icon = (ImageView) koos.findViewById(R.id.wallet_card_item_income_icon);
                    icon.setImageResource(t.getIcon());
                    ImageView walletCardIcon = (ImageView) holder.itemView.findViewById(R.id.wallet_card_icon);
                    walletCardIcon.setImageResource(R.drawable.all_wallets);
//                TextView category=(TextView) koos.findViewById(R.id.overview_card_item_income_category_name);
//                category.setText(FakeDataProvider.getCategories().get(t.getCategoryId()).getName());
                }
//
//
                TextView totalTop = (TextView) holder.itemView.findViewById(R.id.wallet_card_total_amount);
                if (total < 0) {
                    totalTop.setText("-R " + String.format("%.2f", total * -1));
                    totalTop.setTextColor(ContextCompat.getColor(holder.p.getContext(), R.color.expense_Red_Desaturaed));

                } else {
                    totalTop.setText("R " + String.format("%.2f", total));
                    totalTop.setTextColor(ContextCompat.getColor(holder.p.getContext(), R.color.income_Green_Desaturaed));

                }
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
        TextView topName;
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
            this.ll = (LinearLayout) itemView.findViewById(R.id.wallet_ll);
            this.topName = (TextView) itemView.findViewById(R.id.wallet_card_name);
//            this.transactionDailyDayName = (TextView) itemView.findViewById(R.id.transaction_daily_day_name);
//            this.transactionDailyDayYearAndMonth = (TextView) itemView.findViewById(R.id.transaction_daily_monthAndYear);


        }
    }
}


