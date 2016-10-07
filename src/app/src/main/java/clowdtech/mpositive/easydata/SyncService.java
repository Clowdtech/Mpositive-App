package clowdtech.mpositive.easydata;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import clowdtech.mpositive.App;

/**
 * Created by jrgos on 23/06/2016.
 */
public class SyncService extends IntentService {

    private Context context = null;
    DBAppFuncs dbAppFuncs;
    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            context = getApplicationContext();

            dbAppFuncs  = ((App) context.getApplicationContext()).getDBAppFunctionsObj();

            AppPreferences.set(context, AppPreferences.IS_POLL_RUNNING, true);

            while (dbAppFuncs != null && AppPreferences.get(context, AppPreferences.IS_POLL_RUNNING, false) && dbAppFuncs.isOpen) {
                try {
                    Thread.sleep(500, 0);
                } catch (InterruptedException e1) {
                    AppPreferences.set(context, AppPreferences.IS_POLL_RUNNING, false);

                    stopSelf();
                } catch (Exception e) {
                    AppPreferences.set(context, AppPreferences.IS_POLL_RUNNING, false);

                    stopSelf();
                }

                if (Utils.isOnline(context)) {

                    //Auth Check First
                    if (!AppPreferences.has(context, AppPreferences.LAST_AUTH_TIME))
                        refreshToken();
                    else {
                        long lastAuthNanos = AppPreferences.get(context, AppPreferences.LAST_AUTH_TIME, System.nanoTime());

                        //Auth Refresh every 7 minutes
                        if ((System.nanoTime() - lastAuthNanos) / 1000000 >= (7 * 60 * 1000)) {
                            refreshToken();
                        }
                    }

                    //upload Check
                    if (!AppPreferences.has(context, AppPreferences.LAST_UPLOAD_TIME)) {
                        dbAppFuncs.createTask(DBAppFuncs.TaskType.PRODUCTS_UPLOAD);
                    }
                    else {
                        long lastUpNanos = AppPreferences.get(context, AppPreferences.LAST_UPLOAD_TIME, System.nanoTime());

                        //upload every 15 seconds
                        if ((System.nanoTime() - lastUpNanos) / 1000000 >= (15 * 1000)) {
                            dbAppFuncs.createTask(DBAppFuncs.TaskType.PRODUCTS_UPLOAD);
                        }
                    }

                    //download Check
                    if (!AppPreferences.has(context, AppPreferences.LAST_DOWNLOAD_TIME)) {
                        dbAppFuncs.createTask(DBAppFuncs.TaskType.CATEGORY_DOWNLOAD);
                        dbAppFuncs.createTask(DBAppFuncs.TaskType.PRODUCTS_DOWNLOAD);
                    }
                    else {
                        long lastUpNanos = AppPreferences.get(context, AppPreferences.LAST_DOWNLOAD_TIME, System.nanoTime());

                        //upload every 15 seconds
                        if ((System.nanoTime() - lastUpNanos) / 1000000 >= (15 * 1000)) {
                            dbAppFuncs.createTask(DBAppFuncs.TaskType.CATEGORY_DOWNLOAD);
                            dbAppFuncs.createTask(DBAppFuncs.TaskType.PRODUCTS_DOWNLOAD);
                        }
                    }

                    //transactions Check
                    if (!AppPreferences.has(context, AppPreferences.LAST_TRANSACTION_TIME)) {
                        dbAppFuncs.createTask(DBAppFuncs.TaskType.TRANSACTIONS_UPLOAD);
                    }
                    else {
                        long lastUpNanos = AppPreferences.get(context, AppPreferences.LAST_TRANSACTION_TIME, System.nanoTime());

                        //upload every 15 seconds
                        if ((System.nanoTime() - lastUpNanos) / 1000000 >= (15 * 1000)) {
                            dbAppFuncs.createTask(DBAppFuncs.TaskType.TRANSACTIONS_UPLOAD);
                        }
                    }


                    //Run Tasks
                    processTasks();
                }

            }

        } catch (Exception e) {
            Log.e("SyncService err", e.getMessage());
        }
    }


    private void processTasks() {
        ArrayList<DBAppFuncs.Task> tasks = new ArrayList<>();
        try { tasks = dbAppFuncs.getTasks(); } catch (Exception ex) { Log.e("get Tasks", ex.getMessage()); }

        for (DBAppFuncs.Task t: tasks) {
            boolean isOK = false;
            switch (t.TaskType) {
                case AUTH_REFRESH:
                    isOK = refreshToken();
                    break;
                case PRODUCTS_UPLOAD:
                    AppPreferences.set(context, AppPreferences.LAST_UPLOAD_TIME, System.nanoTime());
                    try { isOK = productUpload(); } catch (Exception ex) { Log.e("product Upload", ex.getMessage()); }
                    break;
                case CATEGORY_UPLOAD:
                    AppPreferences.set(context, AppPreferences.LAST_UPLOAD_TIME, System.nanoTime());
                    try { isOK = categoryUpload(); } catch (Exception ex) { Log.e("category Upload", ex.getMessage()); }
                    break;
                case PRODUCTS_DOWNLOAD:
                    AppPreferences.set(context, AppPreferences.LAST_DOWNLOAD_TIME, System.nanoTime());
                    try { isOK =  productDownload(); } catch (Exception ex) { Log.e("product Download", ex.getMessage()); }
                    break;
                case CATEGORY_DOWNLOAD:
                    AppPreferences.set(context, AppPreferences.LAST_DOWNLOAD_TIME, System.nanoTime());
                    try { isOK = categoryDownload(); } catch (Exception ex) { Log.e("category Download", ex.getMessage()); }
                    break;
                case TRANSACTIONS_UPLOAD:
                    AppPreferences.set(context, AppPreferences.LAST_TRANSACTION_TIME, System.nanoTime());
                    try { isOK = uploadTransactions(); } catch (Exception ex) { Log.e("upload Transactions", ex.getMessage()); }
                    break;
            }

            try { if (isOK)
                dbAppFuncs.deleteTask(t.TaskId);
            } catch (Exception ex) { Log.e("delete Task", ex.getMessage()); }
        }
    }


    private String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            Log.e("URLEncoder Error", ex.getMessage() + " STRING: " + str);
        }

        return str;
    }


    private boolean refreshToken() {
        String[] login = dbAppFuncs.getLogin();

        if (!login[0].isEmpty() && !login[1].isEmpty()) {
            String key = login[0];
            String secret = login[1];
            RestWSClient client = new RestWSClient(RestWSClient.defaultUrl, RestWSClient.ENDPOINT_AUTH);

            String loginJson = RestWSClient.buildLoginPOSTData(key, secret);

            try {
                client.Post(loginJson);
            } catch (Exception e) {
                Log.e("Login Error", e.getMessage());
                return false;
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

                    if(jError.isEmpty() && (!jUID.isEmpty() || !jToken.isEmpty())) {
                        dbAppFuncs.updateAccessToken(jToken);
                        AppPreferences.set(context, AppPreferences.CURRENT_TOKEN, jToken);
                        AppPreferences.set(context, AppPreferences.LAST_AUTH_TIME, System.nanoTime());
                    }
                    return true;
                }
                catch (JSONException e) {
                    Log.e("Login Refresh Error", e.getMessage());
                    return false;
                }
            }
        }

        return false;
    }

    private boolean categoryUpload() {
        Cursor curCats = dbAppFuncs.getCategoriesForSync();

        //name=&background_color=&font_color=

        for(curCats.moveToFirst(); !curCats.isAfterLast(); curCats.moveToNext()) {
            String authToken = AppPreferences.get(context, AppPreferences.CURRENT_TOKEN, "");

            if(authToken.isEmpty()) {
                refreshToken();
            }

            if(authToken.isEmpty())
                return false;

            int categoryID = curCats.getInt(curCats.getColumnIndex(DBApplication.CATEGORIES_ID));
            String categoryName = curCats.getString(curCats.getColumnIndex(DBApplication.CATEGORIES_NAME));
            int tileID = curCats.getInt(curCats.getColumnIndex(DBApplication.CATEGORIES_TILE));
            String fontColour = "";
            String backColour = "";

            Cursor curTile = dbAppFuncs.getCategoryTile(tileID);

            for(curTile.moveToFirst(); !curTile.isAfterLast(); curTile.moveToNext()) {
                fontColour = curTile.getString(curTile.getColumnIndex(DBApplication.CATEGORY_TILES_FONT_COLOUR));
                backColour = curTile.getString(curTile.getColumnIndex(DBApplication.CATEGORY_TILES_BACKGROUND_COLOUR));
            }

            curTile.close();

            if (!categoryName.isEmpty() && !fontColour.isEmpty() && !backColour.isEmpty()) {

                RestWSClient client = new RestWSClient(RestWSClient.defaultUrl, RestWSClient.ENDPOINT_CREATE_CATEGORY);

                client.AddHeader(RestWSClient.HEADER_AUTHORISATION, "Bearer:" + authToken);
                String data = "name=" + encode(categoryName) + "&background_color=" + encode(backColour) + "&font_color=" + encode(fontColour);
                client.Post(data);

                String response = client.getResponse();
                if(response == null || response.isEmpty()) {
                    Log.e("categoryUpload Error", "NULL server response.");
                }
                else {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String serverID = jObj.getString("id");

                        if (!serverID.isEmpty()) {
                            dbAppFuncs.updateCategoryServerID(categoryID, serverID);
                            dbAppFuncs.setProductSynced(categoryID);
                        }
                    }
                    catch (JSONException ex) {
                        Log.e("categoryUpload Error", "JSONException on server response.");
                    }
                }
            }

        }

        curCats.close();
        return true;
    }

    private boolean productUpload() {
        categoryUpload();

        Cursor curProds = dbAppFuncs.getProductsForSync();

        //name=&background_color=&font_color=&description=&category_id=&price=

        for(curProds.moveToFirst(); !curProds.isAfterLast(); curProds.moveToNext()) {
            String authToken = AppPreferences.get(context, AppPreferences.CURRENT_TOKEN, "");

            if(authToken.isEmpty()) {
                refreshToken();
            }

            if(authToken.isEmpty())
                return false;

            int productID = curProds.getInt(curProds.getColumnIndex(DBApplication.PRODUCTS_ID));
            int tileID = curProds.getInt(curProds.getColumnIndex(DBApplication.PRODUCTS_TILE));
            int categoryServerId = dbAppFuncs.getProductCategoryServerId(productID);
            String productName = curProds.getString(curProds.getColumnIndex(DBApplication.PRODUCTS_NAME));
            String productDesc = curProds.getString(curProds.getColumnIndex(DBApplication.PRODUCTS_DESCRIPTION));
            String price = curProds.getString(curProds.getColumnIndex(DBApplication.PRODUCTS_PRICE));

            String fontColour = "";
            String backColour = "";

            Cursor curTile = dbAppFuncs.getProductTile(tileID);

            for(curTile.moveToFirst(); !curTile.isAfterLast(); curTile.moveToNext()) {
                fontColour = curTile.getString(curTile.getColumnIndex(DBApplication.PRODUCT_TILES_FONT_COLOUR));
                backColour = curTile.getString(curTile.getColumnIndex(DBApplication.PRODUCT_TILES_BACKGROUND_COLOUR));
            }

            curTile.close();

            if (!productName.isEmpty() && !fontColour.isEmpty() && !backColour.isEmpty()&& !price.isEmpty() && categoryServerId > 0) {

                RestWSClient client = new RestWSClient(RestWSClient.defaultUrl, RestWSClient.ENDPOINT_CREATE_PRODUCT);

                client.AddHeader(RestWSClient.HEADER_AUTHORISATION, "Bearer:" + authToken);
                String data = "name=" + encode(productName) + "&background_color=" + encode(backColour) +
                        "&font_color=" + encode(fontColour) + "&description=" + encode(productDesc) +
                        "&category_id=" + categoryServerId + "&price=" + encode(price);
                client.Post(data);

                String response = client.getResponse();
                if(response == null || response.isEmpty()) {
                    Log.e("productUpload Error", "NULL server response.");
                }
                else {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String serverID = jObj.optString("uid", "");

                        if (!serverID.isEmpty()) {
                            dbAppFuncs.updateProductServerID(productID, serverID);
                            dbAppFuncs.setProductSynced(productID);
                        }
                    }
                    catch (JSONException ex) {
                        Log.e("productUpload Error", "JSONException on server response.");
                    }
                }
            }

        }

        curProds.close();
        return true;
    }

    private boolean categoryDownload() {
        String authToken = AppPreferences.get(context, AppPreferences.CURRENT_TOKEN, "");

        if(authToken.isEmpty()) {
            refreshToken();
        }

        if(authToken.isEmpty())
            return false;

        RestWSClient client = new RestWSClient(RestWSClient.defaultUrl, RestWSClient.ENDPOINT_GET_CATEGORIES);
        client.AddHeader(RestWSClient.HEADER_AUTHORISATION, "Bearer:" + authToken);

        try {
            client.Execute(RestWSClient.RequestMethod.GET);
        } catch (Exception e) {
            Log.e("categoryDownload Error", e.getMessage());
        }

        String response = client.getResponse();

        if(response == null || response.isEmpty()) {
            Log.e("categoryDownload Error", "NULL server response.");
            return false;
        }
        else {
            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int ix = 0; ix < jsonArray.length(); ix++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(ix);
                    int id = jsonObject.optInt("category_id", 0);
                    String name = jsonObject.getString("name");
                    String background_color = jsonObject.getString("background_color");
                    String font_color = jsonObject.getString("font_color");

                    dbAppFuncs.addCategory(id, name, background_color, font_color);
                }

                return true;
            }
            catch (JSONException ex) {
                Log.e("categoryUpload Error", "JSONException on server response.");
                return false;
            }
        }
    }

    private boolean productDownload() {
        String authToken = AppPreferences.get(context, AppPreferences.CURRENT_TOKEN, "");

        if(authToken.isEmpty()) {
            refreshToken();
        }

        if(authToken.isEmpty())
            return false;

        RestWSClient client = new RestWSClient(RestWSClient.defaultUrl, RestWSClient.ENDPOINT_GET_PRODUCTS);
        client.AddHeader(RestWSClient.HEADER_AUTHORISATION, "Bearer:" + authToken);

        try {
            client.Execute(RestWSClient.RequestMethod.GET);
        } catch (Exception e) {
            Log.e("productDownload Error", e.getMessage());
        }

        String response = client.getResponse();

        if(response == null || response.isEmpty()) {
            Log.e("productDownload Error", "NULL server response.");
            return false;
        }
        else {
            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int ix = 0; ix < jsonArray.length(); ix++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(ix);
                    String uid = jsonObject.optString("uid", "");
                    String name = jsonObject.getString("name");
                    String description = jsonObject.getString("description");
                    String background_color = jsonObject.getString("background_color");
                    String font_color = jsonObject.getString("font_color");
                    String price = jsonObject.getString("price");

                    dbAppFuncs.addProduct(uid, name, description, background_color, font_color, price);
                }

                return true;
            }
            catch (JSONException ex) {
                Log.e("productDownload Error", "JSONException on server response.");
                return false;
            }
        }
    }

    private boolean uploadTransactions() {
        String authToken = AppPreferences.get(context, AppPreferences.CURRENT_TOKEN, "");

        if (authToken.isEmpty()) {
            refreshToken();
        }

        if (authToken.isEmpty())
            return false;

        ArrayList<DBAppFuncs.PaymentObj> payments = dbAppFuncs.getPayments();

        try {
            for (DBAppFuncs.PaymentObj payment : payments) {
                JSONObject JObjPayment = new JSONObject();

                JSONArray JArrayOrders = new JSONArray();

                for (DBAppFuncs.ProductPayment prod : payment.orders) {
                    JSONObject JObjProd = new JSONObject();
                    JObjProd.put("product_id", prod.product_id);
                    JObjProd.put("price", prod.price);
                    JObjProd.put("qty", prod.qty);

                    JArrayOrders.put(JObjProd);
                }
                JObjPayment.put("orders", JArrayOrders);
                JObjPayment.put("total", payment.total);
                JObjPayment.put("payment_type_id", payment.payment_type_id);


                RestWSClient client = new RestWSClient(RestWSClient.defaultUrl, RestWSClient.ENDPOINT_GET_PRODUCTS);
                client.AddHeader(RestWSClient.HEADER_AUTHORISATION, "Bearer:" + authToken);
                client.Post(JObjPayment.toString());
                String response = client.getResponse();

                if(response == null || response.isEmpty()) {
                    Log.e("productUpload Error", "NULL server response.");
                }
                else {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean isSuccess = jObj.getBoolean("success");

                        if(isSuccess)
                            dbAppFuncs.setTransactionSynced(payment.transactionId);
                    }
                    catch (JSONException ex) {
                        Log.e("productUpload Error", "JSONException on server response.");
                    }
                }
            }
        } catch (JSONException ex) {
            Log.e("uploadTransactions err", "JSONException on server response.");
        }

        return true;
    }
}
