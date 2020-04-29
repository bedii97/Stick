package com.example.stick.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stick.Model.NoteModel;
import com.example.stick.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{
    private Context mContext;
    private List<NoteModel> mNoteModelList;
    private OnNoteClickListener mListener;
    private static final String TAG = "NoteAdapter";

    public interface OnNoteClickListener{
        void onNoteClick(int position);
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
        String title = currentNote.getTitle();
        String color = currentNote.getColor();
        holder.tv1.setText(title);
        holder.tv2.setText(color);
    }

    @Override
    public int getItemCount() {
        return mNoteModelList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        //Tanımlama
        TextView  tv1, tv2;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            //init
            tv1 = itemView.findViewById(R.id.note_item_note_name_text_view);
            tv2 = itemView.findViewById(R.id.note_item_date_time_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onNoteClick(position);
                        }
                    }
                }
            });
        }
    }
}
