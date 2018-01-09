package cl.aguzman.proyectofinal.notifications;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.FcmToken;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.ValidateNotificationSend;
import cl.aguzman.proyectofinal.models.User;

public class ResponseNotificationActivity extends AppCompatActivity implements ValidateNotificationSend{

    private LinearLayout linearLayout;
    private String phone;
    private String address;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        DatabaseReference reference = new Queries().getUser().child(new CurrentUser().getCurrentUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                phone = user.getPhone();
                address = user.getAdress();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        setContentView(R.layout.activity_response_notification);
        final ArrayList array = new ArrayList();
        array.add("OK");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.contentInfo);
        ImageView userPhoto = (ImageView) findViewById(R.id.photoUserIv);
        TextView nameUser = (TextView) findViewById(R.id.nameUserTv);
        TextView phoneUser = (TextView) findViewById(R.id.phoneUserTv);
        TextView addressUser = (TextView) findViewById(R.id.addressUserTv);

        String body = getIntent().getStringExtra("body");
        String userInfo = getIntent().getStringExtra("userInfo");


        ArrayList<String> list = new ArrayList<String>(Arrays.asList(body.substring(1, body.length() - 1).replaceAll("\"", "").split(",")));
        ArrayList<String> listUserInfo = new ArrayList<String>(Arrays.asList(userInfo.substring(1, userInfo.length() - 1).replaceAll("\"", "").split(",")));

        Picasso.with(userPhoto.getContext()).load(listUserInfo.get(0)).into(userPhoto);
        nameUser.setText(listUserInfo.get(1));
        phoneUser.setText(listUserInfo.get(2));
        addressUser.setText(listUserInfo.get(3));

        for (int i = 0; i < list.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(list.get(i).trim());
            linearLayout.addView(textView);
            if (i % 2 == 0) {
                textView.setTextSize(20);
                textView.setTypeface(null, Typeface.ITALIC);
            } else {
                textView.setTextSize(15);
            }
            Log.d("ARRAY", list.get(i));
        }

        final String token = getIntent().getStringExtra("token");
        final String tokenUser = new FcmToken(this).get();
        Button button = (Button) findViewById(R.id.responseBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendNotification(ResponseNotificationActivity.this).sendNotification(token, "Voy enseguida", "Su veterinario va a su domicilio.", phone, address, array, progressDialog, tokenUser);
            }
        });
    }

    @Override
    public void sendOk() {
    }

    @Override
    public void sendFail() {

    }
}
