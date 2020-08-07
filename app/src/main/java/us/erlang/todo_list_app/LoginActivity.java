package us.erlang.todo_list_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import us.erlang.todo_list_app.data.ToDoRepository;
import us.erlang.todo_list_app.data.User;
import us.erlang.todo_list_app.data.remote.LoginResult;
import us.erlang.todo_list_app.data.remote.LoginStatus;
import us.erlang.todo_list_app.data.remote.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private EditText userName;
    private EditText userPassword;
    private LoginViewModel loginViewModel;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ToDoApplication.getInstance().getSessionKeeper().isSessionAlive()) {
            gotoListActivity();
            return;
        }

        userName = findViewById(R.id.user_name);
        userPassword = findViewById(R.id.user_password);

        checkLoginForm();

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        final Observer<LoginResult> observer = new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable final LoginResult loginResult) {
                if (loginResult.getStatus() == LoginStatus.LoginSucceeded) {
                    Intent intent = new Intent(getApplicationContext(), ToDoListActivity.class);
                    startActivity(intent);
                }
            }
        };
        loginViewModel.getLoginResult().observe(this, observer);

        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(userName.getText().toString(), userPassword.getText().toString());
            }
        });
    }

    private void checkLoginForm() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!isValidUserName(userName.getText().toString())) {
                    userName.setError(getString(R.string.invalid_user_name));
                    loginButton.setEnabled(false);
                }

                if(!isValidPassword(userPassword.getText().toString())) {
                    userPassword.setError(getString(R.string.invalid_password));
                    loginButton.setEnabled(false);
                }

                if(isValidUserName(userName.getText().toString()) && isValidPassword(userPassword.getText().toString())) {
                    loginButton.setEnabled(true);
                }
            }
        };

        userName.addTextChangedListener(textWatcher);
        userPassword.addTextChangedListener(textWatcher);
    }

    private boolean isValidUserName(String name) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9-]{3,12}$");
        Matcher m = pattern.matcher(name);
        return m.matches();
    }

    private boolean isValidPassword(String password) {
        return password != null && password.trim().length() >= 6 && password.trim().length() <= 18;
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
                                loginViewModel.getLoginResult().postValue(result);

                                ToDoApplication.getInstance().getSessionKeeper().login();
                                gotoListActivity();
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

    private void gotoListActivity() {
        Intent intent = new Intent(this, ToDoListActivity.class);
        startActivity(intent);
    }
}