package com.example.test6;
//IM/2021/103 Start
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ContactUs extends AppCompatActivity {

    EditText editTextName,editTextContent;
    Button SendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_us);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SendFeedback = findViewById(R.id.contact_us_button3);
        editTextName = findViewById(R.id.contact_us_fullNameEditText);
        editTextContent = findViewById(R.id.contact_us_messageEditText);

        SendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject, content, to_email;
                subject = "Feedback form: " + editTextName.getText().toString();
                content = editTextContent.getText().toString();
                to_email = "samodawijesooriya@gmail.com";
                if(content.equals("") && to_email.equals("")){
                    Toast.makeText(ContactUs.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else{
                    sendEmail(subject, content, to_email);
                }
            }
        });
    }

    public void sendEmail(String subject, String content, String To_email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{To_email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose email client:"));
    }

    public void backToSignUp(View view) {
        startActivity(new Intent(this, SignUp.class));
    }
}
//IM/2021/103 End