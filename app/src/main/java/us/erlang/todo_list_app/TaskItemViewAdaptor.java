package us.erlang.todo_list_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import us.erlang.todo_list_app.data.Task;
import us.erlang.todo_list_app.view_model.CurrentTaskViewModel;

public class TaskItemViewAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Task> list;
    private FragmentActivity activity;

    public TaskItemViewAdaptor(List<Task> tasks, FragmentActivity activity) {
        list = tasks;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_view, parent, false);
        return new TaskItemViewHolder(view, ViewModelProviders.of(this.activity).get(CurrentTaskViewModel.class));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Task task = list.get(position);
        if (task != null) {
            ((TaskItemViewHolder) holder).setTaskData(task);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

