package com.coderefer.easywallet;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Dbhelper mHelper;
    private SQLiteDatabase mDb;

    private ArrayList<MoneyItem> mMoneyItemList = new ArrayList<>();
    private MoneyListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new Dbhelper(this);
        mDb = mHelper.getReadableDatabase();

        loadDataFromDb();

        mAdapter = new MoneyListAdapter(
                this,
                R.layout.item,
                mMoneyItemList
        );

        ListView lv = findViewById(R.id.list_view);
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MoneyItem item = mMoneyItemList.get(position);
                String mm = item.money;

                Intent intent = new Intent(MainActivity.this,AddMoneyActivity.class);
                intent.putExtra("posistion",mMoneyItemList);
                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                String[] items = new String[]{"แก้ไขข้อมูล", "ลบข้อมูล"};

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) { // แก้ไขข้อมูล
                            MoneyItem item = mMoneyItemList.get(position);
                            int phoneId = item.id;

                            ContentValues cv = new ContentValues();
                            cv.put(Dbhelper.COL_MONEY, " ");

                            mDb.update(
                                    Dbhelper.TABLE_NAME,
                                    cv,
                                    Dbhelper.COL_ID + "=?",
                                    new String[]{String.valueOf(phoneId)}
                            );
                            loadDataFromDb();
                            mAdapter.notifyDataSetChanged();

                        } else if (i == 1) { // ลบข้อมูล
                            MoneyItem item = mMoneyItemList.get(position);
                            int phoneId = item.id;

                            mDb.delete(
                                    Dbhelper.TABLE_NAME,
                                    Dbhelper.COL_ID + "=?",
                                    new String[]{String.valueOf(phoneId)}
                            );
                            loadDataFromDb();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

       final Button fab = findViewById(R.id.revenue_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddMoneyActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    } // ปิดเมธอด onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                loadDataFromDb();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void loadDataFromDb() {
        Cursor cursor = mDb.query(
                Dbhelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        mMoneyItemList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Dbhelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(Dbhelper.COL_TITLE));
            String number = cursor.getString(cursor.getColumnIndex(Dbhelper.COL_MONEY));
            String picture = cursor.getString(cursor.getColumnIndex(Dbhelper.COL_PICTURE));

            MoneyItem item = new MoneyItem(id, title, number, picture);
            mMoneyItemList.add(item);
        }
    }
} // ปิดคลาส MainActivity

