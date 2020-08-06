package us.erlang.todo_list_app.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    public String name;
    public String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
