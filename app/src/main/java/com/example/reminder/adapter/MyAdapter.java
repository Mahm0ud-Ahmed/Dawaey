package com.example.reminder.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder.R;
import com.example.reminder.db.Model;
import com.example.reminder.db.ReminderDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<Model> models;
    Context context;
    int oldSize;

    public MyAdapter(List<Model> models, Context context) {
        this.models = new ArrayList<>();
        this.models = models;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.template_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Model model = models.get(position);
        holder.tv_name.setText(model.getName());
        holder.tv_day.setText(model.getDuration());
        holder.seekBar.setMax(model.getNumber());
        changeColorState(holder, model.getIsActive());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderDbHelper dbHelper = new ReminderDbHelper(context);
                int result = dbHelper.delete(String.valueOf(model.get_id()));
                models.remove(model);
                Toast.makeText(context, "result: " + result, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "reeeeeeeeeeeesult: "+result);
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView tv_name, tv_day, tv_time, tv_state;
        SeekBar seekBar;
        View view;
        LinearLayout layout;
        Button btn_edit, btn_delete;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tv_name = itemView.findViewById(R.id.tv_medicine_name);
            tv_day = itemView.findViewById(R.id.tv_duration);
            tv_time = itemView.findViewById(R.id.tv_next_dose);
            tv_state = itemView.findViewById(R.id.tv_state);
            view = itemView.findViewById(R.id.state);
            layout = itemView.findViewById(R.id.linear_group);
            btn_edit = itemView.findViewById(R.id.tv_edit);
            btn_delete = itemView.findViewById(R.id.tv_delete);
            seekBar = itemView.findViewById(R.id.seekBar);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int originalProgress;

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    originalProgress = seekBar.getProgress();
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int arg1, boolean fromUser) {
                    if(fromUser == true){
                        seekBar.setProgress(originalProgress);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "Loooooooooooooooooooooong", Toast.LENGTH_SHORT).show();
                    if (v.isLongClickable()) {
                        layout.setVisibility(View.VISIBLE);
                    }
                    if (v.isClickable())
                        layout.setVisibility(View.GONE);
                    return false;
                }
            });
        }
    }

    private void changeColorState(ViewHolder holder, int state){
        switch (state){
            case 0:
                holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.Red)));
                holder.tv_state.setText("مغلق");
                break;
            case 1:
                holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.green)));
                holder.tv_state.setText("مفتوح");
                break;
            default:
                holder.view.setVisibility(View.GONE);
                holder.tv_state.setVisibility(View.GONE);
                break;
        }
    }
}
