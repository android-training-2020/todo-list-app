package us.erlang.todo_list_app.data;

import io.reactivex.Maybe;

public interface IUserDataSource {
    public Maybe<User> findUserByName(String userName);
}
