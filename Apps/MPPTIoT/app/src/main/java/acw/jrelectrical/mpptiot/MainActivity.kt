package acw.jrelectrical.mpptiot

import acw.jrelectrical.mpptiot.databinding.ActivityMainBinding
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseListener()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun databaseListener() {
        database = FirebaseDatabase.getInstance().getReference()
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val powerInput = snapshot.child("telemetry/powerInput").value
                binding.powerInput.setText("$powerInput W") // Menambahkan satuan watt

                val batteryPercent = snapshot.child("telemetry/batteryPercent").value
                binding.batteryPercent.setText("$batteryPercent %") // Menambahkan satuan persen

                val voltageInput = snapshot.child("telemetry/voltageInput").value
                binding.voltageInput.setText("$voltageInput V") // Menambahkan satuan volt

                val currentInput = snapshot.child("telemetry/currentInput").value
                binding.currentInput.setText("$currentInput A") // Menambahkan satuan ampere

                val voltageOutput = snapshot.child("telemetry/voltageOutput").value
                binding.voltageOutput.setText("$voltageOutput V") // Menambahkan satuan volt

                val currentOutput = snapshot.child("telemetry/currentOutput").value
                binding.currentOutput.setText("$currentOutput A") // Menambahkan satuan ampere

                val temperature = snapshot.child("telemetry/temperature").value
                binding.temperature.setText("$temperature °C") // Menambahkan satuan derajat Celsius

                val electricalPrice = snapshot.child("telemetry/electricalPrice").value
                binding.electricalPrice.setText("Rp. $electricalPrice") // Menambahkan satuan kilowatt-hour

                val LED1 = snapshot.child("telemetry/LED1").value
                binding.LED1.isChecked = LED1.toString().equals("1")

                val LED2 = snapshot.child("telemetry/LED2").value
                binding.LED2.isChecked = LED2.toString().equals("1")

                val LED3 = snapshot.child("telemetry/LED3").value
                binding.LED3.isChecked = LED3.toString().equals("1")

                val LED4 = snapshot.child("telemetry/LED4").value
                binding.LED4.isChecked = LED4.toString().equals("1")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "failed to Read", Toast.LENGTH_SHORT).show()
            }
        }
        database.addValueEventListener(postListener)
    }
}