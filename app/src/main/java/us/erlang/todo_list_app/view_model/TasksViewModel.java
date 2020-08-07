package us.erlang.todo_list_app.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import us.erlang.todo_list_app.data.Task;

public class TasksViewModel extends ViewModel {
    private MutableLiveData<List<Task>> tasks;

    public MutableLiveData<List<Task>> getTasks() {
        if(tasks == null) {
            tasks = new MutableLiveData<List<Task>>();
        }
        return tasks;
    }
}
