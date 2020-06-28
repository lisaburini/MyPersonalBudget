package com.example.mypersonalbudget.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.mypersonalbudget.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentStatistics extends Fragment {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AnyChartView anyChartView;
    Pie pie;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        anyChartView = view.findViewById(R.id.graficoATorta);
        pie = AnyChart.pie();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        Query statisticsEarningsQuery = db.collection("utenti").document(uid).collection("transazioni");
        statisticsEarningsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    float sumEarnings = 0;
                    float sumOutflows = 0;

                    for (QueryDocumentSnapshot document: task.getResult()) {
                        String category = document.getString("tipologia");
                        float amount = Float.parseFloat(document.getString("cifra"));
                        if(category.equals("Earnings")) {
                            sumEarnings += amount;
                        } else if(category.equals("Outflows")) {
                            sumOutflows += amount;
                        }
                    }

                    float EarningsRound = (float) (Math.round(sumEarnings * 100.0) / 100.0);
                    float OutflowsRound = (float) (Math.round(sumOutflows * 100.0) / 100.0);

                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Earnings", EarningsRound));
                    data.add(new ValueDataEntry("Outflows", OutflowsRound));

                    pie.data(data);

                    anyChartView.setChart(pie);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.wrong),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

