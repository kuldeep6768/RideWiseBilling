package com.geanysoftech.ridewisebilling.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geanysoftech.ridewisebilling.Constant;
import com.geanysoftech.ridewisebilling.R;
import com.geanysoftech.ridewisebilling.SessionManager;
import com.geanysoftech.ridewisebilling.helper.ItemOffsetDecoration;
import com.geanysoftech.ridewisebilling.helper.SimpleDividerItemDecoration;
import com.geanysoftech.ridewisebilling.model.FopModel;
import com.geanysoftech.ridewisebilling.model.PluChildItemsModel;
import com.geanysoftech.ridewisebilling.model.PluModel;
import com.geanysoftech.ridewisebilling.model.PluTaxesModel;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewPlu, recyclerViewSelectedItem;
    Toolbar toolbar;
    List<PluModel> pluModelList = new ArrayList<PluModel>();
    List<SelectedItemModel> selectedItemModelList = new ArrayList<>();
    List<PluTaxesModel> pluTaxesModelList = new ArrayList<>();
    List<PluChildItemsModel> pluChildItemsModelList = new ArrayList<>();
    List<FopModel> fopModelList = new ArrayList<>();


    PluDetailsAdapter pluDetailsAdapter;
    SelectedItemListAdapter selectedItemListAdapter;
    PluModel pluModel;
    PluTaxesModel pluTaxesModel;
    SelectedItemModel selectedItemModel;
    SessionManager sessionManager;
    String id, pluCode, pluName, pluType, hsn, pluPrice, deposit, eventTypeID, resourceID, eventID, quantity, discountPercent, discountAmount,
            taxes, taxID, taxDescription, taxPercent, childItems;
    TextView tvTotalAmount, tvSelectedTotalQuantity;

    Button btnAddQuantity, btnRemoveQuantity, btnSettle, btnRePrint, btnVoid;
    String accessToken;
    public static String defaultQuantity = "1";
    private static int count = 1;
    int selectedListPosition;

    long date;
    Calendar calendar;
    String qrString, eventsDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerViewPlu = findViewById(R.id.recyclerViewPlu);
        recyclerViewSelectedItem = findViewById(R.id.recyclerViewSelectedItemList);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvSelectedTotalQuantity = findViewById(R.id.tvSelectedTotalQuantity);
        btnAddQuantity = findViewById(R.id.btnAddQuantity);
        btnRemoveQuantity = findViewById(R.id.btnRemoveQuantity);
        btnSettle = findViewById(R.id.btnSettle);
        btnRePrint = findViewById(R.id.btnRePrint);
        btnVoid = findViewById(R.id.btnVoid);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        pluModel = new PluModel();
        pluTaxesModel = new PluTaxesModel();
        selectedItemModel = new SelectedItemModel();
        accessToken = sessionManager.getAccessToken();

        /*RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerViewPlu.setLayoutManager(layoutManager);
        recyclerViewPlu.setAdapter(pluDetailsAdapter);*/

        recyclerViewPlu.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        ItemOffsetDecoration offsetDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        recyclerViewPlu.addItemDecoration(offsetDecoration);
        recyclerViewPlu.setAdapter(pluDetailsAdapter);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerViewSelectedItem.setLayoutManager(layoutManager1);
        recyclerViewSelectedItem.setAdapter(selectedItemListAdapter);
        recyclerViewSelectedItem.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        if (Constant.isNetworkAvailable(MainActivity.this)) {

            new GetPluDetails().execute();

        }

        date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        eventsDate = dateFormat.format(date);

        /*SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        dateForPrinting = dateFormat1.format(date);
        //Toast.makeText(this, " "+dateStr, Toast.LENGTH_SHORT).show();

        calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // Time format
        timeForPrinting = timeFormat.format(calendar.getTime());
        //Toast.makeText(this, " "+timeForPrinting, Toast.LENGTH_SHORT).show();*/

        btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedItemModelList.size() == 0) {

                } else {

                    SelectedItemModel itemModel = selectedItemModelList.get(selectedListPosition);
                    count = Integer.parseInt(itemModel.getQuantity());
                    count++;

                    String price = "" + (count * Double.parseDouble(itemModel.getTotalAmount()));
                    addSelectedQuantity(String.valueOf(count), price);
                    Double temp_amount = 0.0;
                    for (int i = 0; i < selectedItemModelList.size(); i++) {
                        SelectedItemModel itemModel1 = selectedItemModelList.get(i);
                        temp_amount = temp_amount + Double.parseDouble(itemModel1.getAmount());
                    }

                    tvTotalAmount.setText(Constant.donDashamlaDakhava("" + temp_amount));
                    recyclerViewSelectedItem.smoothScrollToPosition(selectedListPosition);
                }
            }
        });

        btnRemoveQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (count > 1) {
                        SelectedItemModel itemModel = selectedItemModelList.get(selectedListPosition);
                        count = Integer.parseInt(itemModel.getQuantity());
                        count--;

                        String price = "" + (count * Double.parseDouble(itemModel.getTotalAmount()));
                        String totalAmount = tvTotalAmount.getText().toString();
                        double subValue = Double.parseDouble(totalAmount) - Double.parseDouble(itemModel.getTotalAmount());
                        tvTotalAmount.setText(Constant.donDashamlaDakhava("" + subValue));
                        removeSelectedQuantity(String.valueOf(count), price);

                    } else {
                        if (selectedItemModelList.size() == 0){
                        } else {
                            try {
                                SelectedItemModel itemModel = selectedItemModelList.get(selectedListPosition);
                                String totalAmount = tvTotalAmount.getText().toString();
                                double subValue = Double.parseDouble(totalAmount) - Double.parseDouble(itemModel.getTotalAmount());
                                tvTotalAmount.setText(Constant.donDashamlaDakhava("" + subValue));
                                deleteSelectedItem(selectedListPosition);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        recyclerViewSelectedItem.smoothScrollToPosition(selectedListPosition);
                    }
                }
        });

        btnSettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedItemModelList.size() == 0){
                    Toast.makeText(MainActivity.this, "Please select a PLU", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(MainActivity.this, SettlementActivity.class);
                    intent.putExtra("totalPayableAmount",tvTotalAmount.getText().toString());
                    intent.putExtra("mySelectedItemList", (Serializable) selectedItemModelList);

                    startActivity(intent);
                }



                /*if (Constant.isNetworkAvailable(MainActivity.this)) {

                    //Toast.makeText(MainActivity.this, "Printing start", Toast.LENGTH_SHORT).show();

                    //generateRideWiseBilling();

                    new GetFOPDetails().execute();

                    openFOPLayout();

                }*/

            }
        });

        btnVoid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                //intentIntegrator.initiateScan(Collections.singleton("12345678"));
                intentIntegrator.initiateScan();*/

            }
        });

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {

            //If QR Code has nothing in it
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "Result not found", Toast.LENGTH_SHORT).show();
            } else {
                //If QR Code contains data
                qrString = intentResult.getContents();

                if (qrString.equals("12345678")) {
                    Toast.makeText(this, "Api called", Toast.LENGTH_SHORT).show();
                }

            }

        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    private class GetPluDetails extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        int responseCode;
        String responseMessage;
        boolean error = false;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(MainActivity.this);
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
                url = new URL("http://13.233.226.60/AmuzeTest/" + Constant.url_Get);
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

            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    id = jsonObject.getString("id");
                    pluCode = jsonObject.getString("pluCode");
                    pluName = jsonObject.getString("pluName");
                    pluType = jsonObject.getString("pluType");
                    hsn = jsonObject.getString("hsn");
                    pluPrice = jsonObject.getString("price");
                    deposit = jsonObject.getString("deposit");
                    eventTypeID = jsonObject.getString("eventTypeID");
                    resourceID = jsonObject.getString("resourceID");
                    eventID = jsonObject.getString("eventID");
                    quantity = jsonObject.getString("quantity");
                    discountPercent = jsonObject.getString("discountPercent");
                    discountAmount = jsonObject.getString("discountAmount");
                    //taxes = jsonObject.getString("taxes");

                    double totalTaxPercentage = 0.0;
                    JSONArray jsonTaxesArray = jsonObject.getJSONArray("taxes");
                    for (int j = 0; j < jsonTaxesArray.length(); j++) {
                        JSONObject jsonTaxes = jsonTaxesArray.getJSONObject(j);
                        taxID = jsonTaxes.getString("taxID");
                        taxDescription = jsonTaxes.getString("taxDescription");
                        taxPercent = jsonTaxes.getString("taxPercent");

                        pluTaxesModelList.add(new PluTaxesModel(taxID, taxDescription, taxPercent));
                        pluChildItemsModelList.add(new PluChildItemsModel());

                        totalTaxPercentage = totalTaxPercentage + Double.parseDouble(taxPercent);

                    }

                    childItems = jsonObject.getString("childItems");

                    /*JSONArray jsonChildItemsArray = jsonObject.getJSONArray(childItems);
                    for (int k = 0; k < jsonChildItemsArray.length(); k++){

                         JSONObject jsonChildItemsObject = jsonChildItemsArray.getJSONObject(k);

                    }*/

                    double totalTaxAmount = Double.parseDouble(pluPrice) * totalTaxPercentage / 100;
                    double totalAmount = Double.parseDouble(pluPrice) + totalTaxAmount - Double.parseDouble(discountAmount);

                    pluModelList.add(new PluModel(id, pluCode, pluName, pluType, hsn, pluPrice, deposit, eventTypeID, resourceID, eventID, quantity,
                            discountPercent, discountAmount, pluTaxesModelList, pluChildItemsModelList, ""+ totalAmount));

                    //pluDetailsAdapter = new PluDetailsAdapter(getApplicationContext(), pluModelList, this);
                    pluDetailsAdapter = new PluDetailsAdapter(getApplicationContext(), pluModelList);
                    recyclerViewPlu.setAdapter(pluDetailsAdapter);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public class PluDetailsAdapter extends RecyclerView.Adapter<PluDetailsAdapter.MyViewHolder> {

        private Context context;
        List<PluModel> pluModelList;

        public PluDetailsAdapter(Context context, List<PluModel> pluModelList) {
            this.context = context;
            this.pluModelList = pluModelList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvPluName, tvPluPrice;
            CardView cvPluDetails;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tvPluName = itemView.findViewById(R.id.tvPluName);
                tvPluPrice = itemView.findViewById(R.id.tvPluPrice);
                cvPluDetails = itemView.findViewById(R.id.cvPluDetails);

            }

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(context).inflate(R.layout.plu_show_layout, parent, false);

            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            final PluModel pluModel = pluModelList.get(position);
            holder.tvPluName.setText(pluModel.getPluName());
            holder.tvPluPrice.setText(pluModel.getPrice());

            holder.cvPluDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!pluModel.getResourceID().equals("null") && !pluModel.getEventTypeID().equals("null")) {

                        if (Constant.isNetworkAvailable(MainActivity.this)) {

                            /*new ExecuteGetEvents(pluModel.getResourceID(), pluModel.getEventTypeID(), eventsDate,
                                    pluModel.getPluCode(), pluModel.getPluName(), pluModel.getPrice()).execute();*/

                            new ExecuteGetEvents(pluModel.getId(), pluModel.getPluCode(), pluModel.getPluName(), pluModel.getPluType(), pluModel.getHsn(),
                                    pluModel.getPrice(), pluModel.getDeposit(), pluModel.getEventTypeID(), pluModel.getResourceID(), pluModel.getEventID(),
                                    defaultQuantity, pluModel.getDiscountPercent(), pluModel.getDiscountAmount(), pluModel.getTaxes(), pluModel.getChildItems(),
                                    eventsDate, pluModel.getTotalAmount(), pluModel.getTotalAmount()).execute();

                        }

                    } else {

                        double amount = Double.parseDouble(defaultQuantity) * Double.parseDouble(pluModel.getPrice());
                        String pluAmount = String.valueOf(amount);

                        //selectedItemModelList.add(new SelectedItemModel(pluModel.getPluCode(), pluModel.getPluName(), defaultQuantity, pluModel.getPrice(), pluAmount));

                        selectedItemModelList.add(new SelectedItemModel(pluModel.getId(), pluModel.getPluCode(), pluModel.getPluName(), pluModel.getPluType(), pluModel.getHsn(),
                                pluModel.getPrice(), pluModel.getDeposit(), pluModel.getEventTypeID(), pluModel.getResourceID(), pluModel.getEventID(),
                                defaultQuantity, pluModel.getDiscountPercent(), pluModel.getDiscountAmount(), pluModel.getTaxes(), pluModel.getChildItems(),
                                pluModel.getTotalAmount(), pluModel.getTotalAmount()));

                        selectedItemListAdapter = new SelectedItemListAdapter(getApplicationContext(), selectedItemModelList);


                        recyclerViewSelectedItem.setAdapter(selectedItemListAdapter);
                        //tvTotalAmount.setText(pluModel.getPrice());
                        tvTotalAmount.setText(Constant.donDashamlaDakhava(pluModel.getTotalAmount()));
                        //tvSelectedTotalQuantity.setText("Qty "+ "(" + defaultQuantity + ")");

                        selectedListPosition = selectedItemListAdapter.getItemCount() - 1;
                        addAmountFromPLU();
                        recyclerViewSelectedItem.smoothScrollToPosition(selectedItemListAdapter.getItemCount()-1);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return pluModelList.size();
        }
    }

    private void addAmountFromPLU() {
        if (selectedItemModelList.size() == 0) {

        } else {

            Double temp_amount = 0.0;

            for (int i = 0; i < selectedItemModelList.size(); i++) {
                SelectedItemModel itemModel1 = selectedItemModelList.get(i);
                temp_amount = temp_amount + Double.parseDouble(itemModel1.getTotalAmount());
            }

            tvTotalAmount.setText(Constant.donDashamlaDakhava("" + temp_amount));
        }
    }


    //SelectedItemListAdapter
    public class SelectedItemListAdapter extends RecyclerView.Adapter<SelectedItemListAdapter.MySelectedItem> {

        private Context context;
        private List<SelectedItemModel> selectedItemModelList;
        private int selectedItem = -1;

        public SelectedItemListAdapter(Context context, List<SelectedItemModel> selectedItemModelList) {
            this.context = context;
            this.selectedItemModelList = selectedItemModelList;
        }

        public class MySelectedItem extends RecyclerView.ViewHolder {

            TextView tvItemName, tvQuantity, tvRate, tvAmount, tvTaxAmount;
            RelativeLayout relySelectedItem;

            public MySelectedItem(@NonNull final View itemView) {
                super(itemView);

                tvItemName = itemView.findViewById(R.id.tvItemName);
                tvQuantity = itemView.findViewById(R.id.tvQuantity);
                tvRate = itemView.findViewById(R.id.tvRate);
                tvAmount = itemView.findViewById(R.id.tvAmount);
                tvTaxAmount = itemView.findViewById(R.id.tvTaxAmount);
                relySelectedItem = itemView.findViewById(R.id.relySelectedItem);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        selectedListPosition = getAdapterPosition();
                        selectedItemListAdapter.notifyDataSetChanged();

                        if (selectedListPosition != RecyclerView.NO_POSITION) {
/*
                            SelectedItemModel itemModel = selectedItemModelList.get(selectedListPosition);
                            selectedQty = Integer.parseInt(itemModel.getQuantity());
                            String price = itemModel.getPrice();
                            selectedItemPrice = Double.parseDouble(price);
*/
                            //Toast.makeText(MainActivity.this, "" + selectedItemPrice, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }

        @NonNull
        @Override
        public MySelectedItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(context).inflate(R.layout.selected_item_layout, parent, false);
            return new MySelectedItem(rootView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MySelectedItem holder, final int position) {


            final SelectedItemModel selectedItemModel = selectedItemModelList.get(position);
            holder.tvItemName.setText(selectedItemModel.getPluName());
            holder.tvQuantity.setText(selectedItemModel.getQuantity());
            holder.tvRate.setText(selectedItemModel.getPrice());
            PluTaxesModel pluTaxesModel = pluTaxesModelList.get(selectedListPosition);
            double totalTaxPercentage = Double.parseDouble(pluTaxesModel.getTaxPercent()) + Double.parseDouble(pluTaxesModel.getTaxPercent());

            holder.tvTaxAmount.setText(""+totalTaxPercentage+" %");
            holder.tvAmount.setText(Constant.donDashamlaDakhava(selectedItemModel.getAmount()));

            if (selectedListPosition == position) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.html_dark_grey));

            } else {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.light_cream_color));

            }
        }

        @Override
        public int getItemCount() {
            return selectedItemModelList.size();
        }

    }

    public void addSelectedQuantity(String selectedQuantity, String selectedItemAmount) {
        SelectedItemModel selectedItemModel = selectedItemModelList.get(selectedListPosition);
        selectedItemModel.setQuantity(selectedQuantity);
        selectedItemModel.setAmount(selectedItemAmount);
        selectedItemModelList.set(selectedListPosition, selectedItemModel);
        selectedItemListAdapter.notifyItemChanged(selectedListPosition);

    }

    public void removeSelectedQuantity(String selectedQuantity, String selectedItemAmount) {
        SelectedItemModel selectedItemModel = selectedItemModelList.get(selectedListPosition);
        selectedItemModel.setQuantity(selectedQuantity);
        selectedItemModel.setAmount(selectedItemAmount);
        selectedItemModelList.set(selectedListPosition, selectedItemModel);
        selectedItemListAdapter.notifyItemChanged(selectedListPosition);

    }

    public void deleteSelectedItem(int position){
        selectedItemModelList.remove(position);
        selectedItemListAdapter.notifyItemRemoved(position);
        //selectedItemListAdapter.notifyItemRangeRemoved(position, selectedItemModelList.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                sessionManager.logoutUser();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ExecuteGetEvents extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        int responseCode;
        String responseMessage;
        boolean error = false;
        //String resourceID, eventTypeID, eventsDate, pluCode, pluName, pluPrice;
        String id,pluCode, pluName, pluType, hsn, price, deposit, eventTypeID, resourceID, eventID, quantity, discountPercent, discountAmount;
        String eventsDate, amount, totalAmount;
        List<PluTaxesModel> taxes;
        List<PluChildItemsModel> childItems;

        public ExecuteGetEvents(String id, String pluCode, String pluName, String pluType, String hsn, String price, String deposit, String eventTypeID, String resourceID, String eventID,
                                String quantity, String discountPercent, String discountAmount, List<PluTaxesModel> taxes, List<PluChildItemsModel> childItems, String eventsDate, String amount, String totalAmount){

            this.id = id;
            this.pluCode = pluCode;
            this.pluName = pluName;
            this.pluType = pluType;
            this.hsn = hsn;
            this.price = price;
            this.deposit = deposit;
            this.eventTypeID = eventTypeID;
            this.resourceID = resourceID;
            this.eventID = eventID;
            this.quantity = quantity;
            this.discountPercent = discountPercent;
            this.discountAmount = discountAmount;
            this.taxes = taxes;
            this.childItems = childItems;
            this.eventsDate = eventsDate;
            this.amount = amount;
            this.totalAmount = totalAmount;

        }

        /*public ExecuteGetEvents(String resourceID, String eventTypeID, String eventsDate, String pluCode, String pluName, String pluPrice) {
            this.resourceID = resourceID;
            this.eventTypeID = eventTypeID;
            this.eventsDate = eventsDate;
            this.pluCode = pluCode;
            this.pluName = pluName;
            this.pluPrice = pluPrice;
        }*/

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
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
                url = new URL("http://13.233.226.60/AmuzeTest/" + Constant.url_Get_Events
                        + "?ResourceID=" + resourceID + "&EventTypeID=" + eventTypeID + "&Date=" + eventsDate);
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

            if(error){
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() == 0) {
                        Toast.makeText(MainActivity.this, "" + result, Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String eventId = jsonObject.getString("id");
                            String eventName = jsonObject.getString("eventName");
                            String resourceName = jsonObject.getString("resourceName");
                            String eventTypeName = jsonObject.getString("eventTypeName");
                            String eventStartDate = jsonObject.getString("eventStartDate");
                            String eventEndDate = jsonObject.getString("eventEndDate");
                            String saleStartDate = jsonObject.getString("saleStartDate");
                            String saleEndDate = jsonObject.getString("saleEndDate");
                            String availableCapacity = jsonObject.getString("availableCapacity");
                            String saleStatus = jsonObject.getString("saleStatus");

                            /*double amount = Double.parseDouble(defaultQuantity) * Double.parseDouble(pluPrice);
                            String pluAmount = String.valueOf(amount);*/

                            //selectedItemModelList.add(new SelectedItemModel(pluCode, pluName, defaultQuantity, pluPrice, pluAmount));
                            selectedItemModelList.add(new SelectedItemModel(id, pluCode, pluName, pluType, hsn, price, deposit, eventTypeID, resourceID, eventID,
                                    quantity, discountPercent, discountAmount, taxes, childItems, amount, totalAmount));
                            selectedItemListAdapter = new SelectedItemListAdapter(getApplicationContext(), selectedItemModelList);

                            recyclerViewSelectedItem.setAdapter(selectedItemListAdapter);

                            //tvTotalAmount.setText(pluPrice);
                            //tvTotalAmount.setText(totalAmount);

                            selectedListPosition = selectedItemListAdapter.getItemCount() - 1;
                            addAmountFromPLU();
                            recyclerViewSelectedItem.smoothScrollToPosition(selectedItemListAdapter.getItemCount()-1);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, "No events found", Toast.LENGTH_SHORT).show();
                    Constant.generateSimpleDialog(MainActivity.this, "No events found");
                }
            }
        }
    }

    private void openFOPLayout() {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.fop_selection, null);

        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();

        TextView tvCash, tvCard, tvSVCoupon;
        final EditText etCash, etCardDetails;
        Button btnSettleFop;
        CardView cardViewFops;
        final TextInputLayout tilCashAmount, tilCardDetails;



        btnSettleFop = popupView.findViewById(R.id.btnSettleFop);
        tvCash = popupView.findViewById(R.id.tvCash);
        tvCard = popupView.findViewById(R.id.tvCard);
        tvSVCoupon = popupView.findViewById(R.id.tvSVCoupon);
        etCash = popupView.findViewById(R.id.etCash);
        etCardDetails = popupView.findViewById(R.id.etCardDetails);
        tilCashAmount = popupView.findViewById(R.id.tilCashAmount);
        tilCardDetails = popupView.findViewById(R.id.tilCardDetails);
        RelativeLayout relyViewBg = popupView.findViewById(R.id.relyViewBg);
        cardViewFops = popupView.findViewById(R.id.cardViewFops);

        btnSettleFop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txtCash = etCash.getText().toString().trim();
                String txtCard = etCardDetails.getText().toString().trim();
                //SVCoupon amount qrString
                String txtTotalAmount = tvTotalAmount.getText().toString();

                if (txtTotalAmount.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please select item", Toast.LENGTH_SHORT).show();
                } else {

                    double totalPayableAmt = Double.parseDouble(txtCash) + Double.parseDouble(txtCard) ; /*+ Double.parseDouble(qrString)*/;
                    if (String.valueOf(totalPayableAmt).equals(txtTotalAmount)) {
                        date = System.currentTimeMillis();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        eventsDate = dateFormat.format(date);
                        Toast.makeText(MainActivity.this, "eventsDate: "+eventsDate, Toast.LENGTH_SHORT).show();

                        if (Constant.isNetworkAvailable(MainActivity.this)) {

                            //new GetAddOrders(eventsDate).execute();

                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Please enter correct amount", Toast.LENGTH_SHORT).show();
                    }





                }

            }
        });

        cardViewFops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        relyViewBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }

    private class GetAddOrders  extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        int responseCode;
        String responseMessage;
        boolean error = false;
        String currentDateAndTime;

        public GetAddOrders(String currentDateAndTime){
            this.currentDateAndTime = currentDateAndTime;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
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
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                //connection.setReadTimeout(Constant.READ_TIMEOUT);
                connection.setConnectTimeout(60000);
                //connection.setDoOutput(true);
                connection.setDoInput(true);

            } catch (IOException e) {
                error = true;
                e.printStackTrace();
            }

            try {
                JSONObject jsonOrder = new JSONObject("Order");
                jsonOrder.put("TransactionDate", currentDateAndTime);
                jsonOrder.put("VisitDate", currentDateAndTime);
                jsonOrder.put("ExternalID", "");
                jsonOrder.put("AgentName", "");
                jsonOrder.put("SalesPerson", "");
                jsonOrder.put("PromotionCode", "");
                jsonOrder.put("Channel", "");
                jsonOrder.put("Remarks", "");
                jsonOrder.put("UserColumn1", "");
                jsonOrder.put("UserColumn2","");
                jsonOrder.put("UserColumn3","");
                jsonOrder.put("UserColumn4","");
                jsonOrder.put("UserColumn5","");

                JSONObject jsonContact = new JSONObject("Contact");
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
                jsonContact.put("MobileNumber","");
                jsonContact.put("Email", "");
                jsonOrder.put("Contact", jsonContact);

                JSONObject jsonItems = new JSONObject("items");
                JSONObject jsonItem = jsonItems.getJSONObject("Item");
                for (int i = 0; i < selectedItemModelList.size(); i++){
                    String pluId = selectedItemModelList.get(i).getId();
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

                    JSONObject jsonTaxes = new JSONObject("Taxes");
                    JSONArray jsonTax = jsonTaxes.getJSONArray("Tax");
                    for (int j = 0; j < jsonTax.length(); j++){
                        JSONObject jsonTaxObject = jsonTax.getJSONObject(j);

                        for (int k = 0; k < pluTaxesModelList.size(); k++){
                            String taxId = pluTaxesModelList.get(k).getTaxID();
                            String taxDescription  = pluTaxesModelList.get(k).getTaxDescription();
                            String taxPercent = pluTaxesModelList.get(k).getTaxPercent();

                            jsonTaxObject.put("TaxID", taxId);
                            jsonTaxObject.put("TaxDescription", taxDescription);
                            jsonTaxObject.put("TaxPercent", taxPercent);

                            jsonTaxes.put("Taxes", jsonTaxObject);

                        }
                    }
                    jsonItems.put("Item", jsonItem);
                }

                jsonOrder.put("items", jsonItems);

                JSONObject jsonPayments = new JSONObject("Payments");
                JSONObject jsonPaymentDetails = new JSONObject("Payment");







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
    }

    private class GetFOPDetails extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        int responseCode;
        String responseMessage;
        boolean error = false;
        String currentDateAndTime;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
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

            if(error){
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }else {

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String fopId = jsonObject.getString("id");
                        String fopType = jsonObject.getString("fopType");

                        fopModelList.add(new FopModel(fopId, fopType));



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }



}
