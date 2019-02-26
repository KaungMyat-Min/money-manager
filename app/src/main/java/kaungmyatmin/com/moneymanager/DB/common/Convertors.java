package kaungmyatmin.com.moneymanager.DB.common;

import android.arch.persistence.room.TypeConverter;

import java.sql.Date;

public class Convertors {
    @TypeConverter
    public static Date fromTimeStamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
