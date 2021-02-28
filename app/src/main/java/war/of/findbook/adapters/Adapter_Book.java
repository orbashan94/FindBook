package war.of.findbook.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import war.of.findbook.R;
import war.of.findbook.entities.Feed_Book;
import war.of.findbook.interfaces.IAdapter_Book;
import war.of.findbook.interfaces.IImageDownload;
import war.of.findbook.repositories.Book_Repository;

import static war.of.findbook.entities.Feed_Book.CHILDREN;
import static war.of.findbook.entities.Feed_Book.DEVELOPMENT;
import static war.of.findbook.entities.Feed_Book.PROSE;
import static war.of.findbook.entities.Feed_Book.BIOGRAPHY;
import static war.of.findbook.entities.Feed_Book.STUDY;

public class Adapter_Book extends RecyclerView.Adapter<Adapter_Book.Adapter_Post_View_Holder> {


    private ArrayList<Feed_Book> mData;
    private IAdapter_Book mCallbackAdapter;
    private Context mContext;

    public Adapter_Book(Context context, ArrayList<Feed_Book> postList, IAdapter_Book callbackAdapter) {
        mContext = context;
        mData = postList;
        mCallbackAdapter = callbackAdapter;
    }


    @Override
    public Adapter_Post_View_Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.item_post, parent, false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, null);
        return new Adapter_Post_View_Holder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NotNull Adapter_Post_View_Holder holder, int position) {
        Feed_Book feedBook = mData.get(position);
        Book_Repository.getInstance(mContext).getImage(feedBook.getImageURL(), new IImageDownload() {
            @Override
            public void onImageDownloaded(File file) {
                Glide.with(mContext).load(file).into(holder.imageViewBook);
            }
        });
        holder.txtBookGenre.setText(feedBook.getGenre());
        holder.txtBookName.setText(feedBook.getName());
        holder.txtAuthor.setText(feedBook.getAuthor());


        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String timeFormat = simpleFormatter.format(new Date(feedBook.getTimestamp()));
        holder.txtBookTimeCreated.setText(timeFormat);

        holder.txtCondition.setText(feedBook.getCondition().name());
        holder.txtBookAddress.setText(feedBook.getAddress());
        holder.txtDesc.setText(feedBook.getDesc());
        holder.txtDesc.setSelected(true);
        holder.itemView.setBackgroundColor(getColorByGenre(feedBook.getGenre()));

    }

    private int getColorByGenre(String genre) {
        switch (genre) {
            default:
            case PROSE:
                return Color.argb(120, 200, 120, 150);
            case BIOGRAPHY:
                return Color.argb(120, 200, 120, 180);
            case STUDY:
                return Color.argb(120, 100, 100, 150);
            case CHILDREN:
                return Color.argb(120, 50, 200, 150);
            case DEVELOPMENT:
                return Color.argb(120, 150, 50, 10);

        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    class Adapter_Post_View_Holder extends RecyclerView.ViewHolder {

        private ImageView imageViewBook;
        private TextView txtBookName;
        private TextView txtAuthor;
        private TextView txtCondition;
        private TextView txtBookAddress;
        private TextView txtBookGenre;
        private TextView txtBookTimeCreated;
        private TextView txtDesc;


        public Adapter_Post_View_Holder(@NonNull View itemView) {
            super(itemView);
            imageViewBook = itemView.findViewById(R.id.imageViewBook);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtBookName = itemView.findViewById(R.id.txtBookName);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtCondition = itemView.findViewById(R.id.txtCondition);
            txtBookGenre = itemView.findViewById(R.id.txtBookGenre);
            txtBookAddress = itemView.findViewById(R.id.txtBookAddress);
            txtBookTimeCreated = itemView.findViewById(R.id.txtBookTimeCreated);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallbackAdapter != null) {
                        Feed_Book feedBook = mData.get(getAdapterPosition());
                        mCallbackAdapter.onClickBookItem(feedBook);

                    }
                }
            });


        }
    }
}
