package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.telephony.RadioAccessSpecifier;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.w3c.dom.Text;

import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView m_today_total, m_total, m_active, m_today_active, m_recovered, m_today_recovered, m_deaths, m_today_deaths;
    CountryCodePicker countryCodePicker;

    String country;
    TextView m_filter;
    Spinner spinner;
    String [] types = {"cases", "deaths", "recovered" , "active"};
    private List<model_class> model_classes;
    private List<model_class> model_classes2;
    PieChart m_piechart;
    private RecyclerView recyclerView;
    com.example.covid_19tracker.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryCodePicker = findViewById(R.id.ccp_id);
        m_today_total = findViewById(R.id.today_total_case_id);
        m_total = findViewById(R.id.total_case_id);
        m_active = findViewById(R.id.active_case_id);
        m_today_active = findViewById(R.id.today_active_case_id);
        m_recovered = findViewById(R.id.recover_id);
        m_today_recovered = findViewById(R.id.today_recover_case_id);
        m_deaths = findViewById(R.id.death_case_id);
        m_today_deaths = findViewById(R.id.today_death_case_id);
        m_piechart = findViewById(R.id.piechart_id);
        spinner = findViewById(R.id.spinner_id);
        m_filter = findViewById(R.id.filter_id);
        recyclerView = findViewById(R.id.recycler_view);
       model_classes2 = new ArrayList<>();
       model_classes = new ArrayList<>();

       spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        //fetching data
        Api_utilities.getApiInterface().getcountrydata().enqueue(new Callback<List<model_class>>() {
            @Override
            public void onResponse(Call<List<model_class>> call, Response<List<model_class>> response) {
                model_classes2.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<model_class>> call, Throwable t) {

            }
        });

       adapter= new Adapter(getApplicationContext(), model_classes2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        //how to show data

        countryCodePicker.setAutoDetectedCountry(true);
        country = countryCodePicker.getSelectedCountryName();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country= countryCodePicker.getSelectedCountryName();
                fetch_data();
            }
        });
        fetch_data();

        



    }

    private void fetch_data() {

        Api_utilities.getApiInterface().getcountrydata().enqueue(new Callback<List<model_class>>() {
            @Override
            public void onResponse(Call<List<model_class>> call, Response<List<model_class>> response) {
                model_classes.addAll(response.body());
                for (int i=0; i<model_classes.size();i++)
                {
                    if (model_classes.get(i).getCountry().equals(country))
                    {
                        m_active.setText((model_classes.get(i).getActive()));
                        m_today_deaths.setText((model_classes.get(i).getTodayDeaths()));
                        m_today_recovered.setText((model_classes.get(i).getTodayRecovered()));
                        m_today_total.setText((model_classes.get(i).getTodayCases()));
                        m_total.setText((model_classes.get(i).getCases()));
                        m_deaths.setText((model_classes.get(i).getDeaths()));
                        m_recovered.setText((model_classes.get(i).getRecovered()));


                      int active  , total , recovered , death;

                      active = Integer.parseInt(model_classes.get(i).getActive());
                        total = Integer.parseInt(model_classes.get(i).getCases());
                        recovered = Integer.parseInt(model_classes.get(i).getRecovered());
                        death = Integer.parseInt(model_classes.get(i).getDeaths());

                        update_graph(active, total, recovered, death);


                       // update_graph(model_classes.get(i).getCases(),model_classes.get(i).getActive(),model_classes.get(i).getRecovered(),model_classes.get(i).getDeaths());


                    }
                }
            }

            @Override
            public void onFailure(Call<List<model_class>> call, Throwable t) {

            }
        });

    }

    private void update_graph(int active, int total, int recovered, int death) {

        m_piechart.clearChart();
        m_piechart.addPieSlice(new PieModel("Confirm" ,  total, Color.parseColor("Blue")));
        m_piechart.addPieSlice(new PieModel("Active" ,  total, Color.parseColor("Yellow")));
        m_piechart.addPieSlice(new PieModel("Recovered" ,  total, Color.parseColor("Green")));
        m_piechart.addPieSlice(new PieModel("Deaths" ,  total, Color.parseColor("Red")));
        m_piechart.startAnimation();


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        String item =types[position];
        m_filter.setText(item);
        adapter.filter(item);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}