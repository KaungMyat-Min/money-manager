package kaungmyatmin.com.moneymanager.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import androidx.annotation.NonNull;


public class Id {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
}
