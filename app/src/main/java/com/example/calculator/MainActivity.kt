package com.example.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var computationTV: TextView
    private lateinit var resultsTV: TextView
    private var canAddOperation = false
    private var canAddDecimal = true

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        computationTV = findViewById(R.id.computationTV)
        resultsTV = findViewById(R.id.resultsTV)
    }

    fun numberAction(view: View) {
        if (view is Button) {
            val text = view.text.toString()
            if (text == "." && canAddDecimal) {
                computationTV.append(".")
                canAddDecimal = false
            } else if (text != ".") {
                computationTV.append(text)
                canAddOperation = true
            }
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            computationTV.append(" ${view.text} ")
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View) {
        computationTV.text = ""
        resultsTV.text = ""
        canAddOperation = false
        canAddDecimal = true
    }

    fun backSpaceAction(view: View) {
        val length = computationTV.text.length
        if (length > 0) {
            computationTV.text = computationTV.text.substring(0, length - 1)
            val lastChar = computationTV.text.lastOrNull()
            canAddOperation = lastChar != null && lastChar.isDigit()
            canAddDecimal = !computationTV.text.contains(".")
        }
    }

    fun equalsAction(view: View) {
        try {
            val expression = computationTV.text.toString().trim()
            if (expression.isNotEmpty()) {
                val result = evaluateExpression(expression)
                resultsTV.text = result.toString()
            }
        } catch (e: Exception) {
            resultsTV.text = "Error"
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val tokens = expression.split(" ").filter { it.isNotEmpty() }
        val values = mutableListOf<Double>()
        val operators = mutableListOf<String>()

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> values.add(token.toDouble())
                token in listOf("+", "-", "*", "/") -> operators.add(token)
            }
        }

        // Perform multiplication and division first
        var i = 0
        while (i < operators.size) {
            if (operators[i] == "*" || operators[i] == "/") {
                val left = values[i]
                val right = values[i + 1]
                val operator = operators[i]
                val result = when (operator) {
                    "*" -> left * right
                    "/" -> left / right
                    else -> throw IllegalArgumentException("Invalid operator")
                }
                values[i] = result
                values.removeAt(i + 1)
                operators.removeAt(i)
                // Do not increment `i` because we've modified the list size
            } else {
                i++
            }
        }

        // Perform addition and subtraction
        i = 0
        while (i < operators.size) {
            val left = values[i]
            val right = values[i + 1]
            val operator = operators[i]
            val result = when (operator) {
                "+" -> left + right
                "-" -> left - right
                else -> throw IllegalArgumentException("Invalid operator")
            }
            values[i] = result
            values.removeAt(i + 1)
            operators.removeAt(i)
            // Do not increment `i` because we've modified the list size
        }

        return values[0]
    }
}
