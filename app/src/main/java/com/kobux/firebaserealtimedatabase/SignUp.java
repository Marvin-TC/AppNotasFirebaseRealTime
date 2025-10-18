package com.kobux.firebaserealtimedatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editTextPassword;
    private EditText editEmail;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.editEmail);
        editTextPassword = findViewById(R.id.editPassword);
        btnRegistrar = findViewById(R.id.buttonRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contrasena = editTextPassword.getText().toString().trim();
                String correo = editEmail.getText().toString().trim();

                if (contrasena.isEmpty()){
                    Toast.makeText(SignUp.this,"ingrese una contraseña",Toast.LENGTH_SHORT).show();
                    editTextPassword.requestFocus();
                    return;
                } else if (correo.isEmpty()){
                    Toast.makeText(SignUp.this,"ingrese una correo",Toast.LENGTH_SHORT).show();
                    editEmail.requestFocus();
                    return;
                } else {
                    btnRegistrar.setEnabled(false);
                    auth.createUserWithEmailAndPassword(correo,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SignUp.this,"usuario creado exitosamente",Toast.LENGTH_LONG).show();
                                finish();
                            }else {
                                Toast.makeText(SignUp.this,"error al crear usuario:" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                btnRegistrar.setEnabled(true);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUp.this,"error al crear usuario:"+ e.toString(),Toast.LENGTH_LONG).show();
                            btnRegistrar.setEnabled(true);
                        }
                    });
                }
            }
        });



    }
}