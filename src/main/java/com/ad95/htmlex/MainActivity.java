package com.ad95.htmlex;

import android.app.Activity;
import android.app.admin.DeviceAdminInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.*;
import java.util.*;

class Token {
    String id, lexeme;

    public Token(String id, String lexeme) {
        this.id = id;
        this.lexeme = lexeme;
    }

    public String getId() {
        return id;
    }

    public String getLexeme() {
        return lexeme;
    }
}

class LexAnalyzer {

    public ArrayList<Token> main1(String address) {

        ArrayList<Token> a=null;

        try {
            String input = "", s = "";
            Token t;
            input = address;
            Log.i("input", input);
            a = getToken(input);
        }

        catch(Exception e) {
            e.printStackTrace();
        }
        return a;
    }
    public static ArrayList<Token> getToken(String input) {
        String lexeme = "";
        Token t = null;
        ArrayList<Token> a = new ArrayList<Token>();

        for (int i = 0; i < input.length(); i++) {

            if (input.charAt(i) == '<') {
                lexeme = "" + input.charAt(i);
                i++;

                if (input.charAt(i) == '!') {
                    lexeme += input.charAt(i);
                    i++;
                    while (input.charAt(i) != '>') {
                        lexeme += input.charAt(i);
                        i++;
                    }
                    lexeme += input.charAt(i);
                    t = new Token("COMMENT", lexeme);
                    a.add(t);
                }

                else if (isALetter(input.charAt(i))) {
                    lexeme += input.charAt(i);
                    i++;
                    while (input.charAt(i) != '>') {
                        lexeme += input.charAt(i);
                        i++;
                    }
                    lexeme += input.charAt(i);
                    t = new Token("TAG", lexeme);

                    a.add(t);
                }

                else if ((isANumber(input.charAt(i)))) {
                    lexeme = "" + input.charAt(i);
                    i++;
                    while (input.charAt(i) != '<' || input.charAt(i) == ' ') {
                        lexeme += input.charAt(i);
                        i++;
                    }

                    lexeme += input.charAt(i);
                    t = new Token("LTHAN", lexeme);

                    a.add(t);
                }

                else if (input.charAt(i) == '/') {
                    lexeme += input.charAt(i);
                    i++;
                    if (isALetter(input.charAt(i))) {
                        lexeme += input.charAt(i);
                        i++;
                        while (input.charAt(i) != '>') {
                            lexeme += input.charAt(i);
                            i++;
                        }
                        lexeme += input.charAt(i);
                    }
                    t = new Token("ENDTAG", lexeme);

                    a.add(t);
                }
            }

            else if(isALetter(input.charAt(i))||isANumber(input.charAt(i))){
                lexeme="";
                while(isALetter(input.charAt(i))||isANumber(input.charAt(i))||input.charAt(i)==' '){
                    lexeme += input.charAt(i);
                    i++;
                }
                i--;
                t = new Token("ID", lexeme);
                a.add(t);
            }
            else
                i++;
        }
        return a;
    }
    public static boolean isALetter(char inputChar) {
        Boolean itIsALetter = false;
        if("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(inputChar) != -1) {
            itIsALetter = true;
        }
        return itIsALetter;
    }

    public static boolean isANumber(char inputChar) {
        Boolean itIsANumber = false;
        if("1234567890".indexOf(inputChar) != -1) {
            itIsANumber = true;
        }
        return itIsANumber;
    }
}

