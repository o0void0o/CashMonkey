package com.koostech.cashmonkey;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.design.widget.TabLayout;
import android.support.v4.os.CancellationSignal;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.koostech.cashmonkey.db.FakeDataProvider;
import com.koostech.cashmonkey.dummy.Fragment_one;
import com.koostech.cashmonkey.dummy.Fragment_two;
import com.koostech.cashmonkey.model.Tran;
import com.koostech.cashmonkey.model.Wallet;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Wallet wallet;
    private Fragment_one fragment_one;
    public boolean isDrawerOpen;
    DrawerLayout drawer;
    ImageView koos;
    TextView activeWallet;
    TextView activeWalletBalance;
    TextView shit;
    private SmoothActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setupStuff();


    }

    private void setupStuff() {

        JodaTimeAndroid.init(this);

        wallet = FakeDataProvider.getAciveWallet();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent starter = new Intent(MainActivity.this, AddTransaction.class);
                startActivityForResult(starter, 1);
            }
        });


        activeWallet = (TextView) findViewById(R.id.titleWalletName);
        activeWalletBalance = (TextView) findViewById(R.id.titleWalletBalance);
        koos = (ImageView) findViewById(R.id.appbarWalletIcon);
        activeWallet.setText(wallet.getName());
        //  activeWalletBalance.setText("R " + wallet.getBalance());
        koos.setImageResource(wallet.getIcon());
        shit = (TextView) findViewById(R.id.shit);

        activeWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Wallets");

                final ArrayList<Wallet> wallets = FakeDataProvider.getToolbarWallets();

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
                        megmeg(wallets,which);
                        //  MainActivity.this.getSupportFragmentManager().beginTransaction().detach(fragment_one).attach(fragment_one).commit();

                    }
                });


                b.show();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new SmoothActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset > .55 && !isDrawerOpen) {
                    onDrawerOpened(drawerView);
                    isDrawerOpen = true;
                } else if (slideOffset < .45 && isDrawerOpen) {
                    onDrawerClosed(drawerView);
                    isDrawerOpen = false;
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    ViewPagerAdapter adapter;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                Fragment_one f = (Fragment_one) adapter.getItem(position);
                Double meh = FakeDataProvider.getWalletBalanceAtStartofPeriod(f.start);
                if (meh != null) {
                    shit.setText("R " + String.format("%.2f", FakeDataProvider.getWalletBalanceAtStartofPeriod(f.start)));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//todo weekly/monthly/custom
        if (true) {


            DateTime now = new DateTime();
            now = now.minusWeeks(4);


            for (int i = 0; i <= 4; i++) {

                DateTime weekStart = now.withDayOfWeek(1).withTimeAtStartOfDay();
                DateTime weekEnd = now.withDayOfWeek(7).withTimeAtStartOfDay();
                Interval week = new Interval(weekStart, weekEnd);
                now = now.plusWeeks(1);
                Bundle b = new Bundle();
                b.putLong("start", week.getStart().toDate().getTime());
                b.putLong("end", week.getEnd().toDate().getTime());
                Fragment_one one = new Fragment_one();
                one.setArguments(b);
                if (i == 3) {
                    adapter.addFragment(one, "Last Week");

                } else if (i == 4) {
                    adapter.addFragment(one, "This Week");


                } else {
                    adapter.addFragment(one, weekStart.toString("dd/MM") + " - " + weekEnd.toString("dd/MM"));


                }


            }

            //  adapter.addFragment(new Fragment_one(), "Future");
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(adapter.getCount(), true);

        }
    }

    public void megmeg(ArrayList<Wallet> wallets, int which) {

        activeWallet.setText(wallets.get(which).getName());
        System.out.print(activeWallet.getText());
       // activeWalletBalance.setText("R " + String.format("%.2f", (wallets.get(which).getBalance())));

        // activeWallet.setText("R " + String.valueOf(wallets.get(which).getBalance()));
        FakeDataProvider.saveSetting("monkey.activeWallet", String.valueOf(wallets.get(which).getId()));
        koos.setImageResource(wallets.get(which).getIcon());
        wallet = wallets.get(which);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public int getItemPosition(Object object) {

            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the budgetMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        toggle.selectItem(item);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }

        private void selectItem(MenuItem item) {

            int id = item.getItemId();

            if (id == R.id.nav_transactions) {

            }
            if (id == R.id.nav_dashboard) {
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, DashBoard.class);
                        startActivity(intent);
                    }
                });
            } else if (id == R.id.nav_budget) {
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, Budgets.class);
                        startActivity(intent);
                    }
                });
            } else if (id == R.id.nav_bills) {
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, Bills.class);
                        startActivity(intent);
                    }
                });

            } else if (id == R.id.nav_wallets) {
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, Wallets.class);
                        startActivity(intent);
                    }
                });

            } else if (id == R.id.nav_categories) {


                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, Categories.class);
                        startActivity(intent);
                    }
                });
            } else if (id == R.id.nav_transfer) {


                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, BudgetsViewer.class);
                        startActivity(intent);
                    }
                });
            }

            drawer.closeDrawers();


        }
    }

    String action;
    boolean refresh = false;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (refresh) {

            if (action != null) {
                Fragment_one f = (Fragment_one) adapter.getItem(viewPager.getCurrentItem());
                if (action.equals("delete")) {
                    f.showSnackBar("Transaction Removed.");

                } else if (action.equals("update")) {
                    f.showSnackBar("Transaction Updated.");

                } else {


                    f.showSnackBar("Transaction Added.");

                }
            }
        }
        adapter.notifyDataSetChanged();
        refresh = false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        refresh = true;

        if (resultCode == RESULT_OK) {
            action = (String) imageReturnedIntent.getExtras().get("p2");
        }

    }
}
