package us.erlang.todo_list_app;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import us.erlang.todo_list_app.data.Task;

public class TaskItemViewHolder extends RecyclerView.ViewHolder {
    private View itemView;
    private TextView title, deadline;
    private CheckBox completed;

    public TaskItemViewHolder(View view) {
        super(view);
        itemView = view;
        title = itemView.findViewById(R.id.item_title);
        completed = itemView.findViewById(R.id.item_complete);
        deadline = itemView.findViewById(R.id.item_deadline);
    }

    public void setTaskData(Task task) {
        title.setText(task.getTitle());

        completed.setTag(task.getId());
        completed.setChecked(task.isCompleted());

        if(task.getDeadline() != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(task.getDeadline());
            deadline.setText(dateString);
        }
    }
}
