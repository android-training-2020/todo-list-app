package us.erlang.todo_list_app.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import us.erlang.todo_list_app.data.Task;

@Database(entities = {Task.class}, version = 3, exportSchema=false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TasksDao tasksDao();
}