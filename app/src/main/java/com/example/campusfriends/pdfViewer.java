package com.example.campusfriends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public class pdfViewer extends AppCompatActivity {
    PDFView pdfView;
    String PDF_PATH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfView = findViewById(R.id.pdfView);

        Intent i = getIntent();
        PDF_PATH = "PDF/"+(i.getStringExtra("name_pdf")).trim()+".pdf";
        Log.d("harsh", PDF_PATH);

        downloadAndDisplayPdf(PDF_PATH);
    }
    private void downloadAndDisplayPdf(String PDF_PATH) {
        FirebaseStorage.getInstance().getReference().child(PDF_PATH)
                .getFile(new File(getCacheDir(), "temp_pdf.pdf"))
                .addOnSuccessListener(taskSnapshot -> {
                    // PDF downloaded successfully, now display it
                    displayPdfFromCache();
                })
                .addOnFailureListener(e -> {
                    Log.d("harsh", "Error downloading");
                    Log.d("harsh", e.getMessage());
                });
    }
    private void displayPdfFromCache(){
        // Get the cache directory
        File cacheDir = getCacheDir();

        // Specify the path to the PDF file in cache
        File pdfFile = new File(cacheDir, "temp_pdf.pdf");

        // Load PDF file from cache
        pdfView.fromFile(pdfFile)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        // PDF load is complete
                    }
                })
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        // Page has changed
                    }
                })
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        Log.d("harsh", "Error loading");
                    }
                })
                .swipeHorizontal(false)
                .load();
    }
}