package com.example.componentecalculator;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddComponent extends AppCompatActivity {
    private ComponentDatabase database = null;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_component);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        database = Room.databaseBuilder(this, ComponentDatabase.class, "ComponentsDB").build();
        databaseReference = FirebaseDatabase.getInstance().getReference("components");
        Intent intent = getIntent();
        if(intent.hasExtra("component")) {
            Component component = intent.getParcelableExtra("component");
            EditText editName = findViewById(R.id.EditName);
            EditText editCategory = findViewById(R.id.EditCategory);
            EditText editPrice = findViewById(R.id.EditPrice);
            CheckBox checkDiscount = findViewById(R.id.CheckDiscount);
            EditText editQuantity = findViewById(R.id.EditQuantity);

            assert component != null;
            editName.setText(component.getName());
            editCategory.setText(component.getCategory());
            editPrice.setText(String.valueOf(component.getPrice()));
            checkDiscount.setChecked(component.isDiscount());
            editQuantity.setText(String.valueOf(component.getQuantity()));
        }

        Button btnAddNewComponent = findViewById(R.id.button_add_new_component);
        btnAddNewComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etName = findViewById(R.id.EditName);
                String name = etName.getText().toString();
                EditText etCategory = findViewById(R.id.EditCategory);
                String category = etCategory.getText().toString();
                EditText etPrice = findViewById(R.id.EditPrice);
                int price = Integer.parseInt(etPrice.getText().toString());
                CheckBox cbDiscount = findViewById(R.id.CheckDiscount);
                boolean discount = (cbDiscount.isChecked());
                EditText etQuantity = findViewById(R.id.EditQuantity);
                int quantity = Integer.parseInt(etQuantity.getText().toString());

                Component component = new Component(name, category, price, discount, quantity);

                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        database.getDaoObject().insertComponent(component);
                    }
                });

                try {
                    File file = new File(getFilesDir(), "components.txt");
                    FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(component);
                    objectOutputStream.close();
                    fileOutputStream.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                CheckBox cbOnline = findViewById(R.id.CheckOnline);
                if (cbOnline.isChecked()) {
                    databaseReference.push().setValue(component)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddComponent.this, "Componenta salvată cu succes în Firebase", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddComponent.this, "Eroare la salvarea în Firebase: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                }

                Intent it = new Intent();
                it.putExtra("component", (Parcelable) component);
                setResult(RESULT_OK, it);
                finish();
            }
        });
    }
}
