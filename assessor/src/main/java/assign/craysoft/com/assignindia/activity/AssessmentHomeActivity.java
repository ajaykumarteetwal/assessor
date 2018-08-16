package assign.craysoft.com.assignindia.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import assign.craysoft.com.assignindia.R;
import assign.craysoft.com.assignindia.bean.Home;
import assign.craysoft.com.assignindia.fragment.AssessmentFragment;
import assign.craysoft.com.assignindia.fragment.DashboardFragment;
import assign.craysoft.com.assignindia.fragment.StudentPasswordFragment;
import assign.craysoft.com.assignindia.util.Util;

public class AssessmentHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String DATA = "EXAM_TYPE";

    private DashboardFragment categoryFragment;
    private NavigationView navigationView;
    private Home home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Object extra = getIntent().getSerializableExtra(DATA);
        if (extra != null && extra instanceof Home) {
            home = (Home) extra;
            View hView = navigationView.getHeaderView(0);
            if (hView != null) {
                TextView textView = hView.findViewById(R.id.teacherNameTextView);
                if (textView != null)
                    textView.setText("Welcome " + (home.getTeacherName() != null ? home.getTeacherName() : ""));
                textView = hView.findViewById(R.id.teacherEmailTextView);
                if (textView != null)
                    textView.setText(home.getTeacherEmailId() != null ? home.getTeacherEmailId() : "");
            }
        }
        loadDashBoard();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null && fragmentManager.getBackStackEntryCount() > 0)
                loadDashBoard();
            else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navDashboard)
            loadDashBoard();
        else if (id == R.id.navPendingAssessment)
//                loadAssessment(0, "Instructions");
            loadAssessment(1, "Pending Assessment");
        else if (id == R.id.navAttemptedAssessment)
            loadAssessment(2, "Attempted Assessment");
        else if (id == R.id.navCompleteAssessment)
            loadAssessment(3, "Complete Assessment");
        else if (id == R.id.navStudentPassword)
            loadStudentPasswordFragment("Student Password");
        else if (id == R.id.nav_share)
            Util.shareApp(this);
        else if (id == R.id.nav_send)
            Util.openWhatsAppConversationFor(this, "8273246651");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadStudentPasswordFragment(String name) {
        StudentPasswordFragment fragment = StudentPasswordFragment.newInstance(0);
        Util.loadFragment(this, fragment, name);
    }

    private void loadAssessment(int type, String name) {
        AssessmentFragment fragment = AssessmentFragment.newInstance(type);
        Util.loadFragment(this, fragment, name);
    }

    private void loadDashBoard() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            if (categoryFragment == null) {
                categoryFragment = DashboardFragment.newInstance(home);
                fragmentManager.removeOnBackStackChangedListener(null);
                fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        String name = getString(R.string.app_name);
                        int position = getSupportFragmentManager().getBackStackEntryCount();
                        if (position != 0) {
                            FragmentManager.BackStackEntry stackEntry = getSupportFragmentManager().getBackStackEntryAt(position - 1);
                            name = stackEntry.getName();
                        }
                        ActionBar actionBar = getSupportActionBar();
                        if (actionBar != null)
                            actionBar.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + name + "</font>"));
                    }
                });
            }
            if (fragmentManager.getBackStackEntryCount() > 0)
                fragmentManager.popBackStackImmediate();
//            if (categoryFragment.isAdded())
//                return;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainner, categoryFragment, getString(R.string.app_name));
            transaction.commit();
            if (navigationView != null)
                navigationView.setCheckedItem(R.id.navDashboard);
        }
    }
}
