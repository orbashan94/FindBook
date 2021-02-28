package war.of.findbook.adapters;

import android.content.Context;
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
import war.of.findbook.entities.Borrowed_Feed_Book;
import war.of.findbook.entities.Feed_Book;
import war.of.findbook.entities.My_Book;
import war.of.findbook.interfaces.IAdapter_Book;
import war.of.findbook.interfaces.IImageDownload;
import war.of.findbook.repositories.Book_Repository;

public class Adapter_Book_Borrowed extends RecyclerView.Adapter<Adapter_Book_Borrowed.Adapter_Post_View_Holder> {


    private ArrayList<Borrowed_Feed_Book> mData;
    private IAdapter_Book mCallbackAdapter;
    private Context mContext;

    public Adapter_Book_Borrowed(Context context, ArrayList<Borrowed_Feed_Book> postList, IAdapter_Book callbackAdapter) {
        mContext = context;
        mData = postList;
        mCallbackAdapter = callbackAdapter;
    }


    @Override
    public Adapter_Post_View_Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.item_post, parent, false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_book, null);
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
        holder.txtBookName.setText(feedBook.getName());
        holder.txtAuthor.setText(feedBook.getAuthor());
        holder.txtCondition.setText(feedBook.getCondition().name());
        holder.txtBookGenre.setText(feedBook.getGenre());
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String timeFormat = simpleFormatter.format(new Date(feedBook.getTimestamp()));
        holder.txtBookTimeCreated.setText(timeFormat);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    class Adapter_Post_View_Holder extends RecyclerView.ViewHolder {

        private TextView txtBookGenre;
        private ImageView imageViewBook;
        private TextView txtBookName;
        private TextView txtAuthor;
        private TextView txtCondition;
        private TextView txtBookTimeCreated;


        public Adapter_Post_View_Holder(@NonNull View itemView) {
            super(itemView);
            imageViewBook = itemView.findViewById(R.id.imageViewBook);
            txtBookName = itemView.findViewById(R.id.txtBookName);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtCondition = itemView.findViewById(R.id.txtCondition);
            txtBookGenre = itemView.findViewById(R.id.txtBookGenre);
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
