package clowdtech.mpositive.easydata;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import clowdtech.mpositive.App;


public class FrgRegistration extends Fragment {
    public static String REG_OK = "clowdtech.mpositive.easydata.REG_OK";
    public static String REG_FAILED = "clowdtech.mpositive.easydata.REG_FAILED";
    public static String REG_USERID = "clowdtech.mpositive.easydata.REG_USERID";
    public static String TAG_REG = "clowdtech.mpositive.activities.FrgRegistration";

    RegTask cTask;
    Intent broadcastIntent;

    public Context context;
    public String jsonRegData;
    public int userId;
    public Boolean isRunning = false;

    public FrgRegistration() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    public void StartProcess() {
        if (cTask == null || cTask.isCancelled() || !isRunning) {
            cTask = new RegTask();
            cTask.execute();
        }
    }

    public static void StartRegProcess(FragmentActivity parent, String regDetails) {
        FragmentManager fm = parent.getSupportFragmentManager();
        FrgRegistration regFragment = (FrgRegistration) fm.findFragmentByTag(TAG_REG);
        if (regFragment == null) {
            regFragment = new FrgRegistration();
            regFragment.context = parent;
            regFragment.jsonRegData = regDetails;
            fm.beginTransaction().add(regFragment, TAG_REG).commit();
        }
        else {
            regFragment.context = parent;
            regFragment.jsonRegData = regDetails;
        }
        regFragment.StartProcess();
    }

//    private void initDBObjects() {
//        if (_dbFuncs == null || !_dbFuncs.isOpen) _dbFuncs = ((ActParent)context.getApplicationContext()).getDBFunctionsObj();
//        if (_dbAppFuncs == null || !_dbAppFuncs.isOpen) _dbAppFuncs = ((ActParent)context.getApplicationContext()).getDBAppFunctionsObj();
//    }

    private class RegTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            isRunning = true;
        }

        @Override
        protected void onPostExecute(Boolean regOK) {
            isRunning = false;
            broadcastIntent = new Intent();

            if (regOK) {
                broadcastIntent.setAction(REG_OK);
                broadcastIntent.putExtra(REG_USERID, userId);
            }
            else
                broadcastIntent.setAction(REG_FAILED);

            try { context.sendBroadcast(broadcastIntent); }
            catch (Exception e) { }
        }

        @Override
        protected Boolean doInBackground(Void...voids) {

            final DBAppFuncs dbAppFuncs = ((App) context.getApplicationContext()).getDBAppFunctionsObj();

            String _siteUrl = RestWSClient.defaultUrl; // getString(R.string.site_url);

            RestWSClient client = new RestWSClient(_siteUrl, RestWSClient.ENDPOINT_REG);

            String regJson = "";

            try {
                //String name, String password, String companyName, String businessType, String email, String phone, String address
                JSONObject jObj = new JSONObject(jsonRegData);
                regJson = RestWSClient.buildRegistrationPOSTData(jObj.getString(RestWSClient.REG_NAME),
                        jObj.getString(RestWSClient.REG_PASSWORD),
                        jObj.getString(RestWSClient.REG_COMPANY_NAME),
                        jObj.getString(RestWSClient.REG_BUSINESS_TYPE),
                        jObj.getString(RestWSClient.REG_EMAIL),
                        jObj.getString(RestWSClient.REG_PHONE),
                        jObj.getString(RestWSClient.REG_ADDRESS));
            }
            catch (JSONException e) { }

            try {
                client.Post(regJson);
            } catch (Exception e) {
                Log.e("Registration Error", e.getMessage());
            }

            String response = client.getResponse();

            if(response == null) {
                Log.e("Registration Error", "NULL server response.");
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

                        //AppPreferences.set(context, AppPreferences.CURRENT_UID, jUID);
                        //AppPreferences.set(context, AppPreferences.CURRENT_TOKEN, jToken);
                        //AppPreferences.set(context, AppPreferences.CURRENT_SECRET, secret);
                        //AppPreferences.set(context, AppPreferences.CURRENT_KEY, key);

                        client = new RestWSClient(_siteUrl, RestWSClient.ENDPOINT_ME);
                        client.AddHeader("Authorization", "Bearer:" + jToken);
                        try {
                            client.Execute(RestWSClient.RequestMethod.GET);
                        } catch (Exception e) { Log.e("Me Error", e.getMessage()); }

                        String meResponse = client.getResponse();

                        if (!meResponse.isEmpty() && !meResponse.equalsIgnoreCase("Device cannot be authorized.")) {
                            JSONObject jMeObj = new JSONObject(meResponse);

                            String key = jMeObj.getString("access_key");
                            String secret = jMeObj.getString("access_secret");

                            dbAppFuncs.saveLogin(jUID, key, secret);
                            dbAppFuncs.updateAccessToken(jToken);

                            return true;
                        }
                        else {
                            AppPreferences.set(context, AppPreferences.LAST_REGISTRATION_ERROR, meResponse);

                            return false;
                        }
                    }
                    else {
                        AppPreferences.set(context, AppPreferences.LAST_REGISTRATION_ERROR, jError);

                        return false;
                    }
                }
                catch (JSONException e)
                {
                    Log.e("Registration Error", e.getMessage());
                    return false;
                }
            }
        }
    }

}
