package mx.com.pelayo.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import mx.com.pelayo.api.TdeService;
import mx.com.pelayo.database.entities.Usuario;
import mx.com.pelayo.database.entities.custom.ItemAutocomplete;
import mx.com.pelayo.database.entities.custom.TicketInsert;
import mx.com.pelayo.database.entities.custom.TicketResponse;
import mx.com.pelayo.database.entities.custom.UserInformation;
import mx.com.pelayo.repository.SecurityRepository;
import mx.com.pelayo.repository.TicketAddRepository;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

@Singleton
public class AddTicketViewModel extends ViewModel {

    private TicketAddRepository ticketAddRepository;
    private SecurityRepository securityRepository;
    private TdeService tdeService;
    private ExecutorService executorService;

    private LiveData<List<ItemAutocomplete>> regions;
    private LiveData<List<Usuario>> users;
    private LiveData<List<ItemAutocomplete>> technicians;
    private LiveData<List<ItemAutocomplete>> districts;
    private LiveData<List<ItemAutocomplete>> stores;
    private LiveData<List<ItemAutocomplete>> departments;
    private LiveData<List<ItemAutocomplete>> usersDepartment;

    private LiveData<ItemAutocomplete> diagnostic;

    private MutableLiveData<Integer> filterRegion = new MutableLiveData<>();
    private MutableLiveData<Integer> filterDistrict = new MutableLiveData<>();
    private MutableLiveData<Integer> filterDepartment = new MutableLiveData<>();
    private MutableLiveData<ExpertParams> expertParams = new MutableLiveData<>();
    private MutableLiveData<Integer> filterDiagnostic = new MutableLiveData<>();

    private LiveData<RegionStates> regionStates;

    @Inject
    public AddTicketViewModel(TicketAddRepository ticketAddRepository, SecurityRepository securityRepository, TdeService tdeService, ExecutorService executorService) {
        this.securityRepository = securityRepository;
        this.ticketAddRepository = ticketAddRepository;
        this.tdeService = tdeService;
        this.executorService = executorService;
        this.regions = ticketAddRepository.getAllRegions();
        this.users = Transformations.switchMap(filterRegion, ticketAddRepository::getAllUsuarios);
        this.technicians = Transformations.switchMap(expertParams, technicalParams -> {
            if (technicalParams.getDepartmentId() == 3) {
                return ticketAddRepository.getAllTechnicianByDepartment(technicalParams.getDepartmentId());
            } else {
                return ticketAddRepository.getAllTechnicianByRegion(technicalParams.getRegionId());
            }
        });
        this.districts = Transformations.switchMap(filterRegion, ticketAddRepository::getAllDistritosByRegion);
        this.stores = Transformations.switchMap(filterDistrict, ticketAddRepository::getAllTiendasByDistrito);
        this.departments = ticketAddRepository.getAllDepartments();
        this.usersDepartment = Transformations.switchMap(filterDepartment, ticketAddRepository::getAllUsuariosByDepartment);
        this.diagnostic = Transformations.switchMap(filterDiagnostic, ticketAddRepository::getDiagnosticById);
        this.regionStates = Transformations.switchMap(filterRegion, regionId -> {
            RegionStates regionStates;
            if (regionId == 0) {
                regionStates = new RegionStates(false, false, true, false, true);
            } else {
                regionStates = new RegionStates(true, true, false, true, false);
            }
            MutableLiveData<RegionStates> data = new MutableLiveData<>();
            data.setValue(regionStates);
            return data;
        });
    }

    public LiveData<List<ItemAutocomplete>> getStores() {
        return stores;
    }

    public LiveData<List<ItemAutocomplete>> getRegions() {
        return regions;
    }

    public LiveData<List<Usuario>> getUsers() {
        return users;
    }

    public LiveData<List<ItemAutocomplete>> getExperts() {
        return technicians;
    }

    public LiveData<List<ItemAutocomplete>> getDistricts() {
        return districts;
    }

