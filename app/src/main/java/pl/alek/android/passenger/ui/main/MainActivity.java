package pl.alek.android.passenger.ui.main;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import pl.alek.android.passenger.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int MAIN_FRAGMENT_NO = 0;
    private static final int ABOUT_APP_FRAGMENT_NO = 1;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.left_drawer)
    ListView leftDrawer;

    private String[] menuList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        ButterKnife.bind(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        setMenu();
        selectFragment(MAIN_FRAGMENT_NO);
    }

    private void setMenu() {
        setToggle();
        menuList = getResources().getStringArray(R.array.main_drawer_menu);
        leftDrawer.setAdapter(new DrawerAdapter(this, menuList));
        leftDrawer.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void setToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.menu_open,  /* "open drawer" description */
                R.string.menu_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.menu_name);
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectFragment(position);
        }
    }

    private void selectFragment(int position) {
        Fragment fragment = getFragment(position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, String.valueOf(position))
                .commit();

        leftDrawer.setItemChecked(position, true);
        setTitle(menuList[position]);
        drawerLayout.closeDrawer(leftDrawer);
    }

    private Fragment getFragment(int position) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(position));
        if (fragment == null) {
            switch (position) {
                case ABOUT_APP_FRAGMENT_NO:
                    fragment = new AboutAppFragment();
                    break;
                default:
                    fragment = new MainFragment();
                    break;
            }
        }
        return fragment;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
