package com.yashverma.oldeage.adapter;

import android.content.Context;
<<<<<<< HEAD
import android.content.Intent;
=======
>>>>>>> origin/master
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
<<<<<<< HEAD
=======
import android.widget.Toast;
>>>>>>> origin/master

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< HEAD
import com.yashverma.oldeage.FullDetails.Blank2;
=======
>>>>>>> origin/master
import com.yashverma.oldeage.R;
import com.yashverma.oldeage.User;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;
    ArrayList<User> List;
<<<<<<< HEAD
    private MyViewHolder.OnRecyclierViewClick onclickLis;
    public Adapter(Context c, ArrayList<User> l, MyViewHolder.OnRecyclierViewClick oncl) {
        this.context = c;
        this.List = l;
        this.onclickLis=oncl;
=======

    public Adapter(Context c, ArrayList<User> l) {
        this.context = c;
        this.List = l;
>>>>>>> origin/master
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.guestcard,parent,false);
<<<<<<< HEAD
        return new MyViewHolder(v,onclickLis);
=======
        return new MyViewHolder(v);
>>>>>>> origin/master
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.guestId.setText(List.get(position).getGuestid());
        holder.guestName.setText(List.get(position).getGuestName());
<<<<<<< HEAD

=======
>>>>>>> origin/master
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

<<<<<<< HEAD
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView guestId,guestName;
        Button update, Delete;
        OnRecyclierViewClick onrcl;

        public MyViewHolder(@NonNull View itemView,OnRecyclierViewClick onrci) {
=======
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView guestId,guestName;
        Button update, Delete;
        public MyViewHolder(@NonNull View itemView) {
>>>>>>> origin/master
            super(itemView);
            guestId=(TextView) itemView.findViewById(R.id.fetchedId);
            guestName=(TextView) itemView.findViewById(R.id.fetchedName);
            update=itemView.findViewById(R.id.Update);
            Delete=itemView.findViewById(R.id.Delete);
<<<<<<< HEAD
            this.onrcl=onrci;
            itemView.setOnClickListener(this);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context= itemView.getContext();
                    context.startActivity(new Intent(context,Blank2.class));
=======
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","Update click");
>>>>>>> origin/master
                }
            });
            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","Delete click");
                }
            });
        }
<<<<<<< HEAD

        @Override
        public void onClick(View view) {
            onrcl.Onclick(getAdapterPosition());
        }

        public  interface  OnRecyclierViewClick{
           void Onclick(int position);
=======
        public  interface  OnRecyclierViewClick{
            void Onclick(int position);
>>>>>>> origin/master
        }
    }
}
