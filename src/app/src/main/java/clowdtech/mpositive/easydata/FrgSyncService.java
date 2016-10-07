package clowdtech.mpositive.easydata;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class FrgSyncService extends Fragment {

    boolean isRunning = false;
    boolean isLTaskRunning;
    Context context;

    LoginTask lTask;

    public FrgSyncService() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }



    public void StartProcesses() {
        isRunning = true;

        if (lTask == null || lTask.isCancelled() || !isLTaskRunning) {
            lTask = new LoginTask();
            lTask.execute();
        }


    }



    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            isRunning = true;
            isLTaskRunning = true;
        }

        @Override
        protected void onPostExecute(Boolean loginOK) {
            isLTaskRunning = false;
        }

        @Override
        protected Boolean doInBackground(Void...voids) {
            //initDBObjects();

            while (isRunning && isLTaskRunning) {
                try {
                    Thread.sleep(8 * 60 * 1000, 0);
                } catch (InterruptedException e1) { }

                String currentKey = AppPreferences.get(context, AppPreferences.CURRENT_KEY, "");
                String currentSecret = AppPreferences.get(context, AppPreferences.CURRENT_KEY, "");

                String _siteUrl = RestWSClient.defaultUrl; // getString(R.string.site_url);

                RestWSClient client = new RestWSClient(_siteUrl, RestWSClient.ENDPOINT_AUTH);
                String loginJson = RestWSClient.buildLoginPOSTData(currentKey, currentSecret);

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

                            AppPreferences.set(context, AppPreferences.CURRENT_UID, jUID);
                            AppPreferences.set(context, AppPreferences.CURRENT_TOKEN, jToken);
                            AppPreferences.set(context, AppPreferences.CURRENT_SECRET, currentKey);
                            AppPreferences.set(context, AppPreferences.CURRENT_KEY, currentSecret);

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

            return false;
        }
    }



}

