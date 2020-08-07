package us.erlang.todo_list_app.data;

import android.content.Context;

import androidx.room.Room;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import us.erlang.todo_list_app.data.local.AppDatabase;
import us.erlang.todo_list_app.data.local.TasksDao;
import us.erlang.todo_list_app.data.remote.LoginResult;
import us.erlang.todo_list_app.data.remote.LoginStatus;
import us.erlang.todo_list_app.data.remote.UserRemoteDataSource;
import us.erlang.todo_list_app.util.Encryptor;

public class ToDoRepository {
    private TasksDao tasksDao;
    private UserRemoteDataSource userRemoteDataSource;

    public ToDoRepository(Context context) {
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "tasks").build();
        tasksDao = db.tasksDao();

        this.userRemoteDataSource = new UserRemoteDataSource();
    }

    /**
     * check user in local db, if not found, then try remote api.
     */
    public Observable<LoginResult> login(User loginUser) {
        return Observable.create(observable -> {
            LoginResult loginResult = new LoginResult();

            this.userRemoteDataSource.findUserByName(loginUser.name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DisposableMaybeObserver<User>() {
                        @Override
                        public void onSuccess(User remoteUser) {
                            if (remoteUser != null && remoteUser.name.equals(loginUser.name)) {
                                if (remoteUser.password.equals(Encryptor.md5(loginUser.password))) {
                                    loginResult.setStatus(LoginStatus.LoginSucceeded);
                                } else {
                                    loginResult.setStatus(LoginStatus.InvalidPassword);
                                }
                            } else {
                                loginResult.setStatus(LoginStatus.NoUserExists);
                            }
                            observable.onNext(loginResult);
                            observable.onComplete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            observable.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            loginResult.setStatus(LoginStatus.NoUserExists);
                            observable.onNext(loginResult);
                            observable.onComplete();
                        }
                    });
        });
    }


    public TasksDao getTasksDao() {
        return tasksDao;
    }
}
