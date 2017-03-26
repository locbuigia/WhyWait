package group5.tcss450.uw.edu.whywait.model;

import org.json.JSONObject;

/**
 * Created by jnbui on 3/25/2017.
 */

public class places {
    private double mLat;
    private double mLot;
    private boolean mOpen;
    private String mImageURl;
    private int mImageHeight;
    private int mImageWidth;
    private String mPhotoRef;
    public places(JSONObject json) {
        createObject(json);
    }

    private void createObject(JSONObject json) {

    }
}
