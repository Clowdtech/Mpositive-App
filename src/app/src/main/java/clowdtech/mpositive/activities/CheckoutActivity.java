package clowdtech.mpositive.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.till.activities.TillActivity;
import clowdtech.mpositive.easydata.AppPreferences;
import clowdtech.mpositive.easydata.FrgRegistration;
import clowdtech.mpositive.easydata.RestWSClient;

public class CheckoutActivity extends FragmentActivity {

    public static String REGISTRATION_DETAILS = "clowdtech.mpositive.activities.REGISTRATION_DETAILS";
    JSONObject jRegistrationDetails;

    TextView lblName;
    TextView lblEmail;
    TextView lblPhone;
    TextView lblAddress;

    Button btnBack;
    Button btnEdit;
    Button btnCheckoutNext;

    RegReceiver regReceiver;

    ProgressDialog _progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Bundle actParams = getIntent().getExtras();

        if (actParams.containsKey(REGISTRATION_DETAILS)) {
            try {
                jRegistrationDetails = new JSONObject(actParams.getString(REGISTRATION_DETAILS));
            }
            catch (JSONException e) {
                jRegistrationDetails = null;
            }
        }
        else
            Toast.makeText(CheckoutActivity.this, "Unable to load registration details.", Toast.LENGTH_SHORT);

        init();
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(FrgRegistration.REG_OK);
        iFilter.addAction(FrgRegistration.REG_FAILED);

        regReceiver = new RegReceiver();

        try { registerReceiver(regReceiver, iFilter);
        } catch (Exception e) { }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (_progressDialog != null && _progressDialog.isShowing())
            _progressDialog.dismiss();

        if (regReceiver != null)
            unregisterReceiver(regReceiver);

    }

    private void showProgress() {
        if (_progressDialog == null) {
            _progressDialog = ProgressDialog.show(CheckoutActivity.this,
                    "Processing",
                    "Please wait&#8230;", true);

            _progressDialog.show();
        }

        if (!_progressDialog.isShowing())
            _progressDialog.show();
    }

    private void init() {
        btnBack = (Button) findViewById(R.id.btnBack);
        btnEdit = (Button) findViewById(R.id.btnEdit);

        lblName = (TextView) findViewById(R.id.lblName);
        lblEmail = (TextView) findViewById(R.id.lblEmail);
        lblPhone = (TextView) findViewById(R.id.lblPhone);
        lblAddress = (TextView) findViewById(R.id.lblAddress);

        if (jRegistrationDetails != null) {
            try {
                lblName.setText(jRegistrationDetails.getString(RestWSClient.REG_NAME));
                lblEmail.setText(jRegistrationDetails.getString(RestWSClient.REG_EMAIL));
                lblPhone.setText(jRegistrationDetails.getString(RestWSClient.REG_PHONE));
                lblAddress.setText(jRegistrationDetails.getString(RestWSClient.REG_ADDRESS));
            }
            catch (JSONException e) {
                Log.e("Error CheckoutActivity", e.getMessage());
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCheckoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                FrgRegistration.StartRegProcess(CheckoutActivity.this, jRegistrationDetails.toString());
            }
        });
    }

    public class RegReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context _context, Intent _intent) {
            if (_progressDialog != null && _progressDialog.isShowing())
                _progressDialog.dismiss();

            if(_intent.getAction() == FrgRegistration.REG_OK) {
                Intent intent = new Intent(getBaseContext(), TillActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(CheckoutActivity.this, "An error has occured during registration. " + AppPreferences.get(CheckoutActivity.this, AppPreferences.LAST_REGISTRATION_ERROR, ""), Toast.LENGTH_LONG).show();
                AppPreferences.set(CheckoutActivity.this, AppPreferences.LAST_REGISTRATION_ERROR, "");
                finish();
            }

        }
    }
}
