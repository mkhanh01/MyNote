package com.example.notemanagement.ui.category;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.notemanagement.databinding.FragmentCategoryBinding;
import com.example.notemanagement.dialog.CategoryDialog;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Detail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private CategoryViewModel categoryViewModel;
    private DetailAdapter detailAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DataLocalManager.setCheckEdit(false);

        binding.fragmentCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        detailAdapter = new DetailAdapter(this.getContext());
        binding.fragmentCategoryRecyclerView.setHasFixedSize(true);
        binding.fragmentCategoryRecyclerView.setAdapter(detailAdapter);

        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);

        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), categories -> {
            detailAdapter.setDetailList(categories);
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
                List<Detail> task = detailAdapter.getDetailList();
                Detail category = task.get(position);

                if (direction == ItemTouchHelper.RIGHT){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure ??");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            categoryViewModel.deleteCategory(category.getName()).enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                    BaseResponse baseResponse = response.body();

                                    if (baseResponse != null){
                                        if (baseResponse.getStatus() == 1){
                                            Toast.makeText(getContext(), "Delete successfully",
                                                    Toast.LENGTH_SHORT).show();
                                            categoryViewModel.refreshData();
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
                            categoryViewModel.refreshData();
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else{
                    DataLocalManager.setCategoryName(category.getName());
                    DataLocalManager.setCheckEdit(true);
                    callCategoryDialog();
                    categoryViewModel.refreshData();
                }
            }
        }).attachToRecyclerView(binding.fragmentCategoryRecyclerView);


        binding.fragmentCategoryBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCategoryDialog();
            }
        });

        return root;
    }

    public void callCategoryDialog(){
        DialogFragment categoryDialog = CategoryDialog.newInstance();
        categoryDialog.show(getActivity().getSupportFragmentManager(), "catagoryDialog");
    }


    @Override
    public void onResume(){
        super.onResume();
        categoryViewModel.refreshData();
    }
}