package coach.coach;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Send mail to the App developer.
 */
public class Mail extends AppCompatActivity {
    /**
     * The M edit text subject.
     */
    EditText mEditTextSubject;
    /**
     * The M edit text message.
     */
    EditText mEditTextMessage;
    /**
     * The Button send.
     */
    Button buttonSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        setTitle("שליחת מייל למפתח");


        mEditTextSubject = findViewById(R.id.mail_edit_text_subject);
        mEditTextMessage = findViewById(R.id.mail_edit_text_message);
        buttonSend = findViewById(R.id.mail_button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    private void sendMail() {

        String recipient = "alonspivack1@gmail.com";
        String[] recipients = recipient.split(",");
        String subject = mEditTextSubject.getText().toString();
        String message = mEditTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }}
