package com.example.notemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.R;
import com.example.notemanagement.model.Note;

import org.w3c.dom.Text;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> noteList;
    private Context context;

    public NoteAdapter(Context context){
        this.context = context;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_note, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        Note note = noteList.get(position);

        if (note == null)
            return;

        holder.tvName.setText("Name: " + note.getName());
        holder.tvPriority.setText("Priority: " + note.getPriority());
        holder.tvCategory.setText("Category: " + note.getCategory());
        holder.tvStatus.setText("Status: " + note.getStatus());
        holder.tvPlanDate.setText("Plan Date: " + note.getPlanDate());
        holder.tvCreatedDate.setText("Created Date: " + note.getCreatedDate());
    }

    @Override
    public int getItemCount() {
        if (noteList != null)
            return noteList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private TextView tvPriority;
        private TextView tvCategory;
        private TextView tvStatus;
        private TextView tvPlanDate;
        private TextView tvCreatedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.item_note_name);
            tvPriority = itemView.findViewById(R.id.item_note_priority);
            tvCategory = itemView.findViewById(R.id.item_note_category);
            tvStatus = itemView.findViewById(R.id.item_note_status);
            tvPlanDate = itemView.findViewById(R.id.item_note_planDate);
            tvCreatedDate = itemView.findViewById(R.id.item_note_createdDate);
        }
    }
}
