package us.erlang.todo_list_app;

import android.app.Application;

import us.erlang.todo_list_app.data.UserDataSource;
import us.erlang.todo_list_app.data.local.TasksLocalDataSource;
import us.erlang.todo_list_app.data.remote.UserRemoteDataSource;

public class ToDoApplication extends Application {
    private UserDataSource userDataSource;
    private TasksLocalDataSource tasksDataSource;
    private static ToDoApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        tasksDataSource = new TasksLocalDataSource(getApplicationContext());
        userDataSource = new UserRemoteDataSource();
        application = this;
    }

    public static ToDoApplication getInstance(){
        return application;
    }

    public TasksLocalDataSource getTasksLocalDataSource() {
        return tasksDataSource;
    }

    public UserDataSource getUserRemoteDataSource() {
        return userDataSource;
    }
}
