package us.erlang.todo_list_app;

import android.content.SharedPreferences;

public class SessionKeeper {
    private SharedPreferences sp;
    private static String SP_LIVE_SESSION = "user_session";

    public SessionKeeper(SharedPreferences sp) {
        this.sp = sp;
    }

    public boolean isSessionAlive() {
        return this.sp.getBoolean(SP_LIVE_SESSION, false);
    }

    public void login(){
        this.update(true);
    }

    public void logout(){
        this.update(false);
    }

    private void update(boolean isValidSession){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SP_LIVE_SESSION, isValidSession);
        editor.commit();
    }
}
