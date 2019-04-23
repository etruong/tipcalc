package edu.uw.ischool.elisat15.tipcalc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.Log

import android.widget.Toast
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast.LENGTH_LONG
import java.math.BigDecimal
import java.math.RoundingMode
import android.widget.RadioButton




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.tip_btn)
        val input = findViewById<TextInputEditText>(R.id.amount)
        val tipPercent = findViewById<RadioGroup>(R.id.tip_value)

        // Default values when starting application:
        var percentage = 0.15
        var percentageText = "15%"
        button.isEnabled = false // Disable button

        // Change listener for amount user input
        input.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {
                button.isEnabled = s.toString() != "$"
                if (!s.toString().startsWith("$"))
                    input.setText("$");

                if (s.toString().indexOf(".") != -1 && s.substring(s.indexOf(".") + 1).length > 2)
                    input.setText(s.substring(0, s.indexOf(".") + 3))
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       count: Int, after: Int) {}
        })

        // When button is clicked calculate tip value of given amount
        button.setOnClickListener {
            val amount = input.text.toString()

            val amountNum = amount.substring(1).toBigDecimal().setScale(2, RoundingMode.HALF_UP)

            val dollars = amount.substring(1).toBigDecimal().setScale(0, RoundingMode.FLOOR)
            val cents = amountNum.times(BigDecimal(100)).rem( BigDecimal(100))

            val convertAmountCents = dollars.times(BigDecimal(100)).plus(cents)

            val tipInCents = convertAmountCents.times(BigDecimal(percentage))
            val tipDollar = tipInCents.divide(BigDecimal(100)).setScale(2, RoundingMode.HALF_UP)
            val tipString = "Your total is: $${amountNum}, \n A ${percentageText} tip would be: $${tipDollar.toString()}"

            // Output values from above:
            //
            // Log.d("MainActivity", "Dollars of Amount: " + dollars)
            // Log.d("MainActivity", "Cents of Amount: " + cents)
            // Log.d("MainActivity", "Convert Amount: " + convertAmountCents)
            // Log.d("MainActivity", "Tip Amount: " + tipDollar)
            // Log.d("MainActivity", tipPercent.toString())

            val duration = LENGTH_LONG

            val toast = Toast.makeText(this, tipString, duration)
            toast.show()
        }

        tipPercent.setOnCheckedChangeListener { group, checkedId ->

            // This will get the radiobutton that has changed in its check state
            val checkedRadioButton = group.findViewById<RadioButton>(checkedId)

            // This puts the value (true/false) into the variable
            val isChecked = checkedRadioButton.isChecked

            // If the radiobutton that has changed in check state is now checked...
            if (isChecked) {

                percentageText = checkedRadioButton.text.toString()
                if (checkedRadioButton.text == "10%")
                    percentage = 0.10
                else if (checkedRadioButton.text == "15%")
                    percentage = 0.15
                else if (checkedRadioButton.text == "18%")
                    percentage = 0.18
                else // (checkedRadioButton.text == "18%")
                    percentage = 0.20
            }
        }

    }
}
