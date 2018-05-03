package il.ac.huji.thepoosh.selfmessageboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    final List<MessagePojo> data;
    private final OnItemClickedListener itemClickedListener;

    public MessageAdapter(List<MessagePojo> input, OnItemClickedListener listener) {
        this.data = input;
        this.itemClickedListener = listener;
    }

    public void addMessage(MessagePojo msg) {
        data.add(msg);
        notifyItemInserted(data.size() - 1);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new MessageViewHolder(view, itemClickedListener);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void deleteMessage(MessagePojo msg) {
        int position = data.indexOf(msg);
        if (position != -1) {
            data.remove(msg);
            notifyItemRemoved(position);
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final OnItemClickedListener listener;
        TextView name;
        TextView content;
        TextView timestamp;
        private MessagePojo message;

        public MessageViewHolder(View itemView, final OnItemClickedListener itemClickedListener) {
            super(itemView);
            this.listener = itemClickedListener;
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
            timestamp = itemView.findViewById(R.id.timestamp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickedListener != null) {
                        itemClickedListener.onItemClicked(message);
                    }
                }
            });
        }

        public void onBind(MessagePojo messagePojo) {
            this.message = messagePojo;
            timestamp.setText(DateUtils.getRelativeTimeSpanString(itemView.getContext(), messagePojo.getTimestamp()));
            content.setText(messagePojo.getContent());
            name.setText(messagePojo.getNameOfAuthor());
        }
    }

    public interface OnItemClickedListener {
        void onItemClicked(MessagePojo message);
    }
}

