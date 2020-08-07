package us.erlang.todo_list_app.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurrentTaskViewModel extends ViewModel {
    private MutableLiveData<Long> currentTaskId;

    public MutableLiveData<Long> getCurrentTaskId() {
        if(currentTaskId == null) {
            currentTaskId = new MutableLiveData<Long>();
        }
        return currentTaskId;
    }
}
