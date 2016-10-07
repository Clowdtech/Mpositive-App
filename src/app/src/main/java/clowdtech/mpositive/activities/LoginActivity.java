package clowdtech.mpositive.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.till.activities.TillActivity;
import clowdtech.mpositive.easydata.FrgLogin;

public class LoginActivity extends FragmentActivity {

    EditText edtKey;
    EditText edtSecret;
    Button btnLogin;
    Button btnActionCreateAccount;

    LoginReceiver loginReceiver;

    String TAG_LOGIN = "clowdtech.mpositive.activities.FrgLogin";
    ProgressDialog _progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (_progressDialog != null && _progressDialog.isShowing())
            _progressDialog.dismiss();

        if (loginReceiver != null)
            unregisterReceiver(loginReceiver);

    }
    private  void init() {
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(FrgLogin.LOGIN_OK);
        iFilter.addAction(FrgLogin.LOGIN_FAILED);

        loginReceiver = new LoginReceiver();

        try { registerReceiver(loginReceiver, iFilter);
        } catch (Exception e) { }

        edtKey = (EditText) findViewById(R.id.edtKey);
        edtSecret = (EditText) findViewById(R.id.edtSecret);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnActionCreateAccount = (Button) findViewById(R.id.btnActionCreateAccount);

        TextView login_sitelink = (TextView)findViewById(R.id.login_sitelink);
        login_sitelink.setMovementMethod(LinkMovementMethod.getInstance());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtKey.getText().toString().trim().isEmpty() && !edtSecret.getText().toString().trim().isEmpty()) {
                    String key = edtKey.getText().toString().trim();
                    String secret = edtSecret.getText().toString().trim();
                    showProgress();
                    StartLoginProcess(key, secret);
                }
            }
        });

//        btnLogin.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                try {
//                    File sd = Environment.getExternalStorageDirectory();
//
//                    if (ContextCompat.checkSelfPermission(LoginActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(LoginActivity.this, new String[] { "android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
//                    }
//                    if (sd.canWrite()) {
//                        String currentDBPath = "/data/data/" + getPackageName() + "/databases/Application.db";
//                        String backupDBPath = "MPOS_Application_backup.db";
//                        File currentDB = new File(currentDBPath);
//                        File backupDB = new File(sd, backupDBPath);
//
//                        if (currentDB.exists()) {
//                            FileInputStream in = new FileInputStream(currentDB);
//                            FileOutputStream out = new FileOutputStream(backupDB);
//                            FileChannel src = in.getChannel();
//                            FileChannel dst = out.getChannel();
//                            dst.transferFrom(src, 0, src.size());
//                            src.close();
//                            dst.close();
//                            in.close();
//                            out.close();
//                        }
//                    }
//                } catch (Exception e) {
//                    Log.e("Saving database", e.getMessage());
//                }
//                return true;
//            }
//        });

        btnActionCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showProgress() {
        if (_progressDialog == null) {
            _progressDialog = ProgressDialog.show(LoginActivity.this,
                    "Logging In",
                    "Verifying login&#8230;", true);

            _progressDialog.show();
        }

        if (!_progressDialog.isShowing())
            _progressDialog.show();
    }

    private void StartLoginProcess(String key, String secret) {
        FragmentManager fm = getSupportFragmentManager();
        FrgLogin loginFragment = (FrgLogin)fm.findFragmentByTag(TAG_LOGIN);
        if (loginFragment == null) {
            loginFragment = new FrgLogin();
            loginFragment.context = this;
            loginFragment.key = key;
            loginFragment.secret = secret;
            fm.beginTransaction().add(loginFragment, TAG_LOGIN).commit();
        }
        else {
            loginFragment.context = this;
            loginFragment.key = key;
            loginFragment.secret = secret;
        }
        loginFragment.StartProcess();
    }

    public class LoginReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context _context, Intent _intent) {

            if (_progressDialog != null && _progressDialog.isShowing())
                _progressDialog.dismiss();

            if(_intent.getAction() == FrgLogin.LOGIN_OK) {
                onLoginOK();
            }
            else {
                onLoginFailed();
            }

        }
    }

    private void onLoginOK() {
        Intent intent = new Intent(getBaseContext(), TillActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(LoginActivity.this, "Incorrect Credentials.", Toast.LENGTH_LONG).show();
        //setResult(Activity.RESULT_CANCELED);
        //finish();
    }
}
