package com.example.notemanagement.ui.status;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notemanagement.adapter.DetailAdapter;
import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.FragmentStatusBinding;

import com.example.notemanagement.R;
import com.example.notemanagement.dialog.CategoryDialog;
import com.example.notemanagement.dialog.StatusDialog;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Detail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusFragment extends Fragment {
    private FragmentStatusBinding binding;
    private StatusViewModel statusViewModel;
    private DetailAdapter adapter;

    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DataLocalManager.setCheckEdit(false);

        binding.fragmentStatusRvStatus.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DetailAdapter(this.getContext());
        binding.fragmentStatusRvStatus.setHasFixedSize(true);
        binding.fragmentStatusRvStatus.setAdapter(adapter);

        statusViewModel = new ViewModelProvider(getActivity()).get(StatusViewModel.class);

        statusViewModel.getStatusList().observe(getViewLifecycleOwner(), statuses ->{
            adapter.setDetailList(statuses);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                List<Detail> task = adapter.getDetailList();
                Detail status = task.get(position);

                if (direction == ItemTouchHelper.RIGHT){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure ??");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            statusViewModel.deleteStatus(status.getName()).enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                    BaseResponse baseResponse = response.body();

                                    if (baseResponse != null){
                                        if (baseResponse.getStatus() == 1){
                                            Toast.makeText(getContext(), "Delete successfully",
                                                    Toast.LENGTH_SHORT).show();
                                            statusViewModel.refreshData();
                                        } else {
                                            Toast.makeText(getContext(), "Can't delete", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {

                                }
                            });
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            statusViewModel.refreshData();
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    DataLocalManager.setStatusName(status.getName());
                    DataLocalManager.setCheckEdit(true);
                    callStatusDialog();
                    statusViewModel.refreshData();
                }
            }
        }).attachToRecyclerView(binding.fragmentStatusRvStatus);

        binding.fragmentStatusBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callStatusDialog();
            }
        });

        return root;
    }

    public void callStatusDialog(){
        DialogFragment statusDialog = StatusDialog.newInstance();
        statusDialog.show(getActivity().getSupportFragmentManager(), "statusDialog");
    }


    @Override
    public void onResume(){
        super.onResume();
        statusViewModel.refreshData();
    }

}