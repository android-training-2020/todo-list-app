package us.erlang.todo_list_app.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import us.erlang.todo_list_app.data.Task;
import us.erlang.todo_list_app.data.User;

@Database(entities = {Task.class, User.class}, version = 2, exportSchema=false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TasksDao tasksDao();
    public abstract UserDao userDao();
}