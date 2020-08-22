package com.example.findit;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListActivity extends AppCompatActivity {
    ProgressBar mprogressBar, mprogressBar_suhu, getMprogressBar_kelembaban_tanah, progress_kelembaban_tanah;
    TextView txt_kelembabann, txt_suhu, txt_kelembaban_tanah, txt_nilai_kelembaban_tanah;

    String value, string_nilai_kelembaban, string_nilai_suhu;
    int humidity_nilai = 0;
    int temperatur_nilai = 0;
    int nilai_kelembaban_tanah2 = 0;

    int kelembaban_tanah_status = 0;
    int nilai_kelembaban = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("Monitoring");

    final DatabaseReference humidity = myRef.child("SensorSuhu").child("Humidity");
    final DatabaseReference temperatur = myRef.child("SensorSuhu").child("Temperature");

    final DatabaseReference kondisi_tanah = myRef.child("SensorTanah").child("Kondisi");
    final DatabaseReference n_kelembaban_tanah = myRef.child("SensorTanah").child("Kelembaban");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
//        getActionBar().hide();
//        getSupportActionBar().hide();

        mprogressBar = (ProgressBar) findViewById(R.id.circularProgressbar_kelembaban);
        mprogressBar_suhu = (ProgressBar) findViewById(R.id.circularProgressbar_suhu);

        getMprogressBar_kelembaban_tanah = findViewById(R.id.circularProgressbar_kelembaban_tanah);
        progress_kelembaban_tanah = findViewById(R.id.circularProgressbar_kelembaban_tanah2);

        txt_kelembabann = findViewById(R.id.text_kelembaban);
        txt_suhu = findViewById(R.id.text_suhu);
        txt_kelembaban_tanah = findViewById(R.id.text_kelembaban_tanah);
        txt_nilai_kelembaban_tanah = findViewById(R.id.text_nilai_kelembaban_tanah);

        humidity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                humidity_nilai = dataSnapshot.getValue(Integer.class);
                Log.d("file", "Value is: " + humidity);
                txt_kelembabann.setText(humidity_nilai+"%");
//
//                string_nilai_kelembaban = value;
//                Log.d("file", "ooooooooooo is: " + string_nilai_kelembaban);

//                int nilai_kelembaban = humidity;
//                Log.d("file", "k is: " + nilai_kelembaban);

                ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "SecondaryProgress", 0, humidity_nilai);
                anim.setDuration(10000);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();

            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w("File", "Failed to read value.", error.toException());

            }
        });

        temperatur.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                temperatur_nilai = dataSnapshot.getValue(Integer.class);
                Log.d("file", "Value is: " + temperatur_nilai);
                txt_suhu.setText(temperatur_nilai+"°C");

//                string_nilai_suhu = value;
//                Log.d("file", "ooooooooooo is: " + string_nilai_suhu);

//                int nilai_suhu = Integer.parseInt(value);
//                Log.d("file", "k is: " + nilai_suhu);

                ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar_suhu, "SecondaryProgress", 0, temperatur_nilai);
                anim.setDuration(10000);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();

            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w("File", "Failed to read value.", error.toException());

            }
        });

        kondisi_tanah.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                kelembaban_tanah_status = dataSnapshot.getValue(Integer.class);
                Log.d("file", "Value is: " + kelembaban_tanah_status);
//                txt_kelembaban_tanah.setText(kelembaban_tanah_status+"%");

                if (kelembaban_tanah_status == 1){
                    txt_kelembaban_tanah.setText("Lembab");
                    nilai_kelembaban = 100;
                } else {
                    txt_kelembaban_tanah.setText("Kering");
                    nilai_kelembaban = 0;
                }

                ObjectAnimator anim = ObjectAnimator.ofInt(getMprogressBar_kelembaban_tanah, "SecondaryProgress", 0, nilai_kelembaban);
                anim.setDuration(10000);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w("File", "Failed to read value.", error.toException());

            }
        });

        n_kelembaban_tanah.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nilai_kelembaban_tanah2 = dataSnapshot.getValue(Integer.class);
                Log.d("file", "Value is: " + nilai_kelembaban_tanah2);
                txt_nilai_kelembaban_tanah.setText(nilai_kelembaban_tanah2+"%");
//
//                string_nilai_kelembaban = value;
//                Log.d("file", "ooooooooooo is: " + string_nilai_kelembaban);

//                int nilai_kelembaban = humidity;
//                Log.d("file", "k is: " + nilai_kelembaban);

                ObjectAnimator anim = ObjectAnimator.ofInt(progress_kelembaban_tanah, "SecondaryProgress", 0, nilai_kelembaban_tanah2);
                anim.setDuration(10000);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();

            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w("File", "Failed to read value.", error.toException());

            }
        });
    }
}