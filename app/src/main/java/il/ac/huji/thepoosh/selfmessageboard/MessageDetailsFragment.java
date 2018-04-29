package il.ac.huji.thepoosh.selfmessageboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageDetailsFragment extends Fragment {

    private static final String TAG = "MessageDetailsFragment";
    public static final String KEY_MESSAGE = "message";

    @BindView(R.id.author)
    TextView title;
    @BindView(R.id.timestamp)
    TextView timestamp;
    @BindView(R.id.content)
    TextView content;

    MessageDeletedListener listener;
    MessagePojo message;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_details_fragment, container, false);
        updateMessage();
        ButterKnife.bind(this, v);
        title.setText(message.getNameOfAuthor());
        timestamp.setText(DateUtils.getRelativeTimeSpanString(message.getTimestamp(), System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_ALL));
        content.setText(message.getContent());

        return v;
    }

    private void updateMessage() {
        if (getArguments() != null) {
            try {
                JSONObject messageJs = new JSONObject(getArguments().getString(KEY_MESSAGE));
                message = MessagePojo.Companion.fromJsonObject(messageJs);
            } catch (JSONException e) {
                Log.w(TAG, e);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageDeletedListener) {
            this.listener = (MessageDeletedListener) context;
        }
    }


    @OnClick(R.id.share_external)
    public void shareExternal() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message.toJsonObject().toString());
        startActivity(shareIntent);
    }

    @OnClick(R.id.delete)
    public void deleteMesage(){
        if (this.listener != null && message != null) {
            this.listener.onMessageDeleted(message);
        }
    }

    public static MessageDetailsFragment newInstance(MessagePojo message) {

        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message.toJsonObject().toString());

        MessageDetailsFragment fragment = new MessageDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface MessageDeletedListener {
        void onMessageDeleted(MessagePojo msg);
    }
}
