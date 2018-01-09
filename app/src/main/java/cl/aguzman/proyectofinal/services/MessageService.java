package cl.aguzman.proyectofinal.services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import cl.aguzman.proyectofinal.notifications.MessageNotification;

public class MessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String token = remoteMessage.getData().get("tokenUser");
            String info = remoteMessage.getData().get("info");
            String userInfo = remoteMessage.getData().get("userInfo");
            MessageNotification.notify(this, title, body, token, info, userInfo);
        }

    }
}
