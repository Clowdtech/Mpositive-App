package clowdtech.mpositive.easydata;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import clowdtech.mpositive.App;


public class FrgLogin extends Fragment {
    public static String LOGIN_OK = "clowdtech.mpositive.easydata.LOGIN_OK";
    public static String LOGIN_FAILED = "clowdtech.mpositive.easydata.LOGIN_FAILED";
    public static String LOGIN_USERID = "clowdtech.mpositive.easydata.LOGIN_USERID";

    LoginTask cTask;
    Intent broadcastIntent;

    public Context context;
    public String key;
    public String secret;
    public int userId;
    public Boolean isRunning = false;

    public FrgLogin() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    public void StartProcess() {
        if (cTask == null || cTask.isCancelled() || !isRunning) {
            cTask = new LoginTask();
            cTask.execute();
        }
    }

//    private void initDBObjects() {
//        if (_dbFuncs == null || !_dbFuncs.isOpen) _dbFuncs = ((ActParent)context.getApplicationContext()).getDBFunctionsObj();
//        if (_dbAppFuncs == null || !_dbAppFuncs.isOpen) _dbAppFuncs = ((ActParent)context.getApplicationContext()).getDBAppFunctionsObj();
//    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            isRunning = true;
        }

        @Override
        protected void onPostExecute(Boolean loginOK) {
            isRunning = false;
            broadcastIntent = new Intent();

            if (loginOK) {
                broadcastIntent.setAction(LOGIN_OK);
                broadcastIntent.putExtra(LOGIN_USERID, userId);
            }
            else
                broadcastIntent.setAction(LOGIN_FAILED);

            try { context.sendBroadcast(broadcastIntent); }
            catch (Exception e) { }
        }

        @Override
        protected Boolean doInBackground(Void...voids) {
            final DBAppFuncs dbAppFuncs = ((App) context.getApplicationContext()).getDBAppFunctionsObj();

            String _siteUrl = RestWSClient.defaultUrl; // getString(R.string.site_url);

            RestWSClient client = new RestWSClient(_siteUrl, RestWSClient.ENDPOINT_AUTH);
            String loginJson = RestWSClient.buildLoginPOSTData(key, secret);

            try {
                client.Post(loginJson);
            } catch (Exception e) {
                Log.e("Login Error", e.getMessage());
            }

            String response = client.getResponse();
            if(response == null) {
                Log.e("Login Error", "NULL server response.");
                return false;
            }
            else {
                try{
                    JSONObject jLoginObj = new JSONObject(response);
                    String jUID = jLoginObj.optString("uid", "");
                    String jToken = jLoginObj.optString("token", "");
                    String jError = jLoginObj.optString("error", "");
                    //error":"Authentication failed.

                    if(jError.isEmpty() && (!jUID.isEmpty() || !jToken.isEmpty())) {

                        dbAppFuncs.saveLogin(jUID, key, secret);
                        dbAppFuncs.updateAccessToken(jToken);

                        return true;
                    }
                    else
                        return false;
                }
                catch (JSONException e)
                {
                    Log.e("Login Error", e.getMessage());
                    return false;
                }
            }
        }
    }

}
