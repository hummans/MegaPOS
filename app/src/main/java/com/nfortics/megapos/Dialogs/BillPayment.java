package com.nfortics.megapos.Dialogs;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nfortics.megapos.Application.MyApplication;
import com.nfortics.megapos.Helper.ControlHelpers;
import com.nfortics.megapos.Helper.Message;
import com.nfortics.megapos.Helper.Useful;
import com.nfortics.megapos.Models.TransHistory;
import com.nfortics.megapos.Models.TransHistoryDetails;
import com.nfortics.megapos.Network.ConnectionDetector;
import com.nfortics.megapos.Network.VolleySingleton;
import com.nfortics.megapos.R;
import com.nfortics.megapos.ThirrtySevenSignals.RootAccess;
import com.nfortics.megapos.utils.GsonMan;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillPayment extends DialogFragment {

    private OnbuttonClickListener mListener;

    RelativeLayout
            inputValuesLayout1,
            inputValuesLayout,
            finalButtonLayout,
            confirmLayout;

    Spinner telcosSpinners,topUpModeSpinners;
    TextView dialogTitle,providersLabel,topUpModeLabel;

    TextView
            confirmLabel1,
            confirmLabel2,
            confirmLabel3,
            confirmLabel4;

    Button butSubmit,butClose,
            confirmYesButton,
            confirmNoButton;

    EditText BillReference,
            ByCashamount,
            FrAccCashcustomerMobileNumber,
            FrAccCashamount,FrAccCashAccNumber;

    ControlHelpers controlHelpers;
    Useful useful;
    String newTitle;
    TransHistory transHistory;
    TransHistoryDetails transHistoryDetails;
    public BillPayment() {
        // Required empty public constructor
    }
    Bundle extras;
    String  userName;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    ConnectionDetector cd ;
    RootAccess rootAccess;
    GsonMan gsonMan;
    JSONObject jsonObject;


    public  String  billReference,Amount,provider;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            extras = getActivity().getIntent().getExtras();
            extras = getActivity().getIntent().getExtras();


            if(extras == null) {

            }else {


                userName= extras.getString("UserDetails");

            }
            mListener = (OnbuttonClickListener) activity;
        } catch (ClassCastException e) {

        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlHelpers=new ControlHelpers();
        useful=new Useful();
        jsonObject = new JSONObject();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_bill_payment, container, false);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if(manager.findFragmentByTag(tag)==null){
            super.show(manager, tag);
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitializeView(view);
        setDialogTitle("BILL PAYMENT");
        InitializeSpinners();
        InitializeOnClickListeners();
    }
    public void InitializeView(View view){

        inputValuesLayout1=(RelativeLayout)view.findViewById(R.id.inputValuesLayout1);
        inputValuesLayout=(RelativeLayout)view.findViewById(R.id.inputValuesLayout);
        finalButtonLayout=(RelativeLayout)view.findViewById(R.id.finalButtonLayout);
        confirmLayout=(RelativeLayout)view.findViewById(R.id.confirmLayout);

        telcosSpinners = (Spinner)view.findViewById(R.id.spinner1);
        topUpModeSpinners= (Spinner)view.findViewById(R.id.topUpModeSpinner);
        dialogTitle= (TextView)view.findViewById(R.id.dialogTitle);


        butSubmit=(Button)view.findViewById(R.id.butSubmit);
        butClose=(Button)view.findViewById(R.id.butClose);

        confirmYesButton=(Button)view.findViewById(R.id.confirmButtonYes);
        confirmNoButton=(Button)view.findViewById(R.id.confirmButtonNo);

        providersLabel=(TextView)view.findViewById(R.id.txtProvidersLabel);
        topUpModeLabel=(TextView)view.findViewById(R.id.txtTopUpModeLabel);


        confirmLabel1=(TextView)view.findViewById(R.id.confirm1);
        confirmLabel2=(TextView)view.findViewById(R.id.confirm2);
        confirmLabel3=(TextView)view.findViewById(R.id.confirm3);
        confirmLabel4=(TextView)view.findViewById(R.id.confirm4);


        BillReference=(EditText)view.findViewById(R.id.editCustomerMnumber);
        ByCashamount=(EditText)view.findViewById(R.id.edtEnterAmount);

        FrAccCashcustomerMobileNumber=(EditText)view.findViewById(R.id.AeditCustomerMnumber);
        FrAccCashamount=(EditText)view.findViewById(R.id.AedtEnterAmount);
        FrAccCashAccNumber=(EditText)view.findViewById(R.id.AedtEnterAmount);
        topUpModeSpinners.setEnabled(false);
    }
    public void setDialogTitle(String title){

        dialogTitle.setText(title);
    }
    public void SetChangeListenersForSpinners(){

        setProvidersSpinnerChangeListener();

    }
    void setProvidersSpinnerChangeListener(){

        telcosSpinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (parent.getSelectedItem().toString()) {


                    case " ":
                        topUpModeSpinners.setSelection(0);
                        topUpModeSpinners.setEnabled(false);
                        inputValuesLayout1.setVisibility(View.GONE);
                        inputValuesLayout.setVisibility(View.GONE);
                        finalButtonLayout.setVisibility(View.GONE);
                        break;

                    case "....":
                        topUpModeSpinners.setSelection(0);
                        topUpModeSpinners.setEnabled(false);
                        inputValuesLayout1.setVisibility(View.GONE);
                        inputValuesLayout.setVisibility(View.GONE);
                        finalButtonLayout.setVisibility(View.GONE);
                        break;

                    default:
                        topUpModeSpinners.setEnabled(true);
                        setTopUpModeSpinnerChangeListener();
                        break;


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                topUpModeSpinners.setEnabled(false);
            }
        });

    }
    void setTopUpModeSpinnerChangeListener(){

        topUpModeSpinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                switch (parent.getItemAtPosition(position).toString()) {

                    case "By Cash":
                        inputValuesLayout1.setVisibility(View.GONE);
                        inputValuesLayout.setVisibility(View.VISIBLE);
                        finalButtonLayout.setVisibility(View.VISIBLE);
                        setCancelable(false);

                        break;
                    case "From Account":
                        inputValuesLayout.setVisibility(View.GONE);
                        inputValuesLayout1.setVisibility(View.VISIBLE);
                        finalButtonLayout.setVisibility(View.VISIBLE);
                        setCancelable(false);
                        break;

                    case "....":
                        inputValuesLayout1.setVisibility(View.GONE);
                        inputValuesLayout.setVisibility(View.GONE);
                        finalButtonLayout.setVisibility(View.GONE);
                        setCancelable(true);
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    public void InitializeOnClickListeners(){
        SubmitButtonClickListener();
        CloseButtonClickListener();
        confirmNoButtonClickListener();
        confirmYesButtonClickListener();
    }
    String BuildStringTopPRINT(){

        StringBuilder sb = new StringBuilder();
        sb.append(confirmLabel2.getText().toString() + "\n"
                + confirmLabel3.getText().toString() + "\n"
                + confirmLabel4.getText().toString());


        return sb.toString();
    }
    void SubmitButtonClickListener(){

        butSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    switch (topUpModeSpinners.getSelectedItem().toString()) {

                        case "By Cash":


                            if (ByCashamount.getText().toString().length() == 0 ||
                                    BillReference.getText().toString().length() == 0) {


                            } else {
                                ConfirmDialog(true, topUpModeSpinners.getSelectedItem().toString(),
                                        setGetLayout(topUpModeSpinners.getSelectedItem().toString()));
                                SetValuesToConfirmLabels("Bill Payment",
                                        telcosSpinners.getSelectedItem().toString(),
                                        BillReference.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(ByCashamount.getText().toString())));


                                ///SECOND SCREEN
                                mListener.BillPaySecondScreen("Bill Pay /" + topUpModeSpinners.getSelectedItem().toString(),
                                        "Provider :" + telcosSpinners.getSelectedItem().toString(),
                                        BillReference.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(ByCashamount.getText().toString())));
                                //mListener.CurrencyDetectOn();


                            }
                            break;
                        case "From Account":

                            if (BillReference.getText().toString().length() == 0 ||
                                    FrAccCashamount.getText().toString().length() == 0 ||
                                    FrAccCashAccNumber.getText().toString().length() == 0) {


                            } else {
                                ConfirmDialog(true, topUpModeSpinners.getSelectedItem().toString(),
                                        setGetLayout(topUpModeSpinners.getSelectedItem().toString()));

                                SetValuesToConfirmLabels("Bill Payment",
                                        telcosSpinners.getSelectedItem().toString(),
                                        BillReference.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(FrAccCashamount.getText().toString())));


                                ///SECOND SCREEN
                                mListener.BillPaySecondScreen("Bill Pay /" + topUpModeSpinners.getSelectedItem().toString(),
                                        "Provider :" + telcosSpinners.getSelectedItem().toString(),
                                        BillReference.getText().toString(),
                                        useful.formatMoney(Double.parseDouble(ByCashamount.getText().toString())));
                                //  mListener.CurrencyDetectOn();
                            }
                            break;

                        case "....":

                            break;
                    }


                } catch (Exception ex) {


                }


            }
        });

    }
    void CloseButtonClickListener(){

        butClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //mListener.BillPayCurrencyDetectOff();
            }
        });


    }
    void confirmYesButtonClickListener(){
        confirmYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //billReference,Amount,provider;
                billReference= BillReference.getText().toString();
                Amount=ByCashamount.getText().toString();
                provider= telcosSpinners.getSelectedItem().toString();
                PayBills(
                        billReference,
                        Amount,
                        provider,
                        MyApplication.getPayment_Provider(),
                        MyApplication.getApplicationClient(),
                        MyApplication.getApp_name(),
                        "",
                        MyApplication.getApplicationSeriousness() + "");


                //Dismiss();
            }
        });
    }
    void Dismiss(){
        dismiss();
        mListener.BillPayPrint("BillPay", provider, billReference, useful.formatMoney(Double.parseDouble(Amount)));
        //mListener.BillPaymentPrint(confirmLabel1.getText().toString(), BuildStringTopPRINT());
        mListener.BillPayPositiveSecondScreen();
    }
    void confirmNoButtonClickListener() {

        confirmNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog(false, "BILL PAYMENT", setGetLayout(newTitle));
                setDialogTitle("BILL PAYMENT");
                mListener.BillPayClearSecondScreen();

            }
        });
    }
    public  void InitializeSpinners(){



        controlHelpers.makeListForBillersSpinners(getActivity(), telcosSpinners, userName);


        List<String> data2=new ArrayList<>();
        data2.add("....");
        data2.add("By Cash");
        // data2.add("From Account");

        controlHelpers.populateSpinner(topUpModeSpinners, data2, getActivity());
        SetChangeListenersForSpinners();
    }
    void SetValuesToConfirmLabels(String one,String two,String three,String four){
        confirmLabel1.setText(one);
        confirmLabel2.setText("Provider :" + two);
        confirmLabel3.setText("Bill Ref  :" + three);
        confirmLabel4.setText("Total :" + four);
    }
    void HideThemSpinners(boolean value){

        if(value){

            telcosSpinners.setVisibility(View.GONE);
            topUpModeSpinners.setVisibility(View.GONE);
            providersLabel.setVisibility(View.GONE);
            topUpModeLabel.setVisibility(View.GONE);
            finalButtonLayout.setVisibility(View.GONE);
        }else{

            telcosSpinners.setVisibility(View.VISIBLE);
            topUpModeSpinners.setVisibility(View.VISIBLE);
            providersLabel.setVisibility(View.VISIBLE);
            topUpModeLabel.setVisibility(View.VISIBLE);
            finalButtonLayout.setVisibility(View.VISIBLE);
        }
    }
    void ConfirmDialog(boolean value,String type,RelativeLayout layout) {

        getSetDialogTitle(type);

        if(value){
            HideThemSpinners(true);
            layout.setVisibility(View.GONE);
            confirmLayout.setVisibility(View.VISIBLE);

            setDialogTitle("CONFIRM");
        }else{
            HideThemSpinners(false);
            layout.setVisibility(View.VISIBLE);
            confirmLayout.setVisibility(View.GONE);

        }

    }
    public RelativeLayout setGetLayout(String value){

        RelativeLayout relativeLayout=null;

        if(value.contains("By Cash")){

            relativeLayout= inputValuesLayout;
        }else if(value.contains("From Account")){

            relativeLayout= inputValuesLayout1;


        }else if(value.contains("")) {

            relativeLayout = inputValuesLayout;
        }
        return relativeLayout;
    }
    String getSetDialogTitle(String value){

        if(value.contains("By Cash")){

            newTitle="By Cash";
        }else if(value.contains("From Account")){

            newTitle="From Account";
        }else if(value.contains("....")){
            newTitle="";
        }else if(value.contains("")){
            newTitle="";
        }

        return newTitle;
    }
    TransHistory transHistory(){
        transHistory= new TransHistory();
        transHistory.setDate(useful.getDateWithFormatter());
        transHistory.setTime(useful.getTimeWithFormatter());
        transHistory.setTransID("0001");
        transHistory.setSellerID("Yaw Boafo");
        transHistory.setMerchantID("CCML");
        transHistory.setType("OffLine");
        transHistory.setDetailsID(useful.getTimeWithFormatter());
        transHistory.setModetype("Banker");
        transHistory.setTotalSale(confirmLabel4.getText().toString());
        return transHistory;
    }
    TransHistoryDetails transHistoryDetails(){

        transHistoryDetails = new TransHistoryDetails();
        transHistoryDetails.setDeTailsID(useful.getTimeWithFormatter());
        transHistoryDetails.setItems(BuildStringTopPRINT());
        transHistoryDetails.setSale("");


        return transHistoryDetails;
    }
    public void PayBills(String billReference,
                         String amount,
                         String serviceProvider,
                         String provider,
                         String client,
                         String application,
                         String country,
                         String test){

        final ProgressDialog pDialog  = new ProgressDialog(getActivity());
        pDialog.setMessage("Trying to Pay a Bill");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        final HashMap<String, String> params = new HashMap<String, String>();

        params.put("billReference",billReference);
        params.put("amount", amount);
        params.put("serviceProvider", serviceProvider);
        params.put("provider", provider);
        params.put("client", client);
        params.put("application",application);
        params.put("country", country);
        params.put("test", test);


        volleySingleton=VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                MyApplication.getBillpayUrl(),
                new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {

                            if(response.getString("success").toString().equals("true")){


                                Log.d("Response : ", response.toString());
                                Dismiss();

                            }else if(response.getString("success").toString().equals("false")){


                                controlHelpers.AnAlertertDialog(getActivity(),
                                        response.getString("message").toString(),"Sorry Try Again").show();

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }



                        //  Message.messageShort(MyApplication.getAppContext(),""+tokenValue+"\n"+response.toString()+"\n"+booleaner);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String ErrorMessage="Connection Failed !";
                String successMessage="ErrorMessage";


                try {
                    VolleyError earror = new VolleyError(new String(error.networkResponse.data));
                    JSONObject jobj = new JSONObject(earror.getMessage().toString());

                    ErrorMessage=jobj.getString("message");
                    successMessage=jobj.getString("success");
                    Log.e("oxinbo", earror.getMessage().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                pDialog.hide();

                controlHelpers.AnAlertertDialog(getActivity(),ErrorMessage,"Sorry Try Again").show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/json; charset=utf-8");
                headers.put("User-agent", "Ozinbo");
                return headers;
            }
        };
        int socketTimeout = 240000000;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);




    }


    public interface OnbuttonClickListener{
        public void BillPayPrint(String name,String provider,String account,String Amount);
        public void BillPayementButtonClickListener(String name, String price);
        public void BillPaymentPrint(String type,String things);
        public void BillPayInsertHistory(TransHistory transHistory,TransHistoryDetails transHistoryDetails);
        public void BillPaySecondScreen(String type,String provider,String Account, String amt);
        public void BillPayClearSecondScreen();
        public void BillPayPositiveSecondScreen();
        public void BillPayCurrencyDetectOn();
        public void BillPayCurrencyDetectOff();
    }

}
