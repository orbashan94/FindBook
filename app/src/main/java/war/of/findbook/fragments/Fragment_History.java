package war.of.findbook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import war.of.findbook.R;
import war.of.findbook.adapters.Adapter_Book_Borrowed;
import war.of.findbook.adapters.Adapter_My_Book;
import war.of.findbook.entities.Borrowed_Feed_Book;
import war.of.findbook.entities.Feed_Book;
import war.of.findbook.entities.My_Book;
import war.of.findbook.interfaces.IAdapter_Book;
import war.of.findbook.repositories.Book_Repository;


public class Fragment_History extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    private RecyclerView recycle_my_book;
    private RecyclerView recycle_history_borrowed;
    private RecyclerView recycle_borrowed;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycle_my_book = view.findViewById(R.id.recycle_my_book);
        recycle_history_borrowed = view.findViewById(R.id.recycle_history_borrowed);
        recycle_borrowed = view.findViewById(R.id.recycle_borrowed);

        recycle_my_book.setHasFixedSize(true);
        recycle_history_borrowed.setHasFixedSize(true);
        recycle_borrowed.setHasFixedSize(true);


        Book_Repository repository = Book_Repository.getInstance(getContext());

        repository.getAllMyBook().observe(getViewLifecycleOwner(), new Observer<ArrayList<My_Book>>() {
            @Override
            public void onChanged(ArrayList<My_Book> my_books) {
                initAdapterMyBook(my_books);
            }
        });
        repository.getAllBorrowedBook().observe(getViewLifecycleOwner(), new Observer<ArrayList<Borrowed_Feed_Book>>() {
            @Override
            public void onChanged(ArrayList<Borrowed_Feed_Book> borrowed_feed_books) {
                initListPosts(borrowed_feed_books, recycle_borrowed);

            }
        });

        repository.getAllHistoryBook().observe(getViewLifecycleOwner(), new Observer<ArrayList<Borrowed_Feed_Book>>() {
            @Override
            public void onChanged(ArrayList<Borrowed_Feed_Book> borrowed_feed_books) {
                initListPosts(borrowed_feed_books, recycle_history_borrowed);
            }
        });

    }

    private void initAdapterMyBook(ArrayList<My_Book> my_books) {
        Adapter_My_Book adapterBook = new Adapter_My_Book(getContext(), my_books, new IAdapter_Book() {
            @Override
            public void onClickBookItem(Feed_Book book) {
                Toast.makeText(getContext(), book.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        recycle_my_book.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycle_my_book.setAdapter(adapterBook);
    }


    private void initListPosts(ArrayList<Borrowed_Feed_Book> feedBooks, RecyclerView recycle_history_borrowed) {
        Adapter_Book_Borrowed adapterBook = new Adapter_Book_Borrowed(getContext(), feedBooks, new IAdapter_Book() {
            @Override
            public void onClickBookItem(Feed_Book book) {

                Toast.makeText(getContext(), book.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        recycle_history_borrowed.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recycle_history_borrowed.setAdapter(adapterBook);
    }
}