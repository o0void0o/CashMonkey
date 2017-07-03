package com.koostech.cashmonkey;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TextView;

import com.koostech.cashmonkey.db.FakeDataProvider;
import com.koostech.cashmonkey.model.Budget;
import com.koostech.cashmonkey.model.Category;
import com.koostech.cashmonkey.model.Tran;
import com.koostech.cashmonkey.model.Wallet;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateBudget extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Menu budgetMenu;
    Budget budget;
    boolean editMode;
    TextView amountTF;
    TextView budgetStartPeriodTF;
    TextView budgetEndPeriodTF;
    TextView budgetFundingWalletTF;
    TextView budgetSelectedCateory;
    Calendar budgetStartDate;
    Calendar budgetEndDate;
    Calendar c;
    TextView periodEndDate;
    ArrayAdapter adapter;
    Wallet wallet;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);

        if (this.getIntent().getExtras() != null) {
            budget = FakeDataProvider.getBudgetByID((Long) (this.getIntent().getExtras().get("p1")));
            editMode = (boolean) (this.getIntent().getExtras().get("p2"));
        }
        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().get("p1") != null) {
            budget = FakeDataProvider.getBudgetByID((Long) (this.getIntent().getExtras().get("p1")));
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_create_budget_title);

        budgetStartPeriodTF = (TextView) findViewById(R.id.budgetStartPeriodTF);
        budgetEndPeriodTF = (TextView) findViewById(R.id.budgetEndPeriodTF);

        LinearLayout budgetStartPeriodLL = (LinearLayout) findViewById(R.id.budgetStartPeriodLL);
        budgetStartPeriodLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (budget == null || editMode) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            CreateBudget.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }
            }
        });
        LinearLayout budgetEndPeriodLL = (LinearLayout) findViewById(R.id.budgetEndPeriodLL);
        budgetEndPeriodLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (budget == null || editMode) {
                    Calendar now = Calendar.getInstance();
                    Bundle b = new Bundle();
                    b.putString("type", "budgetEndPeriodTF");
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            CreateBudget.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)

                    );
                    dpd.setArguments(b);
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }
            }
        });

        budgetFundingWalletTF = (TextView) findViewById(R.id.budget_funding_wallet);
        LinearLayout walletLL = (LinearLayout) findViewById(R.id.ll_wallet);
        walletLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (budget == null || editMode) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(CreateBudget.this);
                    View walletDialogTitle = (LayoutInflater.from(dialog.getContext()).inflate(R.layout.wallet_list_with_flex, null));

                    periodEndDate = (TextView) walletDialogTitle.findViewById(R.id.walletTF);
                    periodEndDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Calendar now = Calendar.getInstance();
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "walletDialogItem");
                            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                                    CreateBudget.this,
                                    now.get(Calendar.YEAR),
                                    now.get(Calendar.MONTH),
                                    now.get(Calendar.DAY_OF_MONTH)

                            );
                            datePickerDialog.setArguments(bundle);
                            datePickerDialog.show(getFragmentManager(), "Datepickerdialog");


                        }
                    });

                    dialog.setCustomTitle(walletDialogTitle);
                    final ArrayList<Wallet> wallets = FakeDataProvider.getWallets();
                    adapter = new ArrayAdapter(getApplicationContext(), R.layout.koos, wallets) {
                        ViewHolder holder;
                        Drawable icon;

                        class ViewHolder {
                            ImageView icon;
                            TextView title;
                        }


                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            if (convertView == null) {
                                convertView = inflater.inflate(R.layout.koos, null);
                                holder = new ViewHolder();
                                holder.icon = (ImageView) convertView.findViewById(R.id.picture);
                                holder.title = (TextView) convertView.findViewById(R.id.title);
                                convertView.setTag(holder);
                            } else {
                                holder = (ViewHolder) convertView.getTag();
                            }

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_income_grayblue_card_black);
                            holder.title.setText(wallets.get(position).getName());
                            holder.icon.setImageResource(wallets.get(position).getIcon());
                            return convertView;
                        }

                        ;


                    };
                    dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            budgetFundingWalletTF.setText(wallets.get(which).getName());
                            ImageView budgetFundingWalletIcon = (ImageView) findViewById(R.id.budget_funding_wallet_icon);
                            budgetFundingWalletIcon.setImageResource(wallets.get(which).getIcon());
                            wallet = wallets.get(which);
                        }
                    });


                    dialog.show();
                }
            }
        });


        final TextView categoryTF = (TextView) findViewById(R.id.category);

        LinearLayout ll_Cat = (LinearLayout) findViewById(R.id.ll_cat);
        ll_Cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (budget == null || editMode) {
                    AlertDialog.Builder b = new AlertDialog.Builder(CreateBudget.this);
                    // b.setIcon(R.drawable.all_wallets);
                    b.setTitle("Frequently Used");

                    final ArrayList<Category> cats = FakeDataProvider.getTop5Categories();
                    ListAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.category_dialog_item, cats) {

                        ViewHolder holder;
                        Drawable icon;

                        class ViewHolder {
                            ImageView icon;
                            TextView title;


                        }


                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            if (convertView == null) {
                                convertView = inflater.inflate(R.layout.category_dialog_item, null);
                                holder = new ViewHolder();
                                holder.icon = (ImageView) convertView.findViewById(R.id.picture);
                                holder.title = (TextView) convertView.findViewById(R.id.title);
                                convertView.setTag(holder);
                            } else {
                                holder = (ViewHolder) convertView.getTag();
                            }

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_category_family);
                            holder.title.setText(cats.get(position).getName());

                            holder.icon.setImageResource(getResources().getIdentifier(cats.get(position).getIcon(), "drawable", getPackageName()));
                            return convertView;
                        }

                        ;


                    };
                    b.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ImageView koos = (ImageView) findViewById(R.id.budget_selected_category_icon);


                            koos.setImageResource(getResources().getIdentifier(cats.get(which).getIcon(), "drawable", getPackageName()));
                            category = cats.get(which);

                            TextView e2e = (TextView) findViewById(R.id.budget_selected_category);
                            e2e.setText(cats.get(which).getName());


                        }
                    });


                    b.show();
                }
            }
        });


        amountTF = (TextView) findViewById(R.id.amount);

        if (budget != null) {
            amountTF.setText(String.valueOf(budget.getAmount()));
        }
        amountTF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (budget == null || editMode) {
                    Intent starter = new Intent(CreateBudget.this, Calculator.class);
                    Bundle b = new Bundle();
                    b.putString("p1", amountTF.getText().toString().replace("R", "").trim());
                    starter.putExtras(b);
                    startActivityForResult(starter, 10);

                }
            }

        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Bundle bundle = view.getArguments();
        String value = null;
        if (bundle != null) {
            value = bundle.getString("type");
        }

        if (bundle == null) {
            budgetStartPeriodTF.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));
            budgetStartDate = Calendar.getInstance();
            budgetStartDate.set(Calendar.YEAR, year);
            budgetStartDate.set(Calendar.MONTH, monthOfYear);
            budgetStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        } else if (value.equals("budgetEndPeriodTF")) {


            budgetEndPeriodTF.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));
            budgetEndDate = Calendar.getInstance();
            budgetEndDate.set(Calendar.YEAR, year);
            budgetEndDate.set(Calendar.MONTH, monthOfYear);
            budgetEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        } else if (value.equals("walletDialogItem")) {


            adapter.clear();
            ArrayList<Wallet> wallets = FakeDataProvider.getWallets();

            adapter.addAll(wallets);

            adapter.notifyDataSetChanged();

            periodEndDate.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));
            c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.budget_actions, menu);
        this.budgetMenu = menu;
        if (budget != null) {
            menu.findItem(R.id.budget_save).setVisible(false);

            if (editMode) {

                menu.findItem(R.id.budget_edit).setVisible(false);
                menu.findItem(R.id.budget_delete).setVisible(true);
            } else {
                menu.findItem(R.id.budget_update).setVisible(false);
            }


        } else {
            menu.findItem(R.id.budget_edit).setVisible(false);
            menu.findItem(R.id.budget_update).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        budgetMenu.findItem(R.id.budget_delete).setVisible(false);
        Bundle b = new Bundle();
        if (item.getItemId() == R.id.budget_delete) {
            editMode = false;
            Intent i = getIntent();
            i.putExtra("p1", budget);
            i.putExtra("p2", "delete");
            setResult(Activity.RESULT_OK, i);
            budget.delete();
            finish();
            return true;
        } else if (item.getItemId() == R.id.budget_update) {
            editMode = false;
            Intent i = getIntent();
            i.putExtra("p1", budget);
            i.putExtra("p2", "update");
            setResult(Activity.RESULT_OK, i);

            try {
                budget.setAmount(Double.parseDouble(amountTF.getText().toString().replace("R", "").trim()));
                if (budget.getAmount() == 0d) {
                    Snackbar s = Snackbar.make(findViewById(R.id.main_content), "Amount is empty.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    s.getView().setBackgroundColor(getResources().getColor(R.color.expense_Red_Desaturaed));
                    s.show();
                    return true;
                }


            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.main_content), "Amount invalid.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
            if (category == null) {
                Snackbar.make(findViewById(R.id.main_content), "Category is empty.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
            budget.setCat(category.getId());
            budget.setStart(budgetStartDate.getTime().getTime());
            budget.setWallet(wallet.getId());
            budget.setEnd(budgetEndDate.getTime().getTime());
            budget.save();
            finish();
            return true;
        } else if (item.getItemId() == R.id.budget_edit) {
            editMode = true;
            budgetMenu.findItem(R.id.budget_update).setVisible(true);
            budgetMenu.findItem(R.id.budget_edit).setVisible(false);
            budgetMenu.findItem(R.id.budget_delete).setVisible(true);
            Snackbar s = Snackbar.make(findViewById(R.id.main_content), "Edit mode.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            s.show();
        } else if (item.getItemId() == R.id.budget_save) {


            Budget t = new Budget();

            try {
                t.setAmount(Double.parseDouble(amountTF.getText().toString().replace("R", "").trim()));
                if (t.getAmount() == 0d) {
                    Snackbar s = Snackbar.make(findViewById(R.id.main_content), "Amount is empty.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    s.getView().setBackgroundColor(getResources().getColor(R.color.expense_Red_Desaturaed));
                    s.show();
                    return true;
                }


            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.main_content), "Amount invalid.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
            if (category == null) {
                Snackbar.make(findViewById(R.id.main_content), "Category is empty.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
            t.setCat(category.getId());
            t.setStart(new DateTime(budgetStartDate.getTime()).withTimeAtStartOfDay().getMillis());
            t.setEnd(new DateTime(budgetEndDate.getTime()).withTimeAtStartOfDay().getMillis());
            t.setWallet(wallet.getId());
            t.setAmountRemaining(t.getAmount());


            t.save();

            Intent i = getIntent();
            i.putExtra("p1", t);
            i.putExtra("p2", "save");
            setResult(Activity.RESULT_OK, i);

            finish();
            return true;
        }
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // ff.setExpanded(true);
//                    final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
//
//                    Glide.with(this).load(mCurrentPhotoPath).centerCrop().into(imageView);
                    //Convert file path into bitmap image using below line.
                    // yourSelectedImage = BitmapFactory.decodeFile(filePath);


                    //put bitmapimage in your imageview
                    //yourimgView.setImageBitmap(yourSelectedImage);
                }
                break;

            case 10:
                if (resultCode == RESULT_OK) {

                    amountTF.setText(String.valueOf(imageReturnedIntent.getExtras().get("p1")));

                }
                break;
        }

    }

}
