package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diary.DB.DBHelper;
import com.example.diary.DB.DataBases;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private TextView todayDate, diaryTitle;
    private ImageButton calendar;
    private Button diaryAdd, todoAdd;
    private EditText diary, todo;
    private String Name, Date, Diary, Todo, btnDiary;
    private String resultDiary, resultTodo;

    private ListView listview_todolist;
    private TodoAdapter todoAdapter;

    final Calendar mCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener mDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String mFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(mFormat, Locale.KOREA);

            todayDate.setText(sdf.format(mCalendar.getTime()));

            Date = String.valueOf(todayDate.getText());
            //일기 조회
            resultDiary = dbHelper.getResult_diary(Date);
            resultDiary = resultDiary != "" ? resultDiary : "데이터 없음";

            if(resultDiary.contains("데이터 없음")) {
                diary.setText("등록된 일기가 없습니다. 일기를 등록해주세요.");
                diaryAdd.setText("등록");
            } else {
                diary.setText(resultDiary);
                diaryAdd.setText("수정");
            }

            //할일 조회
            resultTodo = dbHelper.getResult_todo(Date);
            resultTodo = resultTodo != "" ? resultTodo : "데이터 없음";

            if(resultTodo.contains("데이터 없음")) {
                todoAdapter.clearItem();
                todoAdapter.notifyDataSetChanged();
            } else {
                todoAdapter.clearItem();
                String split_data[] = resultTodo.split("\n");
                int size = split_data.length;
                for(int i=0; i<size; i++) {
                    String ssplit_data[] = split_data[i].split("/");
                    todoAdapter.addItem(ssplit_data[1]);
                    //ListView 갱신
                    todoAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todayDate = findViewById(R.id.txt_date);
        diaryTitle = findViewById(R.id.todayDiary_title);
        calendar = findViewById(R.id.btn_calender);
        diaryAdd = findViewById(R.id.diaryAdd);
        todoAdd = findViewById(R.id.todoAdd);
        diary = findViewById(R.id.todayDiary);
        todo = findViewById(R.id.todayTodo);

        listview_todolist = findViewById(R.id.listview_todolist);
        todoAdapter = new TodoAdapter();
        listview_todolist.setAdapter(todoAdapter);

        dbHelper = new DBHelper(MainActivity.this, DataBases.CreateDB._DATABASE, null, 1);

        //LoginActivity에서 넘겨준 Intent값 받기
        Intent intent = getIntent();
        Name = intent.getStringExtra("Name");
        Date = intent.getStringExtra("Date");
        Log.d("진입MainActivityIntent", Name + ", " + Date);
        diaryTitle.setText("[ " + Name + "님의 한줄 일기장 ]");
        todayDate.setText(Date);

        //일기 조회
        resultDiary = dbHelper.getResult_diary(Date);
        resultDiary = resultDiary != "" ? resultDiary : "데이터 없음";

        if(resultDiary.contains("데이터 없음")) {
            diary.setText("등록된 일기가 없습니다. 일기를 등록해주세요.");
            diaryAdd.setText("등록");
        } else {
            diary.setText(resultDiary);
            diaryAdd.setText("수정");
        }

        //할일 조회
        resultTodo = dbHelper.getResult_todo(Date);
        resultTodo = resultTodo != "" ? resultTodo : "데이터 없음";

        if(resultTodo.contains("데이터 없음")) {
            todoAdapter.clearItem();
            todoAdapter.notifyDataSetChanged();
        } else {
            todoAdapter.clearItem();
            String split_data[] = resultTodo.split("\n");
            int size = split_data.length;
            for(int i=0; i<size; i++) {
                String ssplit_data[] = split_data[i].split("/");
                todoAdapter.addItem(ssplit_data[1]);
                //ListView 갱신
                todoAdapter.notifyDataSetChanged();
            }
        }

        //달력 아이콘 클릭 리스너
        calendar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, mDatePicker, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //한줄 일기장 등록버튼 클릭 리스너
        diaryAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date = String.valueOf(todayDate.getText());
                Diary = String.valueOf(diary.getText());
                btnDiary = String.valueOf(diaryAdd.getText());

                if(Diary.equals("")) {
                    Toast.makeText(MainActivity.this, "한줄 일기장을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if(btnDiary.contains("등록")) {
                        Toast.makeText(MainActivity.this, "일기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        dbHelper.insert_today_diary(0, Date, Diary);
                        diaryAdd.setText("수정");
                    } else if(btnDiary.contains("수정")) {
                        Toast.makeText(MainActivity.this, "일기가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        dbHelper.insert_today_diary(1, Date, Diary);
                    }
                }
            }
        });

        //오늘의 할일 추가버튼 클릭 리스너
        todoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo = String.valueOf(todo.getText());
                Date = String.valueOf(todayDate.getText());
                Log.d("진입Todo", Todo);

                if(Todo.equals("")) {
                    Toast.makeText(MainActivity.this, "할 일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    todoAdapter.addItem(Todo);
                    dbHelper.insert_today_todo(Date, Todo);

                    //ListView 갱신
                    todoAdapter.notifyDataSetChanged();
                    todo.setText("");
                }
            }
        });
    }
}