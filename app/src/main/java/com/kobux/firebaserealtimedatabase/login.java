package com.kobux.firebaserealtimedatabase;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnLogin;
    private Button btnSignup;
    private Button btnSignupGoogle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editEmail);
        editTextPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        btnSignup = findViewById(R.id.buttonSignupnormal);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,SignUp.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (user.isEmpty()){
                    editTextEmail.setError("ingrese un usuario");
                    editTextEmail.requestFocus();
                    return;
                } else if (password.isEmpty()){
                    editTextPassword.setError("ingrese una contraseña");
                    editTextPassword.requestFocus();
                    return;
                } else {
                    btnLogin.setEnabled(false);
                    auth.signInWithEmailAndPassword(user,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = auth.getCurrentUser();
                                if (currentUser != null) {
                                    String uid = currentUser.getUid();
                                    Toast.makeText(login.this, "Inicio de sesión exitoso", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    intent.putExtra("USER_UID", uid);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                btnLogin.setEnabled(true);
                                Toast.makeText(login.this,"usuario o contraseña incorrecto",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            btnLogin.setEnabled(true);
                            editTextPassword.setText("");
                            editTextEmail.setText("");
                            editTextPassword.requestFocus();
                            Toast.makeText(login.this,"error al iniciar sesion "+e.toString(),Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }
        });

    }

}