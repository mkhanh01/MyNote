package com.example.notemanagement.ui.note;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notemanagement.R;
import com.example.notemanagement.adapter.NoteAdapter;

import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.FragmentNoteBinding;
import com.example.notemanagement.dialog.NoteDialog;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteFragment extends Fragment {

    private FragmentNoteBinding binding;
    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;

    public static NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNoteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DataLocalManager.setCheckEdit(false);

        binding.fragmentNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NoteAdapter(this.getContext());
        binding.fragmentNoteRecyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(getActivity()).get(NoteViewModel.class);

        noteViewModel.getNoteList().observe(getViewLifecycleOwner(), notes -> {
            adapter.setNoteList(notes);
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
                int position = viewHolder.getPosition();
                List<Note> task = adapter.getNoteList();
                Note note = task.get(position);

                if (direction == ItemTouchHelper.RIGHT){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure ??");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            noteViewModel.deleteNote(note.getName()).enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                    if (response.body() != null){
                                        if (response.body().getStatus() == 1){
                                            Toast.makeText(getContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
                                            noteViewModel.refreshData();
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
                            noteViewModel.refreshData();
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    DataLocalManager.setNoteName(note.getName());
                    DataLocalManager.setPlanDate(note.getPlanDate());
                    DataLocalManager.setCheckEdit(true);
                    callNoteDialog();
                    noteViewModel.refreshData();
                }
            }
        }).attachToRecyclerView(binding.fragmentNoteRecyclerView);

        binding.fragmentNoteBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNoteDialog();
                noteViewModel.refreshData();
            }
        });


        return root;
    }

    public void callNoteDialog(){
        DialogFragment dialogFragment = NoteDialog.newInstance();
        dialogFragment.show(getActivity().getSupportFragmentManager(),"noteDialog");
    }

    @Override
    public void onResume(){
        super.onResume();
        noteViewModel.refreshData();
    }

}