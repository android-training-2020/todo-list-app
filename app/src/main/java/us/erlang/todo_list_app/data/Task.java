package us.erlang.todo_list_app.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import us.erlang.todo_list_app.util.DateConverter;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo
    public String title;

    @ColumnInfo
    public boolean completed;

    @ColumnInfo
    @TypeConverters(DateConverter.class)
    public Date deadline;
}
