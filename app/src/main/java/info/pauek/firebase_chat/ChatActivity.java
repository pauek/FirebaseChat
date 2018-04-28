package info.pauek.firebase_chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import info.pauek.firebase_chat.Data.Message;

public class ChatActivity extends AppCompatActivity {

    private static final int EDIT_NAME = 0;
    private RecyclerView recyclerview;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> adapter;
    private DatabaseReference chat;
    private EditText edit;
    private String user;

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textview, userview;

        MessageViewHolder(View root) {
            super(root);
            textview = root.findViewById(R.id.text);
            userview = root.findViewById(R.id.user);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        chat = FirebaseDatabase.getInstance().getReference("chat");

        recyclerview = findViewById(R.id.recyclerview);
        edit = findViewById(R.id.edit);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);

        Query query = chat.limitToLast(100);

        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
                return new MessageViewHolder(root);
            }
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
                holder.textview.setText(model.text);
                holder.userview.setText(model.user);
            }
        };

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);

        user = getPreferences(MODE_PRIVATE).getString("user", null);
        if (user == null) {
            Intent intent = new Intent(this, ChangeNameActivity.class);
            startActivityForResult(intent, EDIT_NAME);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDIT_NAME:
                if (resultCode == RESULT_OK) {
                    user = data.getStringExtra("name");
                    saveUserName();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void saveUserName() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user", user);
        editor.apply();
    }

    public void postMessage(View view) {
        chat.push().setValue(new Message(edit.getText().toString(), user));
        edit.setText("");
        adapter.notifyItemInserted(-1);
    }
}
