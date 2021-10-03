package com.example.maha

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var cRoot:ConstraintLayout
    private lateinit var myEdAmount:EditText
    private lateinit var myEdAmount2:EditText
    private lateinit var myBtDeposit :Button
    private lateinit var myBtWithdraw: Button
    private lateinit var messages: ArrayList<String>
    private lateinit var myText: TextView
    private lateinit var myRv : RecyclerView

   private lateinit var sharedPreferences: SharedPreferences


    private var  cAccount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        cAccount = sharedPreferences.getInt("Account", 0).toDouble()

        setContentView(R.layout.activity_main)
        myEdAmount = findViewById(R.id.edAmount)
        myBtDeposit=findViewById(R.id.btDeposit)
        cRoot=findViewById(R.id.clRoot)
        myRv=findViewById(R.id.recyclerView)

        myText=findViewById(R.id.textView)

        myEdAmount2=findViewById(R.id.etAmount2)
        myBtWithdraw=findViewById(R.id.btWithdraw)

        myBtWithdraw.setOnClickListener { withdraw() }

        messages = ArrayList()

        myRv.adapter = MessageAdapter(this, messages)
        myRv.layoutManager = LinearLayoutManager(this)

        myBtDeposit.setOnClickListener { deposit() }
        updateText()


    }

    private fun deposit() {

       val dMoney = myEdAmount.text.toString()

        if(dMoney.isNotEmpty()){
            cAccount = dMoney.toDouble()
            messages.add("Deposit: $dMoney")
            updateText()
           myText.setTextColor(Color.BLACK)
        }

        myEdAmount.text.clear()
        myEdAmount.clearFocus()
      recyclerView.adapter?.notifyDataSetChanged()

    }

    private fun withdraw() {
        val wMoney = myEdAmount2.text.toString()
        val balance =  cAccount - wMoney.toDouble()

        if(cAccount>0) {
            if (balance>0) {

                messages.add("Withdraw : $wMoney ")
                cAccount = balance
                updateText()

            } else  {
                cAccount = balance
                messages.add("Withdraw : $wMoney ")
                messages.add("Negative Balance Fee: 20 ")
                updateText()
                myText.setTextColor(Color.RED)

            }

           myEdAmount2.text.clear()
           myEdAmount2.clearFocus()
           recyclerView.adapter?.notifyDataSetChanged()

        }else if(cAccount<0){
            Toast.makeText(this, "You cant withdraw more ", Toast.LENGTH_LONG).show()}

    }

   private fun updateText(){

       myText.text="current acount : "+cAccount

       with(sharedPreferences.edit()) {
           putInt("cAccount", cAccount.toInt())
           apply()
       }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("cAccount",cAccount)

    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val user = savedInstanceState.getDouble("cAccount",0.0)
        cAccount=user
        myText.text=cAccount.toString()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
       return true
    }
   override  fun onOptionsItemSelected(item: MenuItem): Boolean {
         when(item.itemId) {
             R.id.mN -> {
                 messages.clear()
                 return true
             }
         }
             return super.onOptionsItemSelected(item)
         }

}


