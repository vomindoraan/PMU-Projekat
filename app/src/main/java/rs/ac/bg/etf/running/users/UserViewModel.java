package rs.ac.bg.etf.running.users;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import rs.ac.bg.etf.running.data.User;
import rs.ac.bg.etf.running.data.UserRepository;

public class UserViewModel extends ViewModel {

    private final UserRepository userRepository;

    private MutableLiveData<User> currentUser;

    @ViewModelInject
    public UserViewModel(
            UserRepository userRepository,
            @Assisted SavedStateHandle savedStateHandle) {
        this.userRepository = userRepository;
    }

    public void login(String username, String password) {
        User user = userRepository.getByUsername(username);
        currentUser.setValue(
                user != null && user.getPassword().equals(password)
                ? user : null);
    }

    public void register(String username, String password) {
        User u = new User(0, username, password);
        userRepository.insert(u);
        currentUser.setValue(u);
    }

    public MutableLiveData<User> getCurrentUser() {
        if (currentUser == null) {
            currentUser = new MutableLiveData<>();
        }
        return currentUser;
    }
}
