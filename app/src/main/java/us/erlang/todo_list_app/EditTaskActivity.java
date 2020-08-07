package us.erlang.todo_list_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import us.erlang.todo_list_app.data.Task;

public class EditTaskActivity extends AppCompatActivity {

    private EditText taskTitle, taskDescription;
    private CalendarView calendarView;
    private Task currentTask;
    private Date deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        taskTitle = findViewById(R.id.edit_task_title);
        taskDescription = findViewById(R.id.edit_task_desc);
        calendarView = findViewById(R.id.edit_calendar_view);

        Long taskId = getIntent().getLongExtra("currentTaskId", 0);
        ToDoApplication.getInstance().getToDoRepository().getTasksDao()
                .findTaskById(taskId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new MaybeObserver<Task>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Task task) {
                        currentTask = task;
                        taskTitle.setText(task.getTitle());
                        taskDescription.setText(task.getDescription() != null ? task.getDescription() : "");
                        if (task.getDeadline() != null) {
                            calendarView.setDate(task.getDeadline().getTime());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String chosenDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    deadline = sdf.parse(chosenDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        FloatingActionButton editButton = findViewById(R.id.edit_task);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTask.setTitle(taskTitle.getText().toString());
                currentTask.setDescription(taskDescription.getText().toString());
                if (deadline != null) {
                    currentTask.setDeadline(deadline);
                }

                updateTask(currentTask);
            }
        });

        FloatingActionButton deleteButton = findViewById(R.id.delete_task);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask(currentTask.getId());
            }
        });
    }

    private void updateTask(Task task) {
        ToDoApplication.getInstance().getToDoRepository().getTasksDao()
                .updateTask(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        gotoListViewActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("EditTaskActivity", "failed to update task", e);
                    }
                });
    }

    private void deleteTask(Long taskId) {
        ToDoApplication.getInstance().getToDoRepository().getTasksDao()
                .deleteTask(taskId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        gotoListViewActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("EditTaskActivity", "failed to delete task", e);
                    }
                });
    }

    private void gotoListViewActivity() {
        Intent intent = new Intent(this, ToDoListActivity.class);
        startActivity(intent);
    }
}