package com.geanysoftech.ridewisebilling.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geanysoftech.ridewisebilling.Constant;
import com.geanysoftech.ridewisebilling.R;
import com.geanysoftech.ridewisebilling.SessionManager;
import com.geanysoftech.ridewisebilling.model.FopModel;
import com.geanysoftech.ridewisebilling.model.GetTicketModel;
import com.geanysoftech.ridewisebilling.model.PluTaxesModel;
import com.geanysoftech.ridewisebilling.model.SVTransactionsModel;
import com.geanysoftech.ridewisebilling.model.SelectedItemModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mswipetech.wisepad.sdk.Print;
import com.socsi.smartposapi.printer.Align;
import com.socsi.smartposapi.printer.FontLattice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettlementActivity extends AppCompatActivity {
    String [] fopIDArray = new String[3];
    String [] fopTypeArray = new String[3];
    TextView tvCash, tvCard, tvSVCoupon, tvTotalAmtToPay, tvRemainingAmtToPay, tvSVAmount;
    EditText etCash, etCardDetails, etSVCouponDetails;
    CheckBox cbCash, cbCard, cbSvCoupon;
    Button btnSettleFop, btnCancelFop;
    CardView cardViewFops;
    TextInputLayout tilCashAmount, tilCardDetails, tilSVCoupon;

    SessionManager sessionManager;
    String accessToken, eventsDate;
    long date;
    String svId, totalPayableAmount, qrString, dateForPrinting, timeForPrinting;
    Calendar calendar;

    String SVTransactionId, externalID, SVAmount;
    LinearLayout lilySVCoupon;
    //double remainingAmount = 0.0;

    List<SelectedItemModel> selectedItemModelList = new ArrayList<>();
    List<PluTaxesModel> pluTaxesModelList = new ArrayList<>();
    List<FopModel> fopModelList = new ArrayList<>();
    List<GetTicketModel> getTicketModelList = new ArrayList<>();
    List<SVTransactionsModel> svTransactionsModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);

        btnSettleFop = findViewById(R.id.btnSettleFop);
        btnCancelFop = findViewById(R.id.btnCancelFop);
        tvTotalAmtToPay = findViewById(R.id.tvTotalAmtToPay);
        tvRemainingAmtToPay = findViewById(R.id.tvRemainingAmtToPay);
        etCash = findViewById(R.id.etCash);
        etCardDetails = findViewById(R.id.etCardDetails);
        etSVCouponDetails = findViewById(R.id.etSVCouponDetails);
        tilCashAmount = findViewById(R.id.tilCashAmount);
        tilCardDetails = findViewById(R.id.tilCardDetails);
        tilSVCoupon = findViewById(R.id.tilSVCoupon);
        cardViewFops = findViewById(R.id.cardViewFops);
        lilySVCoupon = findViewById(R.id.lilySVCoupon);
        tvSVAmount = findViewById(R.id.tvSVAmount);

        cbCash = findViewById(R.id.cbCash);
        cbCard = findViewById(R.id.cbCard);
        cbSvCoupon = findViewById(R.id.cbSvCoupon);

        sessionManager = new SessionManager(getApplicationContext());
        accessToken = sessionManager.getAccessToken();

        totalPayableAmount = getIntent().getStringExtra("totalPayableAmount");
        selectedItemModelList = (List<SelectedItemModel>) getIntent().getSerializableExtra("mySelectedItemList");
        tvTotalAmtToPay.setText(totalPayableAmount);
        tvRemainingAmtToPay.setText(totalPayableAmount);


        double originalCost = Double.parseDouble(totalPayableAmount);
        double gstAmount = (Double.parseDouble(totalPayableAmount) / 100.f) * 18;
        //tvRemainingAmtToPay.setText(""+gstAmount);

        if (Constant.isNetworkAvailable(SettlementActivity.this)) {
            //new GetFOPDetails().execute();
        }

        fopIDArray[0] = "1";
        fopIDArray[1] = "3";
        fopIDArray[2] = "28";
        fopTypeArray[0] = "INR";
        fopTypeArray[1] = "EDC";
        fopTypeArray[2] = "GIFT CARD";

        etCash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (etCash.getText().toString().length() == 0){

                } else {
                    double my_remaining_balance = 0.0, remainingAmount = 0.0;
                    my_remaining_balance = Double.parseDouble(etCash.getText().toString());

                    remainingAmount = Double.parseDouble(totalPayableAmount) - my_remaining_balance;
                    tvRemainingAmtToPay.setText(Constant.donDashamlaDakhava("" + remainingAmount));
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etCardDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 0){

                } else {
                    double my_remaining_balance = 0.0, remainingAmount = 0.0;
                    my_remaining_balance = Double.parseDouble(etCardDetails.getText().toString());


                    remainingAmount = Double.parseDouble(totalPayableAmount) - my_remaining_balance;
                    tvRemainingAmtToPay.setText(Constant.donDashamlaDakhava(""+remainingAmount));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etSVCouponDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 0){

                } else {
                    double my_remaining_balance = 0.0, remainingAmount = 0.0;
                    my_remaining_balance = Double.parseDouble(etSVCouponDetails.getText().toString());

                    remainingAmount = Double.parseDouble(totalPayableAmount) - my_remaining_balance;
                    tvRemainingAmtToPay.setText(Constant.donDashamlaDakhava(""+remainingAmount));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cbCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //fopCashId = 1
                if (isChecked) {
                    tilCashAmount.setVisibility(View.VISIBLE);
                    if (etCash.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                } else {
                    tilCashAmount.setVisibility(View.GONE);
                    etCash.setText("");
                }
            }
        });

        cbCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //fopCardId = 3
                if (isChecked) {
                    tilCardDetails.setVisibility(View.VISIBLE);
                    if (etCardDetails.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                } else {
                    tilCardDetails.setVisibility(View.GONE);
                    etCardDetails.setText("");
                }
            }
        });

        cbSvCoupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //fopSvCouponId = 28
                if (isChecked) {
                    IntentIntegrator intentIntegrator = new IntentIntegrator(SettlementActivity.this);
                    //intentIntegrator.initiateScan(Collections.singleton("12345678"));
                    intentIntegrator.initiateScan();

                    lilySVCoupon.setVisibility(View.VISIBLE);
                    if (etSVCouponDetails.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }

                } else {
                    lilySVCoupon.setVisibility(View.GONE);
                    etSVCouponDetails.setText("");
                }
            }
        });

        btnSettleFop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double my_total_amount = 0.0;
                boolean allowToProceed = false;
                //SVCoupon amount qrString
                date = System.currentTimeMillis();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                eventsDate = dateFormat.format(date);

                //check if the payable amount is equal to the entered amount in cash, card and sv coupon
                if (totalPayableAmount.isEmpty()) {
                    Toast.makeText(SettlementActivity.this, "Please select item", Toast.LENGTH_SHORT).show();
                } else {
                    if (cbCash.isChecked()) {
                        if (etCash.getText().toString().length() == 0) {
                            allowToProceed = false;
                            Toast.makeText(SettlementActivity.this, "Please enter cash amount", Toast.LENGTH_SHORT).show();
                        } else {
                            my_total_amount += Double.parseDouble(etCash.getText().toString());
                            allowToProceed = true;
                        }
                    }

                    if (cbCard.isChecked()) {
                        if (etCardDetails.getText().toString().length() == 0) {
                            allowToProceed = false;
                            Toast.makeText(SettlementActivity.this, "Please enter card amount", Toast.LENGTH_SHORT).show();
                        } else {
                            my_total_amount += Double.parseDouble(etCardDetails.getText().toString());
                            allowToProceed = true;
                        }
                    }

                    if (cbSvCoupon.isChecked()) {
                        if (qrString.length() == 0) {
                            allowToProceed = false;
                            Toast.makeText(SettlementActivity.this, "Please enter SvCoupon amount", Toast.LENGTH_SHORT).show();
                        } else {
                            //TODO coupon
                            my_total_amount += Double.parseDouble(etSVCouponDetails.getText().toString());
                            allowToProceed = true;
                        }
                    }

                    if (Constant.isNetworkAvailable(SettlementActivity.this)) {
                        if (allowToProceed) {
                            if(my_total_amount >= Double.parseDouble(totalPayableAmount)){
                                new ExecuteAddOrders(eventsDate).execute();
                                //generateRideWiseBilling();

                            }else{
                                Toast.makeText(SettlementActivity.this, "Entered amount is not correct", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(SettlementActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        btnCancelFop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {

            //If QR Code has nothing in it
            if (intentResult.getContents() == null) {
                lilySVCoupon.setVisibility(View.GONE);
                etSVCouponDetails.setText("");
                Toast.makeText(this, "Result not found", Toast.LENGTH_SHORT).show();
            } else {
                //If QR Code contains data
                qrString = intentResult.getContents();
                new ExecuteSVDetails(qrString).execute(qrString);
                lilySVCoupon.setVisibility(View.VISIBLE);

                if (qrString.equals("12345678")) {
                    Toast.makeText(this, "Api called", Toast.LENGTH_SHORT).show();
                }

            }

        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class ExecuteAddOrders extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        int responseCode;
        String responseMessage;
        boolean error = false;
        String currentDateAndTime;

        public ExecuteAddOrders(String currentDateAndTime) {
            this.currentDateAndTime = currentDateAndTime;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SettlementActivity.this);
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
                url = new URL("http://13.233.226.60/AmuzeTest/" + Constant.url_Get_Orders);
            } catch (MalformedURLException e) {
                error = true;
                e.printStackTrace();
            }

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/xml");
                connection.setRequestProperty("Accept", "application/xml");
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                connection.setRequestMethod("POST");
                //connection.setReadTimeout(Constant.READ_TIMEOUT);
                connection.setConnectTimeout(5000);
                connection.setDoOutput(true);
                connection.setDoInput(true);

            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            }

            JSONObject jsonOrderMain = new JSONObject();
            try {

                JSONObject jsonOrder = new JSONObject();
                jsonOrder.put("TransactionDate", currentDateAndTime);
                jsonOrder.put("VisitDate", currentDateAndTime);
                jsonOrder.put("ExternalID", "");
                jsonOrder.put("AgentName", "");
                jsonOrder.put("SalesPerson", "");
                jsonOrder.put("PromotionCode", "");
                jsonOrder.put("Channel", "");
                jsonOrder.put("Remarks", "");
                jsonOrder.put("UserColumn1", "");
                jsonOrder.put("UserColumn2", "");
                jsonOrder.put("UserColumn3", "");
                jsonOrder.put("UserColumn4", "");
                jsonOrder.put("UserColumn5", "");

                //Start Contacts JsonObject "Contacts"
                JSONObject jsonContact = new JSONObject();
                jsonContact.put("FirstName", "");
                jsonContact.put("LastName", "");
                jsonContact.put("Organisation", "");
                jsonContact.put("Address1", "");
                jsonContact.put("Address2", "");
                jsonContact.put("Address3", "");
                jsonContact.put("City", "");
                jsonContact.put("State", "");
                jsonContact.put("Pincode", "");
                jsonContact.put("ContactNumber", "");
                jsonContact.put("MobileNumber", "");
                jsonContact.put("Email", "");
                jsonOrder.put("Contact", jsonContact);

                //Start Contacts JsonObject "Items"
                JSONObject jsonItems = new JSONObject();
                JSONArray jsonArrayItem = new JSONArray();
                for (int i = 0; i < selectedItemModelList.size(); i++) {
                    //Start Contacts JsonObject "Item"
                    JSONObject jsonItem = new JSONObject();
                    String pluCode = selectedItemModelList.get(i).getPluCode();
                    String pluName = selectedItemModelList.get(i).getPluName();
                    String eventId = selectedItemModelList.get(i).getEventID();
                    String quantity = selectedItemModelList.get(i).getQuantity();
                    String price = selectedItemModelList.get(i).getPrice();
                    String discountAmount = selectedItemModelList.get(i).getDiscountAmount();

                    jsonItem.put("PLU", pluCode);
                    jsonItem.put("PLUDescription", pluName);
                    jsonItem.put("EventID", eventId);
                    jsonItem.put("Quantity", quantity);
                    jsonItem.put("Price", price);
                    jsonItem.put("Discount", discountAmount);

                    //Start Contacts JsonObject "Taxes"
                    JSONObject jsonTaxes = new JSONObject();
                    JSONArray jArrayTax = new JSONArray();
                    JSONObject jsonTax = null;
                    for (int j = 0; j <= 1; j++) {
                        jsonTax = new JSONObject();
                        List<PluTaxesModel> pluTaxesModelList = new ArrayList<>();
                        pluTaxesModelList = selectedItemModelList.get(i).getTaxes();

                        jsonTax.put("TaxID" , pluTaxesModelList.get(j).getTaxID());
                        jsonTax.put("TaxPercent" , pluTaxesModelList.get(j).getTaxPercent());
                        jsonTax.put("TaxDescription" , pluTaxesModelList.get(j).getTaxDescription());

                        jArrayTax.put(j, jsonTax);
                    }
                    jsonTaxes.put("Tax", jArrayTax);
                    jsonItem.put("Taxes", jsonTaxes);
                    jsonArrayItem.put(i, jsonItem);
                    //jsonItems.put("Item", jsonItem);
                }

                jsonItems.put("Item", jsonArrayItem);
                //jsonOrder.put("items", jsonItems);
                jsonOrder.put("Items", jsonItems);

                //Start Contacts JsonObject "Payments"
                JSONObject jsonPayments = new JSONObject();
                JSONObject jsonPaymentDetails = null;
                JSONArray jArrayPaymentDetails = new JSONArray();

                if(cbCash.isChecked()){
                    jsonPaymentDetails = new JSONObject();
                    jsonPaymentDetails.put("FOPID", fopIDArray[0]);
                    jsonPaymentDetails.put("FOPDescription", fopTypeArray[0]);
                    jsonPaymentDetails.put("SVID","");
                    jsonPaymentDetails.put("Amount", "" + etCash.getText().toString());

//                    jsonPayments.put("Payment", jsonPaymentDetails);
                    jArrayPaymentDetails.put(jsonPaymentDetails);

                }

                if(cbCard.isChecked()){
                    jsonPaymentDetails = new JSONObject();
                    jsonPaymentDetails.put("FOPID", fopIDArray[1]);
                    jsonPaymentDetails.put("FOPDescription", fopTypeArray[1]);
                    jsonPaymentDetails.put("SVID","");
                    jsonPaymentDetails.put("Amount", "" + etCardDetails.getText().toString());

//                    jsonPayments.put("Payment", jsonPaymentDetails);
                    jArrayPaymentDetails.put(jsonPaymentDetails);

                }

                if(cbSvCoupon.isChecked()){
                    jsonPaymentDetails = new JSONObject();
                    jsonPaymentDetails.put("FOPID", fopIDArray[2]);
                    jsonPaymentDetails.put("FOPDescription", fopTypeArray[2]);
                    jsonPaymentDetails.put("SVID", SVTransactionId);
                    //TODO Coupon Edittext
                    jsonPaymentDetails.put("Amount", "" + etSVCouponDetails.getText().toString());

//                    jsonPayments.put("Payment", jsonPaymentDetails);
                    jArrayPaymentDetails.put(jsonPaymentDetails);

                }

                jsonPayments.put("Payment", jArrayPaymentDetails);
                jsonOrder.put("Payments", jsonPayments);

                jsonOrderMain.put("Order", jsonOrder);

            } catch (JSONException e) {
                error = true;
                e.printStackTrace();
            }

            OutputStream outputStream;
            BufferedWriter writer;

            try {

                outputStream = connection.getOutputStream();
                outputStream.write(jsonOrderMain.toString().getBytes("UTF-8"));
                outputStream.close();

                connection.connect();
                responseCode = connection.getResponseCode();
                responseMessage = connection.getResponseMessage();

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
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String orderId = jsonObject.getString("OrderID");

                        if (Constant.isNetworkAvailable(SettlementActivity.this)) {
                            new ExecuteTicketPrinting(orderId).execute();

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SettlementActivity.this, ""+result, Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(SettlementActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class GetFOPDetails extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        int responseCode;
        String responseMessage;
        boolean error = false;
        String currentDateAndTime;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SettlementActivity.this);
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
                url = new URL("http://13.233.226.60/AmuzeTest/" + Constant.url_Get_Fops);
            } catch (MalformedURLException e) {
                error = true;
                e.printStackTrace();
            }

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                //connection.setReadTimeout(Constant.READ_TIMEOUT);
                connection.setConnectTimeout(60000);
                //connection.setDoOutput(true);
                connection.setDoInput(true);

            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            }

            OutputStream outputStream;
            BufferedWriter writer;

            try {

                /*outputStream = connection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                outputStream.close();*/

                connection.connect();
                responseCode = connection.getResponseCode();
                responseMessage = connection.getResponseMessage();

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

            if (error) {
                Toast.makeText(SettlementActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        /*fopId = jsonObject.getString("id");
                        fopType = jsonObject.getString("fopType");

                        fopModelList.add(new FopModel(fopId, fopType));*/


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private class ExecuteTicketPrinting extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        int responseCode;
        String responseMessage;
        boolean error = false;
        String orderId;

        public ExecuteTicketPrinting(String orderId) {
            this.orderId = orderId;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SettlementActivity.this);
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
                url = new URL("http://13.233.226.60/AmuzeTest/" + Constant.url_Get_Tickets);
            } catch (MalformedURLException e) {
                error = true;
                e.printStackTrace();
            }

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                //connection.setReadTimeout(Constant.READ_TIMEOUT);
                connection.setConnectTimeout(60000);
                //connection.setDoOutput(true);
                connection.setDoInput(true);

            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("OrderID", orderId);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            OutputStream outputStream;
            BufferedWriter writer;

            try {

                /*outputStream = connection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                outputStream.close();*/

                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes("UTF-8"));
                outputStream.close();

                connection.connect();
                responseCode = connection.getResponseCode();
                responseMessage = connection.getResponseMessage();

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

            if (!error){

                try {
                    JSONObject jsonObject  = new JSONObject(result);

                    JSONObject jsonArrayOfTicket = jsonObject.getJSONObject("ArrayOfTicket");

                    JSONArray jsonArrayTicket = jsonArrayOfTicket.getJSONArray("Ticket");

                    for (int i = 0; i < jsonArrayTicket.length(); i++){
                        JSONObject jsonDataTickets = jsonArrayTicket.getJSONObject(i);

                        String ticketId = jsonDataTickets.getString("TicketID");
                        String businessUnitName = jsonDataTickets.getString("BusinessUnitName");
                        String pluCode = jsonDataTickets.getString("PLUCode");
                        String pluName = jsonDataTickets.getString("PLUName");
                        String eventID = jsonDataTickets.getString("EventID");
                        String eventName = jsonDataTickets.getString("EventName");
                        String eventTypeName = jsonDataTickets.getString("EventTypeName");
                        String resourceName = jsonDataTickets.getString("ResourceName");
                        String validFrom = jsonDataTickets.getString("ValidFrom");
                        String validTo = jsonDataTickets.getString("ValidTo");

                        getTicketModelList.add(new GetTicketModel(ticketId, businessUnitName, pluCode, pluName, eventID, eventName,
                                eventTypeName, resourceName, validFrom, validTo));

                        generateRideWiseBilling(ticketId);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(SettlementActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void generateRideWiseBilling(String ticketId) {

        try {

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            dateForPrinting = dateFormat1.format(date);

            calendar = Calendar.getInstance();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeForPrinting = timeFormat.format(calendar.getTime());

            Print.StartPrinting();
            Print.StartPrinting("CASH MEMO <br>", FontLattice.FORTY_EIGHT, true, Align.CENTER, true);
            Print.StartPrinting("Hotel Salimgarh Restaurant<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("Surve 29/1”,<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("M/s.Adlabs Entertainment Ltd<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("Surve no-30/31,Sangdewadi,<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("Pali Khopoli Road,<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("Tal-Khalapur, Dist – Raigad.<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("Web:www.adlabsimagica.com<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("------------------------<br>", FontLattice.THIRTY, true, Align.LEFT, true);
            //TicketId
            //Print.StartPrinting("BILL NO    :" + ticketId + "<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            //Date
            Print.StartPrinting("BILL DATE  :" + dateForPrinting + "<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            //Time
            Print.StartPrinting("BILL TIME  :" + timeForPrinting + "SHIFT :1<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("CASHIER    :SWAPNIL HARIBHAU GAIKWAD<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("------------------------<br>", FontLattice.THIRTY, true, Align.LEFT, true);
            Print.StartPrinting("QTY  DESCRIPTION         AMOUNT<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("------------------------<br>", FontLattice.THIRTY, true, Align.LEFT, true);
            //Print.StartPrinting("1 WEEKDAY BUFFET KIDS     283.02<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            //All quantities, pluName, pluAmountincludedTax
            for (int i = 0; i < selectedItemModelList.size(); i++) {
                Print.StartPrinting("4" + selectedItemModelList.get(i).getQuantity() + "WEEKDAY BUFFET ADULT" + selectedItemModelList.get(i).getPluName() +
                        "1509.44" + selectedItemModelList.get(i).getTotalAmount() + "<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);

            }
            //GST amount  = (Original cost * GST%) / 100
            double originalCost = Double.parseDouble(totalPayableAmount);
            double gstAmount = (originalCost / 100.0f) / 18;
            // GST Amount
            Print.StartPrinting(" GST @18%          " + "100.38" + gstAmount + "<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("------------------------<br>", FontLattice.THIRTY, true, Align.LEFT, true);
            //TotalPayableAmount
            Print.StartPrinting(" Debit Total        " + "1900.00" + totalPayableAmount + "<br>", FontLattice.TWENTY_FOUR, true, Align.LEFT, true);
            Print.StartPrinting("------------------------<br>", FontLattice.THIRTY, true, Align.LEFT, true);
            Print.StartPrinting();
            Print.StartPrinting("-----Thank You & Visit Again------<br>", FontLattice.SIXTEEN, true, Align.LEFT, true);
            Print.StartPrinting();
            Print.StartPrinting();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private class ExecuteSVDetails extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        int responseCode;
        String responseMessage;
        boolean error = false;
        String qrString;

        public ExecuteSVDetails(String qrString){
            this.qrString = qrString;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SettlementActivity.this);
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
                url = new URL("http://13.233.226.60/AmuzeTest/" + Constant.url_Get_SVTransactions);
            } catch (MalformedURLException e) {
                error = true;
                e.printStackTrace();
            }

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/xml");
                connection.setRequestProperty("Accept", "application/xml");
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                //connection.setReadTimeout(Constant.READ_TIMEOUT);
                connection.setConnectTimeout(50000);
                //connection.setDoOutput(true);
                connection.setDoInput(true);

            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            }

            OutputStream outputStream;
            BufferedWriter writer;

            try {

                /*outputStream = connection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                outputStream.close();*/

                outputStream = connection.getOutputStream();
                outputStream.write(qrString.getBytes("UTF-8"));
                outputStream.close();

                connection.connect();
                responseCode = connection.getResponseCode();
                responseMessage = connection.getResponseMessage();

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

            if (!error){

                Toast.makeText(SettlementActivity.this, ""+result, Toast.LENGTH_SHORT).show();
                tvSVAmount.setText(result);

                /*try {
                    JSONObject jsonObject = new JSONObject(result);
                    //String storedValue = jsonObject.getString("string");

                    JSONObject jObjArrayOfStoredValueTransaction = jsonObject.getJSONObject("ArrayOfStoredValueTransaction");

                    JSONArray jArrayStoredValueTransaction = jObjArrayOfStoredValueTransaction.getJSONArray("StoredValueTransaction");
                    for (int i = 0; i < jArrayStoredValueTransaction.length(); i++){
                        JSONObject jsonObjMainData = jArrayStoredValueTransaction.getJSONObject(i);

                        SVTransactionId = jsonObjMainData.getString("SVID");
                        String transactionDate = jsonObjMainData.getString("TransactionDate");
                        String sourceName = jsonObjMainData.getString("SourceName");
                        String externalID = jsonObjMainData.getString("ExternalID");

                        SVAmount = jsonObjMainData.getString("Amount");
                        String transactionID = jsonObjMainData.getString("ID");
                        String transactionType = jsonObjMainData.getString("TransactionType");
                        String nodeNo = jsonObjMainData.getString("NodeNo");

                        tvSVAmount.setText(Constant.donDashamlaDakhava(SVAmount));

                        svTransactionsModelList.add(new SVTransactionsModel(SVTransactionId, transactionDate, sourceName, externalID,
                                SVAmount, transactionID, transactionType, nodeNo));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            }else {
                Toast.makeText(SettlementActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
