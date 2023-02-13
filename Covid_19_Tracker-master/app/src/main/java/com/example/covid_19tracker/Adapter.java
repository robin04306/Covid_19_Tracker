package com.example.covid_19tracker;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    int m=1;
    Context context;
    List<model_class> country_list;

    public Adapter(Context context, List<model_class> country_list) {
        this.context = context;
        this.country_list = country_list;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view = LayoutInflater.from(context).inflate(R.layout.layout_items , null , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
model_class model_class = country_list.get(position);
        if (m==1)
        {
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(model_class.getCases())));
        }else if (m==2)
        {
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(model_class.getRecovered())));
        }else if (m==3)
        {
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(model_class.getDeaths())));
        }else
        {
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(model_class.getActive())));
        }

        holder.country.setText(model_class.getCountry());


    }

    @Override
    public int getItemCount() {
        return country_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

TextView cases;
TextView country;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cases = itemView.findViewById(R.id.country_case_id);
            country = itemView.findViewById(R.id.country_name_id);
        }
    }

    public void filter(String charText)
    {
        if (charText.equals("cases"))
        {
            m=1;
        } else if (charText.equals("recovered"))
    {
        m=2;
    } else if (charText.equals("deaths"))
    {
        m=3;
    }else
        {
           m=4;
        }
        notifyDataSetChanged();
    }
}
