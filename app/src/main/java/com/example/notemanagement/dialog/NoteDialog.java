package com.example.notemanagement.dialog;


import com.example.notemanagement.R;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.DialogAddNoteBinding;

import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.model.Note;
import com.example.notemanagement.ui.category.CategoryViewModel;
import com.example.notemanagement.ui.note.NoteViewModel;
import com.example.notemanagement.ui.priority.PriorityViewModel;
import com.example.notemanagement.ui.status.StatusViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteDialog extends DialogFragment implements View.OnClickListener {

    private final String Add = "Add";
    private final String Edit = "Edit";

    private DialogAddNoteBinding binding;

    private CategoryViewModel categoryViewModel;
    private PriorityViewModel priorityViewModel;
    private StatusViewModel statusViewModel;
    private NoteViewModel noteViewModel;

    public static NoteDialog newInstance(){
        return new NoteDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState){
        binding = DialogAddNoteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        noteViewModel = new ViewModelProvider(getActivity()).get(NoteViewModel.class);
        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        priorityViewModel = new ViewModelProvider(getActivity()).get(PriorityViewModel.class);
        statusViewModel = new ViewModelProvider(getActivity()).get(StatusViewModel.class);

        categoryViewModel.refreshData();
        priorityViewModel.refreshData();
        statusViewModel.refreshData();
        noteViewModel.refreshData();

        setCancelable(false);

        binding.dialogAddNoteTvDate.setOnClickListener(this);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    public void init(){

        getSpinner();

        if (DataLocalManager.getCheckEdit()){
            binding.dialogAddNoteBtnAdd.setText(Edit);
            binding.dialogAddNoteEtNoteName.setText(DataLocalManager.getNoteName());
            binding.dialogAddNoteTvDate.setText(DataLocalManager.getPlanDate());

            DataLocalManager.setCategoryName("");
            DataLocalManager.setPriorityName("");
            DataLocalManager.setStatusName("");
            DataLocalManager.setPlanDate("");
            DataLocalManager.setCheckEdit(false);
        }

        binding.dialogAddNoteBtnPickDate.setOnClickListener(this);
        binding.dialogAddNoteBtnAdd.setOnClickListener(this);
        binding.dialogAddNoteBtnClose.setOnClickListener(this);

    }

    private void getSpinner(){

        final List<String> priorityName = new ArrayList<>();
        final List<String> categoryName = new ArrayList<>();
        final List<String> statusName = new ArrayList<>();

        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), categories ->{
            for (int i = 0; i < categories.size(); i++) {
                categoryName.add(categories.get(i).getName());
                binding.dialogAddNoteTvCategory.setText(categoryName.get(0), false);

            }
        });

        priorityViewModel.getPriorityList().observe(getViewLifecycleOwner(), priorities -> {
            for (int i = 0; i < priorities.size(); i++) {
                priorityName.add(priorities.get(i).getName());
                binding.dialogAddNoteTvPriority.setText(priorityName.get(0), false);
            }
        });

        statusViewModel.getStatusList().observe(getViewLifecycleOwner(), statuses -> {
            for (int i = 0; i < statuses.size(); i++) {
                statusName.add(statuses.get(i).getName());
                binding.dialogAddNoteTvStatus.setText(statusName.get(0), false);
            }
        });

        ArrayAdapter<String> adapterCategory = new ArrayAdapter(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categoryName);
        binding.dialogAddNoteTvCategory.setAdapter(adapterCategory);

        ArrayAdapter<String> adapterPriority = new ArrayAdapter(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item ,priorityName);
        binding.dialogAddNoteTvPriority.setAdapter(adapterPriority);

        ArrayAdapter<String> adapterStatus = new ArrayAdapter(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, statusName);
        binding.dialogAddNoteTvStatus.setAdapter(adapterStatus);


    }

    public void DatePicker(){
        Calendar calendar = Calendar.getInstance();

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        binding.dialogAddNoteBtnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = day + "/" + month + "/" + year ;
                        binding.dialogAddNoteTvDate.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
            }
        });
    }

    public boolean checkInput(){

        binding.dialogAddNoteEtNoteName.setError(null);
        binding.dialogAddNoteTvDate.setError(null);

        if (TextUtils.isEmpty(binding.dialogAddNoteEtNoteName.getText().toString().trim())){
            binding.dialogAddNoteEtNoteName.setError("Require");
            return false;
        }

        if (TextUtils.isEmpty(binding.dialogAddNoteTvDate.getText().toString().trim())){
            binding.dialogAddNoteTvDate.setError("Require");
            return false;
        }

        return true;
    }

    private void addNote(){
        if (!checkInput())
            return;

        final Note note = note();

        noteViewModel.addNote(note).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null){
                    if (response.body().getStatus() == Constants.SUCCESSFULLY){
                        Toast.makeText(getContext(), getString(R.string.add_success),
                                Toast.LENGTH_LONG).show();
                        noteViewModel.refreshData();
                        dismiss();
                    } else {
                        binding.dialogAddNoteEtNoteName.setError(getString(R.string.invalid_name));
                        Toast.makeText(getContext(),getString(R.string.add_fail), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    private void editNote(){
        if (!checkInput())
            return;

        final Note note = note();

        noteViewModel.updateNote(DataLocalManager.getNoteName(), note)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            if (response.body().getStatus() == Constants.SUCCESSFULLY){
                                Toast.makeText(getContext(),getString(R.string.edit_success),
                                        Toast.LENGTH_LONG).show();
                                noteViewModel.refreshData();
                                dismiss();
                            } else {
                                Toast.makeText(getContext(),getString(R.string.edit_fail), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                    }
                });
    }

    private Note note(){
        return new Note(binding.dialogAddNoteEtNoteName.getText().toString(),
                binding.dialogAddNoteTvCategory.getText().toString(),
                binding.dialogAddNoteTvPriority.getText().toString(),
                binding.dialogAddNoteTvStatus.getText().toString(),
                binding.dialogAddNoteTvDate.getText().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_add_note_btn_add:
                if (TextUtils.equals(binding.dialogAddNoteBtnAdd.getText().toString(),Add)){
                    addNote();
                } else
                    editNote();
                break;

            case R.id.dialog_add_note_btn_close:
                dismiss();
                break;
            case R.id.dialog_add_note_btn_pick_date:
                DatePicker();
                break;
        }
    }
}
