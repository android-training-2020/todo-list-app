package us.erlang.todo_list_app.view_model;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import us.erlang.todo_list_app.data.remote.LoginResult;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginResult> loginResult;

    public MutableLiveData<LoginResult> getLoginResult() {
        if (loginResult == null) {
            loginResult = new MutableLiveData<LoginResult>();
        }
        return loginResult;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
