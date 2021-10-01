package rs.ac.bg.etf.running.data;

import androidx.lifecycle.LiveData;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class UserRepository {

    private final ExecutorService executorService;

    private final UserDao userDao;

    @Inject
    public UserRepository(
            ExecutorService executorService,
            UserDao userDao) {
        this.executorService = executorService;
        this.userDao = userDao;
    }

    public void insert(User user) {
        executorService.submit(() -> userDao.insert(user));
    }

    public List<User> getAll() {
        return userDao.getAll();
    }

    public LiveData<List<User>> getAllLiveData() {
        return userDao.getAllLiveData();
    }

    public User getByUsername(String username) {
        return userDao.getByUsername(username);
    }

    public LiveData<User> getByUsernameLiveData(String username) {
        return userDao.getByUsernameLiveData(username);
    }
}
