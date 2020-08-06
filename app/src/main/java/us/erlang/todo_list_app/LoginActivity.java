package us.erlang.todo_list_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import us.erlang.todo_list_app.data.ToDoRepository;
import us.erlang.todo_list_app.data.User;
import us.erlang.todo_list_app.data.IUserDataSource;
import us.erlang.todo_list_app.data.remote.LoginResult;
import us.erlang.todo_list_app.data.remote.LoginStatus;
import us.erlang.todo_list_app.data.remote.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private EditText userName;
    private EditText userPassword;
    private LoginViewModel viewModel;
    private LoginResult loginResult = new LoginResult();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userName = findViewById(R.id.user_name);
        userPassword = findViewById(R.id.user_password);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        final Observer<LoginResult> observer = new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable final LoginResult loginResult) {
                if (loginResult.getStatus() == LoginStatus.LoginSucceeded) {
                    Intent intent = new Intent(getApplicationContext(), ToDoListActivity.class);
                    startActivity(intent);
                }
            }
        };
        viewModel.getLoginResult().observe(this, observer);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(userName.getText().toString(), userPassword.getText().toString());
            }
        });
    }

    private void login(String name, String password) {
        User user = new User(name, password);
        final ToDoRepository repository = ToDoApplication.getInstance().getToDoRepository();
        repository
                .login(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                            LoginStatus status = result.getStatus();
                            if (status == LoginStatus.LoginSucceeded) {
                                showMessage(getString(R.string.login_succeeded));
                                viewModel.getLoginResult().postValue(result);
                                repository.getUserDao()
                                        .save(user)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe();
                            } else if (status == LoginStatus.InvalidPassword) {
                                showMessage(getString(R.string.login_invalid_password));
                            } else if (status == LoginStatus.NoUserExists) {
                                showMessage(getString(R.string.login_no_user));
                            }
                        },
                        error -> {
                            showMessage(getString(R.string.login_failed));
                        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}