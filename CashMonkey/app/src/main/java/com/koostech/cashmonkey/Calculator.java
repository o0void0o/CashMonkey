package com.koostech.cashmonkey;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Calculator extends AppCompatActivity {
    TextView koos;

    MathEval mathEval= new MathEval();
    public String number = "0";
    boolean opererator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Amount");
        number= (String) getIntent().getExtras().get("p1");

        koos = (TextView) findViewById(R.id.poep);
        koos.setText(number);
        Button n1 = (Button) findViewById(R.id.button1);
        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("1"));
            }
        });

        Button n2 = (Button) findViewById(R.id.button2);
        n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("2"));
            }
        });
        Button n3 = (Button) findViewById(R.id.button3);
        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("3"));
            }
        });
        Button n4 = (Button) findViewById(R.id.button4);
        n4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("4"));
            }
        });
        Button n5 = (Button) findViewById(R.id.button5);
        n5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("5"));
            }
        });
        Button n6 = (Button) findViewById(R.id.button6);
        n6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("6"));
            }
        });
        Button n7 = (Button) findViewById(R.id.button7);
        n7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("7"));
            }
        });
        Button n8 = (Button) findViewById(R.id.button8);
        n8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("8"));
            }
        });
        Button n9 = (Button) findViewById(R.id.button9);
        n9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("9"));
            }
        });
        Button n14 = (Button) findViewById(R.id.button14);
        n14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("0"));
            }
        });

        ImageButton n16 = (ImageButton) findViewById(R.id.button16);
        n16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("back"));
            }
        });
        ImageButton n13 = (ImageButton) findViewById(R.id.plus);
        n13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fok=processInputNumber("+");
                koos.setText(fok);
            }
        });
        ImageButton n12 = (ImageButton) findViewById(R.id.button12);
        n12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("-"));
            }
        });
        ImageButton n10 = (ImageButton) findViewById(R.id.button10);
        n10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("/"));
            }
        });
        ImageButton n11 = (ImageButton) findViewById(R.id.button11);
        n11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koos.setText(processInputNumber("*"));

            }
        });

        ImageButton n19 = (ImageButton) findViewById(R.id.button19);
        n19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number=String.format("%.2f",mathEval.evaluate(number)).replace(",",".");


                koos.setText(number);
            }
        });

        Button n17 = (Button) findViewById(R.id.button17);
        n17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number="0.00";


                koos.setText(number);
            }
        });

    }

    public String processInputNumber(String number) {

        if (this.number.equals("0")||this.number.equals("0.00")) {
            if (!number.equals("back")
                    && !number.equals("+")
                    && !number.equals("/")
                    && !number.equals("*")
                    && !number.equals("-")) {
                this.number = number;
                opererator = false;
                return this.number;
            }
        }
         if (number.equals("+")) {
            if (!opererator) {
                this.number =this.number + "+";
                opererator = true;
            } else {
                this.number = this.number.substring(0, this.number.length() - 1);
                this.number =this.number + "+";
            }
        } else if (number.equals("-")) {
            if (!opererator) {
                this.number += number;
                opererator = true;
            } else {
                this.number = this.number.substring(0, this.number.length() - 1);
                this.number += number;
            }
        } else if (number.equals("*")) {
            if (!opererator) {
                this.number += number;
                opererator = true;
            } else {
                this.number = this.number.substring(0, this.number.length() - 1);
                this.number += number;
            }
        } else if (number.equals("/")) {
            if (!opererator) {
                this.number += number;
                opererator = true;
            } else {
                this.number = this.number.substring(0, this.number.length() - 1);
                this.number += number;

            }
        } else if (number.equals("back")) {
            opererator = false;
            if (this.number.length() == 1) {
                this.number = "0";
            } else {
                this.number = this.number.substring(0, this.number.length() - 1);
            }

        } else {
            opererator = false;
            this.number += number;

        }


        return this.number;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        number=String.format("%.2f",mathEval.evaluate(number)).replace(",",".");


        koos.setText(number);
        Bundle b = new Bundle();
        b.putString("p1", number);
        Intent i = getIntent();
        i.putExtras(b);
        setResult(Activity.RESULT_OK, i);
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_amount_action, menu);
        return true;
    }
}
