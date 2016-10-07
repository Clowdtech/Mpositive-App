package clowdtech.mpositive.easydata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Created by jrgos on 02/06/2016.
 */
public class DBAppFuncs {

    private Context context;
    private SQLiteDatabase database;
    private DBApplication dbHelper;
    public boolean isOpen = false;

    public DBAppFuncs (Context _context){
        this.context = _context.getApplicationContext();
    }

    public synchronized DBAppFuncs open() throws SQLException {
        if(database == null || (!isOpen && !database.isOpen())) {
            dbHelper = new DBApplication(context);
            database = dbHelper.getWritableDatabase();
            isOpen = true;
        }

        return this;
    }

    public synchronized void close() {
        if(isOpen)
            dbHelper.close();
        isOpen = false;
    }



    public void initMainTables() {
        try { database.rawQuery("SELECT * FROM " + DBApplication.USERLOGINS_TABLE, null); }
        catch (SQLException e) {
            database.execSQL(DBApplication.DATABASE_CREATEUSERLOGINS);
        }
        try { database.rawQuery("SELECT * FROM " + DBApplication.TASKS_TABLE, null); }
        catch (SQLException e) {
            database.execSQL(DBApplication.DATABASE_CREATETASKS);
        }
    }


    /**  LOGIN  **/

    public boolean hasLogin() {
        boolean ok = false;
        Cursor c = database.query(DBApplication.USERLOGINS_TABLE, null, null, null, null, null, null);

        try {
            ok = c.getCount() > 0;
        }
        finally {
            c.close();
        }

        return ok;
    }

    /**
     * @return String[0] = Key, String[1] = Secret
     */
    public String[] getLogin() {
        String[] vals = new String[2];

        Cursor c = database.query(DBApplication.USERLOGINS_TABLE, new String[]{ DBApplication.USERLOGINS_KEY, DBApplication.USERLOGINS_SECRET }, null, null, null, null, null);

        try {
            c.moveToFirst();
            vals[0] = c.getString(c.getColumnIndex(DBApplication.USERLOGINS_KEY));
            vals[1] = c.getString(c.getColumnIndex(DBApplication.USERLOGINS_SECRET));
        }
        finally {
            c.close();
        }

        return vals;
    }

    public void saveLogin(String uid, String key, String secret) {
        ContentValues values = createLoginContextValues(uid, key, secret);

        logout();

        database.insert(DBApplication.USERLOGINS_TABLE, null, values);
    }

    public void updateAccessToken(String token) {
        ContentValues values = new ContentValues();
        values.put(DBApplication.USERLOGINS_TOKEN, token);

        database.update(DBApplication.USERLOGINS_TABLE, values, null, null);
    }

    public String getAccessToken() {
        String token = "";
        Cursor c = database.query(DBApplication.USERLOGINS_TABLE, new String[]{ DBApplication.USERLOGINS_TOKEN }, null, null, null, null, null);

        try {
            c.moveToFirst();
            token = c.getString(c.getColumnIndex(DBApplication.USERLOGINS_TOKEN));
        }
        finally {
            c.close();
        }

        return token;
    }

    public void logout() {
        database.delete(DBApplication.USERLOGINS_TABLE, null, null);
    }

    private static ContentValues createLoginContextValues(String uid, String access_key, String access_secret) {
        ContentValues values = new ContentValues();
        values.put(DBApplication.USERLOGINS_USERID, uid);
        values.put(DBApplication.USERLOGINS_KEY, access_key);
        values.put(DBApplication.USERLOGINS_SECRET, access_secret);
        return values;
    }


    /** TASKS **/

    public void createTask(TaskType taskType) {
        Cursor c = database.query(DBApplication.TASKS_TABLE, null, DBApplication.TASKS_TASK_TYPE + " = " + taskType.getTtypeId(), null, null, null, null);

        if (c.getCount() == 0) {
            ContentValues contentVals = new ContentValues();
            contentVals.put(DBApplication.TASKS_TASK_TYPE, taskType.getTtypeId());
            database.insert(DBApplication.TASKS_TABLE, null, contentVals);
        }

        c.close();
    }

