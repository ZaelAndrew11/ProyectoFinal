package cl.aguzman.proyectofinal.notifications;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
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
import cl.aguzman.proyectofinal.interfaces.ValidateVetRequestCallback;
import cl.aguzman.proyectofinal.models.User;
import cl.aguzman.proyectofinal.presenters.ValidateVetRequest;

public class ResponseNotificationActivity extends AppCompatActivity implements ValidateNotificationSend, ValidateVetRequestCallback {

    private LinearLayout linearLayout;
    private String phone, address, commune, city;
    private ProgressDialog progressDialog;
    private Button button;

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
                commune = user.getCommune();
                city = user.getCity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        setContentView(R.layout.activity_response_notification);
        final ArrayList array = new ArrayList();
        array.add(getString(R.string.message_vet));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.contentInfo);
        CircularImageView userPhoto = (CircularImageView) findViewById(R.id.photoUserIv);
        ImageView phoneUserIv = (ImageView) findViewById(R.id.phoneUserIv);
        TextView nameUser = (TextView) findViewById(R.id.nameUserTv);
        final TextView phoneUser = (TextView) findViewById(R.id.phoneUserTv);
        TextView addressUser = (TextView) findViewById(R.id.addressUserTv);
        TextView communeUser = (TextView) findViewById(R.id.communeUserTv);
        TextView cityUser = (TextView) findViewById(R.id.cityUserTv);

        String body = getIntent().getStringExtra("body");
        String userInfo = getIntent().getStringExtra("userInfo");

        phoneUserIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "9" + phoneUser.getText().toString(), null));
                if (ActivityCompat.checkSelfPermission(ResponseNotificationActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                    return;
                }
            }
        });

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(body.substring(1, body.length() - 1).replaceAll("\"", "").split(",")));
        ArrayList<String> listUserInfo = new ArrayList<String>(Arrays.asList(userInfo.substring(1, userInfo.length() - 1).replaceAll("\"", "").split(",")));


        if (TextUtils.isEmpty(listUserInfo.get(0))) {
            Picasso.with(userPhoto.getContext()).load(R.mipmap.ic_person_black_24dp).into(userPhoto);
        } else {
            Picasso.with(userPhoto.getContext()).load(listUserInfo.get(0)).into(userPhoto);
        }
        nameUser.setText(listUserInfo.get(1));
        phoneUser.setText(listUserInfo.get(2));
        addressUser.setText(listUserInfo.get(3));
        communeUser.setText(listUserInfo.get(4));
        cityUser.setText(listUserInfo.get(5));

        Log.d("LISTA", String.valueOf(listUserInfo));

        for (int i = 0; i < list.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(list.get(i).trim());
            linearLayout.addView(textView);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
            if (i % 2 == 0) {
                textView.setTextSize(22);
                textView.setAllCaps(true);
                textView.setTypeface(null, Typeface.ITALIC);
            } else {
                textView.setTextSize(16);
                params.setMargins(0, 0, 0, 16);
            }
        }

        final String token = getIntent().getStringExtra("token");
        final String tokenUser = new FcmToken(this).get();
        button = (Button) findViewById(R.id.responseBtn);

        new ValidateVetRequest(this).validateVet(list);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendNotification(ResponseNotificationActivity.this).sendNotification(token, getString(R.string.vet_response_text), getString(R.string.vet_response_second_text), phone, address, commune, city, array, progressDialog, tokenUser);
            }
        });
    }

    @Override
    public void isVet() {
        button.setVisibility(View.VISIBLE);
    }

    @Override
    public void sendOk() {
        alertDialogShow(R.string.response_ok);
    }

    @Override
    public void sendFail() {
        alertDialogShow(R.string.response_faild);
    }

    public void alertDialogShow(final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ResponseNotificationActivity.this);
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialogShow = builder.create();
                dialogShow.show();
            }
        });
    }

}
