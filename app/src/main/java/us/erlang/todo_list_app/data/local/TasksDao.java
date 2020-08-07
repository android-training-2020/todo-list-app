package us.erlang.todo_list_app.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import us.erlang.todo_list_app.data.Task;

@Dao
public interface TasksDao {
    @Query("SELECT * FROM tasks WHERE id=:id")
    Maybe<Task> findTaskById(Long id);

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    Completable insertTask(Task task);

    @Update
    Completable updateTask(Task task);

    @Query("SELECT * FROM Tasks order by completed, deadline")
    Observable<List<Task>> getTasks();

    @Query("UPDATE tasks SET completed = :completed WHERE id = :taskId")
    Completable updateCompleted(Long taskId, boolean completed);

    @Query("DELETE FROM Tasks WHERE id = :id")
    Completable deleteTask(Long id);
}
