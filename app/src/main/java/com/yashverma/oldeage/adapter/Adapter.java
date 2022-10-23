package com.yashverma.oldeage.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yashverma.oldeage.R;
import com.yashverma.oldeage.User;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;
    ArrayList<User> List;

    public Adapter(Context c, ArrayList<User> l) {
        this.context = c;
        this.List = l;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.guestcard,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.guestId.setText(List.get(position).getGuestid());
        holder.guestName.setText(List.get(position).getGuestName());
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView guestId,guestName;
        Button update, Delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            guestId=(TextView) itemView.findViewById(R.id.fetchedId);
            guestName=(TextView) itemView.findViewById(R.id.fetchedName);
            update=itemView.findViewById(R.id.Update);
            Delete=itemView.findViewById(R.id.Delete);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","Update click");
                }
            });
            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","Delete click");
                }
            });
        }
        public  interface  OnRecyclierViewClick{
            void Onclick(int position);
        }
    }
}
