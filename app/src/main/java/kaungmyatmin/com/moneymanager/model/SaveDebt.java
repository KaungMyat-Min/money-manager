package kaungmyatmin.com.moneymanager.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

import java.util.Date;

import androidx.annotation.NonNull;

@Entity(tableName = "save_debt",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE))
public class SaveDebt {
    @Embedded
    Id id;

    @Embedded
    Time time;

    @NonNull
    public int amount;

    @NonNull
    public String title;

    @ColumnInfo(name = "user_id", index = true)
    @NonNull
    public int userId;

    java.sql.Date when;

    @Ignore
    public int total;


    public SaveDebt() {

    }

    public SaveDebt(int id, @NonNull int amount, @NonNull String title, @NonNull int userId, int day, int month, int year, Date when) {
        this.amount = amount;
        this.title = title;
        this.id.id = id;
        this.time.day = day;
        this.time.month = month;
        this.time.year = year;
        this.userId = userId;

    }
}
