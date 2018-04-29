package il.ac.huji.thepoosh.selfmessageboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MessageAdapter.OnClickListener, MessageDetailsFragment.MessageDeletedListener {
    private static final String TAG = "MainActivity";
    private static final String MESSAGES = "messages";
    ImageButton mSend;
    RecyclerView mList;
    EditText mInput;
    private MessageAdapter mAdapter;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<String> messages = new ArrayList<>(mAdapter.data.size());
        for (MessagePojo msg : mAdapter.data) {
            messages.add(msg.toJsonObject().toString());
        }
        outState.putStringArrayList(MESSAGES, messages);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = findViewById(R.id.list);

        ArrayList<MessagePojo> input = getInput(savedInstanceState);
        mAdapter = new MessageAdapter(input, this);
        mList.setAdapter(mAdapter);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mSend = findViewById(R.id.send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mInput.getText())) {
                    Snackbar.make(mInput, "must enter a message to send", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                MessagePojo pojo = new MessagePojo("Yishai", mInput.getText().toString(), System.currentTimeMillis());
                mAdapter.addMessage(pojo);
                mInput.setText("");
            }
        });
        mInput = findViewById(R.id.input_layout);
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSend.setEnabled(!TextUtils.isEmpty(s));
            }
        });
    }

    private ArrayList<MessagePojo> getInput(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return new ArrayList<>();
        }
        if (savedInstanceState.getStringArrayList(MESSAGES) == null) {
            return new ArrayList<>();
        }
        ArrayList<MessagePojo> output = new ArrayList<>();
        for (String singleMessage : Objects.requireNonNull(savedInstanceState.getStringArrayList(MESSAGES))) {
            try {
                JSONObject msg = new JSONObject(singleMessage);
                output.add(MessagePojo.Companion.fromJsonObject(msg));
            } catch (JSONException e) {
                Log.e(TAG, "getInput: ", e);
            }
        }
        return output;
    }

    @Override
    public void onClick(MessagePojo message) {
        MessageDetailsFragment frag = MessageDetailsFragment.newInstance(message);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, frag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMessageDeleted(MessagePojo msg) {
            mAdapter.removeItem(msg);
            getSupportFragmentManager().popBackStack();
    }
}
