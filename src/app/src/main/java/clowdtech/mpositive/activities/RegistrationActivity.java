package clowdtech.mpositive.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.till.activities.TillActivity;
import clowdtech.mpositive.easydata.AppPreferences;
import clowdtech.mpositive.easydata.FrgRegistration;
import clowdtech.mpositive.easydata.RestWSClient;

public class RegistrationActivity extends FragmentActivity {

    public static String ACCOUNT_TYPE_EXTRA = "ACCOUNT_TYPE_EXTRA";
    public static int ACCOUNT_TYPE_FREE = 0;
    public static int ACCOUNT_TYPE_PRO = 1;

    ProgressDialog _progressDialog = null;

    int accountType = 0;

    Button btnBack;
    Button btnRegistrationNext;

    EditText txtFullName;
    EditText txtCompanyName;
    EditText txtBusinessType;
    EditText txtEmailAddress;
    EditText txtPassword;
    EditText txtContactNumber;
    EditText txtAddress1;
    EditText txtAddress2;
    EditText txtTownCity;
    EditText txtPostCode;

    RegReceiver regReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        try {
            accountType = getIntent().getExtras().getInt(ACCOUNT_TYPE_EXTRA);
        }
        catch (Exception e) { accountType = 0; }

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
            _progressDialog = ProgressDialog.show(RegistrationActivity.this,
                    "Processing",
                    "Please wait&#8230;", true);

            _progressDialog.show();
        }

        if (!_progressDialog.isShowing())
            _progressDialog.show();
    }

    private void init() {
        btnBack = (Button) findViewById(R.id.btnBack);
        btnRegistrationNext = (Button) findViewById(R.id.btnRegistrationNext);

        txtFullName = (EditText) findViewById(R.id.txtFullName);
        txtCompanyName = (EditText) findViewById(R.id.txtCompanyName);
        txtBusinessType = (EditText) findViewById(R.id.txtBusinessType);
        txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtContactNumber = (EditText) findViewById(R.id.txtContactNumber);
        txtAddress1 = (EditText) findViewById(R.id.txtAddress1);
        txtAddress2 = (EditText) findViewById(R.id.txtAddress2);
        txtTownCity = (EditText) findViewById(R.id.txtTownCity);
        txtPostCode = (EditText) findViewById(R.id.txtPostCode);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegistrationNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtFullName.getText().toString();
                String companyName = txtCompanyName.getText().toString();
                String businessType = txtBusinessType.getText().toString();
                String email = txtEmailAddress.getText().toString();
                String phone = txtContactNumber.getText().toString();
                String password = txtPassword.getText().toString();
                String address = "";
                String address1 = txtAddress1.getText().toString();
                String address2 = txtAddress2.getText().toString();
                String townCity = txtTownCity.getText().toString();
                String postCode = txtPostCode.getText().toString();

                if (name.trim().isEmpty() || companyName.trim().isEmpty() || businessType.trim().isEmpty() || email.trim().isEmpty() ||
                        phone.trim().isEmpty() || password.trim().isEmpty() || address1.trim().isEmpty() || townCity.trim().isEmpty() ||
                        postCode.trim().isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please enter all the required fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    address += address1;
                    address += address2.isEmpty() ? "" : ", " + address2;
                    address += townCity.isEmpty() ? "" : ", " + townCity;
                    address += postCode.isEmpty() ? "" : ", " + postCode;
                }
                catch (Exception e) { }

                String json = RestWSClient.buildRegistrationJSONData(accountType, name, password, companyName, businessType, email, phone, address);

                if (accountType == ACCOUNT_TYPE_FREE) {
                    showProgress();
                    FrgRegistration.StartRegProcess(RegistrationActivity.this, json);
                }
                else {
                    Intent intent = new Intent(RegistrationActivity.this, CheckoutActivity.class);
                    intent.putExtra(CheckoutActivity.REGISTRATION_DETAILS, json);
                    startActivity(intent);
                }
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
                Toast.makeText(RegistrationActivity.this, "An error has occured during registration. " + AppPreferences.get(RegistrationActivity.this, AppPreferences.LAST_REGISTRATION_ERROR, ""), Toast.LENGTH_LONG).show();
                AppPreferences.set(RegistrationActivity.this, AppPreferences.LAST_REGISTRATION_ERROR, "");
                finish();
            }

        }
    }
}
