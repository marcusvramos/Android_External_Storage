package com.example.mynotes;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btFechar;
    private EditText etNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btFechar = findViewById(R.id.btFechar);
        etNote = findViewById(R.id.etNote);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btFechar.setOnClickListener(v -> {
            salvarDados();
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDados();
    }

    private void recuperarDados() {
        File arq = getFilePublic();
        if (arq.exists() && arq.length() > 0) {
            try (FileInputStream fileInputStream = new FileInputStream(arq)) {
                byte[] bytes = new byte[(int) arq.length()];
                int bytesRead = fileInputStream.read(bytes);
                if (bytesRead == bytes.length) {
                    etNote.setText(new String(bytes));
                } else {
                    Toast.makeText(this, "Erro ao ler o arquivo", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void salvarDados() {
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "O armazenamento externo não está disponível para escrita.", Toast.LENGTH_SHORT).show();
            return;
        }

        File arq = getFilePublic();
        try (FileOutputStream fileOutputStream = new FileOutputStream(arq, true)) {
            fileOutputStream.write(etNote.getText().toString().getBytes());
            Toast.makeText(this, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File getFilePublic() {
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        return new File(documentsDir, "note.txt");
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
