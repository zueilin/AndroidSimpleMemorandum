package com.zuei.mytestproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {
    ListView listView;
    TextView textView;
    FloatingActionButton fab2_2;
    ScrollView scrollView2_2;
    EditText editText2_2;
    Intent intent;
    ArrayList<String> aryDate;
    ArrayList<String> aryText;
    String serch;
    MyAdapter adapter;
    SQLUse sqlUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        listView=findViewById(R.id.listView);
        textView=findViewById(R.id.textView2);
        fab2_2=findViewById(R.id.closeList2_2);
        scrollView2_2=findViewById(R.id.scrollView2_2);
        editText2_2=findViewById(R.id.editText2_2);
        fab2_2.setVisibility(View.INVISIBLE);
        scrollView2_2.setVisibility(View.INVISIBLE);
        intent=getIntent();
        serch=intent.getStringExtra("serch");
        textView.setText(serch);
        aryDate=new ArrayList<>();
        aryText=new ArrayList<>();
        sqlUse=new SQLUse();
        sqlUse.serchSQL(this,serch,aryDate,aryText);
        adapter=new MyAdapter(this);
        listView.setAdapter(adapter);
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public MyAdapter(Context context){
            layoutInflater=LayoutInflater.from(context);
            aryDate=new ArrayList<>();
            aryText=new ArrayList<>();
            sqlUse=new SQLUse();
            sqlUse.serchSQL(getBaseContext(),serch,aryDate,aryText);
        }

        @Override
        public int getCount() {
            return aryDate.size();
        }

        @Override
        public Object getItem(int i) {
            return aryDate.get(i) + aryText.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=layoutInflater.inflate(R.layout.list,null);
            TextView tv1=view.findViewById(R.id.textView);
            TextView tv3=view.findViewById(R.id.textView3);
            tv1.setText(aryDate.get(i));
            tv3.setText(aryText.get(i));
            return view;
        }
    }

    public void back(View view){
        setResult(RESULT_OK,intent);
        finish();
    }

    public void closeList(View view){
        scrollView2_2.setVisibility(View.INVISIBLE);
        fab2_2.setVisibility(View.INVISIBLE);
    }

    String editListDate;

    public void editList(View view){
        scrollView2_2.setVisibility(View.VISIBLE);
        fab2_2.setVisibility(View.VISIBLE);
        TextView tv1=view.findViewById(R.id.textView);
        TextView tv3=view.findViewById(R.id.textView3);
        editListDate=tv1.getText().toString();
        editText2_2.setText(tv3.getText());
    }

    public void editText(View view){
        new AlertDialog.Builder(SelectActivity.this)
                .setMessage("確定要編輯嗎?")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sqlUse=new SQLUse();
                        sqlUse.updateSQL(getBaseContext(),editListDate,editText2_2.getText().toString());
                        adapter=new MyAdapter(SelectActivity.this);
                        listView.setAdapter(adapter);
                        closeList(view);
                    }
                })
                .setPositiveButton("否",null)
                .show();
    }

    public void deleteText(View view){
        new AlertDialog.Builder(SelectActivity.this)
                .setMessage("確定要刪除嗎?")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sqlUse=new SQLUse();
                        sqlUse.deleteSQL(getBaseContext(),editListDate);
                        adapter=new MyAdapter(SelectActivity.this);
                        listView.setAdapter(adapter);
                        closeList(view);
                    }
                })
                .setPositiveButton("否",null)
                .show();
    }

}