    public LiveData<List<ItemAutocomplete>> getDepartments() {
        return departments;
    }

    public LiveData<List<ItemAutocomplete>> getUsersDepartment() {
        return usersDepartment;
    }

    public LiveData<ItemAutocomplete> getDiagnostic() {
        return diagnostic;
    }

    public LiveData<RegionStates> getRegionStates() {
        return regionStates;
    }

    public void setFilterRegion(Integer regionId) {
        this.filterRegion.setValue(regionId);
    }

    public void setFilterDistrict(Integer distritoId) {
        this.filterDistrict.setValue(distritoId);
    }

    public void setExpertParams(ExpertParams expertParams) {
        this.expertParams.setValue(expertParams);
    }

    public void setFilterDepartment(Integer departmentId) {
        this.filterDepartment.setValue(departmentId);
    }

    public void setFilterDiagnostic(Integer diagnosticId) {
        this.filterDiagnostic.setValue(diagnosticId);
    }

    public UserInformation getUsuarioInfo() {
        return securityRepository.getUsuarioInfo();
    }

    public Observable<Response<TicketResponse>> insertTicket(File file, TicketInsert ticketInsert) {

        if (file != null) {

            return tdeService.insertFile(MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file)))
                    .flatMap(ticketFileResponse -> {
                        ticketInsert.setArchivo(ticketFileResponse == null ? null : ticketFileResponse.body().getFilePath());
                        return tdeService.insertTicket(ticketInsert);
                    });
        } else {
            return tdeService.insertTicket(ticketInsert);
        }

    }

    public Integer getTypeSymptomId(Integer typeId, Integer symptomId) {
        try {
            return executorService.submit(() -> ticketAddRepository.getTypeSymptomId(typeId, symptomId)).get();
        } catch (Exception e) {
            return null;
        }
    }

    public String getTypeSymptomName(Integer typeId, Integer symptomId) {
        try {
            return executorService.submit(() -> ticketAddRepository.getTypeSymptomName(typeId, symptomId)).get();
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getSymptomDiagnostic(Integer symptomId, Integer diagnosticId) {
        try {
            return executorService.submit(() -> ticketAddRepository.getSymptomDiagnosticId(symptomId, diagnosticId)).get();
        } catch (Exception e) {
            return null;
        }
    }

    public String getSymptomDiagnosticName(Integer symptomId, Integer diagnosticId) {
        try {
            return executorService.submit(() -> ticketAddRepository.getSymptomDiagnosticName(symptomId, diagnosticId)).get();
        } catch (Exception e) {
            return null;
        }
    }

    public static class ExpertParams {

        private Integer departmentId;
        private Integer regionId;

        public ExpertParams(Integer departmentId, Integer regionId) {
            this.departmentId = departmentId;
            this.regionId = regionId;
        }

        public Integer getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(Integer departmentId) {
            this.departmentId = departmentId;
        }

        public Integer getRegionId() {
            return regionId;
        }

        public void setRegionId(Integer regionId) {
            this.regionId = regionId;
        }
    }

    public static class RegionStates {
        private boolean optionEnable;
        private boolean storeChecked;
        private boolean otherChecked;
        private boolean storeLayoutVisible;
        private boolean otherLayoutVisible;

        public RegionStates(boolean optionEnable, boolean storeChecked, boolean otherChecked, boolean storeLayoutVisible, boolean otherLayoutVisible) {
            this.optionEnable = optionEnable;
            this.storeChecked = storeChecked;
            this.otherChecked = otherChecked;
            this.storeLayoutVisible = storeLayoutVisible;
            this.otherLayoutVisible = otherLayoutVisible;
        }

        public boolean isOptionEnable() {
            return optionEnable;
        }

        public boolean isStoreChecked() {
            return storeChecked;
        }

        public boolean isOtherChecked() {
            return otherChecked;
        }

        public boolean isStoreLayoutVisible() {
            return storeLayoutVisible;
        }

        public boolean isOtherLayoutVisible() {
            return otherLayoutVisible;
        }
    }
}
