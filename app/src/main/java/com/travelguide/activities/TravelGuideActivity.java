package com.travelguide.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.travelguide.R;
import com.travelguide.foursquare.constants.FoursquareConstants;
import com.travelguide.fragments.FullscreenFragment;
import com.travelguide.fragments.LoginFragment;
import com.travelguide.fragments.NewTripFragment;
import com.travelguide.fragments.ProfileFragment;
import com.travelguide.fragments.SearchListFragment;
import com.travelguide.fragments.TripPlanDetailsFragment;
import com.travelguide.fragments.TripPlanListFragment;
import com.travelguide.helpers.DeviceDimensionsHelper;
import com.travelguide.helpers.NetworkAvailabilityCheck;
import com.travelguide.helpers.Preferences;
import com.travelguide.layouts.CustomCoordinatorLayout;
import com.travelguide.listener.OnTripPlanListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TravelGuideActivity extends AppCompatActivity implements
        OnTripPlanListener,
        FragmentManager.OnBackStackChangedListener,
        ProfileFragment.OnFragmentInteractionListener,
        LoginFragment.OnLoginLogoutListener,
        AppBarLayout.OnOffsetChangedListener {

    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView ivProfile;
    private TextView tvProfileUsername;
    private TextView tvProfileEmail;

    private FrameLayout fragmentFrameFullscreen;
    private CustomCoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBar;
    private CircleImageView ivProfilePic;
    private ImageView ivCoverPic;
    private TextView tvName;
    private TextView tvEmail;

    private MaterialDialog settingsDialog;
    private LinearLayout llSettingsDialogLayout;
    private Spinner spnGroup;
    private Spinner spnSeason;

    private String city;
    private String group;
    private String season;

    private String name = null;
    private String email = null;
    private String profilePicUrl = null;
    private String coverPicUrl = null;

    private boolean mLoginStatus = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_guide);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the collapsing Toolbar animation views
        coordinatorLayout = (CustomCoordinatorLayout) findViewById(R.id.main_content);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        appBar.addOnOffsetChangedListener(this);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        // Get the frame to show images in fullscreen
        fragmentFrameFullscreen = (FrameLayout) findViewById(R.id.fragment_frame_fullscreen);

        // The backdrop components for collapsing toolbar
        ivCoverPic = (ImageView) findViewById(R.id.ivCoverPicInProfile);
        ivProfilePic = (CircleImageView) findViewById(R.id.ivProfilePicInProfile);
        tvName = (TextView) findViewById(R.id.tvNameInProfile);
        tvEmail = (TextView) findViewById(R.id.tvEmailInProfile);
        loadBackdrop();

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        nvDrawer.addHeaderView(header);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        ivProfile = (ImageView) header.findViewById(R.id.ivProfile);
        tvProfileUsername = (TextView) header.findViewById(R.id.tvProfileUsername);
        tvProfileEmail = (TextView) header.findViewById(R.id.tvProfileEmail);

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        FoursquareConstants.setClientIdAndSecret(
                getApplicationContext().getResources().getString(R.string.foursquare_client_id),
                getApplicationContext().getResources().getString(R.string.foursquare_client_secret));
        buildSettingsDialog();
        city = "Any";
        group = "Any";
        season = "Any";

        mLoginStatus = Preferences.readBoolean(this, Preferences.User.LOG_IN_STATUS);
        setMenuItemLoginTitle();

        setContentFragment(R.id.fragment_frame, new TripPlanListFragment());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoginStatus = Preferences.readBoolean(this, Preferences.User.LOG_IN_STATUS);
        setMenuItemLoginTitle();
        setHeaderProfileInfo(true);
        loadBackdrop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.login_fragment:
                if (!mLoginStatus)
                    new LoginFragment().show(getSupportFragmentManager(), "Login_with_Facebook");
                else
                    new LoginFragment().logout(ParseUser.getCurrentUser(), this);
                break;
            case R.id.profile_fragment:
                setContentFragment(R.id.fragment_frame, new ProfileFragment());
                break;
            case R.id.settings_fragment:
                showSettingsDialog();
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    public void buildSettingsDialog() {
        settingsDialog = new MaterialDialog.Builder(this)
                .title(R.string.label_settings)
                .customView(R.layout.dialog_settings, true)
                .positiveText(R.string.label_save_button)
                .negativeText(R.string.label_cancel_button)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        setContentFragment(R.id.fragment_frame, SearchListFragment.newInstance(city, group, season));
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        settingsDialog.dismiss();
                    }
                })
                .build();
        llSettingsDialogLayout = (LinearLayout) settingsDialog.getCustomView();
        spnGroup = (Spinner) llSettingsDialogLayout.findViewById(R.id.spnGroup);
        spnSeason = (Spinner) llSettingsDialogLayout.findViewById(R.id.spnSeason);
        setupSpinnerGroup();
        setupSpinnerSeason();
    }

    private void setupSpinnerGroup() {
        spnGroup.setSelection(getSpinnerIndex(spnGroup, group));
        spnGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                group = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do Nothing
            }
        });
    }

    private void setupSpinnerSeason() {
        spnSeason.setSelection(getSpinnerIndex(spnSeason, season));
        spnSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                season = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do Nothing
            }
        });
    }

    // Get the position of an Spinner item
    private int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        return index;
    }

    public void showSettingsDialog() {
        settingsDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ResultCode", Integer.toString(resultCode));
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_travel_guide_activity, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query))
                    query = "Any";
                city = formatQueryForSearch(query.trim());
                searchItem.collapseActionView();
                setContentFragment(R.id.fragment_frame, SearchListFragment.newInstance(city, group, season));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText) && newText.length() > 2) {
                    if (NetworkAvailabilityCheck.networkAvailable(TravelGuideActivity.this)) {
                        loadCitySuggestions(searchView, formatQueryForSuggestions(newText));
                        return true;
                    }
                }
                return false;
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                MatrixCursor cursor = (MatrixCursor) searchView.getSuggestionsAdapter().getItem(position);
                int indexColumnSuggestion = cursor.getColumnIndex("city");
                searchView.setQuery(cursor.getString(indexColumnSuggestion), false);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }
        });
        return true;
    }

    private void loadCitySuggestions(final SearchView searchView, String input) {
        final ArrayList<String> cityList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CityDetails");
        query.whereEqualTo("CountryCode", "US");
        query.whereEqualTo("TargetType", "City");
        query.whereStartsWith("CanonicalName", input);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        cityList.add(list.get(i).getString("CanonicalName").trim());
                    }
                    SimpleCursorAdapter adapter = createCursorAdapter(cityList);
                    if (searchView != null)
                        searchView.setSuggestionsAdapter(adapter);
                }
            }
        });
    }

    // Capitalize each word
    private String formatQueryForSuggestions(String query) {
        StringTokenizer st = new StringTokenizer(query, " ");
        if (st.hasMoreTokens()) {
            query = "";
        }
        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            query = query + str.substring(0,1).toUpperCase() + str.substring(1) + " ";
        }

        st = new StringTokenizer(query, ",");
        if (st.hasMoreTokens()) {
            query = "";
        }
        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            query = query + str.substring(0,1).toUpperCase() + str.substring(1);
            if (st.hasMoreTokens())
                query = query + ",";
        }

        return query.trim();
    }

    // Strip everything after comma
    private String formatQueryForSearch(String query) {
        int indexOfComma = query.indexOf(",");
        if (indexOfComma != -1)
            query = query.substring(0, query.indexOf(","));
        return query;
    }

    private SimpleCursorAdapter createCursorAdapter(ArrayList<String> cityList) {
        String[] columnNames = {"_id", "city"};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        String[] cityArray = new String[cityList.size()];
        cityArray = cityList.toArray(cityArray);
        String[] row = new String[2];
        int id = 0;
        for (String city : cityArray) {
            row[0] = Integer.toString(id++);
            row[1] = city;
            cursor.addRow(row);
        }
        String[] from = {"city"};
        int[] to = new int[]{android.R.id.text1};
        return new SimpleCursorAdapter(this, R.layout.serach_view_suggestion_list, cursor, from, to, 0);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_frame_fullscreen);
        if (fragment != null && fragment instanceof FullscreenFragment) {
            coordinatorLayout.setVisibility(View.VISIBLE);
            fragmentFrameFullscreen.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onBackPressed();
        if (!(getSupportFragmentManager().getBackStackEntryCount() > 0)) {
            finish();
        }
    }

    @Override
    public void onTripPlanItemSelected(String tripPlanObjectId) {
        TripPlanDetailsFragment fragment = TripPlanDetailsFragment.newInstance(tripPlanObjectId);
        setContentFragment(R.id.fragment_frame, fragment);
    }

    @Override
    public void onTripPlanNew() {
        setContentFragment(R.id.fragment_frame, new NewTripFragment());
    }

    @Override
    public void onTripPlanCreated(String tripPlanObjectId, String imageUrl) {
        //Opening details passing ID of new item
        TripPlanDetailsFragment fragment = TripPlanDetailsFragment.newInstance(tripPlanObjectId, imageUrl);
        setContentFragment(R.id.fragment_frame, fragment);
    }

    @Override
    public void onShowImageSlideShow(ArrayList<String> imageUrlSet) {
        FullscreenFragment fragment = FullscreenFragment.newInstance(imageUrlSet);
        setContentFragment(R.id.fragment_frame_fullscreen, fragment);
    }

    private void setContentFragment(int fragmentFrame, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(fragmentFrame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        if (fragment instanceof FullscreenFragment) {
            coordinatorLayout.setVisibility(View.GONE);
            fragmentFrameFullscreen.setVisibility(View.VISIBLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            coordinatorLayout.setVisibility(View.VISIBLE);
            fragmentFrameFullscreen.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }

    @Override
    public void onBackStackChanged() {
        final View.OnClickListener originalToolbarListener = drawerToggle.getToolbarNavigationClickListener();

        boolean canBack = getSupportFragmentManager().getBackStackEntryCount() > 1;
        if (canBack) {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float slideOffset = (Float) valueAnimator.getAnimatedValue();
                    drawerToggle.onDrawerSlide(mDrawer, slideOffset);
                }
            });
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    drawerToggle.setDrawerIndicatorEnabled(false);
                    setDisplayHomeAsUpEnabled(true);
                }
            });
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(500);
            anim.start();

            drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportFragmentManager().popBackStack();
                }
            });
        } else {
            setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(true);
            ValueAnimator anim = ValueAnimator.ofFloat(1, 0);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float slideOffset = (Float) valueAnimator.getAnimatedValue();
                    drawerToggle.onDrawerSlide(mDrawer, slideOffset);
                }
            });
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(500);
            anim.start();
            drawerToggle.setToolbarNavigationClickListener(originalToolbarListener);
        }

        allowCollapsingToolbarScroll(getSupportFragmentManager().findFragmentById(R.id.fragment_frame));
    }

    private void setHeaderProfileInfo(boolean force) {
        if (force
                || mLoginStatus != Preferences.readBoolean(this, Preferences.User.LOG_IN_STATUS)
                || (mLoginStatus && TextUtils.isEmpty(tvProfileUsername.getText()))
                || (!mLoginStatus && !TextUtils.isEmpty(tvProfileUsername.getText()))) {
            final ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                try {
                    ParseFile parseFile = currentUser.getParseFile("profileThumb");
                    byte[] data = parseFile.getData();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    ivProfile.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tvProfileUsername.setText(currentUser.getUsername());
                tvProfileEmail.setText(currentUser.getEmail());
                loadBackdrop();
            } else {
                ivProfile.setImageResource(R.drawable.profile_placeholder);
                tvProfileUsername.setText("");
                tvProfileEmail.setText("");
            }
        }
    }

    private void showOrHideProfileButton(boolean show) {
        MenuItem menuItem = nvDrawer.getMenu().findItem(R.id.profile_fragment);
        menuItem.setVisible(show);
    }

    private void setMenuItemLoginTitle() {
        MenuItem item = nvDrawer.getMenu().findItem(R.id.login_fragment);
        if (mLoginStatus)
            item.setTitle(R.string.label_logout);
        else
            item.setTitle(R.string.action_login);
        showOrHideProfileButton(mLoginStatus);
    }

    @Override
    public void onLoginOrLogout(boolean status) {
        mLoginStatus = status;
        setMenuItemLoginTitle();
        setHeaderProfileInfo(false);
    }

    private void getSharedPreferences() {
        name = Preferences.readString(this, Preferences.User.NAME);
        email = Preferences.readString(this, Preferences.User.EMAIL);
        profilePicUrl = Preferences.readString(this, Preferences.User.PROFILE_PIC_URL);
        coverPicUrl = Preferences.readString(this, Preferences.User.COVER_PIC_URL);
    }

    private void loadBackdrop() {
        getSharedPreferences();
        if (!Preferences.DEF_VALUE.equals(name))
            tvName.setText(name);
        if (!Preferences.DEF_VALUE.equals(email))
            tvEmail.setText(email);
        if (!Preferences.DEF_VALUE.equals(profilePicUrl))
            Glide.with(this).load(profilePicUrl).into(ivProfilePic);
        if (!Preferences.DEF_VALUE.equals(coverPicUrl))
            Picasso.with(this).load(coverPicUrl).resize(DeviceDimensionsHelper.getDisplayWidth(this), 0).into(ivCoverPic);
    }

    private void allowCollapsingToolbarScroll(Fragment fragment) {
        if (fragment != null && fragment instanceof ProfileFragment) {
            appBar.setExpanded(true, true);
            coordinatorLayout.setAllowForScroll(true);
        } else {
            appBar.setExpanded(false, false);
            coordinatorLayout.setAllowForScroll(false);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
        if (collapsingToolbar.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbar)) {
            if (fragment != null && fragment instanceof ProfileFragment)
                collapsingToolbar.setTitle(name);
        }
        else {
            collapsingToolbar.setTitle("");
        }
    }
}
