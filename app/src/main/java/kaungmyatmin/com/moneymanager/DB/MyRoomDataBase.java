package kaungmyatmin.com.moneymanager.DB;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import kaungmyatmin.com.moneymanager.DB.common.Convertors;
import kaungmyatmin.com.moneymanager.DB.dao.MoneyUsageDao;
import kaungmyatmin.com.moneymanager.DB.dao.SaveDao;
import kaungmyatmin.com.moneymanager.DB.dao.UserDAO;
import kaungmyatmin.com.moneymanager.model.MoneyUsage;
import kaungmyatmin.com.moneymanager.model.SaveDebt;
import kaungmyatmin.com.moneymanager.model.User;

@Database(entities = {MoneyUsage.class, SaveDebt.class, User.class}, version = 1, exportSchema = false)
@TypeConverters({Convertors.class})
public abstract class MyRoomDataBase extends RoomDatabase {
    private static MyRoomDataBase INSTANCE;

    public static MyRoomDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MyRoomDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, MyRoomDataBase.class, "money_manager_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UserDAO getUserDao();

    public abstract SaveDao getSaveDao();

    public abstract MoneyUsageDao getMoneyUsageDao();

}
