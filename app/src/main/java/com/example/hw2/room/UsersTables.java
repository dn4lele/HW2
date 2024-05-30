package com.example.hw2.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users", indices = {@Index(value = {"id"}, unique = true)})
public class UsersTables {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "counter")
    public int counter;
    @ColumnInfo(name = "id")
    public String uid;
    @ColumnInfo(name = "firstname")
    public String firstname;
    @ColumnInfo(name = "lastname")
    public String lastname;

    @ColumnInfo(name = "city")
    public String city;

    @ColumnInfo(name = "country")
    public String country;
    @ColumnInfo(name = "picture")
    public String picture;


}
