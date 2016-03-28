package clowdtech.mpositive.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.shared.views.ViewMenuView;
import clowdtech.mpositive.ui.Presentable;

public class NavDrawerActivity extends BaseActionBarActivity {

    protected DrawerLayout _drawerLayout;
    protected ListView _drawerList;
    protected ActionBarDrawerToggle _drawerToggle;
    protected NavDrawerIntents _drawerIntents = new NavDrawerIntents();
    protected RelativeLayout _drawerLayoutContainer;

    protected FrameLayout frameLayout;
    private ArrayList<MenuItem> _visibleItems;
    protected ViewMenuView customActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        frameLayout = (FrameLayout) findViewById(R.id.fragment_container);

//        setupActionBar();

        setupNavigationDrawer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((Presentable)findViewById(R.id.container)).getPresenter().onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((Presentable)findViewById(R.id.container)).getPresenter().unbindView();
    }

    protected void setupActionBar() {
        customActionBar = (ViewMenuView) findViewById(R.id.action_bar);

        // Always cast your custom Toolbar here, and set it as the ActionBar.
        setSupportActionBar(customActionBar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();

        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

//        customActionBar.setOnLogoClicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (_drawerLayout.isDrawerOpen(Gravity.START)) {
//                    _drawerLayout.closeDrawer(Gravity.START);
//                } else {
//                    _drawerLayout.openDrawer(Gravity.START);
//                }
//            }
//        });
    }

    private void setupNavigationDrawer() {
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        _drawerLayoutContainer = (RelativeLayout) findViewById(R.id.navigation_container);

        setupMainMenuItems();

        setupBottomMenuItems();

        _drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                _drawerLayout,         /* DrawerLayout object */
                R.string.captions_open,  /* "open drawer" description */
                R.string.captions_close  /* "close drawer" description */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

                customActionBar.showViewMenu();

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

                customActionBar.hideViewMenu();

                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        _drawerLayout.setDrawerListener(_drawerToggle);
    }

    private void setupBottomMenuItems() {
        String[] settingsItems = new String[1];
        settingsItems[0] = "Settings";

        int[] settingsIcons = new int[1];
        settingsIcons[0] = R.drawable.ic_action_settings;

        SimpleAdapter settingsAdapter = buildListAdapter(settingsItems, settingsIcons);

        ListView navBottom = (ListView) findViewById(R.id.nav_bottom);

        navBottom.setAdapter(settingsAdapter);

        navBottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                switch (position) {
                    case 0:
                        intent = new Intent(getBaseContext(), PreferenceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);

                        break;
                }

                _drawerLayout.closeDrawer(_drawerLayoutContainer);
            }
        });
    }

    private void setupMainMenuItems() {
        _drawerList = (ListView) findViewById(R.id.left_drawer);

        // move this to a shared area
        String[] menuItems = new String[4];
        menuItems[0] = getResources().getString(R.string.nav_menu_till);
        menuItems[1] = getResources().getString(R.string.nav_menu_products);
        menuItems[2] = getResources().getString(R.string.nav_menu_reporting_transaction);
        menuItems[3] = getResources().getString(R.string.nav_menu_product_reporting);

        int[] icons = new int[4];
        icons[0] = R.drawable.ic_action_dial_pad;
        icons[1] = R.drawable.ic_action_collection;
        icons[2] = R.drawable.ic_action_view_as_list;
        icons[3] = R.drawable.ic_action_view_as_list;

        SimpleAdapter adapter = buildListAdapter(menuItems, icons);

        // Set the adapter for the list view
        _drawerList.setAdapter(adapter);

        // Set the list's click listener
        _drawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private SimpleAdapter buildListAdapter(String[] menuItems, int[] icons) {
        ArrayList<HashMap<String, String>> mList = new ArrayList<>();

        for (int i = 0; i < menuItems.length; i++) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("icon", Integer.toString(icons[i]));
            hm.put("title", menuItems[i]);
            mList.add(hm);
        }

        String[] from = {"title", "icon"};

        int[] to = {R.id.nav_item_title, R.id.nav_list_icon};

        return new SimpleAdapter(this, mList, R.layout.drawer_list_item, from, to);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = _drawerLayout.isDrawerOpen(_drawerLayoutContainer);

        if (!drawerOpen && _visibleItems != null) {
            showMenuItems();
        } else if (drawerOpen) {
            hideMenuItems(menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private void hideMenuItems(Menu menu) {
        _visibleItems = new ArrayList<>();

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isVisible()) {
                _visibleItems.add(item);
                item.setVisible(false);
            }
        }
    }

    private void showMenuItems() {
        for (int i = 0; i < _visibleItems.size(); i++) {
            _visibleItems.get(i).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        boolean drawerHandled = _drawerToggle.onOptionsItemSelected(item);

        return drawerHandled || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        _drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        _drawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            onNavItemSelected(position);

            // Highlight the selected item, addManualEntryToReceipt the title, and close the drawer
            _drawerList.setItemChecked(position, true);

            _drawerLayout.closeDrawer(_drawerLayoutContainer);
        }
    }

    public void onNavItemSelected(int position) {
        Context baseContext = getBaseContext();
        Intent intent = _drawerIntents.getIntent(position, baseContext);
        startActivity(intent);
    }
}