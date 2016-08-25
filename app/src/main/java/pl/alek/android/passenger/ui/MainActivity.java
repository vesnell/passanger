package pl.alek.android.passenger.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        ButterKnife.bind(this);

        setMenu();
        selectFragment(MAIN_FRAGMENT_NO);
    }

    private void setMenu() {
        menuList = getResources().getStringArray(R.array.main_drawer_menu);
        leftDrawer.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, menuList));
        leftDrawer.setOnItemClickListener(new DrawerItemClickListener());
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
                .replace(R.id.content_frame, fragment)
                .commit();

        leftDrawer.setItemChecked(position, true);
        setTitle(menuList[position]);
        drawerLayout.closeDrawer(leftDrawer);
    }

    private Fragment getFragment(int position) {
        switch (position) {
            case ABOUT_APP_FRAGMENT_NO:
                return new AboutAppFragment();
            default:
                return new MainFragment();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}
