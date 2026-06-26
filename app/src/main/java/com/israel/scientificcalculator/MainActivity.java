package com.israel.scientificcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvDisplay, tvExpression;
    private StringBuilder currentInput = new StringBuilder();

    private double val1 = Double.NaN;
    private int arithmeticOp = -1;
    private int scientificOp = -1;

    private boolean isInverseMode = false;
    private boolean isDegreeMode = true;
    private boolean isNewCalculation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        tvExpression = findViewById(R.id.tvExpression);

        int[] ids = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnDot, R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv, R.id.btnEqual,
                R.id.btnClear, R.id.btnDel, R.id.btnSin, R.id.btnCos, R.id.btnTan,
                R.id.btnSinh, R.id.btnCosh, R.id.btnTanh, R.id.btnLog, R.id.btnLn,
                R.id.btnPi, R.id.btnE, R.id.btn2nd, R.id.btnDegRad, R.id.btnSqrt,
                R.id.btnFact, R.id.btnInv, R.id.btnPercent,
                R.id.btnMatrixMode, R.id.btnStatsMode
        };

        for (int id : ids) {
            View b = findViewById(id);
            if (b != null) b.setOnClickListener(this);
        }
        updateLabels();
    }

    private void updateLabels() {
        ((Button) findViewById(R.id.btnSin)).setText(isInverseMode ? "sin⁻¹" : "sin");
        ((Button) findViewById(R.id.btnCos)).setText(isInverseMode ? "cos⁻¹" : "cos");
        ((Button) findViewById(R.id.btnTan)).setText(isInverseMode ? "tan⁻¹" : "tan");
        ((Button) findViewById(R.id.btnSinh)).setText(isInverseMode ? "sinh⁻¹" : "sinh");
        ((Button) findViewById(R.id.btnCosh)).setText(isInverseMode ? "cosh⁻¹" : "cosh");
        ((Button) findViewById(R.id.btnTanh)).setText(isInverseMode ? "tanh⁻¹" : "tanh");
        ((Button) findViewById(R.id.btnDegRad)).setText(isDegreeMode ? "Deg" : "Rad");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // Navigation
        if (id == R.id.btnMatrixMode) {
            startActivity(new Intent(MainActivity.this, MatrixActivity.class));
            return;
        }
        if (id == R.id.btnStatsMode) {
            startActivity(new Intent(MainActivity.this, StatsActivity.class));
            return;
        }

        // Toggles & Clear
        if (id == R.id.btn2nd) { isInverseMode = !isInverseMode; updateLabels(); return; }
        if (id == R.id.btnDegRad) { isDegreeMode = !isDegreeMode; updateLabels(); return; }
        if (id == R.id.btnClear) { clear(); return; }
        if (id == R.id.btnDel) { delete(); return; }

        // --- FRESH START CHECK ---
        // If we just calculated a result, any new input clears the display
        if (isNewCalculation) {
            currentInput.setLength(0);
            tvDisplay.setText("0");
            isNewCalculation = false;
        }

        // Numbers & Dot
        if (id >= R.id.btn0 && id <= R.id.btn9 || id == R.id.btnDot) {
            currentInput.append(((Button) v).getText());
            tvDisplay.setText(currentInput.toString());
            return;
        }

        // Constants (Clear current input first)
        if (id == R.id.btnPi) { currentInput.setLength(0); currentInput.append(Math.PI); tvDisplay.setText("π"); return; }
        if (id == R.id.btnE) { currentInput.setLength(0); currentInput.append(Math.E); tvDisplay.setText("e"); return; }

        // Arithmetic
        if (id == R.id.btnAdd || id == R.id.btnSub || id == R.id.btnMul || id == R.id.btnDiv) {
            if (currentInput.length() > 0) val1 = Double.parseDouble(currentInput.toString());
            arithmeticOp = id;
            tvExpression.setText(((Button) v).getText());
            currentInput.setLength(0);
            return;
        }

        // Scientific
        if (id == R.id.btnSin || id == R.id.btnCos || id == R.id.btnTan || id == R.id.btnSinh ||
                id == R.id.btnCosh || id == R.id.btnTanh || id == R.id.btnLog || id == R.id.btnLn ||
                id == R.id.btnSqrt || id == R.id.btnFact || id == R.id.btnInv || id == R.id.btnPercent) {
            scientificOp = id;
            tvExpression.setText(((Button) v).getText() + "(");
            return;
        }

        if (id == R.id.btnEqual) { calculate(); }
    }

    private void calculate() {
        double input = currentInput.length() > 0 ? Double.parseDouble(currentInput.toString()) : 0;
        double result = 0;

        if (arithmeticOp != -1) {
            if (arithmeticOp == R.id.btnAdd) result = val1 + input;
            else if (arithmeticOp == R.id.btnSub) result = val1 - input;
            else if (arithmeticOp == R.id.btnMul) result = val1 * input;
            else if (arithmeticOp == R.id.btnDiv) result = val1 / input;
        }
        else if (scientificOp != -1) {
            if (scientificOp == R.id.btnSin) result = isInverseMode ? Math.asin(input) : Math.sin(isDegreeMode ? Math.toRadians(input) : input);
            else if (scientificOp == R.id.btnCos) result = isInverseMode ? Math.acos(input) : Math.cos(isDegreeMode ? Math.toRadians(input) : input);
            else if (scientificOp == R.id.btnTan) result = isInverseMode ? Math.atan(input) : Math.tan(isDegreeMode ? Math.toRadians(input) : input);
            else if (scientificOp == R.id.btnSinh) result = isInverseMode ? Math.log(input + Math.sqrt(input * input + 1)) : Math.sinh(input);
            else if (scientificOp == R.id.btnCosh) result = isInverseMode ? Math.log(input + Math.sqrt(input * input - 1)) : Math.cosh(input);
            else if (scientificOp == R.id.btnTanh) result = isInverseMode ? 0.5 * Math.log((1 + input) / (1 - input)) : Math.tanh(input);
            else if (scientificOp == R.id.btnLog) result = Math.log10(input);
            else if (scientificOp == R.id.btnLn) result = Math.log(input);
            else if (scientificOp == R.id.btnSqrt) result = Math.sqrt(input);
            else if (scientificOp == R.id.btnFact) result = factorial((int) input);
            else if (scientificOp == R.id.btnInv) result = 1.0 / input;
            else if (scientificOp == R.id.btnPercent) result = input / 100.0;
        } else {
            result = input;
        }

        tvDisplay.setText(String.valueOf(result));
        tvExpression.setText("");
        currentInput.setLength(0);
        currentInput.append(result);
        val1 = Double.NaN;
        arithmeticOp = -1;
        scientificOp = -1;

        // This triggers the FRESH START logic in the next click
        isNewCalculation = true;
    }

    private double factorial(int n) {
        if (n < 0) return 0;
        double res = 1;
        for (int i = 2; i <= n; i++) res *= i;
        return res;
    }

    private void clear() {
        currentInput.setLength(0);
        val1 = Double.NaN;
        arithmeticOp = -1;
        scientificOp = -1;
        isNewCalculation = false;
        tvDisplay.setText("0");
        tvExpression.setText("");
    }

    private void delete() {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            tvDisplay.setText(currentInput.length() == 0 ? "0" : currentInput.toString());
        }
    }
}