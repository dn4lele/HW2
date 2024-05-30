package com.example.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hw2.databinding.ActivityMainBinding;
import com.example.hw2.http.UserAPIClient;
import com.example.hw2.http.UserService;
import com.example.hw2.objects.Root;
import com.example.hw2.objects.User;
import com.example.hw2.room.UserDatabase;
import com.example.hw2.room.UsersTables;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    UserDatabase db;
    User nowUser=new User("Error","Error","Error","Error",0,"Error","Error", "Error");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.btnSeeNext.setOnClickListener(onClickListenerGetPerson);
        binding.btnAdd.setOnClickListener(onClickListenerAddPerson);
        binding.btnView.setOnClickListener(onClickListenerMoveActivity);
    }
    View.OnClickListener onClickListenerMoveActivity = v -> {
        Intent intent = new Intent(MainActivity.this, UsersActivity.class);
        startActivity(intent);
    };

    View.OnClickListener onClickListenerAddPerson =v -> {
        if( nowUser.id.equals("Error")){
            Toast.makeText(this, "cant add Error User to the collection", Toast.LENGTH_SHORT).show();
            return;

        }
        //user in the database already (same id)
        db = UserDatabase.getInstance(this);

        //i want to print all the users uid in the log.d
//        for(UsersTables user : db.userDao().getAll()){
//            Log.d("User!!!!!!!!!!!!",""+user.uid);
//        }

        UsersTables userInDB = db.userDao().findUserById(nowUser.id);
        if(userInDB != null){
            Toast.makeText(this, "Can't Add Same Person(Person in DB)", Toast.LENGTH_SHORT).show();
            return;

        }


        UsersTables user = new UsersTables();
        user.uid = nowUser.id;
        user.firstname = nowUser.firstname;
        user.lastname = nowUser.lastname;
        user.city = nowUser.city;
        user.country = nowUser.country;
        user.picture = nowUser.picture;
        db.userDao().insertoneUser(user);
        Toast.makeText(this, "User added to the collection", Toast.LENGTH_SHORT).show();
        //db.userDao().deleteAll(); // delete all!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        binding.btnAdd.setEnabled(true);
        binding.btnSeeNext.setEnabled(true);
        binding.btnView.setEnabled(true);

        };
    View.OnClickListener onClickListenerGetPerson = v->NextUser();


    private void NextUser(){
        String load="Loading...";
        binding.TVfirstname.setText(load);
        binding.TVlastname.setText(load);
        binding.TVage.setText( load );
        binding.TVemail.setText(load);
        binding.TVcity.setText(load);
        binding.TVcountry.setText(load);

        //can add here load img
        binding.btnAdd.setEnabled(false);
        binding.btnSeeNext.setEnabled(false);
        binding.btnView.setEnabled(false);
        Retrofit retrofit = UserAPIClient.getClient();
        UserService service = retrofit.create(UserService.class);
        Call<Root> call = service.getUser(null,null,null);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
                Root root = response.body();
                assert root != null;
                binding.TVfirstname.setText(root.results.get(0).name.first);
                binding.TVlastname.setText(root.results.get(0).name.last);
                binding.TVage.setText( String.valueOf(root.results.get(0).dob.age) );
                binding.TVemail.setText(root.results.get(0).email);
                binding.TVcity.setText(root.results.get(0).location.city);
                binding.TVcountry.setText(root.results.get(0).location.country);

                Glide.with(MainActivity.this).load(root.results.get(0).picture.large).apply(new RequestOptions().placeholder(R.drawable.placeholder_ico).error(R.drawable.error_ico)).into(binding.imageView);

                nowUser.id = root.results.get(0).login.uuid;
                nowUser.firstname = root.results.get(0).name.first;
                nowUser.lastname = root.results.get(0).name.last;
                nowUser.email = root.results.get(0).email;
                nowUser.age = root.results.get(0).dob.age;
                nowUser.city = root.results.get(0).location.city;
                nowUser.country = root.results.get(0).location.country;
                nowUser.picture = root.results.get(0).picture.large;

                binding.btnAdd.setEnabled(true);
                binding.btnSeeNext.setEnabled(true);
                binding.btnView.setEnabled(true);
            }

            @Override
            public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
                String err="Error";
                binding.TVfirstname.setText(err);
                binding.TVlastname.setText(err);
                binding.TVage.setText(err);
                binding.TVemail.setText(err);
                binding.TVcity.setText(err);
                binding.TVcountry.setText(err);
                Glide.with(MainActivity.this)
                        .load(R.drawable.error_ico)
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder_ico).error(R.drawable.error_ico)).into(binding.imageView);

                nowUser.id = "Error";
                nowUser.firstname = "Error";
                nowUser.lastname = "Error";
                nowUser.email = "Error";
                nowUser.age = 0;
                nowUser.city = "Error";
                nowUser.country = "Error";
                nowUser.picture = "Error";

                binding.btnAdd.setEnabled(true);
                binding.btnSeeNext.setEnabled(true);
                binding.btnView.setEnabled(true);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        NextUser();

    }
}