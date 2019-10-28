package mx.com.pelayo.ui.ticket.add;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.arch.persistence.room.util.StringUtil;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Iterables;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import mx.com.pelayo.App;
import mx.com.pelayo.R;
import mx.com.pelayo.database.entities.Usuario;
import mx.com.pelayo.database.entities.custom.ItemAutocomplete;
import mx.com.pelayo.database.entities.custom.TicketInsert;
import mx.com.pelayo.database.entities.custom.TicketResponse;
import mx.com.pelayo.database.entities.custom.UserInformation;
import mx.com.pelayo.ui.MainActivity;
import mx.com.pelayo.ui.ticket.add.adapter.CustomArrayAdapter;
import mx.com.pelayo.ui.util.DialogFactory;
import mx.com.pelayo.util.Tools;
import mx.com.pelayo.viewmodel.AddTicketViewModel;
import retrofit2.Response;

public class AddTicketFragment extends Fragment {

    private static final String TYPE_PARAM = "type_id";
    private static final String SYMPTOM_PARAM = "symptom_id";
    private static final String DIAGNOSTIC_PARAM = "diagnostic_id";

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Inject
    AddTicketViewModel addTicketViewModel;

    private AutoCompleteTextView applicant;
    private AutoCompleteTextView region;
    private AutoCompleteTextView expert;
    private AutoCompleteTextView district;
    private AutoCompleteTextView store;
    private AutoCompleteTextView user;
    private AutoCompleteTextView diagnostic;
    private AutoCompleteTextView place;
    private TextView comment;
    private CheckBox approved;
    private RadioGroup options;
    private RadioButton optionStore;
    private RadioButton optionOthers;

    private AutoCompleteTextView department;

    private LinearLayout layoutOther;
    private LinearLayout layoutStore;

    private Dialog progressDialog;
    private Dialog errorDialog;
    private Dialog infoDialog;

    private ImageView image;
    private TextView emptyImage;
    private Button buttonCamera;
    private File file;

    private int typeId;
    private int symptomId;
    private int diagnosticId;

    private Integer regionId;
    private Integer applicantId;
    private Integer expertId;
    private Integer districtId;
    private Integer storeId;
    private Integer departmentId;
    private Integer userId;

    private String departmentName;
    private String districtName;
    private String regionName;
    private String userName;
    private String storeName;
    private String applicantName;
    private String expertName;

    private UserInformation usuarioInfo;

    public AddTicketFragment() {
        // Required empty public constructor
    }

