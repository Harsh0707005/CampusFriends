package com.example.campusfriends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockTest extends AppCompatActivity {

    TextView question, question_number;
    RadioGroup options;
    RadioButton option_a, option_b, option_c, option_d, chosen_button;
    ProgressBar progressBar4;
    Button next_question, previous_question;
    List<String> allQuestions;
    List<QuestionData> randomQuestionsData;
    QuestionData questionData;
    int current_question_number;
    String chosen_option, correct_option;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_test);
        question = findViewById(R.id.question);
        question_number = findViewById(R.id.question_number);
        options = findViewById(R.id.options);
        option_a = findViewById(R.id.option_a);
        option_b = findViewById(R.id.option_b);
        option_c = findViewById(R.id.option_c);
        option_d = findViewById(R.id.option_d);
        progressBar4 = findViewById(R.id.progressBar4);
        next_question = findViewById(R.id.next_question);
        previous_question = findViewById(R.id.previous_question);
        progressBar4.setVisibility(View.VISIBLE);

        chosen_option = "";

        current_question_number = 0;
        allQuestions = new ArrayList<>();
        randomQuestionsData = new ArrayList<>();

        Intent i = getIntent();
        String mock_name = i.getStringExtra("name");
        fetchFileWithSpecificName(mock_name+".xlsx");

        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                chosen_button = findViewById(i);
            }
        });
        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    chosen_option = String.valueOf(chosen_button.getText());
                }catch (Exception e){
                    chosen_option = "";
                }
                if(!chosen_option.equals("")){
                    progressBar4.setVisibility(View.VISIBLE);
                    question.setVisibility(View.INVISIBLE);
                    question_number.setVisibility(View.INVISIBLE);
                    options.setVisibility(View.INVISIBLE);
                    previous_question.setVisibility(View.INVISIBLE);
                    next_question.setVisibility(View.INVISIBLE);
                    if (chosen_option.equals(correct_option)) {
                        score += 1;
                    }
                    if (next_question.getText().equals("Submit")) {
                        progressBar4.setVisibility(View.INVISIBLE);
                        question.setVisibility(View.VISIBLE);
                        question.setText("You got " + score + " correct out of 5 questions!!!");
                    } else {
                        current_question_number += 1;
                        showQuestion(current_question_number);
                        if (current_question_number == 4) {
                            next_question.setText("Submit");
                        } else {
                            next_question.setText("Save and Next");
                        }
                    }
                    options.clearCheck();
                    chosen_option = "";
                }else{
                    Toast.makeText(MockTest.this, "Please choose an option", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void fetchFileWithSpecificName(String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(fileName);

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            downloadExcelFile(storageRef);
        }).addOnFailureListener(exception -> {
            Log.d("harsh", "fetching failed");
        });
    }

    private void downloadExcelFile(StorageReference fileRef) {
        try {
            File localFile = File.createTempFile("temp", "xlsx");
            fileRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        parseExcelFile(localFile);
                    })
                    .addOnFailureListener(exception -> {
                        Log.d("harsh", "Download failed");
                    });
        } catch (IOException e) {
            Log.d("harsh", "error");
        }
    }

    private void parseExcelFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assuming questions are in the first sheet

            List<QuestionData> allQuestionsData = new ArrayList<>();

            // Loop through each row and extract the question, options, and correct option
            for (Row row : sheet) {
                Cell cellQuestion = row.getCell(0); // for the question
                Cell cellOption1 = row.getCell(1);  // for option 1
                Cell cellOption2 = row.getCell(2);  // for option 2
                Cell cellOption3 = row.getCell(3);  // for option 3
                Cell cellOption4 = row.getCell(4);  // for option 4
                Cell cellCorrectOption = row.getCell(5); // for the correct option


//                if (cellQuestion != null && cellQuestion.getCellTypeEnum() == CellType.STRING) {
//                    String question = cellQuestion.getStringCellValue();
//
//                    // Collect options for the question
//                    List<String> options = new ArrayList<>();
//                    if (cellOption1 != null && cellOption1.getCellTypeEnum() == CellType.STRING) {
//                        options.add(cellOption1.getStringCellValue());
//                    }
//                    if (cellOption2 != null && cellOption2.getCellTypeEnum() == CellType.STRING) {
//                        options.add(cellOption2.getStringCellValue());
//                    }
//                    if (cellOption3 != null && cellOption3.getCellTypeEnum() == CellType.STRING) {
//                        options.add(cellOption3.getStringCellValue());
//                    }
//                    if (cellOption4 != null && cellOption4.getCellTypeEnum() == CellType.STRING) {
//                        options.add(cellOption4.getStringCellValue());
//                    }
//
//                    // Get the correct option
//                    String correctOption = "";
//                    if (cellCorrectOption != null && cellCorrectOption.getCellTypeEnum() == CellType.STRING) {
//                        correctOption = cellCorrectOption.getStringCellValue();
//                    }
//
//                    allQuestionsData.add(new QuestionData(question, options, correctOption));
//                }
                if (cellQuestion != null && cellQuestion.getCellTypeEnum() == CellType.STRING) {
                    String question = cellQuestion.getStringCellValue();

                    // Collect options for the question
                    List<String> options = new ArrayList<>();
                    if (cellOption1 != null) {
                        if (cellOption1.getCellTypeEnum() == CellType.STRING) {
                            options.add(cellOption1.getStringCellValue());
                        } else if (cellOption1.getCellTypeEnum() == CellType.NUMERIC) {
                            options.add(String.valueOf((int)cellOption1.getNumericCellValue()));
                        }
                    }
                    if (cellOption2 != null) {
                        if (cellOption2.getCellTypeEnum() == CellType.STRING) {
                            options.add(cellOption2.getStringCellValue());
                        } else if (cellOption2.getCellTypeEnum() == CellType.NUMERIC) {
                            options.add(String.valueOf((int)cellOption2.getNumericCellValue()));
                        }
                    }
                    if (cellOption3 != null) {
                        if (cellOption3.getCellTypeEnum() == CellType.STRING) {
                            options.add(cellOption3.getStringCellValue());
                        } else if (cellOption3.getCellTypeEnum() == CellType.NUMERIC) {
                            options.add(String.valueOf((int)cellOption3.getNumericCellValue()));
                        }
                    }
                    if (cellOption4 != null) {
                        if (cellOption4.getCellTypeEnum() == CellType.STRING) {
                            options.add(cellOption4.getStringCellValue());
                        } else if (cellOption4.getCellTypeEnum() == CellType.NUMERIC) {
                            options.add(String.valueOf((int)cellOption4.getNumericCellValue()));
                        }
                    }

                    // Get the correct option
                    String correctOption = "";
//                    if (cellCorrectOption != null && cellCorrectOption.getCellTypeEnum() == CellType.STRING) {
//                        correctOption = cellCorrectOption.getStringCellValue();
//                    }
                    if (cellCorrectOption != null) {
                        if (cellCorrectOption.getCellTypeEnum() == CellType.STRING) {
                            correctOption = cellCorrectOption.getStringCellValue();
                        } else if (cellCorrectOption.getCellTypeEnum() == CellType.NUMERIC) {
//                            options.add(String.valueOf((int)cellCorrectOption.getNumericCellValue()));
                            correctOption = String.valueOf((int)cellCorrectOption.getNumericCellValue());
                        }
                    }

                    allQuestionsData.add(new QuestionData(question, options, correctOption));
                }
            }

            workbook.close();
            fis.close();

            // Shuffle the questions list to get them in random order
            Collections.shuffle(allQuestionsData);

            randomQuestionsData = allQuestionsData.subList(0, Math.min(5, allQuestionsData.size()));
            showQuestion(current_question_number);

        } catch (IOException e) {
            Log.d("harsh", "error2");
        }
    }
    private void showQuestion(int questionIndex) {
        try {
            questionData = randomQuestionsData.get(questionIndex);

            progressBar4.setVisibility(View.INVISIBLE);
            question.setVisibility(View.VISIBLE);
            question_number.setVisibility(View.VISIBLE);
            options.setVisibility(View.VISIBLE);
            previous_question.setVisibility(View.VISIBLE);
            next_question.setVisibility(View.VISIBLE);

            // Display the question and options in the TextViews
            question_number.setText("Q" + (current_question_number + 1));
            question.setText(questionData.getQuestion());
            option_a.setText(questionData.getOptions().get(0));
            option_b.setText(questionData.getOptions().get(1));
            option_c.setText(questionData.getOptions().get(2));
            option_d.setText(questionData.getOptions().get(3));
            correct_option = questionData.getCorrectOption();
        }catch (Exception e){
            Log.d("harsh", e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Delete the temporary Excel file when the user closes the app.
        deleteTempFile();
    }

    private void deleteTempFile() {
        File tempFile = new File(getCacheDir(), "temp.xlsx");
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }
}