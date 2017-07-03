package com.koostech.cashmonkey;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.koostech.cashmonkey.db.FakeDataProvider;
import com.koostech.cashmonkey.model.Budget;
import com.koostech.cashmonkey.model.Category;
import com.koostech.cashmonkey.model.Tran;
import com.koostech.cashmonkey.model.Wallet;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTransaction extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView gg;
    EditText description;
    Category selectedCat;
    Wallet wallet;
    TextView tranDate;
    Calendar c;
    String filePath;
    CoordinatorLayout cl;
    Tran transaction;
    AppBarLayout ff;
    Menu menu;
    boolean editmode;
    private Uri imageUri;
    LinearLayout catLL;

    private static final int REQ_CAMERA_IMAGE = 123;

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        tranDate.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().get("p1") != null) {
            transaction = (Tran) this.getIntent().getExtras().get("p1");
            transaction = (Tran) ((ArrayList) new Select().from(Tran.class).where("id==" + this.getIntent().getExtras().get("p2")).execute()).get(0);
            c = Calendar.getInstance();
            c.setTime(new Date(transaction.getDate()));

        } else {
            c = Calendar.getInstance();
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log Transaction");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null && savedInstanceState.get("mCurrentPhotoPath") != null) {
            mCurrentPhotoPath = savedInstanceState.getString("mCurrentPhotoPath");
        }



        catLL=(LinearLayout)findViewById(R.id.add_transaction_cat_ll);
        View koos = LayoutInflater.from(catLL.getContext()).inflate(R.layout.add_transaction_cat_normal, catLL, false);
       catLL.addView(koos);