    public void deleteTask(int taskID) {
        database.delete(DBApplication.TASKS_TABLE, DBApplication.TASKS_TASK_TYPE + " = " + taskID, null);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        Cursor cTasks = database.query(DBApplication.TASKS_TABLE, null, null, null, null, null, null);

        for(cTasks.moveToFirst(); !cTasks.isAfterLast(); cTasks.moveToNext()) {
            Task t = new Task();

            t.TaskId = cTasks.getInt(cTasks.getColumnIndex(DBApplication.KEY_ROWID));
            t.TaskType = TaskType.get(cTasks.getInt(cTasks.getColumnIndex(DBApplication.TASKS_TASK_TYPE)));

            tasks.add(t);
        }

        return tasks;
    }

    public class Task {
        public int TaskId = 0;
        public TaskType TaskType;

    }

    public static enum TaskType {
        UNDEFINED(0),
        PRODUCTS_UPLOAD(1), CATEGORY_UPLOAD(2), AUTH_REFRESH(3),
        PRODUCTS_DOWNLOAD(4), CATEGORY_DOWNLOAD(5), TRANSACTIONS_UPLOAD(6);

        private static final SparseArray<TaskType> lookup = new SparseArray<TaskType>();

        static {
            for(TaskType tt : EnumSet.allOf(TaskType.class))
                lookup.put(tt.getTtypeId(), tt);
        }

        private int ttypeId;

        private TaskType(int ttypeId) {
            this.ttypeId = ttypeId;
        }

        public int getTtypeId() { return ttypeId; }

        public static TaskType get(int ttypeId){
            return lookup.get(ttypeId);
        }
    }


    /**  CATEGORIES  **/

    public void initCategories() {
        try { database.rawQuery("SELECT " + DBApplication.CATEGORIES_SERVERID + " FROM " + DBApplication.CATEGORIES_TABLE, null); }
        catch (Exception e) {
            try { database.execSQL("ALTER TABLE " + DBApplication.CATEGORIES_TABLE + " ADD COLUMN " + DBApplication.CATEGORIES_SERVERID + " text;"); }
            catch (Exception ex) { }
        }
        try { database.rawQuery("SELECT " + DBApplication.KEY_FORSYNC + " FROM " + DBApplication.CATEGORIES_TABLE, null); }
        catch (Exception e) {
            try { database.execSQL("ALTER TABLE " + DBApplication.CATEGORIES_TABLE + " ADD COLUMN " + DBApplication.KEY_FORSYNC + " text;"); }
            catch (Exception ex) { }
        }
    }

    public Cursor getCategoriesForSync() {
        return database.query(DBApplication.CATEGORIES_TABLE, null, DBApplication.KEY_FORSYNC + " IS NULL OR " + DBApplication.KEY_FORSYNC + " = 1", null, null, null, null);
    }

    public Cursor getCategoryTile(int tileID) {
        return database.query(DBApplication.CATEGORY_TILES_TABLE, null, DBApplication.CATEGORY_TILES_ID + " = " + tileID, null, null, null, null);
    }

