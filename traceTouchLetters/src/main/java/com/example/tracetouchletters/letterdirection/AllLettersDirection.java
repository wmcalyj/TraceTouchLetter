package com.example.tracetouchletters.letterdirection;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mengchaowang on 11/28/16.
 */

public class AllLettersDirection implements Serializable {
    public static Map<String, LetterDirection> lettersDirection;
    private static AllLettersDirection mInstance = null;

    private AllLettersDirection() {

    }

    public static AllLettersDirection getInstance() {
        if (mInstance == null) {
            loadData();
        }
        return mInstance;
    }

    private static void loadData() {
        ObjectInput in;
        try {
            String filename = "allLettersDirectionData.data";
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    filename);
            in = new ObjectInputStream(new FileInputStream(file));
            mInstance = (AllLettersDirection) in.readObject();
            lettersDirection = mInstance.lettersDirection == null ? new HashMap<String,
                    LetterDirection>() : mInstance.lettersDirection;
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            mInstance = new AllLettersDirection();
            mInstance.lettersDirection = new HashMap<String, LetterDirection>();
        }
    }

    public LetterDirection getLetterDirection(String letter) {
        return lettersDirection.get(letter);
    }

    public LetterDirection getLetterDirection(Character ch) {
        return lettersDirection.get(String.valueOf(ch));
    }

    public Map<String, LetterDirection> getAllLetterDirection() {
        return this.lettersDirection;
    }

    public void saveLetterDirection(LetterDirection direction) {
        this.lettersDirection.put(direction.letter, direction);
        saveData();
    }

    public void saveAllLettersDirection(Map<String, LetterDirection> allLettersDirection) {
        this.lettersDirection = allLettersDirection;
        saveData();
    }

    private void saveData() {
        ObjectOutput out;
        try {
            File outFile = new File(Environment.getExternalStorageDirectory(),
                    "allLettersDirectionData.data");
            out = new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(mInstance);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
