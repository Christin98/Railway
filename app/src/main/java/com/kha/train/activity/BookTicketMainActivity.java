package com.kha.train.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import com.kha.train.R;
import com.kha.train.fragment.TrainBookFragment;
import com.kha.train.util.SharedPreference;

public class BookTicketMainActivity extends AppCompatActivity {
    SharedPreference sharedPreference;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_book_ticket_main);
        ButterKnife.bind((Activity) this);
        openFragment(new TrainBookFragment(),"TrainBookFragment");
        this.sharedPreference = new SharedPreference(this);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("===>>>");
        stringBuilder.append(this.sharedPreference.getTimeFormate());
    }

    public void openFragment(Fragment fragment,String tag) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame, fragment);
        beginTransaction.addToBackStack(fragment.toString());
        beginTransaction.commit();
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
