package clowdtech.mpositive.easydata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jrgos on 02/06/2016.
 */
public class DBApplication extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Application.db";
    public static int DATABASE_VERSION = 38;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_FORSYNC = "forSync";

    public static final String USERLOGINS_TABLE = "userlogins";
    public static final String USERLOGINS_USERID = "userId";
    public static final String USERLOGINS_TOKEN = "access_token";
    public static final String USERLOGINS_KEY = "access_key";
    public static final String USERLOGINS_SECRET = "access_secret";

    public static final String CATEGORIES_TABLE = "Categories";
    public static final String CATEGORIES_ID = "Id";
    public static final String CATEGORIES_NAME = "Name";
    public static final String CATEGORIES_TILE = "Tile";
    public static final String CATEGORIES_SERVERID = "UID";

    public static final String CATEGORY_TILES_TABLE = "CategoryTiles";
    public static final String CATEGORY_TILES_ID = "Id";
    public static final String CATEGORY_TILES_FONT_COLOUR = "ForegroundColour";
    public static final String CATEGORY_TILES_BACKGROUND_COLOUR = "BackgroundColour";
    public static final String CATEGORY_TILES_VISIBLE_ON_HOMEPAGE = "VisibleOnHomePage";



    public static final String PRODUCTS_TABLE = "Products";
    public static final String PRODUCTS_ID = "Id";
    public static final String PRODUCTS_NAME = "Name";
    public static final String PRODUCTS_DESCRIPTION = "Description";
    public static final String PRODUCTS_TILE = "Tile";
    public static final String PRODUCTS_PRICE = "Price";
//    public static final String PRODUCTS_UPDATED = "LastUpdatedDate";
//    public static final String PRODUCTS_CREATED = "Created";
    public static final String PRODUCTS_SERVERID = "UID";
    public static final String PRODUCTS_REMOTEID = "RemoteId";

    public static final String PRODUCT_TILES_TABLE = "ProductTiles";
    public static final String PRODUCT_TILES_ID = "Id";
    public static final String PRODUCT_TILES_FONT_COLOUR = "ForegroundColour";
    public static final String PRODUCT_TILES_BACKGROUND_COLOUR = "BackgroundColour";
    public static final String PRODUCT_TILES_VISIBLE_ON_HOMEPAGE = "VisibleOnHomePage";
    public static final String PRODUCT_TILES_VISIBLE_ON_CATEGORY = "VisibleInCategory";


    public static final String PRODUCT_CATEGORIES_TABLE = "ProductCategories";
    public static final String PRODUCT_CATEGORIES_ID = "Id";
    public static final String PRODUCT_CATEGORIES_PRODUCTID = "product";
    public static final String PRODUCT_CATEGORIES_CATEGORYID = "category";

    public static final String TRANSACTIONS_TABLE = "Transactions";
    public static final String TRANSACTIONS_ID = "Id";
    public static final String TRANSACTIONS_REFUNDED = "Refunded";

    public static final String PAYMENTS_TABLE = "Payments";
    public static final String PAYMENTS_TRANSACTION_ID = "TransactionId";
    public static final String PAYMENTS_PAYMENT_TYPE = "PaymentType";
    public static final String PAYMENTS_PAYMENT_AMOUNT = "Amount";

    public static final String PRODUCT_TRANSACTIONLINES_TABLE = "ProductTransactionLines";
    public static final String PRODUCT_TRANSACTIONLINES_TRANSACTION_ID = "TransactionId";
    public static final String PRODUCT_TRANSACTIONLINES_PRODUCT_ID = "ProductId";
    public static final String PRODUCT_TRANSACTIONLINES_QUANTITY = "Quantity";

    public static final String TASKS_TABLE = "Tasks";
    public static final String TASKS_TASK_TYPE = "Type";


    public static final String DATABASE_CREATEUSERLOGINS =
            "create table userlogins (" + KEY_ROWID + " integer primary key autoincrement, "
                    + USERLOGINS_USERID + " text not null, "
                    + USERLOGINS_KEY + " text not null,"
                    + USERLOGINS_SECRET + " text not null,"
                    + USERLOGINS_TOKEN + " text); ";

    public static final String DATABASE_CREATETASKS =
            "create table " + TASKS_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + TASKS_TASK_TYPE + " int not null); ";


    public DBApplication(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {

    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
    }
}
