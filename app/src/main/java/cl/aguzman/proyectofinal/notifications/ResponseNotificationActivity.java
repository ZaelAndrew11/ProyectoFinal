package cl.aguzman.proyectofinal.notifications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cl.aguzman.proyectofinal.R;

public class ResponseNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String token = getIntent().getStringExtra("token");
        setContentView(R.layout.activity_response_notification);
        Button button = (Button) findViewById(R.id.responseBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendNotification().sendNotification(token, "Voy enseguida", "Su veterinario va a su domicilio.");
            }
        });
    }
}
