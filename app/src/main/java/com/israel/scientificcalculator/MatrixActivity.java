package com.israel.scientificcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MatrixActivity extends AppCompatActivity {

    private EditText[][] matET = new EditText[3][3];
    private TextView tvMatrixResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);

        // Bind layout EditText matrix grid elements
        matET[0][0] = findViewById(R.id.m00); matET[0][1] = findViewById(R.id.m01); matET[0][2] = findViewById(R.id.m02);
        matET[1][0] = findViewById(R.id.m10); matET[1][1] = findViewById(R.id.m11); matET[1][2] = findViewById(R.id.m12);
        matET[2][0] = findViewById(R.id.m20); matET[2][1] = findViewById(R.id.m21); matET[2][2] = findViewById(R.id.m22);

        tvMatrixResult = findViewById(R.id.tvMatrixResult);

        Button btnDet = findViewById(R.id.btnDet);
        Button btnTranspose = findViewById(R.id.btnTranspose);
        Button btnInverse = findViewById(R.id.btnInverse);

        btnDet.setOnClickListener(v -> handleMatrixOperation("DET"));
        btnTranspose.setOnClickListener(v -> handleMatrixOperation("TRANS"));
        btnInverse.setOnClickListener(v -> handleMatrixOperation("INV"));
    }

    private double[][] parseMatrixInput() {
        double[][] matrix = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String val = matET[i][j].getText().toString().trim();
                matrix[i][j] = val.isEmpty() ? 0.0 : Double.parseDouble(val);
            }
        }
        return matrix;
    }

    private void handleMatrixOperation(String op) {
        try {
            double[][] A = parseMatrixInput();
            if (op.equals("DET")) {
                double det = getDeterminant(A);
                tvMatrixResult.setText("Determinant det(A) =\n" + det);
            } else if (op.equals("TRANS")) {
                double[][] t = getTranspose(A);
                tvMatrixResult.setText("Transpose Aᵀ =\n" + formatMatrix(t));
            } else if (op.equals("INV")) {
                double det = getDeterminant(A);
                if (det == 0) {
                    tvMatrixResult.setText("Math Error:\nDeterminant is 0. Inverse does not exist!");
                } else {
                    double[][] inv = getInverse(A, det);
                    tvMatrixResult.setText("Inverse A⁻¹ =\n" + formatMatrix(inv));
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please check your inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private double getDeterminant(double[][] m) {
        return m[0][0]*(m[1][1]*m[2][2] - m[1][2]*m[2][1])
                - m[0][1]*(m[1][0]*m[2][2] - m[1][2]*m[2][0])
                + m[0][2]*(m[1][0]*m[2][1] - m[1][1]*m[2][0]);
    }

    private double[][] getTranspose(double[][] m) {
        double[][] t = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                t[j][i] = m[i][j];
            }
        }
        return t;
    }

    private double[][] getInverse(double[][] m, double det) {
        double[][] inv = new double[3][3];
        // Calculate adjugate matrix entries directly for 3x3
        inv[0][0] = (m[1][1]*m[2][2] - m[1][2]*m[2][1]) / det;
        inv[0][1] = (m[0][2]*m[2][1] - m[0][1]*m[2][2]) / det;
        inv[0][2] = (m[0][1]*m[1][2] - m[0][2]*m[1][1]) / det;

        inv[1][0] = (m[1][2]*m[2][0] - m[1][0]*m[2][2]) / det;
        inv[1][1] = (m[0][0]*m[2][2] - m[0][2]*m[2][0]) / det;
        inv[1][2] = (m[0][2]*m[1][0] - m[0][0]*m[1][2]) / det;

        inv[2][0] = (m[1][0]*m[2][1] - m[1][1]*m[2][0]) / det;
        inv[2][1] = (m[0][1]*m[2][0] - m[0][0]*m[2][1]) / det;
        inv[2][2] = (m[0][0]*m[1][1] - m[0][1]*m[1][0]) / det;
        return inv;
    }

    private String formatMatrix(double[][] m) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append("[ ");
            for (int j = 0; j < 3; j++) {
                sb.append(String.format("%.2f", m[i][j])).append("   ");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}