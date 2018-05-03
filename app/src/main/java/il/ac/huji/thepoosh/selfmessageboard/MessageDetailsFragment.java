package il.ac.huji.thepoosh.selfmessageboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageDetailsFragment extends Fragment {

    private static final String KEY_MESSAGE = "message";

    Button share, delete;
    TextView author, content, timestamp;
    private MessagePojo message;
    private OnDelete listener;

    public static MessageDetailsFragment newInstance(MessagePojo message) {

        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message.toJsonObject().toString());
        MessageDetailsFragment fragment = new MessageDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.message_details_fragment, container, false);
        share = root.findViewById(R.id.share_button);
        delete = root.findViewById(R.id.delete_button);

        author = root.findViewById(R.id.author);
        content = root.findViewById(R.id.content);
        timestamp = root.findViewById(R.id.timestamp);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            try {
                this.message = MessagePojo.Companion.fromJsonObject(new JSONObject(getArguments().getString(KEY_MESSAGE)));
                timestamp.setText(DateUtils.getRelativeTimeSpanString(message.getTimestamp(), System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_ALL));
                content.setText(message.getContent());
                author.setText(message.getNameOfAuthor());


                this.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.messageDeleted(message);
                        }
                    }
                });

                this.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, message.toJsonObject().toString());
                        shareIntent.setType("text/*");
                        startActivity(shareIntent);

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDelete) {
            this.listener = (OnDelete) context;
        }
    }

    public interface OnDelete {
        void messageDeleted(MessagePojo msg);
    }
}

