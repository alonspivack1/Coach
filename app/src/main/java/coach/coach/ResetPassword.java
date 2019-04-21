package coach.coach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Reset password - send the password to the email if Coach or User forgot his password.
 */
public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    /**
     * The Edit text forgot email.
     */
    EditText editTextForgotEmail;
    /**
     * The Email.
     */
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setTitle("איפוס סיסמה");

        editTextForgotEmail=(EditText)findViewById(R.id.editTextForgotEmail);
        findViewById(R.id.textViewBackToLogIn).setOnClickListener(this);
        findViewById(R.id.textViewBackToSignup).setOnClickListener(this);
        findViewById(R.id.buttonReset).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewBackToSignup:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;

            case R.id.textViewBackToLogIn:
                finish();
                startActivity(new Intent(this, LogInActivity.class));
                finish();
                break;
            case R.id.buttonReset:
                email = editTextForgotEmail.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextForgotEmail.setError("הכנס בבקשה איימל תקין");
                    editTextForgotEmail.requestFocus();
                }
                else{
                    resetUserPassword(email);
                }
                break;
        }
    }

    /**
     * Reset user password.
     *
     * @param email the email of the User/Coach
     */
    public void resetUserPassword(String email){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final ProgressDialog progressDialog = new ProgressDialog(ResetPassword.this);
        progressDialog.setMessage("מאשר....");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "שחזור סיסמא נשלח לאיימל",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "איימל לא נמצא", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }
}
