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
            String token = remoteMessage.getData().get("token");
            MessageNotification.notify(this, title, body, token);
        }

    }
}
