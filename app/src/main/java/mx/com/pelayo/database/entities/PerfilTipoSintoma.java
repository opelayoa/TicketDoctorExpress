package mx.com.pelayo.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "perfil_tipo_sintoma", indices = {@Index(value = {"perfil_id", "id"})})
public class PerfilTipoSintoma implements java.io.Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private Integer id;
    @ColumnInfo(name = "perfil_id")
    @SerializedName("perfil-id")
    private int perfilId;
    @ColumnInfo(name = "ts_id")
    @SerializedName("ts-id")
    private int tsId;
    @ColumnInfo(name = "userreg")
    @SerializedName("userreg")
    private int userreg;
    @ColumnInfo(name = "fechareg")
    @SerializedName("fechareg")
    private Date fechareg;
    @ColumnInfo(name = "userdel")
    @SerializedName("userdel")
    private int userdel;
    @ColumnInfo(name = "fechadel")
    @SerializedName("fechadel")
    private Date fechadel;
    @ColumnInfo(name = "status")
    @SerializedName("status")
    private boolean status;

    public PerfilTipoSintoma() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPerfilId() {
        return this.perfilId;
    }

    public void setPerfilId(int perfilId) {
        this.perfilId = perfilId;
    }

    public int getTsId() {
        return this.tsId;
    }

    public void setTsId(int tsId) {
        this.tsId = tsId;
    }

    public int getUserreg() {
        return this.userreg;
    }

    public void setUserreg(int userreg) {
        this.userreg = userreg;
    }

    public Date getFechareg() {
        return this.fechareg;
    }

    public void setFechareg(Date fechareg) {
        this.fechareg = fechareg;
    }

    public int getUserdel() {
        return this.userdel;
    }

    public void setUserdel(int userdel) {
        this.userdel = userdel;
    }

    public Date getFechadel() {
        return this.fechadel;
    }

    public void setFechadel(Date fechadel) {
        this.fechadel = fechadel;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
