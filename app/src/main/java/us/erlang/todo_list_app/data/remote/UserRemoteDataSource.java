package us.erlang.todo_list_app.data.remote;

import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Maybe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import us.erlang.todo_list_app.data.User;
import us.erlang.todo_list_app.data.IUserDataSource;

public class UserRemoteDataSource implements IUserDataSource {
    @Override
    public Maybe<User> findUserByName(String userName) {
        return Maybe.create(observable -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://twc-android-bootcamp.github.io/fake-data/data/user.json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String json = response.body().string();
                Gson gson = new Gson();
                User user = gson.fromJson(json, User.class);
                observable.onSuccess(user);
            } catch (IOException e) {
                e.printStackTrace();
                observable.onError(e);
            }
        });
    }
}
