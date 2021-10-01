package rs.ac.bg.etf.running;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import dagger.hilt.android.AndroidEntryPoint;
import rs.ac.bg.etf.running.databinding.ActivityMainBinding;
import rs.ac.bg.etf.running.users.UserViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements NavigationUIController {

    public static final String LOG_TAG = "running-app-example";
    public static final String INTENT_ACTION_WORKOUT = "rs.ac.bg.etf.running.WORKOUT";

    private ActivityMainBinding binding;
    private NavController navController;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        AppBarConfiguration configuration =
                new AppBarConfiguration.Builder(
                    R.id.route_browse, R.id.workout_list, R.id.calories)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, navController, configuration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                TextView header_username = findViewById(R.id.header_username);
                header_username.setText(user.getUsername());
            }
        });
    }

    @Override
    public void disableNavigationUI() {
        getSupportActionBar().hide();
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void enableNavigationUI() {
        getSupportActionBar().show();
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}