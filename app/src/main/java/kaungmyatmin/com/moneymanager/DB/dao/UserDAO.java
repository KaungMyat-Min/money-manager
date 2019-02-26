package kaungmyatmin.com.moneymanager.DB.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import kaungmyatmin.com.moneymanager.model.User;

@Dao
public interface UserDAO {
    @Insert
    void insert(User user);

    @Query("DELETE from users")
    void deleteAll();

    @Query("SELECT * FROM users")
    List<User> getUsers();

    @Update
    void update(User user);


}
