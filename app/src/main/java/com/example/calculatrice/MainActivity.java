package com.example.calculatrice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText ecran;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ecran = findViewById(R.id.ecran);
    }

    public void clickFunc(View view) {
        // Enlever les zero au debut si yen a
        String text = ecran.getText().toString();
        if (text.charAt(0) == '0'){
            ecran.setText(text.substring(1));
        }

        switch (view.getId()) {
            case (R.id.btm0):
                ecran.setText(ecran.getText() + "0", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btm1):
                ecran.setText(ecran.getText() + "1", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btm2):
                ecran.setText(ecran.getText() + "2", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btm3):
                ecran.setText(ecran.getText() + "3", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btm4):
                ecran.setText(ecran.getText() + "4", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btm5):
                ecran.setText(ecran.getText() + "5", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btm6):
                ecran.setText(ecran.getText() + "6", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btm7):
                ecran.setText(ecran.getText() + "7", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btm8):
                ecran.setText(ecran.getText() + "8", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btm9):
                ecran.setText(ecran.getText() + "9", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btmPlus):
                ecran.setText(ecran.getText() + "+", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btmMoins):
                ecran.setText(ecran.getText() + "-", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btmMultiplier):
                ecran.setText(ecran.getText() + "*", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btmDivision):
                ecran.setText(ecran.getText() + "/", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btmModulo):

                break;
            case (R.id.btmVirgule):
                ecran.setText(ecran.getText() + ".", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btmRetour):
                String x = ecran.getText().toString();
                if (x.length() > 1) {
                    ecran.setText(x.substring(0, x.length() - 1), TextView.BufferType.EDITABLE);
                }
                else{
                    ecran.setText("0", TextView.BufferType.EDITABLE);
                }
                break;
            case (R.id.btmCarre):
                ecran.setText(ecran.getText() + "sqrt", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btmPlusMoins):

                break;
            case (R.id.btmInverse):
                ecran.setText(ecran.getText() + "inv", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btmC):
                ecran.setText("0", TextView.BufferType.EDITABLE);
                break;
            case (R.id.btmEgale):
                ecran.setText(String.valueOf(eval(ecran.getText().toString())), TextView.BufferType.EDITABLE);
                break;
        }
    }

    public double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("inv")) x = 1 / x;
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
