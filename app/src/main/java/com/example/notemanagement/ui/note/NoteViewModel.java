package com.example.notemanagement.ui.note;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.data.RefreshLiveData;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.model.Note;
import com.example.notemanagement.repository.NoteRepository;

import java.util.List;

import retrofit2.Call;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private RefreshLiveData<List<Note>>  noteList;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init(){
        noteRepository = new NoteRepository();
        noteList = noteRepository.loadAllNotes();
    }

    public void refreshData(){
        noteList.refresh();;
    }

    public LiveData<List<Note>> getNoteList(){
        return noteList;
    }

    public Call<BaseResponse> addNote(Note note){
        return noteRepository.addNote(note);
    }

    public Call<BaseResponse> updateNote(String nName, Note note){
        return noteRepository.updateNote(nName,note);
    }

    public Call<BaseResponse> deleteNote(String name){
        return noteRepository.deleteNote(name);
    }

}