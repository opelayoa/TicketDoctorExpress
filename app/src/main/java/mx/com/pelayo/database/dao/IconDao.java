package mx.com.pelayo.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import java.util.List;

import mx.com.pelayo.database.entities.Icon;
import mx.com.pelayo.database.entities.Perfil;

@Dao
public interface IconDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<Icon> icons);
}
