package com.kha.train.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.kha.train.R;
import com.kha.train.fragment.TrainScheduleListFragment;

public class TrainScheduleMainActivity extends AppCompatActivity {

public void onCreate (Bundle bundle) {
    super.onCreate(bundle);
    setContentView((int) R.layout.activity_train_schedule_main);
    openFragment(new TrainScheduleListFragment());
}

public void openFragment(Fragment fragment) {
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

