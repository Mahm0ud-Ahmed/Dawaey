package com.example.reminder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.example.reminder.db.Model;
import com.example.reminder.db.ReminderDbHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar appBarLayout;

    Button btn_day, btn_dose1, btn_dose2, btn_dose3, btn_dose4, btn_dose5;
    TextView tv_day, tv_dose1, tv_dose2, tv_dose3, tv_dose4, tv_dose5;

    TextInputEditText et_name_medicine;
    AutoCompleteTextView autoCompleteTextView;

    Switch aSwitch;

    private String repositoryTime, repositoryDay;
    private int getNumberText;

    ReminderDbHelper reminderDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        inflateViews();
        listenerOfEditText();

        String[] arr = getResources().getStringArray(R.array.fdgfdgfdg);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arr);
        autoCompleteTextView.setAdapter(adapter);

    }

    private void insertDb(){
        int active = getStateSwitch();
        String name = getNameMedicine();
        int number = getNumberText();
        String day = getRepositoryDay();
        String time = getRepositoryTime();
        long result = reminderDbHelper.insert(new Model(active, name, number, time, day));
        Toast.makeText(this, "Result: "+result, Toast.LENGTH_SHORT).show();
    }

    //اظهار شاشة الوقت لاختيار توقيت الجرعة
    private void showTimePicker(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog pickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String temp = hourOfDay + " : " + minute;
                textView.setText(temp);
                setRepositoryTime(temp);
            }
        }, hour, minute, false);

        pickerDialog.setTitle("اختر توقيت الجرعة");
        pickerDialog.show();
    }

    // اظهار مربع اختيارات للأيام
    private void showDialog() {
        final ArrayList<Integer> list = new ArrayList<>();
        final String[] arr = getResources().getStringArray(R.array.day);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("تكرار الدواء")
                .setMultiChoiceItems(arr, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        Integer i = which;
                        if (isChecked) {
                            list.add(i);
                        }
                        else list.remove(i);
                    }
                })
                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setRepositoryDay("null");
                        StringBuilder stringBuilder = new StringBuilder();
                        String result;
                        Collections.sort(list);
                        for (int num : list) {
                            if (TextUtils.isEmpty(stringBuilder))
                                stringBuilder.append(arr[num]);
                            else
                                stringBuilder.append(",").append(arr[num]);
                        }
                        if (list.size() == 7) {
                            result = "كل يوم";
                        } else {
                            result = stringBuilder.toString();
                        }
                        tv_day.setText(result);
                        setRepositoryDay(result);
                    }
                })
                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setRepositoryDay("null");
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dose1:
                showTimePicker(tv_dose1);
                break;
            case R.id.dose5:
                showTimePicker(tv_dose5);
                break;
            case R.id.dose4:
                showTimePicker(tv_dose4);
                break;
            case R.id.dose3:
                showTimePicker(tv_dose3);
                break;
            case R.id.dose2:
                showTimePicker(tv_dose2);
                break;
            case R.id.btn_duration:
                showDialog();
                break;

        }
    }

    // مستمع لتغير الرقم داخل العنصر الخاص بعدد الجرعات
    private void listenerOfEditText() {
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            String string;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                if (!TextUtils.isEmpty(temp)) {
                    if (!temp.contains(".")) {
                        showTimeFiled(temp);
                        setGetNumberText(temp);
                    }
                } else {
                    showTimeFiled("0");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_Accept:
                insertDb();
                finish();
                return true;
            case R.id.menu_delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setRepositoryTime(String repositoryTime) {
        if (TextUtils.isEmpty(repositoryTime))
            this.repositoryTime = repositoryTime;
        else this.repositoryTime += "," + repositoryTime;

    }

    public String getRepositoryTime() {
        return this.repositoryTime;
    }

    public void setRepositoryDay(String repositoryDay) {
        if (TextUtils.isEmpty(repositoryDay))
            return;
        else
            this.repositoryDay = repositoryDay;

    }

    public String getRepositoryDay() {
        if (TextUtils.isEmpty(repositoryDay)) {
            return null;
        }
        return repositoryDay;
    }

    // اظهار حقول ادخار الوقت للجرعة بناء على عدد الجرعات
    private void showTimeFiled(String number) {
        int num = Integer.parseInt(number);
        switch (num) {
            case 1:
                btn_dose1.setVisibility(View.VISIBLE);
                btn_dose2.setVisibility(View.GONE);
                btn_dose3.setVisibility(View.GONE);
                btn_dose4.setVisibility(View.GONE);
                btn_dose5.setVisibility(View.GONE);
                tv_dose1.setVisibility(View.VISIBLE);
                tv_dose2.setVisibility(View.GONE);
                tv_dose3.setVisibility(View.GONE);
                tv_dose4.setVisibility(View.GONE);
                tv_dose5.setVisibility(View.GONE);
                break;
            case 2:
                btn_dose1.setVisibility(View.VISIBLE);
                btn_dose2.setVisibility(View.VISIBLE);
                btn_dose3.setVisibility(View.GONE);
                btn_dose4.setVisibility(View.GONE);
                btn_dose5.setVisibility(View.GONE);
                tv_dose1.setVisibility(View.VISIBLE);
                tv_dose2.setVisibility(View.VISIBLE);
                tv_dose3.setVisibility(View.GONE);
                tv_dose4.setVisibility(View.GONE);
                tv_dose5.setVisibility(View.GONE);
                break;
            case 3:
                btn_dose1.setVisibility(View.VISIBLE);
                btn_dose2.setVisibility(View.VISIBLE);
                btn_dose3.setVisibility(View.VISIBLE);
                btn_dose4.setVisibility(View.GONE);
                btn_dose5.setVisibility(View.GONE);
                tv_dose1.setVisibility(View.VISIBLE);
                tv_dose2.setVisibility(View.VISIBLE);
                tv_dose3.setVisibility(View.VISIBLE);
                tv_dose4.setVisibility(View.GONE);
                tv_dose5.setVisibility(View.GONE);
                break;
            case 4:
                btn_dose1.setVisibility(View.VISIBLE);
                btn_dose2.setVisibility(View.VISIBLE);
                btn_dose3.setVisibility(View.VISIBLE);
                btn_dose4.setVisibility(View.VISIBLE);
                btn_dose5.setVisibility(View.GONE);
                tv_dose1.setVisibility(View.VISIBLE);
                tv_dose2.setVisibility(View.VISIBLE);
                tv_dose3.setVisibility(View.VISIBLE);
                tv_dose4.setVisibility(View.VISIBLE);
                tv_dose5.setVisibility(View.GONE);
                break;
            case 5:
                btn_dose1.setVisibility(View.VISIBLE);
                btn_dose2.setVisibility(View.VISIBLE);
                btn_dose3.setVisibility(View.VISIBLE);
                btn_dose4.setVisibility(View.VISIBLE);
                btn_dose5.setVisibility(View.VISIBLE);
                tv_dose1.setVisibility(View.VISIBLE);
                tv_dose2.setVisibility(View.VISIBLE);
                tv_dose3.setVisibility(View.VISIBLE);
                tv_dose4.setVisibility(View.VISIBLE);
                tv_dose5.setVisibility(View.VISIBLE);
                break;
            default:
                btn_dose1.setVisibility(View.GONE);
                btn_dose2.setVisibility(View.GONE);
                btn_dose3.setVisibility(View.GONE);
                btn_dose4.setVisibility(View.GONE);
                btn_dose5.setVisibility(View.GONE);
                tv_dose1.setVisibility(View.GONE);
                tv_dose2.setVisibility(View.GONE);
                tv_dose3.setVisibility(View.GONE);
                tv_dose4.setVisibility(View.GONE);
                tv_dose5.setVisibility(View.GONE);
                break;
        }
    }

    private void inflateViews() {
        btn_day = findViewById(R.id.btn_duration);
        btn_dose1 = findViewById(R.id.dose1);
        btn_dose2 = findViewById(R.id.dose2);
        btn_dose3 = findViewById(R.id.dose3);
        btn_dose4 = findViewById(R.id.dose4);
        btn_dose5 = findViewById(R.id.dose5);

        tv_day = findViewById(R.id.tv_duration);
        tv_dose1 = findViewById(R.id.tv_dose1);
        tv_dose2 = findViewById(R.id.tv_dose2);
        tv_dose3 = findViewById(R.id.tv_dose3);
        tv_dose4 = findViewById(R.id.tv_dose4);
        tv_dose5 = findViewById(R.id.tv_dose5);

        autoCompleteTextView = findViewById(R.id.auto_complete);
        et_name_medicine = findViewById(R.id.et_name_medicine);

        aSwitch = findViewById(R.id.switch_item);

        btn_dose1.setOnClickListener(this);
        btn_dose2.setOnClickListener(this);
        btn_dose3.setOnClickListener(this);
        btn_dose4.setOnClickListener(this);
        btn_dose5.setOnClickListener(this);
        btn_day.setOnClickListener(this);

        appBarLayout = findViewById(R.id.editor_toolbar);
        setSupportActionBar(appBarLayout);
        reminderDbHelper = new ReminderDbHelper(this);
    }

    private int getStateSwitch(){
        if(aSwitch.isChecked())
            return 1;
        else
            return 0;
    }

    private String getNameMedicine(){
        return Objects.requireNonNull(et_name_medicine.getText()).toString().trim();
    }

    public void setGetNumberText(String getNumberText) {
        if (!TextUtils.isEmpty(getNumberText) && !getNumberText.equals("0")) {
            this.getNumberText = Integer.parseInt(getNumberText);
        }
    }

    private int getNumberText(){
        return getNumberText;
    }

    /*
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        currentTime = (hourOfDay + " : "+ minute);
    }

    @Override
    public void setPositiveButtonListener(ArrayList<Integer> currentList, String[] originalList) {
        StringBuilder stringBuilder = new StringBuilder();
        String result;
        for (int num : currentList) {
            stringBuilder.append(originalList[num]).append(",");
        }
        if (currentList.size() == 7) {
            result = "كل يوم";
        } else {
            result = stringBuilder.toString();
        }
        tv_day.setText(result);
    }

    @Override
    public void setNegativeButtonListener(ArrayList<Integer> currentList, String[] originalList) {

    }

    private void showDialog() {
        fragment = new Dialog("AlertDialog");
        fragment.setCancelable(false);
        fragment.show(getSupportFragmentManager(),"Show Dialog");
    }

    private void showTimePicker(){
        fragment = new Dialog("TimePicker");
        fragment.show(getSupportFragmentManager(), "Show Dialog");
    }

     */
}