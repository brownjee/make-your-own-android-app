package app.com.brownjee.nigerpot;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import app.com.brownjee.nigerpot.data.CustomerDetails;


public class SendOrder extends Activity {
    String mode;
    String mode1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_order);

        ImageView foodImg = (ImageView)findViewById(R.id.displayImg);
        TextView foodName = (TextView)findViewById(R.id.displayTxt);

        Button send_button = (Button) findViewById(R.id.button1);

        Intent i = getIntent();
        String newFName = i.getStringExtra("foodname");
        int fImg = i.getIntExtra("foodImg", R.drawable.ic_launcher);

        foodImg.setImageResource(fImg);
        foodName.setText(newFName);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText customerName = (EditText) findViewById(R.id.editTextName);
                EditText customerPhone = (EditText) findViewById(R.id.editTextPhone);
                EditText customerAddress = (EditText) findViewById(R.id.editTextAddress);
                EditText quantity = (EditText) findViewById(R.id.editTextQuantity);
                RadioButton pickUp = (RadioButton) findViewById(R.id.radioButtonPickUp);
                RadioButton deliver = (RadioButton) findViewById(R.id.deliver);



                boolean del = deliver.isChecked();
                boolean pick = pickUp.isChecked();
                if (del = true){
                    mode= "TRUE";
                    mode1="FALSE";
                }
                else if (pick = true){
                    mode= "FALSE";
                    mode1 ="TRUE";
                }

                ContentResolver so = getContentResolver();
                ContentValues contentValue = new ContentValues();
                contentValue.put(CustomerDetails.NAME, customerName.getText().toString());
                contentValue.put(CustomerDetails.ADDRESS, customerAddress.getText().toString());
                contentValue.put(CustomerDetails.PHONE, customerPhone.getText().toString());
                contentValue.put(CustomerDetails.QUANTITY, quantity.getText().toString());
                contentValue.put(CustomerDetails.PICK_UP, mode);
                contentValue.put(CustomerDetails.DELIVER, mode1);
                so.insert(CustomerDetails.CONTENT_URI, contentValue);

                // QUERY TABLE FOR ALL COLUMNS AND ROWS
                Cursor c = so.query(CustomerDetails.CONTENT_URI, null, null,
                        null, CustomerDetails.QUANTITY + " ASC");
                //LET THE ACTIVITY MANAGE THE CURSOR
                startManagingCursor(c);
                int idColumn = c.getColumnIndex(CustomerDetails.ID);
                int nameColumn = c.getColumnIndex(CustomerDetails.NAME);
                int addressColumn = c.getColumnIndex(CustomerDetails.ADDRESS);
                int phoneColumn = c.getColumnIndex(CustomerDetails.PHONE);
                int quantityColumn = c.getColumnIndex(CustomerDetails.QUANTITY);
                int pickupColumn = c.getColumnIndex(CustomerDetails.PICK_UP);
                int deliverColumn = c.getColumnIndex(CustomerDetails.DELIVER);
                while (c.moveToNext()) {
                    int id = c.getInt(idColumn);
                    String customer_name = c.getString(nameColumn);
                    String address = c.getString(addressColumn);
                    String phone_no = c.getString(phoneColumn);
                    int quantity_required = c.getInt(quantityColumn);
                    String pick_up = c.getString(pickupColumn);
                    String deli_ver = c.getString(deliverColumn);

                    System.out.println("RETRIEVED ||" + id + "||" + customer_name + "||" + address + "||"
                            + phone_no + "||" + quantity_required + "||" + pick_up + "||" + deli_ver);

                    Intent intent = new Intent(SendOrder.this, Order_Successful.class);
                    startActivity(intent);

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

