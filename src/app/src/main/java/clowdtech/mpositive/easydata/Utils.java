package clowdtech.mpositive.easydata;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by jrgos on 23/06/2016.
 */
public class Utils {
    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm.getActiveNetworkInfo() == null)
            return false;
        return cm.getActiveNetworkInfo().isConnected();
    }
}
