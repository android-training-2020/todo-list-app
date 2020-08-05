package us.erlang.todo_list_app.data.local;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import us.erlang.todo_list_app.data.Task;

public class TasksLocalDataSource implements TasksDao {
    private TasksDao tasksDao;

    public TasksLocalDataSource(Context context) {
        ToDoDatabase db = Room.databaseBuilder(context,
                ToDoDatabase.class, "tasks").build();
        tasksDao = db.tasksDao();
    }

    public Maybe<Task> findByTitle(String title){
        return this.tasksDao.findByTitle(title);
    }

    public Completable save(Task task){
        return this.tasksDao.save(task);
    }

    @Override
    public List<Task> getTasks() {
        return null;
    }

    @Override
    public void insertTask(Task task) {

    }

    @Override
    public int updateTask(Task task) {
        return 0;
    }

    @Override
    public void updateCompleted(String taskId, boolean completed) {

    }

    @Override
    public int deleteCompletedTasks() {
        return 0;
    }
}
