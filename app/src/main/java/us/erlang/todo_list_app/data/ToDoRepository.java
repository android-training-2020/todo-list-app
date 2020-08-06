package us.erlang.todo_list_app.data;

import android.content.Context;

import androidx.room.Room;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import us.erlang.todo_list_app.data.local.AppDatabase;
import us.erlang.todo_list_app.data.local.TasksDao;
import us.erlang.todo_list_app.data.local.UserDao;
import us.erlang.todo_list_app.data.local.UserLocalDataSource;
import us.erlang.todo_list_app.data.remote.LoginResult;
import us.erlang.todo_list_app.data.remote.LoginStatus;
import us.erlang.todo_list_app.data.remote.UserRemoteDataSource;
import us.erlang.todo_list_app.util.Encryptor;

public class ToDoRepository {
    private TasksDao tasksDao;
    private UserDao userDao;
    private UserLocalDataSource userLocalDataSource;
    private UserRemoteDataSource userRemoteDataSource;

    public ToDoRepository(Context context) {
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "tasks").build();
        tasksDao = db.tasksDao();
        userDao = db.userDao();

        this.userLocalDataSource = new UserLocalDataSource(userDao);
        this.userRemoteDataSource = new UserRemoteDataSource();
    }

    /**
     * check user in local db, if not found, then try remote api.
     */
    public Observable<LoginResult> login(User loginUser) {
        return Observable.create(observable -> {
            LoginResult loginResult = new LoginResult();

            this.userLocalDataSource.findUserByName(loginUser.name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DisposableMaybeObserver<User>() {
                        @Override
                        public void onSuccess(User user) {
                            if (user != null && user.name.equals(loginUser.name) && user.password.equals(Encryptor.md5(loginUser.password))) {
                                loginResult.setStatus(LoginStatus.LoginSucceeded);
                                observable.onNext(loginResult);
                                observable.onComplete();
                            } else {
                                remoteLogin(loginUser, observable, loginResult);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            observable.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            remoteLogin(loginUser, observable, loginResult);
                        }
                    });
        });
    }

    private void remoteLogin(User loginUser, ObservableEmitter<LoginResult> observable, LoginResult loginResult) {
        userRemoteDataSource.findUserByName(loginUser.name)
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
    }

    public Completable saveUser(User user) {
        user.password = Encryptor.md5(user.password);
        return this.userDao.save(user);
    }

    public TasksDao getTasksDao() {
        return tasksDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
