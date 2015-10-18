package com.travelguide.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.travelguide.R;
import com.travelguide.helpers.DeviceDimensionsHelper;
import com.travelguide.helpers.NetworkAvailabilityCheck;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author kprav
 *         <p>
 *         History:
 *         10/17/2015     kprav       Initial Version
 */
public class LoginFragment extends Fragment {

    private CircleImageView ivProfilePic;
    private ImageView ivCoverPic;
    private TextView tvName;
    private TextView tvEmail;
    private Button btnLogin;

    private ParseUser parseUser;
    private String name = null;
    private String email = null;
    private String profilePicUrl = null;
    private String coverPicUrl = null;

    public static final List<String> permissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ivProfilePic = (CircleImageView) view.findViewById(R.id.ivProfilePic);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        ivCoverPic = (ImageView) view.findViewById(R.id.ivCoverPic);

        btnLogin.setEnabled(true);
        btnLogin.setVisibility(View.VISIBLE);
        setLoginButtonOnClickListener();

        if (ParseUser.getCurrentUser() != null) {
            ParseFacebookUtils.linkWithReadPermissionsInBackground(ParseUser.getCurrentUser(), getActivity(), permissions, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    getUserDetailsFromParse();
                }
            });
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void setLoginButtonOnClickListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(getActivity(), permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("CANCEL", "The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("SIGN IN", "User signed up and logged in through Facebook!");
                            getUserDetailsFromFB(RequestType.NEW);
                        } else {
                            Log.d("LOGGED IN", "User logged in through Facebook!");
                            getUserDetailsFromParse();
                            getUserDetailsFromFB(RequestType.UPDATE);
                        }
                        if (err != null) {
                            err.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void getUserDetailsFromFB(final RequestType requestType) {
        if (!NetworkAvailabilityCheck.networkAvailable(getActivity())) {
            if (requestType == RequestType.NEW)
                NetworkAvailabilityCheck.showToast(getActivity());
            return;
        }
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, picture, cover");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(final GraphResponse response) {
                        /* handle the result */
                        try {
                            Log.d("response", response.toString());

                            name = response.getJSONObject().getString("name");
                            tvName.setText(name);

                            if (response.getJSONObject().optString("email") != null) {
                                email = response.getJSONObject().getString("email");
                                tvEmail.setText(email);
                            } else {
                                email = "";
                            }

                            profilePicUrl = response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
                            Picasso.with(getContext()).load(profilePicUrl).into(ivProfilePic, new Callback() {

                                @Override
                                public void onSuccess() {
                                    if (response.getJSONObject().optJSONObject("cover") != null) {
                                        try {
                                            coverPicUrl = response.getJSONObject().getJSONObject("cover").getString("source");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (coverPicUrl != null) {
                                            Picasso.with(getContext()).load(coverPicUrl).resize(DeviceDimensionsHelper.getDisplayWidth(getContext()), 0).into(ivCoverPic, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    saveOrUpdateParseUser(requestType);
                                                    btnLogin.setVisibility(View.INVISIBLE);
                                                    btnLogin.setEnabled(false);
                                                }

                                                @Override
                                                public void onError() {
                                                    saveOrUpdateParseUser(requestType);
                                                }
                                            });
                                        }
                                    } else {
                                        coverPicUrl = null;
                                        ivCoverPic.setImageResource(android.R.color.transparent);
                                        saveOrUpdateParseUser(requestType);
                                    }
                                }

                                @Override
                                public void onError() {
                                    // TODO: Handle Error
                                    // saveOrUpdateParseUser(requestType);
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }

    private void saveOrUpdateParseUser(RequestType requestType) {
        parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(name);
        parseUser.setEmail(email);
        // Saving profile photo as a ParseFile
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) ivProfilePic.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        String thumbName = parseUser.getUsername().replaceAll("\\s+", "");
        ParseFile profilePicture = new ParseFile(thumbName + "_thumb.jpg", data);
        ParseFile coverPicture = null;
        // Saving cover photo as a ParseFile
        if (coverPicUrl != null && ivCoverPic.getDrawable() != null) {
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            Bitmap bitmap2 = ((BitmapDrawable) ivCoverPic.getDrawable()).getBitmap();
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            byte[] data2 = stream2.toByteArray();
            String fileName = parseUser.getUsername().replaceAll("\\s+", "");
            coverPicture = new ParseFile(fileName + "_cover.jpg", data2);
        }
        // else {
        //     int width = DeviceDimensionsHelper.getDisplayWidth(getContext());
        //     int height = DeviceDimensionsHelper.getDisplayHeight(getContext());
        //     int[] colors = new int[width * height];
        //     for (int i = 0; i < width * height; i++) {
        //         colors[i] = R.color.colorPrimary;
        //     }
        //     bitmap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
        //     bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        //     data = stream.toByteArray();
        //     String fileName = parseUser.getUsername().replaceAll("\\s+", "");
        //     coverPicture = new ParseFile(fileName + "_cover.jpg", data);
        // }
        if (requestType == RequestType.NEW) {
            saveNewParseUser(parseUser, profilePicture, coverPicture);
        } else {
            updateExistingParseUser(parseUser, profilePicture, coverPicture);
        }
    }

    private void saveNewParseUser(final ParseUser parseUser, final ParseFile profilePicture, final ParseFile coverPicture) {
        coverPicture.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                parseUser.put("coverPic", coverPicture);
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        // Toast.makeText(getActivity(), "New user: " + name + " Signed up", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        profilePicture.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                parseUser.put("profileThumb", profilePicture);
                //Finally save all the user details
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getActivity(), "New user: " + name + " Signed up", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateExistingParseUser(final ParseUser parseUser, final ParseFile profilePicture, final ParseFile coverPicture) {
        String parseUserObjectId = parseUser.getObjectId();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", parseUserObjectId);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    user.put("profileThumb", profilePicture);
                    if (coverPicture != null)
                        user.put("coverPic", coverPicture);
                    else {
                        Toast.makeText(getActivity(), "NULL & REMOVED", Toast.LENGTH_SHORT).show();
                        user.remove("coverPic");
                    }
                    user.saveInBackground();
                    Toast.makeText(getActivity(), "Existing User: " + name + " Updated", Toast.LENGTH_SHORT).show();
                }
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getUserDetailsFromParse() {
        parseUser = ParseUser.getCurrentUser();
        // Fetch profile photo
        try {
            ParseFile parseFile = parseUser.getParseFile("profileThumb");
            byte[] data = parseFile.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            ivProfilePic.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ParseFile parseFile = parseUser.getParseFile("coverPic");
            if (parseFile != null) {
                byte[] data = parseFile.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                ivCoverPic.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvEmail.setText(parseUser.getEmail());
        tvName.setText(parseUser.getUsername());
        Toast.makeText(getActivity(), "Welcome back " + tvName.getText().toString(), Toast.LENGTH_SHORT).show();
        getUserDetailsFromFB(RequestType.UPDATE);
        btnLogin.setVisibility(View.INVISIBLE);
        btnLogin.setEnabled(false);
    }

    enum RequestType {
        NEW, UPDATE;
    }

}
