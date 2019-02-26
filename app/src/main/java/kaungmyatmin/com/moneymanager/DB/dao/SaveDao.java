package kaungmyatmin.com.moneymanager.DB.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import kaungmyatmin.com.moneymanager.model.SaveDebt;

@Dao
public interface SaveDao {
    @Query("SELECT amount,day from save_debt WHERE month = :month order by day DESC")
    List<SaveDebt> getDailySave(int month);

    @Query("SELECT sum (amount) as total, month from save_debt order by :year desc, :month desc limit 10")
    List<SaveDebt> getMonthlySave(int month, int year);

}
