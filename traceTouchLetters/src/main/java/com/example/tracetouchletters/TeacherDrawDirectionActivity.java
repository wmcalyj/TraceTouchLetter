package com.example.tracetouchletters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tracetouchletters.views.DrawDirectionMyImageView;

/**
 * Created by mengchaowang on 11/20/16.
 */

public class TeacherDrawDirectionActivity extends Activity {
    Button save, cancel;
    DrawDirectionMyImageView directionMyImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_draw_letter);
        setButtons();
        directionMyImageView = (DrawDirectionMyImageView) findViewById(R.id.teacherDrawDirection);

    }

    public void setButtons() {
        save = (Button) findViewById(R.id.teacherDrawDirectionSave);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (directionMyImageView == null) {
                    directionMyImageView = (DrawDirectionMyImageView) findViewById(R.id
                            .teacherDrawDirection);
                }
                directionMyImageView.saveLetterDirection();
                Intent intent = new Intent(TeacherDrawDirectionActivity.this,
                        SettingsSelectionActivity
                                .class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
        cancel = (Button) findViewById(R.id.teacherDrawDirectionCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherDrawDirectionActivity.this,
                        SettingsSelectionActivity
                                .class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }
}
