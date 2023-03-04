package com.example.notemanagement.repository;

import com.example.notemanagement.api.APIClient;
import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.data.RefreshLiveData;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.model.Note;
import com.example.notemanagement.service.NoteService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteRepository {
    private NoteService noteService;

    public NoteRepository(){
        noteService = getNoteService();
    }

    public static NoteService getNoteService(){
        return APIClient.getClient().create(NoteService.class);
    }

    public RefreshLiveData<List<Note>> loadAllNotes(){
        final RefreshLiveData<List<Note>> liveData = new RefreshLiveData<>((callback )->{
            noteService.getNote(Constants.TAB_NOTE, DataLocalManager.getEmail()).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    BaseResponse baseResponse = response.body();

                    if (baseResponse != null){
                        List<Note> notes = new ArrayList<>();

                        for (List<String> note : baseResponse.getData()){
                            notes.add(new Note(note.get(0), note.get(1), note.get(2), note.get(3),
                                    note.get(4), note.get(6)));
                        }

                        callback.onDataLoaded(notes);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {

                }
            });
        });

        return liveData;
    }

    public Call<BaseResponse> addNote(Note note){
            return noteService.addNote(Constants.TAB_NOTE,DataLocalManager.getEmail(),
                    note.getName(),note.getPriority(),note.getCategory(), note.getStatus(),
                    note.getPlanDate());
    }

    public Call<BaseResponse> updateNote(String nName, Note note){
        return noteService.updateNote(Constants.TAB_NOTE,DataLocalManager.getEmail(),
                note.getName(),nName,note.getPriority(),note.getCategory(), note.getStatus(),
                note.getPlanDate());
    }

    public Call<BaseResponse> deleteNote(String name){
        return noteService.deleteNote(Constants.TAB_NOTE, DataLocalManager.getEmail(), name);
    }
}
