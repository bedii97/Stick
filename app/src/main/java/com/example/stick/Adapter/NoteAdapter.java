package com.example.stick.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stick.DB.DatabaseHelper;
import com.example.stick.Model.NoteModel;
import com.example.stick.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{
    private Context mContext;
    private List<NoteModel> mNoteModelList;
    private OnNoteClickListener mListener;
    private static final String TAG = "NoteAdapter";

    public interface OnNoteClickListener{
        void onNoteClick(long id);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener){
        mListener = listener;
    }

    public NoteAdapter(Context mContext, List<NoteModel> noteModelList) {
        this.mContext = mContext;
        this.mNoteModelList = noteModelList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteModel currentNote = mNoteModelList.get(position);
        //Binding
        long id = currentNote.getId();
        int taskCount = new DatabaseHelper(mContext).getTaskCount(id);
        String title = currentNote.getTitle();
        String color = currentNote.getColor();
        holder.noteNameTV.setText(title);
        holder.noteDateTV.setText(color);
        holder.noteCountTV.setText(Integer.toString(taskCount));
    }

    @Override
    public int getItemCount() {
        return mNoteModelList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        //Tanımlama
        TextView  noteNameTV, noteDateTV, noteCountTV;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            //init
            noteNameTV = itemView.findViewById(R.id.note_item_note_name_text_view);
            noteDateTV = itemView.findViewById(R.id.note_item_date_time_text_view);
            noteCountTV = itemView.findViewById(R.id.note_item_task_count_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        long id = mNoteModelList.get(position).getId();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onNoteClick(id);
                        }
                    }
                }
            });
        }
    }
}
