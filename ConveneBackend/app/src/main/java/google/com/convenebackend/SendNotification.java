package google.com.convenebackend;

/**
 * Created by Peru on 02-01-2016.
 */
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.sample.backend.messaging.Messaging;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendNotification extends AsyncTask<Void, Void, String>{
    private static Messaging notifyService = null;
    private Context context;
    private String friendId;
    private String message;
    private String senderId;
    private com.google.sample.backend.messaging.model.RegistrationRecord friendRecord;
    private double latitude;
    private double longitude;

    public SendNotification(Context context, String friendId, String message,double latitude, double longitude, String senderId) {
        this.context = context;
        this.friendId = friendId;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
        this.senderId = senderId;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (notifyService == null) {
            Messaging.Builder builder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://convene-backend.appspot.com/_ah/api/");
            // end of optional local run code

            notifyService = builder.build();
        }

        //String msg = "";
        try {
            //msg = "Notification from Sample Application";
            notifyService.sendFriendId(friendId).execute();
            notifyService.sendLatitude(latitude).execute();
            notifyService.sendLongitude(longitude).execute();
            //friendRecord=notifyService.findRecord(friendId).execute();
            //notifyService.sendLatitude(latitude).execute();
            //notifyService.sendLongitude(longitude).execute();
            notifyService.send(message,senderId).execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            message = "Error: " + ex.getMessage();
        }
        return message;
    }
    @Override
    protected void onPreExecute(){
        System.out.println("SendNotification::Latitude "+latitude+" Longitude "+longitude);
    }

    @Override
    protected void onPostExecute(String msg) {
        Toast.makeText(context, "Notification sent", Toast.LENGTH_LONG).show();
        Logger.getLogger("NOTIFICATION").log(Level.INFO, msg);
    }

}
