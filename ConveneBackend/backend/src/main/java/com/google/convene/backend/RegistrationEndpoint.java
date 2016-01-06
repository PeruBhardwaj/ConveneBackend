/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.google.convene.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import javax.inject.Named;

import static com.google.convene.backend.OfyService.ofy;

/**
 * A registration endpoint class we are exposing for a device's GCM registration id on the backend
 * <p/>
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 * <p/>
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(
        name = "registration",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.sample.google.com",
                ownerName = "backend.sample.google.com",
                packagePath = ""
        )
)
public class RegistrationEndpoint {

    private static final Logger log = Logger.getLogger(RegistrationEndpoint.class.getName());
    private String gcmId;

    /**
     * Register a device to the backend
     *
     * @param regId The Google Cloud Messaging registration Id to add
     */
    @ApiMethod(name = "register")
    public void registerDevice(@Named("regId") String regId) {
        if (findRecord(regId) != null) {
            log.info("Device " + regId + " already registered, skipping register");
            RegistrationRecord record =findRecord(regId);
            gcmId = record.getRegId();
            return;
        }
        RegistrationRecord record = new RegistrationRecord();
        record.setRegId(regId);
        gcmId = regId;
        ofy().save().entity(record).now();
    }

    /**
     * Unregister a device from the backend
     *
     * @param regId The Google Cloud Messaging registration Id to remove
     */
    @ApiMethod(name = "unregister")
    public void unregisterDevice(@Named("regId") String regId) {
        RegistrationRecord record = findRecord(regId);
        if (record == null) {
            log.info("Device " + regId + " not registered, skipping unregister");
            return;
        }
        ofy().delete().entity(record).now();
    }



    private RegistrationRecord findRecord(String regId) {
        return ofy().load().type(RegistrationRecord.class).filter("regId", regId).first().now();
    }

    @ApiMethod(name = "setFbId",httpMethod = ApiMethod.HttpMethod.POST)
    public void setFbId(@Named("fbId") String fbId){
        RegistrationRecord record = ofy().load().type(RegistrationRecord.class).filter("regId", gcmId).first().now();
        //String temp = record.getFbId();
       // if(temp == null) {
            record.setFbId(fbId);
            ofy().save().entity(record).now();
       // }
        //else return;
    }

    @ApiMethod(name = "setLatitude")
    public void setLocationLatitude(@Named("latitude") double latitude){
        RegistrationRecord record = ofy().load().type(RegistrationRecord.class).filter("regId", gcmId).first().now();
        record.setLocationLatitude(latitude);
        ofy().save().entity(record).now();
    }


    @ApiMethod(name = "setLongitude")
    public void setLocationLongitude(@Named("longitude") double longitude){
        RegistrationRecord record = ofy().load().type(RegistrationRecord.class).filter("regId", gcmId).first().now();
        record.setLocationLongitude(longitude);
        ofy().save().entity(record).now();
    }

    @ApiMethod(name = "getRecord",
            httpMethod = ApiMethod.HttpMethod.GET)
    public RegistrationRecord getRecord(@Named("fbId")String fbId){
        RegistrationRecord record =  ofy().load().type(RegistrationRecord.class).filter("fbId",fbId).first().now();
        return record;
    }

}