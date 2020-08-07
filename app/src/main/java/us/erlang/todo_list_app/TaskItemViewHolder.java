package us.erlang.todo_list_app;

import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import us.erlang.todo_list_app.data.Task;
import us.erlang.todo_list_app.view_model.CurrentTaskViewModel;

public class TaskItemViewHolder extends RecyclerView.ViewHolder {
    private View itemView;
    private TextView title, deadline;
    private CheckBox completed;
    private ViewModel viewModel;

    public TaskItemViewHolder(View view, ViewModel viewModel) {
        super(view);
        this.viewModel = viewModel;

        itemView = view;
        title = itemView.findViewById(R.id.item_title);
        completed = itemView.findViewById(R.id.item_complete);
        deadline = itemView.findViewById(R.id.item_deadline);
    }

    public void setTaskData(Task task) {
        itemView.setOnClickListener(null);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CurrentTaskViewModel)viewModel).getCurrentTaskId().postValue(task.getId());
            }
        });

        title.setText(task.getTitle());
        if(task.isCompleted()) {
            title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        completed.setTag(task.getId());
        completed.setChecked(task.isCompleted());
        completed.setOnCheckedChangeListener(null);
        completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                ToDoApplication.getInstance().getToDoRepository().getTasksDao()
                        .updateCompleted(task.getId(), checked)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                ;
            }
        });

        if(task.getDeadline() != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(task.getDeadline());
            deadline.setText(dateString);
        }
    }
}
