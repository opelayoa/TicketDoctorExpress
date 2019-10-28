package mx.com.pelayo.api;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import mx.com.pelayo.api.entities.TicketInfo;
import mx.com.pelayo.api.entities.TicketSummary;
import mx.com.pelayo.database.entities.Categoria;
import mx.com.pelayo.database.entities.Departamento;
import mx.com.pelayo.database.entities.DeptoSintomaDiagnostico;
import mx.com.pelayo.database.entities.DeptoTipoSintoma;
import mx.com.pelayo.database.entities.Diagnostico;
import mx.com.pelayo.database.entities.Distrito;
import mx.com.pelayo.database.entities.Estado;
import mx.com.pelayo.database.entities.Icon;
import mx.com.pelayo.database.entities.IconEntity;
import mx.com.pelayo.database.entities.ImpCatMotivo;
import mx.com.pelayo.database.entities.Perfil;
import mx.com.pelayo.database.entities.PerfilSintomaDiagnostico;
import mx.com.pelayo.database.entities.PerfilTipoSintoma;
import mx.com.pelayo.database.entities.PosibleOrigen;
import mx.com.pelayo.database.entities.Proveedor;
import mx.com.pelayo.database.entities.Puestos;
import mx.com.pelayo.database.entities.Region;
import mx.com.pelayo.database.entities.SatUsuario;
import mx.com.pelayo.database.entities.Sintoma;
import mx.com.pelayo.database.entities.SintomaDiagnostico;
import mx.com.pelayo.database.entities.SolucionesStandard;
import mx.com.pelayo.database.entities.StatusProyectos;
import mx.com.pelayo.database.entities.Sucursal;
import mx.com.pelayo.database.entities.Tienda;
import mx.com.pelayo.database.entities.TipoSintoma;
import mx.com.pelayo.database.entities.TipoSucursal;
import mx.com.pelayo.database.entities.Tipotd;
import mx.com.pelayo.database.entities.Usuario;
import mx.com.pelayo.database.entities.Zona;
import mx.com.pelayo.database.entities.custom.TicketFileResponse;
import mx.com.pelayo.database.entities.custom.TicketInsert;
import mx.com.pelayo.database.entities.custom.TicketResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TdeService {

    @GET("usuarios")
    Observable<List<Usuario>> getAllUsuarios();

    @GET("distritos")
    Observable<List<Distrito>> getAllDistritos();

    @GET("perfiles")
    Observable<List<Perfil>> getAllPerfiles();

    @GET("perfil-sintoma-diagnostico")
    Observable<List<PerfilSintomaDiagnostico>> getAllPerfilSintomaDiagnostico();

    @GET("perfil-tipo-sintoma")
    Observable<List<PerfilTipoSintoma>> getAllPerfilTipoSintoma();

    @GET("regiones")
    Observable<List<Region>> getAllRegiones();

    @GET("tiendas")
    Observable<List<Tienda>> getAllTiendas();

    @GET("zonas")
    Observable<List<Zona>> getAllZona();

    @GET("sat-usuarios")
    Observable<List<SatUsuario>> getAllSatUsuarios();

    @GET("estados")
    Observable<List<Estado>> getAllEstados();

    @GET("categorias")
    Observable<List<Categoria>> getAllCategorias();

    @GET("tipos")
    Observable<List<Tipotd>> getAllTipotds();

    @GET("status-proyectos")
    Observable<List<StatusProyectos>> getAllStatusProyectos();

    @GET("sintomas")
    Observable<List<Sintoma>> getAllSintomas();

    @GET("diagnosticos")
    Observable<List<Diagnostico>> getAllDiagnosticos();

    @GET("tipo-sintoma")
    Observable<List<TipoSintoma>> getAllTipoSintoma();

    @GET("sintoma-diagnostico")
    Observable<List<SintomaDiagnostico>> getAllSintoDiagnostico();

    @GET("imp-cat-motivo")
    Observable<List<ImpCatMotivo>> getAllImpCatMotivo();

    @GET("tipo-sucursal")
    Observable<List<TipoSucursal>> getAllTipoSucursal();

    @GET("sucursales")
    Observable<List<Sucursal>> getAllSucursales();

    @GET("soluciones-standard")
    Observable<List<SolucionesStandard>> getAllSolucionesStandard();

    @GET("posible-origen")
    Observable<List<PosibleOrigen>> getAllPosibleOrigen();

    @GET("proveedores")
    Observable<List<Proveedor>> getAllProveedores();

    @GET("departamentos")
    Observable<List<Departamento>> getAllDepartamentos();

    @GET("puestos")
    Observable<List<Puestos>> getAllPuestos();

    @GET("departamento-tipo-sintoma")
    Observable<List<DeptoTipoSintoma>> getAllDepartamentoTipoSintoma();

    @GET("departamento-sintoma-diagnostico")
    Observable<List<DeptoSintomaDiagnostico>> getAllDepartamentoSintomaDiagnostico();

    @GET("icons")
    Observable<List<Icon>> getAllIcons();

    @GET("icon-entity")
    Observable<List<IconEntity>> getAllIconEntities();

    @GET("tickets/info/{ticketId}")
    Observable<TicketInfo> getTicketInfo(@Path("ticketId") Integer ticketId);

    @GET("tickets/summary/user/{userId}/type/{ticketStateId}")
    Observable<List<TicketSummary>> getTicketsSummary(@Path("userId") Integer userId, @Path("ticketStateId") Integer ticketStateId);

    @POST("tickets")
    Observable<Response<TicketResponse>> insertTicket(@Body TicketInsert ticketInsert);

    @Multipart
    @POST("tickets/files")
    Observable<Response<TicketFileResponse>> insertFile(@Part MultipartBody.Part file);
}
