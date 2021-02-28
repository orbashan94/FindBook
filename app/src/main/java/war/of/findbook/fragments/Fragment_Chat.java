package war.of.findbook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import war.of.findbook.R;
import war.of.findbook.adapters.Adapter_Chat;
import war.of.findbook.entities.Feed_Book;
import war.of.findbook.entities.Message;
import war.of.findbook.repositories.Book_Repository;
import war.of.findbook.utilities.Shared_Preference_Helper;

public class Fragment_Chat extends Fragment {

    private RecyclerView recyclerViewChat;
    private Book_Repository repository;
    private EditText edtMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewChat = view.findViewById(R.id.recyclerViewChat);
        edtMessage = view.findViewById(R.id.edtMessage);

        edtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                if (action== EditorInfo.IME_ACTION_SEND) {
                    sentMessage();
                }
                return false;
            }
        });
        recyclerViewChat.setHasFixedSize(true);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewChat.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        recyclerViewChat.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        recyclerViewChat.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
       repository= Book_Repository.getInstance(getContext());

       String myUID= Shared_Preference_Helper.getInstance(getContext()).getUID();
        repository.getChatLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> messages) {
                Adapter_Chat adapter_chat = new Adapter_Chat(getContext(), messages,myUID);
                recyclerViewChat.setAdapter(adapter_chat);
                recyclerViewChat.scrollToPosition(messages.size()-1);
            }
        });
    }

    private void sentMessage() {
        if (edtMessage.getText().toString().isEmpty())
            return;
        repository.sentMessage(edtMessage.getText().toString());
        edtMessage.setText("");
    }
}