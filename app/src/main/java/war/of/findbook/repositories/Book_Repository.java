package war.of.findbook.repositories;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import war.of.findbook.entities.Borrowed_Feed_Book;
import war.of.findbook.entities.Feed_Book;
import war.of.findbook.entities.Genre;
import war.of.findbook.entities.Message;
import war.of.findbook.entities.My_Book;
import war.of.findbook.entities.User;
import war.of.findbook.enums.Firebase_Table;
import war.of.findbook.interfaces.IImageDownload;
import war.of.findbook.interfaces.IImageUpload;
import war.of.findbook.utilities.Shared_Preference_Helper;

public class Book_Repository {

    private final DatabaseReference reference;
    private String myID;
    private final MutableLiveData<ArrayList<Feed_Book>> feedBookArrayListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<My_Book>> myBookAlreadyFreeMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<My_Book>> allMyBookMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Borrowed_Feed_Book>> allBorrowedBookMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Borrowed_Feed_Book>> allHistoryBookMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Message>> chatLiveData = new MutableLiveData<>();

    private final ArrayList<My_Book> myBookArrayList = new ArrayList<>();
    private final StorageReference refStorage;
    private final Shared_Preference_Helper preferenceHelper;
    private static Book_Repository instance;

    public static Book_Repository getInstance(Context context) {
        if (instance == null)
            instance = new Book_Repository(context);

        return instance;
    }

    private Book_Repository(Context context) {
        myID = Shared_Preference_Helper.getInstance(context).getUID();
        preferenceHelper = Shared_Preference_Helper.getInstance(context);
        reference = FirebaseDatabase.getInstance().getReference();
        refStorage = FirebaseStorage.getInstance().getReference();
        startListenerAllFeed();
        startListenerAllMyAvailableBook();
        startListenerToChat();
    }

    private void startListenerToChat() {
        reference.child(Firebase_Table.CHAT.name()).orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Message> messageArrayList=new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    Log.d("Test", message.toString());
                    messageArrayList.add(message);
                }

                chatLiveData.postValue(messageArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startListenerAllMyAvailableBook() {

        reference.child((Firebase_Table.MY_BOOK_LIST.name())).child(myID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                My_Book myBook = snapshot.getValue(My_Book.class);
                if (myBook.getHasAlreadyBorrowed()) {
                    //TODO ceck duplicate
                    myBookArrayList.add(myBook);
                    myBookAlreadyFreeMutableLiveData.postValue(myBookArrayList);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void startListenerAllFeed() {
        reference.child(Firebase_Table.FEED.name()).orderByChild("genre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addBooks(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public LiveData<ArrayList<Feed_Book>> getAllFeedBooks() {
        return feedBookArrayListMutableLiveData;
    }

    public LiveData<ArrayList<Borrowed_Feed_Book>> getAllBorrowedBook() {

        reference.child(Firebase_Table.BORROWED_BOOK.name()).child(myID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               

                ArrayList<Borrowed_Feed_Book> borrowedFeedBooks = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Borrowed_Feed_Book borrowedFeedBook = dataSnapshot.getValue(Borrowed_Feed_Book.class);
                    borrowedFeedBooks.add(borrowedFeedBook);
                }


                allBorrowedBookMutableLiveData.postValue(borrowedFeedBooks);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return allBorrowedBookMutableLiveData;
    }

    public LiveData<ArrayList<Borrowed_Feed_Book>> getAllHistoryBook() {
        reference.child(Firebase_Table.HISTORY_BORROWED_BOOKS.name()).child(myID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Borrowed_Feed_Book> borrowedFeedBooks = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Borrowed_Feed_Book borrowedFeedBook = dataSnapshot.getValue(Borrowed_Feed_Book.class);
                    borrowedFeedBooks.add(borrowedFeedBook);
                }


                allHistoryBookMutableLiveData.postValue(borrowedFeedBooks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return allHistoryBookMutableLiveData;
    }

    public LiveData<ArrayList<My_Book>> getAllMyBook() {
        reference.child(Firebase_Table.MY_BOOK_LIST.name()).child(myID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<My_Book> myBookArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    My_Book myBook = dataSnapshot.getValue(My_Book.class);
                    myBookArrayList.add(myBook);
                }

                allMyBookMutableLiveData.postValue(myBookArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return allMyBookMutableLiveData;
    }

    public LiveData<ArrayList<My_Book>> getAlMyBookAlreadyFreeMutableLiveData() {
        return myBookAlreadyFreeMutableLiveData;
    }
    public LiveData<ArrayList<Message>> getChatLiveData() {
        return chatLiveData;
    }

    public Feed_Book postBookToFeed(String name, String desc, String author, Feed_Book.Condition condition, String genreField, Uri image,Context context) {
        Feed_Book feedBook = new Feed_Book(name, desc, author, condition, myID);
        String feedID = reference.child(Firebase_Table.FEED.name()).push().getKey();
        feedBook.setId(feedID);
        feedBook.setTimestamp(System.currentTimeMillis());
        feedBook.setGenre(genreField);
        feedBook.setImageURL(feedBook.getTimestamp() + "_" + myID);
        feedBook.setAddress(getAddressFromLocation(preferenceHelper.getLastLocation(),context));
        uploadImage(feedBook.getImageURL(), image, new IImageUpload() {
            @Override
            public void onImageUploadFinish(boolean isSuccessful) {
                reference.child(Firebase_Table.FEED.name()).child(feedID).setValue(feedBook);
                My_Book myBook = new My_Book(feedBook.getName(), feedBook.getDesc(), feedBook.getAuthor(), feedBook.getCondition(), feedBook.getOwner(), false);
                myBook.setId(feedID);
                myBook.setTimestamp(feedBook.getTimestamp());
                myBook.setImageURL(feedBook.getImageURL());
                myBook.setGenre(genreField);
                myBook.setAddress(feedBook.getAddress());
                reference.child(Firebase_Table.MY_BOOK_LIST.name()).child(myID).child(feedID).setValue(myBook);
            }
        });

        return feedBook;
    }

    private String getAddressFromLocation(Location lastLocation, Context context) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            return city+" "+address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }


    private void uploadImage(String imageUrl, Uri imageUri, IImageUpload callback) {
        refStorage.child(imageUrl).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callback.onImageUploadFinish(true);

            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onImageUploadFinish(false);
            }
        });
    }

    public void getImage(String imageUrl, IImageDownload imageDownload) {
        if (imageUrl.isEmpty())
            return;
        File file = null;
        try {
            file = File.createTempFile("image", ".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File finalFile = file;
        refStorage.child(imageUrl).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                if (imageDownload != null)
                    imageDownload.onImageDownloaded(finalFile);
            }
        });
    }

    public void takeBook(Feed_Book feedBook) {


        // remove from feed
        reference.child(Firebase_Table.FEED.name()).child(feedBook.getId()).removeValue();


        // add to my history
        String historyBorrowedBooksKey = reference.child(Firebase_Table.HISTORY_BORROWED_BOOKS.name()).child(myID).push().getKey();
        Borrowed_Feed_Book borrowedBook = new Borrowed_Feed_Book(feedBook, myID);
        borrowedBook.setTimestamp(System.currentTimeMillis());
        borrowedBook.setId(historyBorrowedBooksKey);
        borrowedBook.setImageURL(feedBook.getImageURL());
        borrowedBook.setGenre(feedBook.getGenre());
        reference.child(Firebase_Table.HISTORY_BORROWED_BOOKS.name()).child(myID).child(historyBorrowedBooksKey).setValue(borrowedBook).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //tell him who am i
                String myBorrowedBooksID = reference.child(Firebase_Table.BORROWED_BOOK.name()).child(feedBook.getOwner()).push().getKey();
                borrowedBook.setId(myBorrowedBooksID);
                reference.child(Firebase_Table.BORROWED_BOOK.name()).child(feedBook.getOwner()).child(myBorrowedBooksID).setValue(borrowedBook);
            }
        });


