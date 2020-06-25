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
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.Position;
import com.example.mypersonalbudget.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentStatistics extends Fragment {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    float sumEarnings = 0;
    float sumOutflows = 0;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // Crea il grafico a barre.
        Cartesian cartesian = AnyChart.column();

        float totEarnings = sumEarnings();
        float totOutflows = sumOutflows();

        /* Crea un Array list con tutti i valore da assegnare. Puoi teoricamente aggiungere
         * altre variabili come la "y" così da avere altri dati da inserire nella y.
         * Ricordati che per ogni variabile devi anc he dare un valore.
         *
         * Qui poi va anche tutto vuoto, perché i dati saranno presi ed iniettati da Firebase,
         * quelli che vedi ora sono stati inseriti manualmente da me per provare a vedere se
         * funzionasse...
         */
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Earnings", totEarnings));
        data.add(new ValueDataEntry("Outflows", totOutflows));


        // Dove andare ad inserire i dati, quindi qui inietta in sintesi.
        Column column = cartesian.column(data);

        // Estetica del grafico, un .xml di Anychart, ma puoi anche lavorarci dall'XML se vuoi.
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        // Niente di che, animazione quando mostra i dati.
        cartesian.animation(true);

        // Titolo del grafico.
        cartesian.title("Wallet Graphic for the last Period");

        // Stessa roba per Anychart quando chiami un grafico lo devi richiamare qui.
        AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.graficoABarre);

        // Lascialo così questo, anche quelli sopra, ma te li ho commentati per darti una mano haha.
        anyChartView.setChart(cartesian);


    return view;
    }

    public float sumEarnings() {
        Query statisticsEarningsQuery = db.collection("utenti").document(uid).collection("transazioni").whereEqualTo("tipologia", "Earnings");
        statisticsEarningsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        float amount = Float.parseFloat(document.getString("cifra"));
                        sumEarnings += amount;
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.wrong),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return sumEarnings;
    }

    public float sumOutflows() {
        Query statisticsOutflowsQuery = db.collection("utenti").document(uid).collection("transazioni").whereEqualTo("tipologia", "Outflows");
        statisticsOutflowsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        float amount = Float.parseFloat(document.getString("cifra"));
                        sumOutflows -= amount;
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.wrong),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return sumOutflows;
    }

}