public class MainActivity extends Activity {
    private static final int READ_REQUEST_CODE = 10112;
    Uri uri = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (resultData != null) {
                uri = resultData.getData();
            }
        }
    }

    void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/html");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        final int device_width=size.x;
        Bundle bundle=getIntent().getExtras();
        String add=bundle.getString("input");
        LexAnalyzer Lex=new LexAnalyzer();
        final ArrayList<Token> result=Lex.main1(add);
        String result1="";
        final TableLayout lextable = (TableLayout)findViewById(R.id.table);
        lextable.setStretchAllColumns(true);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.bringToFront();
        final List<String> list = new ArrayList<String>();
        list.add("All");
        if(result!=null) {
            for(int i = 0; i < result.size(); i++) {
                if(!list.contains(result.get(i).getId()))
                list.add(result.get(i).getId());
            }
            for (int i = 0; i < 0; i++) {
                TableRow tr = new TableRow(this);
                TextView c0 = new TextView(tr.getContext());
                c0.setText(String.valueOf(i + 1));
                TextView c1 = new TextView(this);
                c1.setText(result.get(i).getId());
                TextView c2 = new TextView(this);
                c2.setText(result.get(i).getLexeme());
                tr.addView(c0);
                tr.addView(c1);
                tr.addView(c2);
                lextable.addView(tr);
                // result1+=result.get(i).getId() + "    " + result.get(i).getLexeme()+"\n";
                // Log.i("result1", result1);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    lextable.removeAllViewsInLayout();
                    TableRow tr1 = new TableRow(lextable.getContext());
                    TextView c01 = new TextView(tr1.getContext());
                    c01.setText("Serial");
                    TextView c11 = new TextView(tr1.getContext());
                    c11.setText("Category");
                    TextView c21 = new TextView(tr1.getContext());
                    c21.setText("Lexeme");
                    tr1.addView(c01);
                    tr1.addView(c11);
                    tr1.addView(c21);
                    lextable.addView(tr1);
                    int size=0;
                    for (int i = 0; i < result.size(); i++) {
                        if (position == 0) {
                            size++;
                            TableRow tr = new TableRow(lextable.getContext());
                            TextView c0 = new TextView(tr.getContext());
                            c0.setText(String.valueOf(i + 1));
                            c0.setMaxWidth(device_width/3);
                            TextView c1 = new TextView(tr.getContext());
                            c1.setText(result.get(i).getId());
                            c1.setMaxWidth(device_width/3);
                            TextView c2 = new TextView(tr.getContext());
                            c2.setText(result.get(i).getLexeme());
                            c2.setMaxWidth(device_width/3);
                            tr.addView(c0);
                            tr.addView(c1);
                            tr.addView(c2);
                            lextable.addView(tr);
                        }
                    else if (list.get(position).equals(result.get(i).getId())) {
                            size++;
                        TableRow tr = new TableRow(lextable.getContext());
                        TextView c0 = new TextView(tr.getContext());
                        c0.setText(String.valueOf(i + 1));
                            c0.setMaxWidth(device_width/3);
                        TextView c1 = new TextView(tr.getContext());
                            c1.setText(result.get(i).getId());
                            c1.setMaxWidth(device_width/3);
                        TextView c2 = new TextView(tr.getContext());
                        c2.setText(result.get(i).getLexeme());
                            c2.setMaxWidth(device_width/3);
                        tr.addView(c0);
                        tr.addView(c1);
                        tr.addView(c2);
                        lextable.addView(tr);
                        // result1+=result.get(i).getId() + "    " + result.get(i).getLexeme()+"\n";
                        // Log.i("result1", result1);
                    }
                }
                    lextable.setStretchAllColumns(true);
                    TextView textView=(TextView)findViewById(R.id.textView5);
                    textView.setText("Total Lexemes : "+size+". ");
            }

            @Override
                public void onNothingSelected(AdapterView<?> parent) {
/*
                    for (int i = 0; i < result.size(); i++) {
                        TableRow tr = new TableRow(lextable.getContext());
                        TextView c0 = new TextView(tr.getContext());
                        c0.setText(String.valueOf(i + 1));
                        TextView c1 = new TextView(tr.getContext());
                        c1.setText(result.get(i).getId());
                        TextView c2 = new TextView(tr.getContext());
                        c2.setText(result.get(i).getLexeme());
                        tr.addView(c0);
                        tr.addView(c1);
                        tr.addView(c2);
                        lextable.addView(tr);
                        // result1+=result.get(i).getId() + "    " + result.get(i).getLexeme()+"\n";
                        // Log.i("result1", result1);
                    }
  */              }
            });
        }
        else
            Log.i("fail","failed");
        //textView.setText(result1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
