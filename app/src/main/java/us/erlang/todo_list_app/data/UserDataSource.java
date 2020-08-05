package us.erlang.todo_list_app.data;

import io.reactivex.Observable;
import us.erlang.todo_list_app.data.remote.LoginResult;

public interface UserDataSource {
    public Observable<LoginResult> login(User user);
}
