package clowdtech.mpositive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import clowdtech.mpositive.R;

public class CreateAccountActivity extends FragmentActivity {

    Button btnLogin;
    Button btnCreateAccountFree;
    Button btnCreateAccountPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        init();
    }

    private void init() {
        btnLogin = (Button) findViewById(R.id.btnActionBarLogin);
        btnCreateAccountFree = (Button) findViewById(R.id.btnCreateAccountFree);
        btnCreateAccountPro = (Button) findViewById(R.id.btnCreateAccountPro);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });

        btnCreateAccountFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, RegistrationActivity.class);
                intent.putExtra(RegistrationActivity.ACCOUNT_TYPE_EXTRA, RegistrationActivity.ACCOUNT_TYPE_FREE);
                startActivity(intent);
            }
        });
        btnCreateAccountPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, RegistrationActivity.class);
                intent.putExtra(RegistrationActivity.ACCOUNT_TYPE_EXTRA, RegistrationActivity.ACCOUNT_TYPE_PRO);
                startActivity(intent);
            }
        });
    }
}
