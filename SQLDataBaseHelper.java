package com.zuei.mytestproject2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLDataBaseHelper extends SQLiteOpenHelper {

    public SQLDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlTable = "CREATE TABLE IF NOT EXISTS List(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT NOT NULL," +
                "listText TEXT " +
                ")";
        sqLiteDatabase.execSQL(sqlTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String sqlDrop = "DROP TABLE List";
        sqLiteDatabase.execSQL(sqlDrop);
    }
}

class SQLUse{
    private static final String dataBaseName="DataBaseList";
    private static final int dataBaseVersion=1;
    private static final String tableName="List";
    private static SQLiteDatabase db;
    private SQLDataBaseHelper sqlDataBaseHelper;

    protected void selectSQL(Context context,ArrayList aryDate,ArrayList aryText){
        sqlDataBaseHelper=new SQLDataBaseHelper(context,dataBaseName,null,dataBaseVersion);
        db=sqlDataBaseHelper.getWritableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM "+tableName,null);
        cursor.moveToLast();
        for (int i = 0; i <cursor.getCount(); ++i) {
            aryDate.add(i,cursor.getString(1));
            aryText.add(i,cursor.getString(2));
            cursor.moveToPrevious();
        }
    }

    protected void insertSQL(Context context,String date,String text){
        sqlDataBaseHelper=new SQLDataBaseHelper(context,dataBaseName,null,dataBaseVersion);
        db=sqlDataBaseHelper.getWritableDatabase();
        db.execSQL("INSERT INTO "+tableName+"(date,listText) VALUES (?,?)",new String[]{date,text});
    }

    protected void updateSQL(Context context,String date,String updateText){
        sqlDataBaseHelper=new SQLDataBaseHelper(context,dataBaseName,null,dataBaseVersion);
        db=sqlDataBaseHelper.getWritableDatabase();
        db.execSQL("UPDATE "+tableName+" SET listText=? WHERE date=?",new String[]{updateText,date});
    }

    protected void deleteSQL(Context context,String date){
        sqlDataBaseHelper=new SQLDataBaseHelper(context,dataBaseName,null,dataBaseVersion);
        db=sqlDataBaseHelper.getWritableDatabase();
        db.execSQL("DELETE FROM "+tableName+" WHERE date=?",new String[]{date});
    }

    protected void serchSQL(Context context,String serch,ArrayList aryDate,ArrayList aryText){
        sqlDataBaseHelper=new SQLDataBaseHelper(context,dataBaseName,null,dataBaseVersion);
        db=sqlDataBaseHelper.getWritableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM "+tableName+" WHERE listText LIKE \'%"+serch+"%\'",null);
        cursor.moveToLast();
        for (int i = 0; i <cursor.getCount(); ++i) {
            aryDate.add(i,cursor.getString(1));
            aryText.add(i,cursor.getString(2));
            cursor.moveToPrevious();
        }
    }

}