    public static AddTicketFragment newInstance(Integer typeId, Integer symptomId, Integer diagnosticId) {
        AddTicketFragment fragment = new AddTicketFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_PARAM, typeId);
        args.putInt(SYMPTOM_PARAM, symptomId);
        args.putInt(DIAGNOSTIC_PARAM, diagnosticId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplicationContext()).getApplicationComponent().inject(this);
        if (getArguments() != null) {
            typeId = getArguments().getInt(TYPE_PARAM);
            symptomId = getArguments().getInt(SYMPTOM_PARAM);
            diagnosticId = getArguments().getInt(DIAGNOSTIC_PARAM);
        }
    }

    @Override
    public void onResume() {
        MainActivity activity = (MainActivity) getActivity();
        activity.hideBanner();
        activity.setTitle("Crear Ticket");
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_ticket, container, false);
        this.usuarioInfo = addTicketViewModel.getUsuarioInfo();
        configImageCapture(view);
        configRegions(view);
        configApplicants(view);
        configExpert(view);
        configDistrict(view);
        configStores(view);
        configDepartments(view);
        configUsers(view);
        configDiagnostic(view);
        configPlace(view);
        configApproved(view);
        configComment(view);
        configRegionStates(view);
        view.findViewById(R.id.save).setOnClickListener(v -> {
            saveTicket();
        });
        return view;
    }

    private void configRegions(View view) {
        region = view.findViewById(R.id.region);
        CustomArrayAdapter<ItemAutocomplete> regionAdapter = new CustomArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        addTicketViewModel.getRegions().observe(this, regions -> {
            regionAdapter.clear();
            regionAdapter.addAll(regions);
            regionAdapter.notifyDataSetChanged();
            ItemAutocomplete item = Iterables.tryFind(regions, input -> input.getId().equals(usuarioInfo.getRegionId())).orNull();
            if (item != null) {
                regionId = item.getId();
                regionName = item.getLabel();
                region.setText(item.getLabel());
            } else {
                regionId = regions.get(0).getId();
                regionName = regions.get(0).getLabel();
                region.setText(regions.get(0).getLabel());
            }
            region.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    region.setText("");
                } else {
                    ItemAutocomplete itemAutocomplete = Iterables.tryFind(regions, input -> input.getId() == regionId).orNull();
                    if (itemAutocomplete != null) {
                        region.setText(itemAutocomplete.getLabel());
                    } else {
                        region.setText(getString(R.string.emptyRegions));
                    }
                }
            });
            addTicketViewModel.setFilterRegion(regionId);
        });
        region.setAdapter(regionAdapter);
        region.setOnItemClickListener((parent, view1, position, id) -> {
            ItemAutocomplete data = (ItemAutocomplete) parent.getItemAtPosition(position);
            regionId = data.getId();
            regionName = data.getLabel();
            addTicketViewModel.setFilterRegion(regionId);
            addTicketViewModel.setExpertParams(new AddTicketViewModel.ExpertParams(departmentId, regionId));
            hideKeyboard();
        });
        region.setOnClickListener(v -> region.setText(""));
    }

    private void configApplicants(View view) {
        applicant = view.findViewById(R.id.applicant);
        CustomArrayAdapter<Usuario> applicantAdapter = new CustomArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        addTicketViewModel.getUsers().observe(this, applicants -> {
            applicantAdapter.clear();
            applicantAdapter.addAll(applicants);
            System.out.println(usuarioInfo.getId());
            applicantAdapter.notifyDataSetChanged();
            Usuario item = Iterables.tryFind(applicants, input -> input.getId() == usuarioInfo.getId()).orNull();
            if (item != null) {
                applicant.setText(item.toString());
                applicantId = item.getId();
                applicantName = item.toString();
                departmentId = item.getDepartamentoId();
            } else {
                applicant.setText(applicants.get(0).toString());
                applicantId = applicants.get(0).getId();
                applicantName = applicants.get(0).toString();
                departmentId = applicants.get(0).getDepartamentoId();
            }
            applicant.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    applicant.setText("");
                } else {
                    Usuario user = Iterables.tryFind(applicants, input -> input.getId() == applicantId).orNull();
                    if (user != null) {
                        applicant.setText(user.toString());
                    } else {
                        applicant.setText(getString(R.string.emptyRegions));
                    }
                }
            });
            addTicketViewModel.setExpertParams(new AddTicketViewModel.ExpertParams(departmentId, regionId));

        });
        applicant.setAdapter(applicantAdapter);
        applicant.setOnItemClickListener((parent, view1, position, id) -> {
            Usuario data = (Usuario) parent.getItemAtPosition(position);
            applicantId = data.getId();
            applicantName = data.toString();
            addTicketViewModel.setExpertParams(new AddTicketViewModel.ExpertParams(data.getDepartamentoId(), regionId));
            hideKeyboard();
        });
        applicant.setOnClickListener(v -> applicant.setText(""));
    }

    private void configExpert(View view) {
        expert = view.findViewById(R.id.expert);
        CustomArrayAdapter<ItemAutocomplete> expertAdapter = new CustomArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        addTicketViewModel.getExperts().observe(this, experts -> {
            expertAdapter.clear();
            expertAdapter.addAll(experts);
            expertAdapter.notifyDataSetChanged();
            expert.setText(experts.get(0).getLabel());
            expertId = experts.get(0).getId();
            expertName = experts.get(0).getLabel();
            expert.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    expert.setText("");
                } else {
                    ItemAutocomplete item = Iterables.tryFind(experts, input -> input.getId() == expertId).orNull();
                    if (item != null) {
                        expert.setText(item.toString());

                    } else {
                        expert.setText(getString(R.string.emptyExperts));
                    }
                }
            });
        });
        expert.setAdapter(expertAdapter);
        expert.setOnItemClickListener((parent, view1, position, id) -> {
            ItemAutocomplete data = (ItemAutocomplete) parent.getItemAtPosition(position);
            expertId = data.getId();
            expertName = data.getLabel();
            hideKeyboard();
        });
        expert.setOnClickListener(v -> expert.setText(""));
    }

    private void configDistrict(View view) {
        district = view.findViewById(R.id.district);
        CustomArrayAdapter<ItemAutocomplete> districtAdapter = new CustomArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        addTicketViewModel.getDistricts().observe(this, districts -> {
            districtAdapter.clear();
            districtAdapter.addAll(districts);
            districtAdapter.notifyDataSetChanged();
            district.setText(districts.get(0).getLabel());
            districtId = districts.get(0).getId();
            districtName = districts.get(0).getLabel();
            this.addTicketViewModel.setFilterDistrict(districtId);
            district.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    district.setText("");
                } else {
                    ItemAutocomplete item = Iterables.tryFind(districts, input -> input.getId() == districtId).orNull();
                    district.setText(item.getLabel());
                    districtName = item.getLabel();
                }
            });
        });
        district.setAdapter(districtAdapter);
        district.setOnItemClickListener((parent, view1, position, id) -> {
            ItemAutocomplete data = (ItemAutocomplete) parent.getItemAtPosition(position);
            districtId = data.getId();
            districtName = data.getLabel();
            this.addTicketViewModel.setFilterDistrict(districtId);
            hideKeyboard();
        });
        district.setOnClickListener(v -> {
            district.setText("");
        });

    }

    private void configStores(View view) {
        store = view.findViewById(R.id.store);
        CustomArrayAdapter<ItemAutocomplete> storeAdapter = new CustomArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        addTicketViewModel.getStores().observe(this, stores -> {
            storeAdapter.clear();
            storeAdapter.addAll(stores);
            storeAdapter.notifyDataSetChanged();
            if (stores != null && stores.size() > 0) {
                store.setText(stores.get(0).getLabel());
                storeId = stores.get(0).getId();
                storeName = stores.get(0).getLabel();
                enableView(store);
            } else {
                store.setText(getString(R.string.stores_empty));
                storeName = getString(R.string.stores_empty);
                disableView(store);
                storeId = null;
            }
            store.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    store.setText("");
                } else {
                    ItemAutocomplete item = Iterables.tryFind(stores, input -> input.getId() == storeId).orNull();
                    if (item != null) {
                        store.setText(item.getLabel());
                    } else {
                        store.setText(getString(R.string.stores_empty));
                    }

                }
            });
        });
        store.setAdapter(storeAdapter);
        store.setOnItemClickListener((parent, view1, position, id) -> {
            ItemAutocomplete data = (ItemAutocomplete) parent.getItemAtPosition(position);
            this.storeId = data.getId();
            storeName = data.getLabel();
            hideKeyboard();
        });
        store.setOnClickListener(v -> store.setText(""));
    }

    private void configDepartments(View view) {
        department = view.findViewById(R.id.department);
        CustomArrayAdapter<ItemAutocomplete> departmentAdapter = new CustomArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        addTicketViewModel.getDepartments().observe(this, departments -> {
            departmentAdapter.clear();
            departmentAdapter.addAll(departments);
            departmentAdapter.notifyDataSetChanged();
            department.setText(departments.get(0).getLabel());
            departmentId = departments.get(0).getId();
            departmentName = departments.get(0).getLabel();
            department.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    department.setText("");
                } else {
                    ItemAutocomplete item = Iterables.tryFind(departments, input -> input.getId() == departmentId).orNull();
                    department.setText(item.getLabel());
                }
            });
            addTicketViewModel.setFilterDepartment(departmentId);
        });
        department.setAdapter(departmentAdapter);
        department.setOnItemClickListener((parent, view1, position, id) -> {
            ItemAutocomplete data = (ItemAutocomplete) parent.getItemAtPosition(position);
            departmentId = data.getId();
            departmentName = data.getLabel();
            addTicketViewModel.setFilterDepartment(departmentId);
            hideKeyboard();
        });
        department.setOnClickListener(v -> department.setText(""));
    }

    private void configUsers(View view) {
        user = view.findViewById(R.id.user);
        CustomArrayAdapter<ItemAutocomplete> userAdapter = new CustomArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        addTicketViewModel.getUsersDepartment().observe(this, users -> {
            userAdapter.clear();
            userAdapter.addAll(users);
            userAdapter.notifyDataSetChanged();
            if (users != null && users.size() > 0) {
                user.setText(users.get(0).getLabel());
                userId = users.get(0).getId();
                userName = users.get(0).getLabel();
                enableView(user);
            } else {
                disableView(user);
                user.setText(getString(R.string.emptyUsers));
                userId = null;
                userName = null;
            }
            user.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    user.setText("");
                } else {
                    ItemAutocomplete item = Iterables.tryFind(users, input -> input.getId() == userId).orNull();
                    user.setText(item.getLabel());
                }
            });
        });
        user.setAdapter(userAdapter);
        user.setOnItemClickListener((parent, view1, position, id) -> {
            ItemAutocomplete data = (ItemAutocomplete) parent.getItemAtPosition(position);
            userId = data.getId();
            userName = data.getLabel();
            hideKeyboard();
        });
        user.setOnClickListener(v -> user.setText(""));
    }

    private void configDiagnostic(View view) {
        diagnostic = view.findViewById(R.id.diagnostic);
        addTicketViewModel.getDiagnostic().observe(this, diagnosticValue -> {
            diagnostic.setText(diagnosticValue.getLabel());
        });
        addTicketViewModel.setFilterDiagnostic(diagnosticId);
        disableView(diagnostic);
    }

    private void configPlace(View view) {
        place = view.findViewById(R.id.place);
        place.setText(String.format("%s - %s", usuarioInfo.getNumero3b(), usuarioInfo.getAlmacenDesc()));
        disableView(place);
    }

    private void configApproved(View view) {
        approved = view.findViewById(R.id.approved);
        approved.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                enableView(comment);
            } else {
                disableView(comment);
                comment.setText("");
            }
        });
    }

    private void configComment(View view) {
        comment = view.findViewById(R.id.comment);
        if (approved.isChecked()) {
            comment.setEnabled(true);
        } else {
            comment.setEnabled(false);
        }
    }

    private void configImageCapture(View view) {
        emptyImage = view.findViewById(R.id.emptyImage);
        image = view.findViewById(R.id.image);
        buttonCamera = view.findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            this.startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("XxX", "requestCode: " + requestCode + " CAMERA_REQUEST: " + CAMERA_REQUEST + " resultCode: " + resultCode + " Activity.RESULT_OK" + Activity.RESULT_OK);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            try {
                file = bitmapToFile(photo);
                image.setImageBitmap(photo);
                image.setVisibility(View.VISIBLE);
                emptyImage.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            file = null;
            image.setVisibility(View.GONE);
            emptyImage.setVisibility(View.VISIBLE);
        }
    }

    private File bitmapToFile(Bitmap bitmap) throws IOException {

        DateFormat dateFormat = new SimpleDateFormat("hhmmssSSS");
        String fileName = dateFormat.format(new Date());
        fileName = "TDE-" + fileName + ".png";
        //create a file to write bitmap data
        File file = new File(this.getContext().getCacheDir(), fileName);
        file.createNewFile();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, byteArrayOutputStream);
        byte[] bitmapdata = byteArrayOutputStream.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        return file;

    }

    private void configRegionStates(View view) {
        options = view.findViewById(R.id.options);
        optionStore = view.findViewById(R.id.optStore);
        optionOthers = view.findViewById(R.id.optOther);
        layoutOther = view.findViewById(R.id.layoutOther);
        layoutStore = view.findViewById(R.id.layoutStore);
        layoutStore.setVisibility(View.GONE);
        layoutOther.setVisibility(View.GONE);
        addTicketViewModel.getRegionStates().observe(this, regionStates -> {
            optionOthers.setChecked(regionStates.isOtherChecked());
            optionStore.setChecked(regionStates.isStoreChecked());
            if (regionStates.isOptionEnable()) {
                enableButton(optionOthers);
                enableButton(optionStore);
            } else {
                disableButton(optionOthers);
                disableButton(optionStore);
            }
            if (optionStore.isChecked()) {
                layoutOther.setVisibility(View.GONE);
                layoutStore.setVisibility(View.VISIBLE);
            } else {
                layoutOther.setVisibility(View.VISIBLE);
                layoutStore.setVisibility(View.GONE);
            }
        });
        optionStore.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutOther.setVisibility(View.GONE);
                layoutStore.setVisibility(View.VISIBLE);
            } else {
                layoutOther.setVisibility(View.VISIBLE);
                layoutStore.setVisibility(View.GONE);
            }
        });

    }

    private boolean validateForm() {
        if (!approved.isChecked()) {
            errorDialog = DialogFactory.getErrorDialog(AddTicketFragment.this.getContext(), "Debes aprovar el ticket.");
            errorDialog.show();
            return false;
        } else {
            if (comment.getText().toString().isEmpty()) {
                errorDialog = DialogFactory.getErrorDialog(AddTicketFragment.this.getContext(), "Debes capturar tus comentarios.");
                errorDialog.show();
                return false;
            }

        }
        return true;
    }

    private void saveTicket() {
        if (!validateForm()) {
            return;
        }
        progressDialog = DialogFactory.getProgressDialog(this.getContext(), "Espere...");
        progressDialog.show();
        TicketInsert ticketInsert = new TicketInsert();
        ticketInsert.setSolicitanteId(applicantId);
        ticketInsert.setSolicitanteName(applicantName);
        ticketInsert.setCapturistaId(usuarioInfo.getId());
        ticketInsert.setCapturistaName(usuarioInfo.getApellido() + ", " + usuarioInfo.getApellido());
        if (usuarioInfo.getRegionId() >= 1000) {
            ticketInsert.setCategoria(3);
        } else {
            ticketInsert.setCategoria(1);
        }
        ticketInsert.setCorreoManual(false);
        ticketInsert.setDepartamento(departmentName);
        ticketInsert.setDiagnosticoId(diagnosticId);
        ticketInsert.setDistrito(districtName);
        ticketInsert.setFechaApertura(new Date());
        ticketInsert.setLugarId(usuarioInfo.getAlmacenId());
        ticketInsert.setLugarName(String.format("%s - %s", usuarioInfo.getNumero3b(), usuarioInfo.getAlmacenDesc()));
        ticketInsert.setCapturistaName(usuarioInfo.getApellido() + ", " + usuarioInfo.getNombre());
        ticketInsert.setTecnicoId(expertId);
        ticketInsert.setTecnicoName(expertName);
        ticketInsert.setObservaciones(comment.getText().toString());
        ticketInsert.setUsuario(userName);
        ticketInsert.setTienda(storeName);
        ticketInsert.setSolucionRemota(false);
        ticketInsert.setTipo(typeId);
        ticketInsert.setSintomaId(addTicketViewModel.getTypeSymptomId(typeId, symptomId));
        ticketInsert.setDiagnosticoId(addTicketViewModel.getSymptomDiagnostic(symptomId, diagnosticId));
        String legend = "TDE - " + addTicketViewModel.getTypeSymptomName(typeId, symptomId) + " - " + addTicketViewModel.getSymptomDiagnosticName(symptomId, diagnosticId);
        if (legend.length() > 45) {
            legend = legend.substring(0, 45);
        }
        ticketInsert.setLeyenda(legend);

        addTicketViewModel.insertTicket(file, ticketInsert)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<Response<TicketResponse>>() {

                    @Override
                    public void onNext(Response<TicketResponse> ticketResponse) {
                        progressDialog.dismiss();
                        DialogFactory.DialogListener dialogListener = dialog -> {
                            dialog.dismiss();
                            AddTicketFragment.this.getActivity()
                                    .getSupportFragmentManager()
                                    .popBackStack("type", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        };
                        if (ticketResponse.code() == 201) {
                            infoDialog = DialogFactory.getInfoDialog(AddTicketFragment.this.getContext(), "Se generó el ticket correctamente: " + ticketResponse.body().getTicketId(), dialogListener);
                            infoDialog.show();
                        } else {
                            errorDialog = DialogFactory.getErrorDialog(AddTicketFragment.this.getContext(), Tools.parseError(ticketResponse.code()));
                            errorDialog.show();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        progressDialog.dismiss();
                        errorDialog = DialogFactory.getErrorDialog(AddTicketFragment.this.getContext(), Tools.parseError(throwable));
                        errorDialog.show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    private void disableView(View view) {
        view.setEnabled(false);
        view.setClickable(false);
        view.setFocusable(false);
    }

    private void enableView(View view) {
        view.setEnabled(true);
        view.setClickable(true);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
    }

    private void enableButton(View view) {
        view.setEnabled(true);
        view.setClickable(true);
    }

    private void disableButton(View view) {
        view.setEnabled(false);
        view.setClickable(false);
    }

    public void hideKeyboard() {
        Activity activity = getActivity();
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
