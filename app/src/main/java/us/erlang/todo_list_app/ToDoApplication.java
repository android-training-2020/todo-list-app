package us.erlang.todo_list_app;

import android.app.Application;

import us.erlang.todo_list_app.data.ToDoRepository;
import us.erlang.todo_list_app.data.local.UserDao;
import us.erlang.todo_list_app.data.local.UserLocalDataSource;
import us.erlang.todo_list_app.data.remote.UserRemoteDataSource;

public class ToDoApplication extends Application {


    private ToDoRepository toDoRepository;
    private static ToDoApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

        toDoRepository = new ToDoRepository(getApplicationContext());

        application = this;
    }

    public static ToDoApplication getInstance() {
        return application;
    }

    public ToDoRepository getToDoRepository() {
        return toDoRepository;
    }

}
