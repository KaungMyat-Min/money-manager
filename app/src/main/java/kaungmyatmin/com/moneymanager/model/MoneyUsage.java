package kaungmyatmin.com.moneymanager.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@Entity(tableName = "money_usage",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UsageType.class,
                        parentColumns = "id",
                        childColumns = "type_id",
                        onDelete = ForeignKey.CASCADE
                )
        }
)

public class MoneyUsage {
    @Embedded
    Id id;

    @NonNull
    public int amount;

    @Nullable
    public String reason;


    public Date when;

    @Embedded
    public Time time;

    @ColumnInfo(name = "user_id", index = true)
    @NonNull
    public int userId;

    @ColumnInfo(name = "type_id", index = true)
    @NonNull
    public int typeId;

    @Ignore
    public int total;

    public MoneyUsage() {
    }

    public MoneyUsage(@NonNull int id, @NonNull int amount, @Nullable String reason, Date when, int day, int month, int year, @NonNull int userId, @NonNull int typeId) {
        this.id.id = id;
        this.amount = amount;
        this.reason = reason;
        this.when = when;
        this.time.day = day;
        this.time.month = month;
        this.time.year = year;
        this.userId = userId;
        this.typeId = typeId;
    }


    public Date getWhen() {
        return when == null ? new Date(System.currentTimeMillis()) : null;

    }


}