    public void updateCategoryServerID(int id, String serverId) {
        ContentValues updateCategoryValues = new ContentValues();
        updateCategoryValues.put(DBApplication.CATEGORIES_SERVERID, serverId);

        database.update(DBApplication.CATEGORIES_TABLE, updateCategoryValues, DBApplication.CATEGORIES_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void addCategory(int serverId, String name, String background_colour, String font_colour) {
        ContentValues addCategoryValues = new ContentValues();
        ContentValues addCategoryTileValues = new ContentValues();

        addCategoryTileValues.put(DBApplication.CATEGORY_TILES_BACKGROUND_COLOUR, background_colour);
        addCategoryTileValues.put(DBApplication.CATEGORY_TILES_FONT_COLOUR, font_colour);
        addCategoryTileValues.put(DBApplication.CATEGORY_TILES_VISIBLE_ON_HOMEPAGE, 1);

        long tileID = database.insert(DBApplication.CATEGORY_TILES_TABLE, null, addCategoryTileValues);

        addCategoryValues.put(DBApplication.CATEGORIES_NAME, name);
        addCategoryValues.put(DBApplication.CATEGORIES_SERVERID, serverId);
        addCategoryValues.put(DBApplication.CATEGORIES_TILE, tileID);

        database.insert(DBApplication.CATEGORIES_TABLE, null, addCategoryValues);
    }

    public void updateCategory(int serverId, String name, String background_colour, String font_colour) {
        ContentValues updateCategoryValues = new ContentValues();
        updateCategoryValues.put(DBApplication.CATEGORIES_NAME, name);

        database.update(DBApplication.CATEGORIES_TABLE, updateCategoryValues, DBApplication.CATEGORIES_SERVERID + " = ?", new String[] { String.valueOf(serverId) });

        Cursor c = database.query(DBApplication.CATEGORIES_TABLE, new String[] { DBApplication.CATEGORIES_TILE },
                DBApplication.CATEGORIES_SERVERID + " = ?", new String[] { String.valueOf(serverId) }, null, null, null);

        try {
            c.moveToFirst();
            int tileID = c.getInt(c.getColumnIndex(DBApplication.CATEGORIES_TILE));
            ContentValues updateTileValues = new ContentValues();
            updateTileValues.put(DBApplication.CATEGORY_TILES_BACKGROUND_COLOUR, background_colour);
            updateTileValues.put(DBApplication.CATEGORY_TILES_FONT_COLOUR, font_colour);

            database.update(DBApplication.CATEGORY_TILES_TABLE, updateTileValues, DBApplication.CATEGORY_TILES_ID + " = ?", new String[] { String.valueOf(tileID) });
        }
        finally {
            c.close();
        }
    }

    public int getCategoryServerId(int categoryId) {
        int serverId = 0;

        Cursor c = database.query(DBApplication.CATEGORIES_TABLE, new String[] { DBApplication.CATEGORIES_SERVERID },
                DBApplication.CATEGORIES_ID + " = " + categoryId, null, null, null, null );

        try {
            c.moveToFirst();
            serverId = c.getInt(c.getColumnIndex(DBApplication.CATEGORIES_SERVERID));
        }
        finally {
            c.close();
            return  serverId;
        }
    }

    public void setCategorySynced(int categoryId) {
        ContentValues values = new ContentValues();
        values.put(DBApplication.KEY_FORSYNC, "0");

        database.update(DBApplication.CATEGORIES_TABLE, values, DBApplication.CATEGORIES_ID + " = ?", new String[] { String.valueOf(categoryId) });
    }


    /**  PRODUCTS  **/

    public void initProducts() {
        try { database.rawQuery("SELECT " + DBApplication.PRODUCTS_SERVERID + " FROM " + DBApplication.PRODUCTS_TABLE, null); }
        catch (Exception e) {
            try { database.execSQL("ALTER TABLE " + DBApplication.PRODUCTS_TABLE + " ADD COLUMN " + DBApplication.PRODUCTS_SERVERID + " text;"); }
            catch (Exception ex) { }
        }
        try { database.rawQuery("SELECT " + DBApplication.KEY_FORSYNC + " FROM " + DBApplication.PRODUCTS_TABLE, null); }
        catch (Exception e) {
            try { database.execSQL("ALTER TABLE " + DBApplication.PRODUCTS_TABLE + " ADD COLUMN " + DBApplication.KEY_FORSYNC + " text;"); }
            catch (Exception ex) { }
        }
    }

    public Cursor getProductsForSync() {
        return database.query(DBApplication.PRODUCTS_TABLE, null, DBApplication.KEY_FORSYNC + " IS NULL OR " + DBApplication.KEY_FORSYNC + " = 1", null, null, null, null);
    }

    public Cursor getProductTile(int tileID) {
        return database.query(DBApplication.PRODUCT_TILES_TABLE, null, DBApplication.PRODUCT_TILES_ID + " = " + tileID, null, null, null, null);
    }

    public void updateProductServerID(int id, String serverId) {
        ContentValues updateProductValues = new ContentValues();
        updateProductValues.put(DBApplication.PRODUCTS_SERVERID, serverId);

        database.update(DBApplication.PRODUCTS_TABLE, updateProductValues, DBApplication.PRODUCTS_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void addProduct(String serverId, String name, String description, String background_colour, String font_colour, String price) {
        ContentValues addProductValues = new ContentValues();
        ContentValues addProductTileValues = new ContentValues();

        addProductTileValues.put(DBApplication.PRODUCT_TILES_BACKGROUND_COLOUR, background_colour);
        addProductTileValues.put(DBApplication.PRODUCT_TILES_FONT_COLOUR, font_colour);
        addProductTileValues.put(DBApplication.PRODUCT_TILES_VISIBLE_ON_CATEGORY, 1);
        addProductTileValues.put(DBApplication.PRODUCT_TILES_VISIBLE_ON_HOMEPAGE, 1);

        long tileID = database.insert(DBApplication.PRODUCT_TILES_TABLE, null, addProductTileValues);

        addProductValues.put(DBApplication.PRODUCTS_NAME, name);
        addProductValues.put(DBApplication.PRODUCTS_DESCRIPTION, description);
        addProductValues.put(DBApplication.PRODUCTS_SERVERID, serverId);
        addProductValues.put(DBApplication.PRODUCTS_PRICE, price);
        addProductValues.put(DBApplication.PRODUCTS_TILE, tileID);
        addProductValues.put(DBApplication.PRODUCTS_REMOTEID, 0);

        database.insert(DBApplication.PRODUCTS_TABLE, null, addProductValues);
    }
    
    public void updateProduct(String serverId, String name, String description, String background_colour, String font_colour, String price) {
        ContentValues updateProductValues = new ContentValues();
        updateProductValues.put(DBApplication.PRODUCTS_NAME, name);
        updateProductValues.put(DBApplication.PRODUCTS_DESCRIPTION, description);
        updateProductValues.put(DBApplication.PRODUCTS_PRICE, price);

        database.update(DBApplication.PRODUCTS_TABLE, updateProductValues, DBApplication.PRODUCTS_SERVERID + " = ?", new String[] { serverId });

        Cursor c = database.query(DBApplication.PRODUCTS_TABLE, new String[] { DBApplication.PRODUCTS_TILE },
                DBApplication.PRODUCTS_SERVERID + " = ?", new String[] { serverId }, null, null, null);

        try {
            c.moveToFirst();
            int tileID = c.getInt(c.getColumnIndex(DBApplication.PRODUCTS_TILE));
            ContentValues updateTileValues = new ContentValues();
            updateTileValues.put(DBApplication.PRODUCT_TILES_BACKGROUND_COLOUR, background_colour);
            updateTileValues.put(DBApplication.PRODUCT_TILES_FONT_COLOUR, font_colour);

            database.update(DBApplication.PRODUCT_TILES_TABLE, updateTileValues, DBApplication.PRODUCT_TILES_ID + " = ?", new String[] { String.valueOf(tileID) });
        }
        finally {
            c.close();
        }
    }

    public int getProductCategoryID(int productId) {
        int categoryId = 0;

        Cursor c = database.query(DBApplication.PRODUCT_CATEGORIES_TABLE, new String[] { DBApplication.PRODUCT_CATEGORIES_CATEGORYID },
                DBApplication.PRODUCT_CATEGORIES_PRODUCTID + " = " + productId, null, null, null, null );

        try {
            c.moveToFirst();
            categoryId = c.getInt(c.getColumnIndex(DBApplication.PRODUCT_CATEGORIES_CATEGORYID));
        }
        finally {
            c.close();
            return  categoryId;
        }
    }

    public int getProductCategoryServerId(int productId) {
        int categoryId = 0;

        Cursor c = database.query(DBApplication.PRODUCT_CATEGORIES_TABLE, new String[] { DBApplication.PRODUCT_CATEGORIES_CATEGORYID },
                DBApplication.PRODUCT_CATEGORIES_PRODUCTID + " = " + productId, null, null, null, null );

        try {
            c.moveToFirst();
            categoryId = c.getInt(c.getColumnIndex(DBApplication.PRODUCT_CATEGORIES_CATEGORYID));
        }
        finally {
            c.close();
            return getCategoryServerId(categoryId);
        }
    }

    public void setProductSynced(int productId) {
        ContentValues values = new ContentValues();
        values.put(DBApplication.KEY_FORSYNC, "0");

        database.update(DBApplication.PRODUCTS_TABLE, values, DBApplication.PRODUCTS_ID + " = ?", new String[] { String.valueOf(productId) });
    }

    /*** PAYMENTS ***/

    public void initTransactions() {
        try { database.rawQuery("SELECT " + DBApplication.KEY_FORSYNC + " FROM " + DBApplication.TRANSACTIONS_TABLE, null); }
        catch (Exception e) {
            database.execSQL("ALTER TABLE " + DBApplication.TRANSACTIONS_TABLE + " ADD COLUMN " + DBApplication.KEY_FORSYNC + " text;");
        }
    }

    public void setTransactionSynced(int transactionID) {
        ContentValues values = new ContentValues();
        values.put(DBApplication.KEY_FORSYNC, "0");

        database.update(DBApplication.TRANSACTIONS_TABLE, values, DBApplication.TRANSACTIONS_ID + " = ?", new String[] { String.valueOf(transactionID) });
    }

    public ArrayList<PaymentObj> getPayments() {
        ArrayList<PaymentObj> payments = new ArrayList<PaymentObj>();

        String sqlTrans = "SELECT Trans." + DBApplication.TRANSACTIONS_ID + ", Pay." + DBApplication.PAYMENTS_PAYMENT_TYPE + ", Pay." + DBApplication.PAYMENTS_PAYMENT_AMOUNT +
                " FROM " + DBApplication.TRANSACTIONS_TABLE + " Trans " +
                " INNER JOIN " + DBApplication.PAYMENTS_TABLE + " Pay ON Pay." + DBApplication.PAYMENTS_TRANSACTION_ID + " = Trans." + DBApplication.TRANSACTIONS_ID +
                " WHERE Trans." + DBApplication.TRANSACTIONS_REFUNDED + " = 0" +
                " AND Trans." + DBApplication.KEY_FORSYNC + " IS NULL OR Trans." + DBApplication.KEY_FORSYNC + " = 1";

        Cursor cTrans = database.rawQuery(sqlTrans, null);

        for(cTrans.moveToFirst(); !cTrans.isAfterLast(); cTrans.moveToNext()) {
            PaymentObj payment = new PaymentObj();

            String transactionID = cTrans.getString(cTrans.getColumnIndex(DBApplication.TRANSACTIONS_ID));
            payment.transactionId = Integer.valueOf(transactionID);
            payment.total = cTrans.getString(cTrans.getColumnIndex(DBApplication.PAYMENTS_PAYMENT_AMOUNT));
            payment.payment_type_id = cTrans.getString(cTrans.getColumnIndex(DBApplication.PAYMENTS_PAYMENT_TYPE));

            String sqlProducts = "SELECT P." + DBApplication.PRODUCTS_SERVERID + ", P." + DBApplication.PRODUCTS_PRICE + ", PT." + DBApplication.PRODUCT_TRANSACTIONLINES_QUANTITY +
                    " FROM " + DBApplication.PRODUCT_TRANSACTIONLINES_TABLE + " PT " +
                    " INNER JOIN " + DBApplication.PRODUCTS_TABLE + " P ON PT." + DBApplication.PRODUCT_TRANSACTIONLINES_PRODUCT_ID + " = P." + DBApplication.PRODUCTS_ID +
                    " WHERE PT." + DBApplication.PRODUCT_TRANSACTIONLINES_TRANSACTION_ID + " = " + transactionID;

            Cursor cProds = database.rawQuery(sqlProducts, null);

            for(cProds.moveToFirst(); !cProds.isAfterLast(); cProds.moveToNext()) {
                ProductPayment prod = new ProductPayment();
                prod.product_id = cProds.getString(cProds.getColumnIndex(DBApplication.PRODUCTS_SERVERID));
                prod.price = cProds.getString(cProds.getColumnIndex(DBApplication.PRODUCTS_PRICE));
                prod.qty = cProds.getString(cProds.getColumnIndex(DBApplication.PRODUCT_TRANSACTIONLINES_QUANTITY));

                payment.orders.add(prod);
            }

            payments.add(payment);
        }

        return payments;
    }

    public class PaymentObj {
        int transactionId = 0;
        String total = "";
        String payment_type_id = "";

        ArrayList<ProductPayment> orders = new ArrayList<ProductPayment>();
    }

    public class ProductPayment {
        String product_id = "";
        String price = "";
        String qty = "";
    }
}
