package us.erlang.todo_list_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import us.erlang.todo_list_app.data.Task;

public class AddTaskActivity extends AppCompatActivity {
    private Date deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText title = findViewById(R.id.task_title);
        EditText desc = findViewById(R.id.task_desc);

        CalendarView calendarView = findViewById(R.id.calendar_view);
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

        FloatingActionButton submitButton = findViewById(R.id.submit_task);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask(title.getText().toString(), desc.getText().toString());
            }
        });
    }

    private void addTask(String title, String desc) {
        ToDoApplication.getInstance().getToDoRepository().getTasksDao()
                .insertTask(new Task(title, desc, false, deadline))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(), "succeed to add task", Toast.LENGTH_SHORT);
                        gotoListViewActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("AddTaskActivity", "failed to add task", e);
                    }
                });
    }

    private void gotoListViewActivity() {
        Intent intent = new Intent(this, ToDoListActivity.class);
        startActivity(intent);
    }
}