package com.zuei.mytestproject2;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{
    ListView listView;
    ScrollView scrollView1;
    ScrollView scrollView2;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    ArrayList<String> aryDate;
    ArrayList<String> aryText;
    MyAdapter adapter;
    SQLUse sqlUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        scrollView1=findViewById(R.id.scrollView);
        scrollView2=findViewById(R.id.scrollView2);
        editText1=findViewById(R.id.editText);
        editText2=findViewById(R.id.editText2);
        editText3=findViewById(R.id.editText3);
        fab1=findViewById(R.id.addList);
        fab2=findViewById(R.id.closeList);

        scrollView1.setVisibility(View.INVISIBLE);
        scrollView2.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.INVISIBLE);

        aryDate=new ArrayList<>();
        aryText=new ArrayList<>();
        sqlUse=new SQLUse();
        sqlUse.selectSQL(this,aryDate,aryText);
        if (aryDate.isEmpty()){
            Calendar calendar = Calendar.getInstance();
            String date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + "：" + calendar.get(Calendar.MINUTE) + "：" + calendar.get(Calendar.SECOND);
            sqlUse.insertSQL(this,date,"輕按可編輯或刪除");
        }
        adapter=new MyAdapter(this);
        listView.setAdapter(adapter);

    }


    public class MyAdapter extends BaseAdapter{

        private LayoutInflater layoutInflater;

        public MyAdapter(Context context){
            layoutInflater=LayoutInflater.from(context);
            aryDate=new ArrayList<>();
            aryText=new ArrayList<>();
            sqlUse=new SQLUse();
            sqlUse.selectSQL(getBaseContext(),aryDate,aryText);
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

    boolean isListNoOpen=true;

    public void addList(View view){
        scrollView1.setVisibility(View.VISIBLE);
        fab1.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.VISIBLE);
        isListNoOpen=false;
    }

    public void closeList(View view){
        if (scrollView1.getVisibility()==View.VISIBLE) {
            scrollView1.setVisibility(View.INVISIBLE);
            isListNoOpen=true;
        }else if(scrollView2.getVisibility()==View.VISIBLE) {
            scrollView2.setVisibility(View.INVISIBLE);
        }
        fab1.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.INVISIBLE);
    }

    public void subList(View view){
        Calendar calendar=Calendar.getInstance();
        String date=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+"："+calendar.get(Calendar.MINUTE)+"："+calendar.get(Calendar.SECOND);
        sqlUse=new SQLUse();
        sqlUse.insertSQL(this,date,editText1.getText().toString());
        adapter=new MyAdapter(this);
        listView.setAdapter(adapter);
        editText1.setText("");
        closeList(view);
    }

    String editListDate;

    public void editList(View view){
        if (isListNoOpen) {
            scrollView2.setVisibility(View.VISIBLE);
            fab1.setVisibility(View.INVISIBLE);
            fab2.setVisibility(View.VISIBLE);
            TextView tv1 = view.findViewById(R.id.textView);
            TextView tv3 = view.findViewById(R.id.textView3);
            editListDate = tv1.getText().toString();
            editText2.setText(tv3.getText());
        }
    }

    public void editText(View view){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("確定要編輯嗎?")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sqlUse=new SQLUse();
                        sqlUse.updateSQL(getBaseContext(),editListDate,editText2.getText().toString());
                        adapter=new MyAdapter(MainActivity.this);
                        listView.setAdapter(adapter);
                        closeList(view);
                    }
                })
                .setPositiveButton("否",null)
                .show();
    }

    public void deleteText(View view){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("確定要刪除嗎?")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sqlUse = new SQLUse();
                        sqlUse.deleteSQL(getBaseContext(), editListDate);
                        adapter = new MyAdapter(MainActivity.this);
                        listView.setAdapter(adapter);
                        closeList(view);
                    }
                })
                .setPositiveButton("否",null)
                .show();
    }

    public void selectList(View view){
        Intent intent=new Intent(MainActivity.this,SelectActivity.class);
        intent.putExtra("serch",editText3.getText().toString());
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            adapter = new MyAdapter(this);
            listView.setAdapter(adapter);
        }
    }
}
