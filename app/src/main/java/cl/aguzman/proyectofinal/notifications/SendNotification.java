package cl.aguzman.proyectofinal.notifications;

import android.os.AsyncTask;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNotification {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public void sendNotification(final String token, final String title, final String body) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject infoJson=new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    infoJson.put("body",body);
                    infoJson.put("title",title);
                    dataJson.put("token", token);
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
                }catch (Exception e){

                }
                return null;
            }
        }.execute();

    }
}
