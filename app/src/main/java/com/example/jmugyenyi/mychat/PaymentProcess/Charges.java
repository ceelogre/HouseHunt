package com.example.jmugyenyi.mychat.PaymentProcess;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Charges extends AsyncTask<String, Void, String> {

    String token;
    public Response delegate = null;

    public Charges(String token) {
        this.token = token;
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("status", "getting here");

        new Thread() {
            @Override
            public void run() {
                postData("house",token,""+120);
            }
        }.start();
        return "Done";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.processFinish(s);
        Log.e("Result",s);
    }

    public void postData(String description, String token,String amount) {

        try {
            URL   url = new URL("http://172.29.52.204/phpserver/charge.php");
            String data  = URLEncoder.encode("username", "UTF-8");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

