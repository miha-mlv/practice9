package com.example.practice9;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaParser;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    Button btnSaveFile, btnReadFile, btnDeleteFile, btnWriteFile;
    EditText editContentFile, editFileName;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //Создание файла
        btnSaveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = editFileName.getText().toString();
                String fileContent = editContentFile.getText().toString();
                File fileFlag = getBaseContext().getFileStreamPath(fileName);
                if(fileFlag.exists())
                {
                    Toast.makeText(MainActivity.this,"Файл уже существует", Toast.LENGTH_SHORT).show();
                    return;
                }
                try(FileOutputStream file = openFileOutput(fileName, Context.MODE_PRIVATE)){
                    file.write(fileContent.getBytes());
                    Toast.makeText(MainActivity.this, "Файл успешно создан", Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
        //Чтение файла
        btnReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fileName = editFileName.getText().toString();
                try (FileInputStream file = openFileInput(fileName)){
                    InputStreamReader inputStreamReader = new InputStreamReader(file, StandardCharsets.UTF_8);
                    StringBuilder stringBuilder = new StringBuilder();
                    try(BufferedReader reader = new BufferedReader(inputStreamReader))
                    {
                        String line = reader.readLine();
                        while(line!=null){
                            stringBuilder.append(line).append('\n');
                            line = reader.readLine();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    String content = stringBuilder.toString();
                    textView.setText(content);
                    Toast.makeText(MainActivity.this, "Файл успешно прочитан", Toast.LENGTH_SHORT).show();
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //Удаление файла
        btnDeleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder windowFlag = new AlertDialog.Builder(MainActivity.this);
                windowFlag.setTitle("Подтверждение");
                windowFlag.setMessage("Вы уверены, что хотите удалить файл "+editFileName.getText().toString()+"?");
                windowFlag.setIcon(android.R.drawable.ic_dialog_alert);
                windowFlag.setPositiveButton("Да", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String fileName = editFileName.getText().toString();
                                File dir = getFilesDir();
                                File file = new File(dir, fileName);
                                boolean deleted = file.delete();
                                if (deleted) {
                                    Toast.makeText(MainActivity.this, "Файл успешно удален", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(MainActivity.this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
                            }
                        });
                windowFlag.setNegativeButton("Отмена", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = windowFlag.create();
                dialog.show();
            }
        });
        //Запись в файл
        btnWriteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = editFileName.getText().toString();
                String fileContent = editContentFile.getText().toString();
                try(FileOutputStream file = openFileOutput(fileName, Context.MODE_APPEND)){
                    file.write(fileContent.getBytes());
                    Toast.makeText(MainActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                    e.printStackTrace();
                }

            }
        });

    }
    private void init()
    {
    btnDeleteFile = findViewById(R.id.btnDeleteFile);
    btnSaveFile = findViewById(R.id.btnSaveFile);
    btnReadFile = findViewById(R.id.btnReadFile);
    btnWriteFile = findViewById(R.id.btnWriteFile);
    editContentFile = findViewById(R.id.editContentFile);
    editFileName = findViewById(R.id.editFileName);
    textView = findViewById(R.id.textView);
}
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("key_EditFileName", editFileName.getText().toString());
        outState.putString("key_EditContentFile", editContentFile.getText().toString());
        outState.putString("key_textView", textView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editFileName.setText(savedInstanceState.getString("key_EditFileName"));
        editContentFile.setText(savedInstanceState.getString("key_EditContentFile"));
        textView.setText(savedInstanceState.getString("key_textView"));
    }

}