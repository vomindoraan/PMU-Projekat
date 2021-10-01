package rs.ac.bg.etf.running.users;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import rs.ac.bg.etf.running.MainActivity;
import rs.ac.bg.etf.running.data.User;
import rs.ac.bg.etf.running.databinding.FragmentLoginBinding;

import rs.ac.bg.etf.running.R;
import rs.ac.bg.etf.running.workouts.WorkoutViewModel;

public class LoginFragment extends Fragment {

    private static final String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";

    private FragmentLoginBinding binding;
    private UserViewModel userViewModel;
    private NavController navController;
    private MainActivity mainActivity;
    private boolean loginSuccessful;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) requireActivity();
        userViewModel = new ViewModelProvider(mainActivity).get(UserViewModel.class);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(LOGIN_SUCCESSFUL, loginSuccessful);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        if (savedInstanceState != null
                && savedInstanceState.getBoolean(LOGIN_SUCCESSFUL, false)) {
//            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.login_fragment, true).build();
            navController.navigate(R.id.nav_graph_routes);
        }

        binding.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                binding.submit.setEnabled(s.length() > 0);
            }
        });

        binding.regSwitch.setOnCheckedChangeListener((bv, isChecked) -> {
            binding.submit.setText(
                    isChecked ? R.string.action_register : R.string.action_sign_in);
            binding.message.setText("");
        });

        mainActivity.disableNavigationUI();
        binding.submit.setOnClickListener(v -> {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            submit(username, password, binding.regSwitch.isChecked());
        });
    }

    private void submit(String username, String password, boolean shouldRegister) {
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null && user.getUsername().equals(username)) {
                loginSuccessful = true;
                binding.message.setText("");
                mainActivity.enableNavigationUI();
                navController.navigate(R.id.nav_graph_routes);
            } else {
                loginSuccessful = false;
                binding.message.setText("Invalid username and/or password");
            }
        });
        if (shouldRegister) {
            userViewModel.register(username, password);
        } else {
            userViewModel.login(username, password);
        }
    }
}
