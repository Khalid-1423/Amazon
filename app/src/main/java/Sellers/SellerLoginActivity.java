package Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amazon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    private Button loginSellerBtn , sellerRegBtn;
    private EditText emailInput , passwordInput;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        sellerRegBtn = findViewById(R.id.loinSeller);

        emailInput = findViewById(R.id.seller_login_email);
        passwordInput=findViewById(R.id.seller_login_password);
        loginSellerBtn = findViewById(R.id.seller_already_have_account_btn);

        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);

        loginSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });

        sellerRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerLoginActivity.this,SellerRegistrationActivity.class));
            }
        });


    }

    private void loginSeller() {

        String email  = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(!email.equals("") && !password.equals(""))
        {
            loadingBar.setTitle("Seller Acount Login");
            loadingBar.setMessage("Please wait, while we're checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Intent intent = new Intent(SellerLoginActivity.this,SelletHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }

                        }
                    });

        }
        else
        {
            Toast.makeText(this, "Please complete the login form", Toast.LENGTH_SHORT).show();
        }




    }
}
