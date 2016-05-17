package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SpamlistActivity extends Activity {

    String tag = null;
    private ListView cmn_list_view;

    private ListAdapter_PhoneBook listAdapter;
    private ArrayList<phoneBook> listdata;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spam_list);




        cmn_list_view = (ListView) findViewById(R.id.list_Contact);
        listdata = new ArrayList<phoneBook>();

        final ListViewSwipeGesture_PhoneBook touchListener = new ListViewSwipeGesture_PhoneBook(
                cmn_list_view, swipeListener, this);

        touchListener.SwipeType = ListViewSwipeGesture_PhoneBook.Dismiss;
        cmn_list_view.setOnTouchListener(touchListener);

        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_PICK);
                mIntent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(mIntent, 0);
            }
        });
        InitializeValues();

    }

    private void InitializeValues() {

        MainActivity.mainSpamList = MainActivity.dbManager.selectAll_prank();
        for (String element : MainActivity.mainSpamList) {
            String[] split = element.split("/");

            String temp_name = split[0];
            String temp_number = split[1];

            listdata.add(new phoneBook(temp_name, temp_number));
        }
        // TODO Auto-generated method stub
        listAdapter = new ListAdapter_PhoneBook(this, listdata);
        cmn_list_view.setAdapter(listAdapter);

    }

    ListViewSwipeGesture_PhoneBook.TouchCallbacks swipeListener = new ListViewSwipeGesture_PhoneBook.TouchCallbacks() {

        @Override
        public void FullSwipeListView(int position) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "Action_2", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void HalfSwipeListView(int position) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "Action_1", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void LoadDataForScroll(int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
            // TODO Auto-generated method stub

            for (int i : reverseSortedPositions) {

                String temp_name = listdata.get(i).getSampleName();
                String temp_number = listdata.get(i).getSampleNumber();
                listdata.remove(i);
                // delete DB
                MainActivity.dbManager.delete_prank("delete from PRANK_LIST where user_number = '" + temp_number + "';");
                listAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Delete " + temp_name, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void OnClickListView(int position) {
            // TODO Auto-generated method stub
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);

            cursor.moveToFirst();
            String sName = cursor.getString(0);
            String sNumber = cursor.getString(1);
            cursor.close();

            if (checkNumber(sNumber)) {
                Log.d(tag, "sName = " + sName + ", " + "sNumber = " + sNumber);
                listdata.add(new phoneBook(sName, sNumber));
                listAdapter.notifyDataSetChanged();

                MainActivity.dbManager.insert_prank("insert into PRANK_LIST values(null, '" + sName + "', '" + sNumber + "');");
            } else {
                Toast.makeText(getApplicationContext(), "Already Exist", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean checkNumber(String sNumber) {
        // 같은게 없으면 true
        String temp_number = MainActivity.dbManager.select_number_prank("select user_number from PRANK_LIST where user_number = '" + sNumber + "';");
        if (temp_number.equals("")) return true;
        else return false;
    }
}