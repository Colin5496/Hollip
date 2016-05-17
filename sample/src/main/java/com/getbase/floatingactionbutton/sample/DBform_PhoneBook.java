package com.getbase.floatingactionbutton.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.getbase.floatingactionbutton.sample.R.layout.activityspam_db;

/**
 * Created by 이상헌 on 2015-10-05.
 */
public class DBform_PhoneBook extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityspam_db);

       final DBManager_PhoneBook dbManager = new DBManager_PhoneBook(getApplicationContext(), "app.db", null, 1);
         // DB에 저장 될 속성을 입력받는다
        final EditText etPakage = (EditText) findViewById(R.id.et_pakage);
        final EditText etApp_name = (EditText) findViewById(R.id.et_app_name);
        final EditText etButton_id = (EditText) findViewById(R.id.et_button_id);
        final EditText etButton_status = (EditText) findViewById(R.id.et_button_status);


        // 쿼리 결과 입력
        final TextView tvResult = (TextView) findViewById(R.id.tv_result);

        // Insert
        Button btnInsert = (Button) findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // insert into 테이블명 values (값, 값, 값...);
                String pakage = etPakage.getText().toString();
                String app_name = etApp_name.getText().toString();
                String button_id = etButton_id.getText().toString();
                String button_status = etButton_status.getText().toString();
                dbManager.insert("insert into APP_LIST values(null, '" + pakage + "', '" + app_name + "', " + button_id +", '"+button_status+"');");

                tvResult.setText( dbManager.PrintData() );
            }
        });
/*

        // Update
        Button btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update 테이블명 where 조건 set 값;
                String name = etName.getText().toString();
                String price = etPrice.getText().toString();
                dbManager.update("update FOOD_LIST set price = " + price + " where name = '" + name + "';");

                tvResult.setText( dbManager.PrintData() );
            }
        });

        // Delete
        Button btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // delete from 테이블명 where 조건;
                String name = etName.getText().toString();
                dbManager.delete("delete from FOOD_LIST where name = '" + name + "';");

                tvResult.setText( dbManager.PrintData() );
            }
        });
*/

        // Select
        Button btnSelect = (Button) findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tvResult.setText( dbManager.PrintData() );
            }
        });
    }
}

