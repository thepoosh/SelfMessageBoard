package il.ac.huji.thepoosh.selfmessageboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    final List<MessagePojo> data;

    public MessageAdapter(List<MessagePojo> input) {
        this.data = input;
    }

    public void addMessage(MessagePojo msg) {
        data.add(msg);
        notifyItemInserted(data.size() - 1);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessagePojo msg = data.get(position);
        holder.timestamp.setText(DateUtils.getRelativeTimeSpanString(holder.itemView.getContext(), msg.getTimestamp()));
        holder.content.setText(msg.getContent());
        holder.name.setText(msg.getNameOfAuthor());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView content;
        TextView timestamp;

        public MessageViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}

