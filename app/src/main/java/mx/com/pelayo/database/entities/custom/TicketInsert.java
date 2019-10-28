package mx.com.pelayo.database.entities.custom;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TicketInsert {

    private String observaciones;
    private String archivo;
    @SerializedName("solucion-remota")
    private boolean solucionRemota;
    @SerializedName("fecha-apertura")
    private Date fechaApertura;
    @SerializedName("solicitante-id")
    private Integer solicitanteId;
    @SerializedName("solicitante-name")
    private String solicitanteName;
    @SerializedName("lugar-id")
    private Integer lugarId;
    @SerializedName("lugar-name")
    private String lugarName;
    @SerializedName("sintoma-id")
    private Integer sintomaId;
    @SerializedName("diagnostico-id")
    private Integer diagnosticoId;
    @SerializedName("estado-id")
    private Integer estadoId;
    @SerializedName("tecnico-id")
    private Integer tecnicoId;
    @SerializedName("tecnico-name")
    private String tecnicoName;
    @SerializedName("capturista-id")
    private Integer capturistaId;
    @SerializedName("capturista-name")
    private String capturistaName;
    private Integer tipo;
    private String leyenda;
    private Integer categoria;
    @SerializedName("correo-manual")
    private boolean correoManual;
    private String distrito;
    private String tienda;
    private String departamento;
    private String usuario;

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isSolucionRemota() {
        return solucionRemota;
    }

    public void setSolucionRemota(boolean solucionRemota) {
        this.solucionRemota = solucionRemota;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Integer getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(Integer solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public String getSolicitanteName() {
        return solicitanteName;
    }

    public void setSolicitanteName(String solicitanteName) {
        this.solicitanteName = solicitanteName;
    }

    public Integer getLugarId() {
        return lugarId;
    }

    public void setLugarId(Integer lugarId) {
        this.lugarId = lugarId;
    }

    public String getLugarName() {
        return lugarName;
    }

    public void setLugarName(String lugarName) {
        this.lugarName = lugarName;
    }

    public Integer getSintomaId() {
        return sintomaId;
    }

    public void setSintomaId(Integer sintomaId) {
        this.sintomaId = sintomaId;
    }

    public Integer getDiagnosticoId() {
        return diagnosticoId;
    }

    public void setDiagnosticoId(Integer diagnosticoId) {
        this.diagnosticoId = diagnosticoId;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }

    public Integer getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Integer tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public String getTecnicoName() {
        return tecnicoName;
    }

    public void setTecnicoName(String tecnicoName) {
        this.tecnicoName = tecnicoName;
    }

    public Integer getCapturistaId() {
        return capturistaId;
    }

    public void setCapturistaId(Integer capturistaId) {
        this.capturistaId = capturistaId;
    }

    public String getCapturistaName() {
        return capturistaName;
    }

    public void setCapturistaName(String capturistaName) {
        this.capturistaName = capturistaName;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getLeyenda() {
        return leyenda;
    }

    public void setLeyenda(String leyenda) {
        this.leyenda = leyenda;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public boolean isCorreoManual() {
        return correoManual;
    }

    public void setCorreoManual(boolean correoManual) {
        this.correoManual = correoManual;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
}
