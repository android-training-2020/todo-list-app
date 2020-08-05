package us.erlang.todo_list_app.data.remote;

import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import us.erlang.todo_list_app.data.User;
import us.erlang.todo_list_app.data.UserDataSource;
import us.erlang.todo_list_app.util.Encryptor;

public class UserRemoteDataSource implements UserDataSource {
    @Override
    public Observable<LoginResult> login(User user) {
        return Observable.create(observable -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://twc-android-bootcamp.github.io/fake-data/data/user.json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String json = response.body().string();
                Gson gson = new Gson();
                User remoteUser = gson.fromJson(json, User.class);
                LoginResult loginResult = new LoginResult();
                if (remoteUser.name.equals(user.name)) {
                    if (remoteUser.password.equals(Encryptor.md5(user.password))) {
                        loginResult.setStatus(LoginStatus.LoginSucceeded);
                    } else {
                        loginResult.setStatus(LoginStatus.InvalidPassword);
                    }
                } else {
                    loginResult.setStatus(LoginStatus.NoUserExists);
                }

                observable.onNext(loginResult);
                observable.onComplete();
            } catch (IOException e) {
                e.printStackTrace();
                observable.onError(e);
            }
        });
    }

}
