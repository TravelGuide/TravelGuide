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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.travelguide.R;
import com.travelguide.activities.LoginActivity;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author kprav
 *
 * History:
 *   10/17/2015     kprav       Initial Version
 */
public class LoginFragment extends Fragment {

    private CircleImageView ivProfilePic;
    private TextView tvName;
    private TextView tvEmail;
    private Button btnLogin;

    private ParseUser parseUser;
    private String name = null;
    private String email = null;
    private String profilePicUrl = null;

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
                            getUserDetailsFromFB();
                        } else {
                            Log.d("LOGGED IN", "User logged in through Facebook!");
                            getUserDetailsFromParse();
                        }
                        if (err != null) {
                            err.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void getUserDetailsFromFB() {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, picture");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        try {
                            Log.d("respone", response.toString());
                            if (response.getJSONObject().optString("email") != null) {
                                email = response.getJSONObject().getString("email");
                                tvEmail.setText(email);
                            } else {
                                email = "";
                            }
                            name = response.getJSONObject().getString("name");
                            tvName.setText(name);
                            profilePicUrl = response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
                            Picasso.with(getContext()).load(profilePicUrl).into(ivProfilePic, new Callback() {
                                @Override
                                public void onSuccess() {
                                    saveNewUser();
                                }

                                @Override
                                public void onError() {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }

    private void saveNewUser() {
        parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(name);
        parseUser.setEmail(email);
        // Saving profile photo as a ParseFile
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) ivProfilePic.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] data = stream.toByteArray();
        String thumbName = parseUser.getUsername().replaceAll("\\s+", "");
        final ParseFile parseFile = new ParseFile(thumbName + "_thumb.jpg", data);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                parseUser.put("profileThumb", parseFile);
                //Finally save all the user details
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getActivity(), "New user:" + name + " Signed up", Toast.LENGTH_SHORT).show();
                    }
                });
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
        tvEmail.setText(parseUser.getEmail());
        tvName.setText(parseUser.getUsername());
        Toast.makeText(getActivity(), "Welcome back " + tvName.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}