package com.geanysoftech.ridewisebilling.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.geanysoftech.ridewisebilling.Constant;
import com.geanysoftech.ridewisebilling.R;
import com.geanysoftech.ridewisebilling.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mswipetech.wisepad.sdk.Print;
import com.socsi.smartposapi.printer.Align;
import com.socsi.smartposapi.printer.FontLattice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etEmail, etPassword;
    TextInputLayout tilEmail, tilPassword;
    Button btnLogin;
    String txt_email, txt_password, firebaseToken;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.login_et_email);
        etPassword = findViewById(R.id.login_et_password);
        btnLogin = findViewById(R.id.login_btn_login);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        sessionManager = new SessionManager(getApplicationContext());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNetworkAvailable()) {

                    txt_email = etEmail.getText().toString().trim();
                    txt_password = etPassword.getText().toString().trim();

                    if (txt_email.equals("MobilePOS") && txt_password.equals("123456789")) {

                        new GetAuthenticate().execute();

                        //sessionManager.createLoginSession(txt_email, txt_password);

                        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        finishAffinity();*/

                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter valid credential", Toast.LENGTH_SHORT).show();
                    }

                    /*if (txt_email.isEmpty() && txt_password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Email and/or Password cannot be blank", Toast.LENGTH_SHORT).show();
                        //Constant.printMessageInDialog(LoginActivity.this, "Email and/or Password cannot be blank", "OK");

                    } else if (!isValidEmail(txt_email)){

                        tilEmail.setError("Invalid email id");

                    } else if (!isValidPassword(txt_password)){
                        tilPassword.setError("Password must contain minimum 8 characters at least 1 Alphabet, 1 Number and 1 Special Character");
                    } else if (txt_email.equals("prashant") && txt_password.equals("prashant")){

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    }*/

                } else {
                    Toast.makeText(LoginActivity.this, "Please turn ON internet to login", Toast.LENGTH_SHORT).show();
                    //Constant.printMessageInDialog(LoginActivity.this, "Please turn ON internet to login", "OK");

                }

            }
        });


    }

    private void generateRideWiseBilling() {

        Print.StartPrinting();
        Print.StartPrinting("CASH MEMO <br>", FontLattice.FORTY_EIGHT, true, Align.CENTER, true);
        Print.StartPrinting("Hotel Salimgarh Restaurant<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("Surve 29/1”,<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("M/s.Adlabs Entertainment Ltd<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("Surve no-30/31,Sangdewadi,<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("Pali Khopoli Road,<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("Tal-Khalapur, Dist – Raigad.<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("Web:www.adlabsimagica.com<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("------------------------<br>" ,FontLattice.THIRTY, true, Align.LEFT, true);
        Print.StartPrinting("BILL NO    :S0145177<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("BILL DATE  :30/06/2016<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("BILL TIME  :15:38 SHIFT :1<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("CASHIER    :SWAPNIL HARIBHAU GAIKWA<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("------------------------<br>" ,FontLattice.THIRTY, true, Align.LEFT, true);
        Print.StartPrinting("QTY  DESCRIPTION         AMOUNT<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("------------------------<br>" ,FontLattice.THIRTY, true, Align.LEFT, true);
        //Print.StartPrinting();
        Print.StartPrinting("4 WEEKDAY BUFFET ADULT   1509.44<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("1 WEEKDAY BUFFET KIDS     283.02<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting(" KKC @ 0.5%            3.58<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting(" SBC @ 0.50%           3.58<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting(" S.TAX @14%          100.38<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("        --------        <br>" ,FontLattice.TWENTY_EIGHT, true, Align.LEFT, true);
        Print.StartPrinting(" Debit Total        1900.00<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("        --------        <br>" ,FontLattice.TWENTY_EIGHT, true, Align.LEFT, true);
        Print.StartPrinting(" CASH               2000.00<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting(" Return              100.00<br>" ,FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
        Print.StartPrinting("------------------------<br>" ,FontLattice.THIRTY, true, Align.LEFT, true);
        Print.StartPrinting("S Tax No  :AAICA2573PSD001<br>" ,FontLattice.SIXTEEN, true, Align.LEFT, true);
        Print.StartPrinting("VAT TIN NO:27350869991V wef01Dec11<br>" ,FontLattice.SIXTEEN, true, Align.LEFT, true);
        Print.StartPrinting("CST TIN NO:27350869991C wef01Dec11<br>" ,FontLattice.SIXTEEN, true, Align.LEFT, true);
        Print.StartPrinting();
        Print.StartPrinting("-----Thank You & Visit Again------<br>" ,FontLattice.SIXTEEN, true, Align.LEFT, true);
        Print.StartPrinting();
        Print.StartPrinting();

    }


    private class GetAuthenticate extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        int responseCode;
        String responseMessage;
        boolean error = false;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            URL url = null;
            try {
                url = new URL("http://13.233.226.60/AmuzeTest/" + Constant.url_Authenticate);
            } catch (MalformedURLException e) {
                error = true;
                e.printStackTrace();
            }

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");
                //connection.setReadTimeout(60000);
                connection.setConnectTimeout(60000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserName", txt_email);
                jsonObject.put("Password", txt_password);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("UserName", txt_email);
            builder.appendQueryParameter("Password", txt_password);

            String query = builder.build().getEncodedQuery();*/

            OutputStream os;
            BufferedWriter writer;
            try {


                os = connection.getOutputStream();
                os.write(jsonObject.toString().getBytes("UTF-8"));
                os.close();


                /*os = connection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
*/
                connection.connect();
                responseCode = connection.getResponseCode();
                responseMessage = connection.getResponseMessage();

            } catch (UnsupportedEncodingException e1) {
                error = true;
                e1.printStackTrace();
            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            }


            BufferedReader in;
            StringBuilder sb = new StringBuilder();

            try {
                if (responseCode != 200) {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                } else {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }

                //in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }

                result = sb.toString();
                in.close();

            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

            if (!error) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    //Toast.makeText(LoginActivity.this, "Its working", Toast.LENGTH_SHORT).show();

                    String accessToken = jsonObject.getString("accessToken");
                    String tokenType = jsonObject.getString("tokenType");
                    String expiresOn = jsonObject.getString("expiresOn");

                    sessionManager.createLoginSession(txt_email, txt_password);
                    sessionManager.saveAccessToken(accessToken);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    finishAffinity();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
    }


}
