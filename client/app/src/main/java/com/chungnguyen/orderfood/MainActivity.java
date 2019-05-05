 package com.chungnguyen.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.chungnguyen.orderfood.Common.Common;
import com.chungnguyen.orderfood.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import io.paperdb.Paper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    Button btnSignIn,btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        Paper.init(this);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
        String user = Paper.book().read(Common.USER_KEY);
        String pdw = Paper.book().read(Common.PDW_KEY);
        if (user!=null&&pdw!=null){
            if (!user.isEmpty()&&!pdw.isEmpty())
                login(user,pdw);
        }
        printKeyHash();
    }

    private void printKeyHash() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.chungnguyen.orderfood", PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    private void login(final String phone, final String pdw) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Vui lòng chờ ....");
        mDialog.dismiss();
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(phone).exists()) {
                    mDialog.dismiss();
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);
                    if (user.getPassword().equals(pdw)) {
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        Common.currentUser = user;
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Bạn đã sai mật khẩu hoặc tài khoản", Toast.LENGTH_LONG).show();
                    }
                } else {
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}