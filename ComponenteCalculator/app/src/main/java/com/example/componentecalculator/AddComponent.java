package com.example.componentecalculator;

import android.content.Intent;
import android.os.Bundle;
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

public class AddComponent extends AppCompatActivity {
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
                boolean discount = ((CheckBox)findViewById(R.id.CheckDiscount)).isChecked();
                EditText etQuantity = findViewById(R.id.EditQuantity);
                int quantity = Integer.parseInt(etQuantity.getText().toString());

                Component component = new Component(name, category, price, discount, quantity);

                Intent it = new Intent();
                it.putExtra("component", component);
                Toast.makeText(AddComponent.this, component.toString(), Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, it);
                finish();
            }
        });
    }
}
