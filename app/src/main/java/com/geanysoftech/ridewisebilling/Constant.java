package com.geanysoftech.ridewisebilling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DecimalFormat;

public class Constant {


    public static final int READ_TIMEOUT = 60000;
    public static final int CONNECT_TIMEOUT = 60000;


    public static final String url_Authenticate = "/api/token/authenticate";
    public static final String url_Get = "/api/plu/get";
    public static final String url_Get_Events = "/api/events/get";
    public static final String url_Get_Orders = "/api/orders/Add";
    public static final String url_Get_Fops = "/api/fops/get";
    public static final String url_Get_Tickets = "/api/orders/GetTickets";
    public static final String url_Get_SVDetails = "/api/StoredValues/Get";
    public static final String url_Get_SVTransactions = "/api/StoredValues/Transactions";


    public static boolean isNetworkAvailable(Activity context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;

    }

    /*public static String donDashamlaDakhava(String value) {
        try {
            if (value == null || value.equals("null")){
                return "";
            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                String formatted = df.format(value);
                return formatted;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }*/

    public static String donDashamlaDakhava(String value) {
        try {
            if (value == null || value.equals("null")) return "";
            return String.format("%.2f", Double.parseDouble(value));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void generateSimpleDialog(Activity context, String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialoga, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialoga.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialoga.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton("OK", dialogClickListener).show();
        builder.setCancelable(false);
    }

    /*If your taxrate is say x%, and your total amount is $y.

    GST = y - y/(1+x/100)

    Of course, if your taxrate is not in percent but is a fraction between 0 and 1, the formula gets

    GST = y - y/(1+x)
*/
}
