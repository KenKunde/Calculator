package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Math.*

@Suppress("unused")
class MainActivity : AppCompatActivity() {
    private lateinit var workingsTV: TextView
    private lateinit var resultsTV: TextView
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        workingsTV = findViewById(R.id.workingsTV)
        resultsTV = findViewById(R.id.resultsTV)
    }

    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text.toString() == "." && canAddDecimal) {
                workingsTV.append(".")
                canAddDecimal = false
            } else {
                workingsTV.append(view.text.toString())
                canAddOperation = true
            }
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            workingsTV.append(" ")
            workingsTV.append(view.text.toString())
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View) {
        workingsTV.text = ""
        resultsTV.text = ""
    }

    fun backSpaceAction(view: View) {
        val length = workingsTV.text.length
        if (length > 0) {
            workingsTV.text = workingsTV.text.substring(0, length - 1)
        }
    }

    fun equalsAction(view: View) {
        try {
            val expression = workingsTV.text.toString().trim()
            val result = evaluateExpression(expression)
            resultsTV.text = result.toString()
        } catch (e: Exception) {
            resultsTV.text = "Error"
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val segments = expression.split(" ").filter { it.isNotEmpty() }.toMutableList()
        var result: Double = 0.0
        var tempResult: Double = 0.0
        var operator: Char? = null

        for (segment in segments) {
            if (segment.toDoubleOrNull() != null) {
                tempResult = segment.toDouble()
                when (operator) {
                    '+' -> result += tempResult
                    '-' -> result -= tempResult
                    '*' -> result *= tempResult
                    '/' -> result /= tempResult
                    else -> result = tempResult
                }
            } else {
                operator = segment[0]
            }
        }

        return result
    }



}



