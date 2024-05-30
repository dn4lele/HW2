package com.example.hw2;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw2.databinding.ActivityUsersBinding;
import com.example.hw2.objects.User;
import com.example.hw2.recyclerview.UserRecyclerViewAdapter;
import com.example.hw2.room.UserDatabase;
import com.example.hw2.room.UsersTables;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    ActivityUsersBinding binding;
    List<User> allUsers = new ArrayList<>();

    UserDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager( new LinearLayoutManager(this , RecyclerView.VERTICAL , false ) );

        //get the data from room
        db = UserDatabase.getInstance(this);
        allUsers.clear();
        List<UsersTables> allEntitys= db.userDao().getAll();
        for (UsersTables user : allEntitys) {
            User user1=new User();
            user1.id=user.uid;
            user1.firstname=user.firstname;
            user1.lastname=user.lastname;
            user1.city=user.city;
            user1.country=user.country;
            user1.picture=user.picture;
            allUsers.add(user1);
        }

        for(int i=0;i<allUsers.size();i++){
            Log.d("User!!2", allUsers.get(i).id+" "+allUsers.get(i).firstname+" "+allUsers.get(i).lastname+" "+allUsers.get(i).city+" "+allUsers.get(i).country+" "+allUsers.get(i).picture);
        }


        //see the data in the recycler view
        UserRecyclerViewAdapter adapter = new UserRecyclerViewAdapter(allUsers);

        binding.recyclerView.setAdapter(adapter);

    }
}