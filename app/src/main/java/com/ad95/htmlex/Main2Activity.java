package com.ad95.htmlex;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Main2Activity extends Activity {
    public static String in = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final String[] i = {""},j={""},k={""};
        final EditText editText=(EditText)findViewById(R.id.editText2);
        Button button=(Button)findViewById(R.id.button);
        final TextView textview= (TextView) findViewById(R.id.t1);
        editText.setText("http://");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k[0] = editText.getText().toString();
                source s = new source();
                s.execute(k[0]);
                //while(in.isEmpty());
                textview.setText(in);
            }
        });
        Button button1=(Button)findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("input", in);
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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

    static class source extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect(params[0]).get();
                //doc.toString();
            } catch (IOException e) {
                Log.e("JSOUP", "JSOUP error!");
                Log.e("JSOUP ERROR",e.toString());
            }
            in=String.valueOf(doc);
            return in;

        }

        protected void onPostExecute(String result) {

        }

    }
}
