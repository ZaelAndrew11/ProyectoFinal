package cl.aguzman.proyectofinal.notifications;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;

import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.interfaces.ValidateNotificationSend;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNotification {
    private ValidateNotificationSend callback;

    public SendNotification(ValidateNotificationSend callback) {
        this.callback = callback;
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public void sendNotification(final String token, final String title, final String body, final String phone, final String address, final String commune, final String city, final ArrayList array, final ProgressDialog progressDialog, final String tokenUser) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Enviando notificación");
        progressDialog.show();
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    ArrayList userInfo = new ArrayList();
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject infoJson=new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    userInfo.add(new CurrentUser().getImageUser());
                    userInfo.add(new CurrentUser().getNameUser());
                    userInfo.add(phone);
                    userInfo.add(address);
                    userInfo.add(commune);
                    userInfo.add(city);
                    infoJson.put("body",body);
                    infoJson.put("title",title);
                    dataJson.put("token", token);
                    dataJson.put("tokenUser", tokenUser);
                    dataJson.put("info", array);
                    dataJson.put("userInfo", userInfo);
                    json.put("notification",infoJson);
                    json.put("data", dataJson);
                    json.put("to",token);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key=AAAAbQ9X0JY:APA91bFT5wktGkqoHN3jseLjGUP83vqppMOVdZ6xbSui2N18DrGFhCuFWojZZWAGNsd7T5ZtN-mophQgoZXL9pBrhcVIKvBax7P2UMBpzIKSRrwfDc5BZYjAlY_oSVz2SJjERKJqrPMf")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    JSONObject jsonResponse = new JSONObject(finalResponse);
                    if (jsonResponse.get("success").equals(1)){
                        progressDialog.dismiss();
                        callback.sendOk();
                    }else {
                        progressDialog.dismiss();
                        callback.sendFail();
                    }
                }catch (Exception e){
                }
                return null;
            }
        }.execute();

    }
}