//        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
//        if (mCurrentPhotoPath != null) {
//            Glide.with(this).load(mCurrentPhotoPath).centerCrop().into(imageView);
//        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isQuestionImage = true;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(AddTransaction.this.getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.e("photofile part", "Error: " + ex);
                    }

                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(AddTransaction.this,
                                "com.example.android.fileprovider", photoFile);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        }
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                            List<ResolveInfo> resolvedIntentActivities = AddTransaction.this
                                    .getPackageManager()
                                    .queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);

                            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                                String packageName = resolvedIntentInfo.activityInfo.packageName;

                                AddTransaction.this.grantUriPermission(packageName,
                                        photoURI,
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        }

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                        if (isQuestionImage)
                            startActivityForResult(takePictureIntent, 1);
                        else
                            startActivityForResult(takePictureIntent, 2);

                        //revoke permissions after use
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                            AddTransaction.this.revokeUriPermission(photoURI, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }

                    }
                }


            }
        });


        description = (EditText) findViewById(R.id.add_transaction_desc);
        if (transaction != null) {
            description.setText(transaction.getDescription());
        }
        gg = (TextView) findViewById(R.id.amount);

        gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (transaction == null || editmode) {
                    Intent starter = new Intent(AddTransaction.this, Calculator.class);
                    Bundle b = new Bundle();
                    b.putString("p1", gg.getText().toString().replace("R", "").trim());
                    starter.putExtras(b);
                    startActivityForResult(starter, 10);

                }
            }

        });

        if (transaction != null) {
            gg.setText("R " + transaction.getAmount());
        }

        final TextView ww = (TextView) findViewById(R.id.add_transaction_wallet);
        wallet = FakeDataProvider.getDefaultWallet();
        ImageView rkoos = (ImageView) findViewById(R.id.add_transaction_wallet_icon);
        rkoos.setImageResource(wallet.getIcon());

        ww.setText(wallet.getName());

        LinearLayout ll_wallet = (LinearLayout) findViewById(R.id.ll_wallet);
        ll_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction == null || editmode) {
                    AlertDialog.Builder b = new AlertDialog.Builder(AddTransaction.this);
                    b.setTitle("Wallets");

                    final ArrayList<Wallet> wallets = FakeDataProvider.getWallets();

                    ListAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.koos, wallets) {

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
                    b.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ww.setText(wallets.get(which).getName());
                            ImageView koos = (ImageView) findViewById(R.id.add_transaction_wallet_icon);
                            koos.setImageResource(wallets.get(which).getIcon());
                            wallet = wallets.get(which);
                        }
                    });


                    b.show();
                }
            }
        });


        final TextView ee = (TextView) findViewById(R.id.category);

        LinearLayout ll_Cat = (LinearLayout) findViewById(R.id.ll_cat);
        ll_Cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction == null || editmode) {
                    AlertDialog.Builder b = new AlertDialog.Builder(AddTransaction.this);
                    // b.setIcon(R.drawable.all_wallets);
                    b.setTitle("Frequently Used");

                    final ArrayList<Category> cats = FakeDataProvider.getTop5Categories();
                    ListAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.koos, cats) {

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

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_category_family);
                            holder.title.setText(cats.get(position).getName());


                            holder.icon.setImageResource(getResources().getIdentifier(cats.get(position).getIcon(),"drawable",getPackageName()));
                            return convertView;
                        }

                        ;


                    };
                    b.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ImageView koos = (ImageView) findViewById(R.id.add_transaction_cat_icon);


                            koos.setImageResource(getResources().getIdentifier(cats.get(which).getIcon(),"drawable",getPackageName()));
                            selectedCat = cats.get(which);
                            catLL.removeAllViews();
                            View fff = LayoutInflater.from(catLL.getContext()).inflate(R.layout.add_transaction_cat_detail, catLL, false);
                            catLL.addView(fff);
                            TextView e2e = (TextView) findViewById(R.id.catpoep);
                            e2e.setText(cats.get(which).getName());



                        }
                    });


                    b.show();
                }
            }
        });

        if (transaction != null) {
            selectedCat = FakeDataProvider.getCategoryById(transaction.getCategoryId());
            ee.setText(selectedCat.getName());

            ImageView ee_icon = (ImageView) findViewById(R.id.add_transaction_cat_icon);



            ee_icon.setImageResource(getResources().getIdentifier(selectedCat.getIcon(),"drawable",getPackageName()));
        }
        tranDate = (TextView) findViewById(R.id.tranDate);
        LinearLayout ll_date = (LinearLayout) findViewById(R.id.budgetStartPeriodLL);
        ll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (transaction == null || editmode) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            AddTransaction.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }
            }
        });

        if (transaction != null) {
            Calendar now = Calendar.getInstance();
            Calendar then = Calendar.getInstance();
            then.setTime(new Date(transaction.getDate()));

            if (then.get(Calendar.YEAR) == now.get(Calendar.YEAR) && then.get(Calendar.MONTH) == now.get(Calendar.MONTH) && then.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
                tranDate.setText("Today");
            } else {
                tranDate.setText(String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(then.get(Calendar.MONTH) + 1) + "/" + String.valueOf(then.get(Calendar.YEAR)));
            }

        }


    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = "koos";
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menu.findItem(R.id.transaction_delete).setVisible(false);
        Bundle b = new Bundle();
        if (item.getItemId() == R.id.transaction_delete) {
            editmode = false;
            Intent i = getIntent();
            i.putExtra("p1", transaction);
            i.putExtra("p2", "delete");
            setResult(Activity.RESULT_OK, i);
            transaction.deleteTransaction();
            finish();
            return true;
        } else if (item.getItemId() == R.id.transaction_update) {
            editmode = false;
            Intent i = getIntent();
            i.putExtra("p1", transaction);
            i.putExtra("p2", "update");
            setResult(Activity.RESULT_OK, i);
            transaction.setDescription(description.getText().toString());
            try {
                transaction.setAmount(Double.parseDouble(gg.getText().toString().replace("R", "").trim()));
                if (transaction.getAmount() == 0d) {
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
            if (selectedCat == null) {
                Snackbar.make(findViewById(R.id.main_content), "Category is empty.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
            transaction.setCategoryId(selectedCat.getId());
            transaction.setDate(c.getTime().getTime());
            transaction.setWalletId(wallet.getId());
            transaction.setDebit(selectedCat.isDebit());
            transaction.save();
            finish();
            return true;
        } else if (item.getItemId() == R.id.transaction_edit) {
            editmode = true;
            menu.findItem(R.id.transaction_update).setVisible(true);
            menu.findItem(R.id.transaction_edit).setVisible(false);
            menu.findItem(R.id.transaction_delete).setVisible(true);
            Snackbar s = Snackbar.make(findViewById(R.id.main_content), "Edit mode.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            s.show();
        } else if (item.getItemId() == R.id.transaction_save) {


            Tran t = new Tran();
            t.setDescription(description.getText().toString());
            try {
                t.setAmount(Double.parseDouble(gg.getText().toString().replace("R", "").trim()));
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
            if (selectedCat == null) {
                Snackbar.make(findViewById(R.id.main_content), "Category is empty.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
            t.setCategoryId(selectedCat.getId());
            t.setDate(new DateTime(c.getTime()).withTimeAtStartOfDay().getMillis());
            t.setWalletId(wallet.getId());
            t.setDebit(selectedCat.isDebit());

            Budget budget = FakeDataProvider.getActiveBudgetForCatAndWallet(t.getCategoryId(), t.getDate(),wallet.getId());
            if (budget != null) {
                budget.setAmountRemaining(budget.getAmountRemaining() - t.getAmount());
                budget.save();
                t.setBudget(budget.getId());
            }

            t.save();
            wallet.setBalance(wallet.getBalance() - t.getAmount());
            wallet.save();
            Intent i = getIntent();
            i.putExtra("p1", t);
            i.putExtra("p2", "save");
            setResult(Activity.RESULT_OK, i);

            finish();
            return true;
        } else {
            if (editmode) {
                editmode = false;
                menu.findItem(R.id.transaction_update).setVisible(false);
                menu.findItem(R.id.transaction_edit).setVisible(true);
                gg.setText("R " + transaction.getAmount());

            } else {
                finish();
                return true;
            }

        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mCurrentPhotoPath", mCurrentPhotoPath);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    ff.setExpanded(true);
//                    final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
//
//                    Glide.with(this).load(mCurrentPhotoPath).centerCrop().into(imageView);
                    //Convert file path into bitmap image using below line.
                    // yourSelectedImage = BitmapFactory.decodeFile(filePath);


                    //put bitmapimage in your imageview
                    //put bitmapimage in your imageviews
                    //yourimgView.setImageBitmap(yourSelectedImage);
                }
                break;

            case 10:
                if (resultCode == RESULT_OK) {

                    gg.setText(String.valueOf(imageReturnedIntent.getExtras().get("p1")));

                }
                break;
        }

    }

//    private void loadBackdrop() {
//        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
//
//        Glide.with(this).load(getOriginalImagePath()).centerCrop().into(imageView);
//    }

    public String getOriginalImagePath() {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        int column_index_data = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();

        return cursor.getString(column_index_data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_transaction_action, menu);
        this.menu = menu;
        if (transaction != null) {
            menu.findItem(R.id.transaction_save).setVisible(false);

            if (editmode) {

                menu.findItem(R.id.transaction_edit).setVisible(false);
            } else {
                menu.findItem(R.id.transaction_update).setVisible(false);
            }


        } else {
            menu.findItem(R.id.transaction_edit).setVisible(false);
            menu.findItem(R.id.transaction_update).setVisible(false);
        }
        return true;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}
