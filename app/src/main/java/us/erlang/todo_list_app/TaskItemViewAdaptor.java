package us.erlang.todo_list_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import us.erlang.todo_list_app.data.Task;

public class TaskItemViewAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Task> list;

    public TaskItemViewAdaptor(List<Task> myDataset) {
        list = myDataset;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_view, parent, false);
        return new TaskItemViewHolder(view);
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

