//package com.example.tracetouchletters;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.SeekBar;
//
//public class TeacherActivity extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.teacher_setting_selection);
//        presetSettings();
//        setButtons();
//    }
//
//    private void presetSettings() {
//        CheckBox noHaptic = (CheckBox) findViewById(R.id.noHaptic);
//        SeekBar hapticSetting = (SeekBar) findViewById(R.id.hapticSettingBar);
//        noHaptic.setChecked(AppCtx.isNoHaptic());
//        hapticSetting.setProgress(AppCtx.getHapticValue());
//
//    }
//
//    private void setButtons() {
//        Button save = (Button) findViewById(R.id.save);
//        save.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveChanges();
//                Intent intent;
//                intent = new Intent(TeacherActivity.this, ModeSelectionActivity.class);
//                startActivity(intent);
//            }
//        });
//        Button cancel = (Button) findViewById(R.id.cancel);
//        cancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent;
//                intent = new Intent(TeacherActivity.this, ModeSelectionActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void saveChanges() {
//        CheckBox noHaptic = (CheckBox) findViewById(R.id.noHaptic);
//        SeekBar hapticSetting = (SeekBar) findViewById(R.id.hapticSettingBar);
//        AppCtx.setNoHaptic(noHaptic.isChecked());
//        AppCtx.setHapticValue(hapticSetting.getProgress());
//
//    }
//
//}
