package com.example.notemanagement.ui.priority;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.adapter.DetailAdapter;
import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.FragmentPriorityBinding;
import com.example.notemanagement.dialog.CategoryDialog;
import com.example.notemanagement.dialog.PriorityDialog;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Detail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriorityFragment extends Fragment {

    private FragmentPriorityBinding binding;
    private PriorityViewModel priorityViewModel;
    private DetailAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        priorityViewModel = new ViewModelProvider(getActivity()).get(PriorityViewModel.class);

        binding = FragmentPriorityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DataLocalManager.setCheckEdit(false);

        binding.fragmentPriorityRvPriority.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DetailAdapter(this.getContext());
        binding.fragmentPriorityRvPriority.setHasFixedSize(true);
        binding.fragmentPriorityRvPriority.setAdapter(adapter);

        priorityViewModel.getPriorityList().observe(getViewLifecycleOwner(), priorities -> {
            adapter.setDetailList(priorities);
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
                Detail priority = task.get(position);

                if (direction == ItemTouchHelper.RIGHT){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure ??");
                    builder.setCancelable(false);

                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            priorityViewModel.deletePriority(priority.getName()).enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                    BaseResponse baseResponse = response.body();

                                    if (baseResponse != null){
                                        if (baseResponse.getStatus() == 1){
                                            Toast.makeText(getContext(), "Delete successfully",
                                                    Toast.LENGTH_SHORT).show();
                                            priorityViewModel.refreshData();
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
                            priorityViewModel.refreshData();
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    DataLocalManager.setPriorityName(priority.getName());
                    DataLocalManager.setCheckEdit(true);
                    callPriorityDialog();
                    priorityViewModel.refreshData();
                }

            }
        }).attachToRecyclerView(binding.fragmentPriorityRvPriority);

        binding.fragmentPriorityBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPriorityDialog();
            }
        });


        return root;
    }

    public void callPriorityDialog(){
        DialogFragment priorityDialog = PriorityDialog.newInstance();
        priorityDialog.show(getActivity().getSupportFragmentManager(), "priorityDialog");
    }


    @Override
    public void onResume(){
        super.onResume();
        priorityViewModel.refreshData();
    }

}