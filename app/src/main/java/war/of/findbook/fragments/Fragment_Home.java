package war.of.findbook.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import war.of.findbook.R;
import war.of.findbook.adapters.Adapter_Book;
import war.of.findbook.entities.Feed_Book;
import war.of.findbook.interfaces.IAdapter_Book;
import war.of.findbook.repositories.Book_Repository;


public class Fragment_Home extends Fragment {

    private Adapter_Book adapter_book;
    private RecyclerView recyclerViewPost;
    private Book_Repository repository;
    private EditText edtSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewPost = view.findViewById(R.id.recyclerview_posts);
        edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    Book_Repository.getInstance(getContext()).startListenerAllFeed();
                    return;
                }
                Book_Repository.getInstance(getContext()).clearFeedList();
                Book_Repository.getInstance(getContext()).searchBook(editable.toString());
            }
        });
        recyclerViewPost.setHasFixedSize(true);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewPost.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        repository = Book_Repository.getInstance(getContext());

        repository.getAllFeedBooks().observe(getViewLifecycleOwner(), new Observer<ArrayList<Feed_Book>>() {
            @Override
            public void onChanged(ArrayList<Feed_Book> feedBooks) {
                initListPosts(feedBooks);
            }
        });


    }

    private void initListPosts(ArrayList<Feed_Book> feedBooks) {
        adapter_book = new Adapter_Book(getContext(), feedBooks, new IAdapter_Book() {
            @Override
            public void onClickBookItem(Feed_Book book) {
                showDialog(book);
                Toast.makeText(getContext(), book.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        recyclerViewPost.setAdapter(adapter_book);
    }

    private void showDialog(Feed_Book book) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_dialog);

        final TextView msg = dialog.findViewById(R.id.txt_msg);
        Button ok = dialog.findViewById(R.id.btn_ok);
        Button cancel = dialog.findViewById(R.id.btn_cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.takeBook(book);
                dialog.dismiss();
                //open chat
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }


}