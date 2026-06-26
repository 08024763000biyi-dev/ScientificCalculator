package com.israel.scientificcalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class StatsActivity extends AppCompatActivity {

    private EditText etStatsInput;
    private TextView tvStatsResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        etStatsInput = findViewById(R.id.etStatsInput);
        tvStatsResult = findViewById(R.id.tvStatsResult);

        Button btnMean = findViewById(R.id.btnMean);
        Button btnMedian = findViewById(R.id.btnMedian);
        Button btnVariance = findViewById(R.id.btnVariance);
        Button btnSD = findViewById(R.id.btnSD);

        btnMean.setOnClickListener(v -> processStats("MEAN"));
        btnMedian.setOnClickListener(v -> processStats("MEDIAN"));
        btnVariance.setOnClickListener(v -> processStats("VARIANCE"));
        btnSD.setOnClickListener(v -> processStats("SD"));
    }

    private ArrayList<Double> parseInputs() {
        String inputStr = etStatsInput.getText().toString().trim();
        if (inputStr.isEmpty()) return null;

        // Split inputs safely by commas
        String[] tokens = inputStr.split(",");
        ArrayList<Double> dataSet = new ArrayList<>();

        for (String token : tokens) {
            String cleanToken = token.trim();
            if (!cleanToken.isEmpty()) {
                dataSet.add(Double.parseDouble(cleanToken));
            }
        }
        return dataSet;
    }

    private void processStats(String operationalMode) {
        try {
            ArrayList<Double> data = parseInputs();
            if (data == null || data.isEmpty()) {
                Toast.makeText(this, "Please insert valid numerical data points first", Toast.LENGTH_SHORT).show();
                return;
            }

            int n = data.size();
            double sum = 0;
            for (double val : data) sum += val;
            double mean = sum / n;

            switch (operationalMode) {
                case "MEAN":
                    tvStatsResult.setText(String.format("Sample Size (N): %d\n\nArithmetic Mean (μ):\n%.4f", n, mean));
                    break;

                case "MEDIAN":
                    Collections.sort(data);
                    double median;
                    if (n % 2 == 0) {
                        median = (data.get(n / 2 - 1) + data.get(n / 2)) / 2.0;
                    } else {
                        median = data.get(n / 2);
                    }
                    tvStatsResult.setText(String.format("Sorted Dataset:\n%s\n\nMedian Value:\n%.4f", data.toString(), median));
                    break;

                case "VARIANCE":
                    double varianceSum = 0;
                    for (double val : data) {
                        varianceSum += Math.pow(val - mean, 2);
                    }
                    double variance = varianceSum / (n - 1); // Sample Variance (N-1)
                    tvStatsResult.setText(String.format("Sample Size (N): %d\n\nSample Variance (s²):\n%.4f", n, variance));
                    break;

                case "SD":
                    double sdVarianceSum = 0;
                    for (double val : data) {
                        sdVarianceSum += Math.pow(val - mean, 2);
                    }
                    double sdVariance = sdVarianceSum / (n - 1);
                    double standardDeviation = Math.sqrt(sdVariance);
                    tvStatsResult.setText(String.format("Sample Size (N): %d\n\nStandard Deviation (s):\n%.4f", n, standardDeviation));
                    break;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error parsing numbers. Use commas only!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}