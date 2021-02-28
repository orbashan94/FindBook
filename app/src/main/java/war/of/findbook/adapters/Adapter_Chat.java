package war.of.findbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import war.of.findbook.R;
import war.of.findbook.entities.Message;

public class Adapter_Chat extends RecyclerView.Adapter<Adapter_Chat.Adapter_Chat_View_Holder> {


    private ArrayList<Message> mData;
    private Context mContext;
    private String myUID;

    public Adapter_Chat(Context context, ArrayList<Message> messages,String myUID) {
        mContext = context;
        mData = messages;
        this.myUID = myUID;
    }


    @Override
    public Adapter_Chat_View_Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.item_post, parent, false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat, null);
        return new Adapter_Chat_View_Holder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NotNull Adapter_Chat_View_Holder holder, int position) {
        Message message = mData.get(position);

        holder.txtName.setText(message.getName());
        holder.txtBody.setText(message.getBody());
        holder.txtPhone.setText(message.getUserId());


        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String timeFormat = simpleFormatter.format(new Date(message.getTimestamp()));
        holder.txtTime.setText(timeFormat);

        if (message.getUserId().equals(myUID)){
            holder.itemView.setBackgroundResource(R.drawable.outgoing_shape);
        }else {
            holder.itemView.setBackgroundResource(R.drawable.incoming_shape);
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    class Adapter_Chat_View_Holder extends RecyclerView.ViewHolder {

        private TextView txtTime;
        private TextView txtName;
        private TextView txtPhone;
        private TextView txtBody;


        public Adapter_Chat_View_Holder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtEmail);
            txtBody = itemView.findViewById(R.id.txtBody);

        }
    }
}
