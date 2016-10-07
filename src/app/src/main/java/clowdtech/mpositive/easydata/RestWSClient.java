package clowdtech.mpositive.easydata;

import android.util.Log;
import android.util.Pair;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jrgos on 02/06/2016.
 */
public class RestWSClient {
    private ArrayList<Pair> params;
    private ArrayList <Pair> headers;

    public static String ENDPOINT_ME = "me";
    public static String ENDPOINT_AUTH = "authenticate";
    public static String ENDPOINT_REG = "register";
    public static String ENDPOINT_CREATE_CATEGORY = "create-category";
    public static String ENDPOINT_CREATE_PRODUCT = "create-product";
    public static String ENDPOINT_GET_CATEGORIES = "categories";
    public static String ENDPOINT_GET_PRODUCTS = "products";
    public static String ENDPOINT_GET_SYNC = "syncs";

    public static String HEADER_AUTHORISATION = "Authorization";

    public static String defaultUrl = "https://mpositive.co.uk/api/";//"http://46.101.26.68/api/";
    private String url = "https://mpositive.co.uk/api/";

    private int responseCode;
    private String message;

    private String response;
    private boolean hasTimedOut = false;

    OkHttpClient client;

    public String getResponse() {
        return response;
    }

    public Boolean hasTimedOut() {
        return hasTimedOut;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public RestWSClient(String url, String endpoint)
    {
        this.url = url + endpoint;

        params = new ArrayList<Pair>();
        headers = new ArrayList<Pair>();
    }

    public static String REG_ACCOUNT_TYPE = "account_type";
    //email name password company_name phone address
    public static String REG_NAME = "name";
    public static String REG_EMAIL = "email";
    public static String REG_PASSWORD = "password";
    public static String REG_BUSINESS_TYPE = "business_type";
    public static String REG_COMPANY_NAME = "company_name";
    public static String REG_PHONE = "phone";
    public static String REG_ADDRESS = "address";

    public static String buildRegistrationJSONData(int accountType, String name, String password, String companyName, String businessType, String email, String phone, String address) {
        JSONObject jObj = new JSONObject();

        try {
            jObj.put(REG_ACCOUNT_TYPE, accountType);
            jObj.put(REG_NAME, name);
            jObj.put(REG_EMAIL, email);
            jObj.put(REG_PASSWORD, password);
            jObj.put(REG_BUSINESS_TYPE, businessType);
            jObj.put(REG_COMPANY_NAME, companyName);
            jObj.put(REG_PHONE, phone);
            jObj.put(REG_ADDRESS, address);

        }
        catch (JSONException e) { }

        return jObj.toString();
    }

    public static String buildLoginPOSTData(String key, String secret) {
        return "access_key=" + key + "&access_secret=" + secret;
    }
    public static String buildRegistrationPOSTData(String name, String password, String companyName, String businessType, String email, String phone, String address) {
        return "email=" + encode(email) + "&name=" + encode(name) + "&password=" + encode(password) + "&company_name=" + encode(companyName) + "&business_type=" + encode(businessType) + "&phone=" + encode(phone) + "&address="  + encode(address);
    }

    private static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    public void AddParam(String name, String value)
    {
        params.add(new Pair(name, value));
    }

    public void AddHeader(String name, String value)
    {
        headers.add(new Pair(name, value));
    }

    public static final MediaType postMediaType = MediaType.parse("application/x-www-form-urlencoded");
    public static final MediaType JSONMediaType = MediaType.parse("application/json; charset=utf-8");

    public void Execute(RequestMethod method) throws Exception {
        Request.Builder builder = new Request.Builder();

        switch(method) {
            case GET:
            {
                //add parameters
                String combinedParams = "";
                if(!params.isEmpty()){
                    combinedParams += "?";
                    for(Pair p : params)
                    {
                        String paramString = p.first.toString() + "=" + URLEncoder.encode(p.second.toString(),"UTF-8");
                        if(combinedParams.length() > 1)
                        {
                            combinedParams  +=  "&" + paramString;
                        }
                        else
                        {
                            combinedParams += paramString;
                        }
                    }
                }
                builder.url(url + combinedParams);

                //add headers
                for(Pair h : headers)
                {
                    builder.addHeader(h.first.toString(), h.second.toString());
                }

                executeRequest(builder.build(), url);
                break;
            }
            case POST:
            {
                builder.url(url);

                //add headers
                for(Pair h : headers)
                {
                    builder.addHeader(h.first.toString(), h.second.toString());
                }

                if(!params.isEmpty()){
                    MultipartBody.Builder builder2 = new MultipartBody.Builder();
                    builder2.setType(postMediaType);

                    for(Pair p : params)
                    {
                        builder2.addFormDataPart(p.first.toString(), p.second.toString());
                    }

                    builder.post(builder2.build());
                }

                executeRequest(builder.build(), url);
                break;
            }
        }
    }

    public void Post(String data){

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Request.Builder builder = new Request.Builder();
        RequestBody body = RequestBody.create(postMediaType, data);

        builder.url(url);
        builder.post(body);

        //add headers
        for(Pair h : headers)
        {
            builder.addHeader(h.first.toString(), h.second.toString());
        }
        Request request = builder.build();

        Response clientResponse = null;
        try {
            clientResponse = client.newCall(request).execute();
            responseCode = clientResponse.code();
            message = clientResponse.message();
            response = clientResponse.body().string();
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
            hasTimedOut = true;
        }
    }

    public void PostJSON(String json){

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Request.Builder builder = new Request.Builder();
        RequestBody body = RequestBody.create(JSONMediaType, json);

        builder.url(url);
        builder.post(body);

        //add headers
        for(Pair h : headers)
        {
            builder.addHeader(h.first.toString(), h.second.toString());
        }
        Request request = builder.build();

        Response clientResponse = null;
        try {
            clientResponse = client.newCall(request).execute();
            responseCode = clientResponse.code();
            message = clientResponse.message();
            response = clientResponse.body().string();
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
            hasTimedOut = true;
        }
    }

    private void executeRequest(Request request, String url)
    {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        okhttp3.Response clientResponse;

        try {
            clientResponse = client.newCall(request).execute();
            responseCode = clientResponse.code();
            message = clientResponse.message();
            response = clientResponse.body().string();

        } catch (ConnectTimeoutException e) {
            Log.e("ConnectTimeoutException", "");
            hasTimedOut = true;
        }  catch (UnknownHostException e) {
            Log.e("UnknownHostException", "");
            hasTimedOut = true;
        } catch (SocketTimeoutException e) {
            Log.e("SocketTimeoutException", "");
            hasTimedOut = true;
        } catch (IOException e) {
            Log.e("IOException", "");
            hasTimedOut = true;
        }
    }

    public void clearParamsHeaders() {
        params.clear();
        headers.clear();
        hasTimedOut = false;
    }

    public enum RequestMethod {
        GET, POST
    }
    public enum Process {
        Login, Documents, Document, Project, DocumentUpdate, DocQuery, VisibilityQuestions, Upload, UpdateSingle,
        Version, ErrorList, MessagesCheck, MessagesUpload, MessageStatuses, Registration, EmailExistsCheck,
        GCMRegister, GCMUnRegister, UserDetails, UserUpdate, UserDetailsUpdate, SendEmailReminder,
        GetButtonTextAndGUID, GetApplicationsList, VerifySiteUrl,
        ProjQuery, Projects, DocAnswersQuery, DocAnswers, GetLookups, LogUpload
    }
    public enum Parameters{
        Proc, User, pwd, UID, upload, updateSingle, updateCommand, RegInfo, GCMID, LastLoc, Firstname, Lastname, Email,
        AppId, CurrDocTitle, CurrDocId, CurrProjId, CurrQuesId, SiteUrl, DocId, ProjId, LanguageISO, TemplateVer, ShowCanCreateDocs,
        AppVer, DeviceModel
    }
}
