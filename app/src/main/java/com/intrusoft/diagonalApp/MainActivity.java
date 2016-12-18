package com.intrusoft.diagonalApp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.intrusoft.squint.DiagonalView;
import com.intrusoft.squint.Squint;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DiagonalView diagonalView = (DiagonalView) findViewById(R.id.diagonal);
//        diagonalView.setImageResource(R.drawable.barney_squint);
//        diagonalView.setDiagonalDirection(Squint.Direction.LEFT_TO_RIGHT);
//        diagonalView.setDiagonalGravity(Squint.Gravity.TOP);
        diagonalView.setScaleType(ImageView.ScaleType.FIT_XY);

    }
}
