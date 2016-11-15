package com.example.tracetouchletters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ModeSelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_selection);
        setModeButtons();
    }

    private void setModeButtons() {
        Button teacher = (Button) findViewById(R.id.teacherMode);
        teacher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ModeSelectionActivity.this, SettingsSelectionActivity.class);
                startActivity(intent);
            }
        });
        Button student = (Button) findViewById(R.id.studentMode);
        student.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ModeSelectionActivity.this, StudentActivity.class);
                startActivity(intent);
            }
        });
    }
}
