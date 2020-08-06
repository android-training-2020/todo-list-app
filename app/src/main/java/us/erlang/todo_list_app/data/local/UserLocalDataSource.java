package us.erlang.todo_list_app.data.local;

import io.reactivex.Maybe;
import us.erlang.todo_list_app.data.IUserDataSource;
import us.erlang.todo_list_app.data.User;

public class UserLocalDataSource implements IUserDataSource {
    private UserDao userDao;

    public UserLocalDataSource(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Maybe<User> findUserByName(String userName) {
        return this.userDao.findByName(userName);
    }
}
