package app.com.brownjee.nigerpot.data;

import android.net.Uri;

/**
 * Created by BROWNJEE on 18-Mar-15.
 */
public class CustomerDetails {

    // EACH CUSTOMER HAS UNIQUE ID
    public static final String ID = "_id";

    // NAME OF THE CUSTOMER
    public static final String NAME = "customer_name";

    // ADDRESS OF CUSTOMER'S RESIDENCE
    public static final String ADDRESS = "address";

    //PHONE NO OF CUSTOMER
    public static final String PHONE = "phone_no";

    // QUANTITY REQUIRED
    public static final String QUANTITY = "quantity_required";

    //OPTION FOR DELIVERY
    public static final String PICK_UP = "pick_up";
    public static final String DELIVER = "deliver";

    // NAME OF THE TABLE
    public static final String TABLE_NAME = "customer_details";

    /**

     * DEFINE THE CONTENT TYPE AND URI
     */
    // THE CONTENT URI TO OUR PROVIDER
    public static final Uri CONTENT_URI = Uri.parse("content://" + FoodContentProvider.AUTHORITY + "/customer");

    //MIME TYPE FOR GROUP OF CUSTOMERS
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + FoodContentProvider.AUTHORITY + "/customer";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + FoodContentProvider.AUTHORITY + "/customer";

    // RELATIVE POSITION OF CUSTOMER CDID  IN URI
    public static final int CDID_PATH_POSITION = 1;

}