        // update him to book taken
        reference.child(Firebase_Table.MY_BOOK_LIST.name()).child(feedBook.getOwner()).child(feedBook.getId()).child("hasAlreadyBorrowed").setValue(true);


    }




    public void sentMessage(  String body) {
        Message message = new Message(myID, body,preferenceHelper.getUserName() ,System.currentTimeMillis());
        reference.child(Firebase_Table.CHAT.name())  .push().setValue(message);
    }

    public void searchBook(String bookName) {
        reference.child(Firebase_Table.FEED.name()).orderByChild("name").equalTo(bookName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addBooks(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addBooks(DataSnapshot snapshot) {
        ArrayList<Feed_Book> feedBooks = new ArrayList<>();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            Feed_Book feedBook = dataSnapshot.getValue(Feed_Book.class);
            if (feedBook.getOwner().equals(myID))
                continue;

            feedBooks.add(feedBook);
        }


        feedBookArrayListMutableLiveData.postValue(feedBooks);
    }

    public void clearFeedList() {
        feedBookArrayListMutableLiveData.postValue(new ArrayList<>());

    }

    public void loadNameFromLogin(String uid ) {
        String uidToFirebaseKey=uid.replaceAll("@","_").replaceAll("\\.","_").replaceAll("\\$","_").replaceAll("\\+","_").replaceAll(" ","_");
        myID=uidToFirebaseKey;
        preferenceHelper.storeUserID(uidToFirebaseKey);
        reference.child(Firebase_Table.USERS.name()).child(uidToFirebaseKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user=snapshot.getValue(User.class);

                    preferenceHelper.storeUserName(user.getUserName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void insertUser(String uid, String userName) {
        String uidToFirebaseKey=uid.replaceAll("@","_").replaceAll("\\.","_").replaceAll("\\$","_").replaceAll("\\+","_").replaceAll(" ","_");

        preferenceHelper.storeUserName(userName);
        preferenceHelper.storeUserID(uidToFirebaseKey);

        myID=uidToFirebaseKey;
         reference.child(Firebase_Table.USERS.name()).child(uidToFirebaseKey).setValue(new User(System.currentTimeMillis(),uid,userName));

    }
}
