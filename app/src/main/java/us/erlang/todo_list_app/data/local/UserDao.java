package us.erlang.todo_list_app.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import us.erlang.todo_list_app.data.User;

@Dao
public interface UserDao {
    @Query("select * from users where name=:name")
    Maybe<User> findByName(String name);

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    Completable save(User user);
}
