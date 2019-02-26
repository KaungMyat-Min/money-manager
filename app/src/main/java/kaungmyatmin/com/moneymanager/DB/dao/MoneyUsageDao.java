package kaungmyatmin.com.moneymanager.DB.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import kaungmyatmin.com.moneymanager.model.MoneyUsage;
import kaungmyatmin.com.moneymanager.model.SaveDebt;

@Dao
public interface MoneyUsageDao {
    @Insert
    void insert(MoneyUsage usage);


    @Query("SELECT SUM(amount) AS totalAmt FROM money_usage where month = :month AND year = :year")
    int getTotalUsageForMonth(int month, int year);

    @Insert
    long saveUsage(MoneyUsage usage);

    @Delete
    int deleteUsage(MoneyUsage usage);

    @Query("DELETE FROM money_usage where id = :id")
    int deleteWithId(int id);

    @Update
    int update(MoneyUsage usage);

    @Query("SELECT totalAmt, type_id FROM (SELECT SUM(amount) AS totalAmt ,type_id FROM money_usage where day = :day AND month = :month AND year = :year GROUP BY type_id) AS tableTemp ORDER BY totalAmt DESC")
    List<List<Integer>> getTotalForCertainDay(int day, int month, int year);

    @Query("SELECT sum (amount),month from money_usage WHERE year = :year group by month order by month desc")
    List<List<Integer>> getTotalMontlyUsage(int year);


    @Query("SELECT SUM(amount) as totalAmt,day from money_usage WHERE month = :month AND year = :year GROUP BY day ORDER BY day DESC")
    List<List<Integer>> getTotalDailyUsage(int month, int year);


    @Query("SELECT sum(amount) as total,day FROM money_usage where month = :month AND year = :year AND type_id = :typeId  GROUP BY day ORDER BY day DESC")
    List<MoneyUsage> getIndividualDailyLineChart(int typeId, int month, int year);

}
