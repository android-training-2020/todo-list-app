package us.erlang.todo_list_app;

import android.app.Application;
import android.content.Context;

import us.erlang.todo_list_app.data.ToDoRepository;

public class ToDoApplication extends Application {
    private static ToDoApplication application;

    private ToDoRepository toDoRepository;
    private SessionKeeper sessionKeeper;

    @Override
    public void onCreate() {
        super.onCreate();

        this.toDoRepository = new ToDoRepository(getApplicationContext());

        this.sessionKeeper = new SessionKeeper(getApplicationContext()
                .getSharedPreferences(getString(R.string.sp_app_id), Context.MODE_PRIVATE));

        application = this;
    }

    public static ToDoApplication getInstance() {
        return application;
    }

    public ToDoRepository getToDoRepository() {
        return toDoRepository;
    }

    public SessionKeeper getSessionKeeper() {
        return sessionKeeper;
    }

}
