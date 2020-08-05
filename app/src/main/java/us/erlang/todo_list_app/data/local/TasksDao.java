package us.erlang.todo_list_app.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import us.erlang.todo_list_app.data.Task;

@Dao
public interface TasksDao {
    @Query("SELECT * FROM tasks WHERE title=:title")
    Maybe<Task> findByTitle(String title);

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    Completable save(Task task);

    @Query("SELECT * FROM Tasks")
    List<Task> getTasks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Update
    int updateTask(Task task);

    @Query("UPDATE tasks SET completed = :completed WHERE id = :taskId")
    void updateCompleted(String taskId, boolean completed);

    @Query("DELETE FROM Tasks WHERE completed = 1")
    int deleteCompletedTasks();

}
