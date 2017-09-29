package sprintylab.com.sgtsmssender.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import sprintylab.com.sgtsmssender.R;

public class SPChoosingActivity extends AppCompatActivity {
    private Button btn_d;
    private Button btn_m;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spchoosing);
        btn_d = (Button) findViewById(R.id.btn_d);
        btn_m = (Button) findViewById(R.id.btn_m);
        context = this;
        btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("sp","d");
                startActivity(intent);
            }
        });
        btn_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("sp","m");
                startActivity(intent);
            }
        });
    }
}
