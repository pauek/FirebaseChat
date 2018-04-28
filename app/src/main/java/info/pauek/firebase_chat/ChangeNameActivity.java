package info.pauek.firebase_chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ChangeNameActivity extends AppCompatActivity {

    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        name = findViewById(R.id.name);
    }

    public void saveName(View view) {
        Intent data = new Intent();
        data.putExtra("name", name.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
