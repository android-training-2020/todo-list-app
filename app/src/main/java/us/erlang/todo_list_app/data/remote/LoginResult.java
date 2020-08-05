package us.erlang.todo_list_app.data.remote;

public class LoginResult {
    private LoginStatus status;


    public LoginResult() {
    }

    public LoginStatus getStatus() {
        return status;
    }

    public void setStatus(LoginStatus status) {
        this.status = status;
    }
}
