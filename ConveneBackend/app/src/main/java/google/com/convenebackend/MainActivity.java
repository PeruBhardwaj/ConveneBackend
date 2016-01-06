package google.com.convenebackend;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final static String EXTRA_MESSAGE = "ebookfrenzy.com.sampleapplication.MESSAGE";
    private GoogleApiClient mGoogleApiClient;
    private GetLocation friendLocation;
    private double userLatitude;
    private double userLongitude;
    private String friendId;
    private String senderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        //new EndpointsAsyncTask().execute(new Pair<Context, String>(this, phone_number));
        // new GcmRegistrationAsyncTask(this,"userId").execute();
        //Use method getFriendLocation(friend_id) to get location of friend.

    }
    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("onConnection");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        userLatitude = mLastLocation.getLatitude();
        userLongitude = mLastLocation.getLongitude();
        new GcmRegistrationAsyncTask(this,"456",userLatitude,userLongitude).execute();
    }
    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("ConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("ConnectionFailed");
    }

    public void notify(View v){
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        userLatitude = mLastLocation.getLatitude();
        userLongitude = mLastLocation.getLongitude();
        friendId = "456";
        senderId = "456";
        new SendNotification(this,friendId,"Notification from sample app",userLatitude,userLongitude,senderId).execute();
    }

    public void getFriendLocation(View view){
        //Intent intent = new Intent(this, GetLocation.class);
        //String fb_id = "456";
        //intent.putExtra(EXTRA_MESSAGE, fb_id);
        //startActivity(intent);

        friendLocation = (GetLocation) new GetLocation("456").execute();//pass friend id as parameter
        double latitude = friendLocation.getFriendLatitude();//not working
        double longitude = friendLocation.getFriendLongitude();//not working
        System.out.println("latitude " + latitude + " longitude " + longitude);
    }
}

