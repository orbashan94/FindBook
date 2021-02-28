package war.of.findbook.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import war.of.findbook.R;
import war.of.findbook.entities.Feed_Book;
import war.of.findbook.interfaces.IFragment_Loader;
import war.of.findbook.repositories.Book_Repository;


public class Fragment_Post_Book extends Fragment {

    private static final int IMAGE_GALLERY_REQUEST = 20;

    private EditText edtDesc, edtName, edtAuthor  ;
    private CheckBox checkBoxCondition;
    private Button postBook_BTN_frontImg, btnPost;
    private RadioGroup rgGenre;
    private IFragment_Loader listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_book, container, false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        initViews(view);
    }

    private void initViews(View view) {
        postBook_BTN_frontImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValid())
                    return;
                createNewPost();
            }
        });

        rgGenre.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                genreSelect=getResources().getResourceEntryName(id).substring(2).toLowerCase();

            }
        });
    }
    private Uri fileUri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
              fileUri = data.getData();


        }
    }

    private void getImage() {

        ImagePicker.Companion.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .galleryOnly()
                .start(IMAGE_GALLERY_REQUEST);
    }

    private boolean isValid() {
        if (edtAuthor.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Fill author", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtDesc.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Fill desc", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtName.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Fill name", Toast.LENGTH_SHORT).show();
            return false;
        }

       if (genreSelect==null){
           Toast.makeText(getContext(), "Select genre", Toast.LENGTH_SHORT).show();
           return false;
       }
       if (fileUri==null){
           Toast.makeText(getContext(), "Upload image", Toast.LENGTH_SHORT).show();
           return false;
       }

        return true;
    }

    private String genreSelect;

    private void createNewPost() {

        Book_Repository repository = Book_Repository.getInstance(getContext());


        String name = edtName.getText().toString();
        String author = edtAuthor.getText().toString();
        String desc = edtDesc.getText().toString();


        Feed_Book.Condition condition = checkBoxCondition.isChecked() ? Feed_Book.Condition.NEW : Feed_Book.Condition.OLD;


        repository.postBookToFeed(name, desc , author, condition,genreSelect,fileUri,getContext());
        Toast.makeText(getContext(), "Post updated", Toast.LENGTH_SHORT).show();


        returnToHomeFragment();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFragment_Loader) {
            listener = (IFragment_Loader) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    private void returnToHomeFragment() {
        if (listener != null)
            listener.loadFragment(new Fragment_Home());

    }

    private void findViews(View view) {
        edtAuthor = view.findViewById(R.id.edtAuthor);
        edtDesc = view.findViewById(R.id.edtDesc);
        edtName = view.findViewById(R.id.edtName);
         checkBoxCondition = view.findViewById(R.id.checkBoxCondition);
        postBook_BTN_frontImg = view.findViewById(R.id.postBook_BTN_frontImg);
        btnPost = view.findViewById(R.id.btnPost);
        rgGenre = view.findViewById(R.id.rgGenre);


    }


}