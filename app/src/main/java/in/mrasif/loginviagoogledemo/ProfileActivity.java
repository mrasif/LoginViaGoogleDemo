package in.mrasif.loginviagoogledemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import in.mrasif.loginviagoogledemo.utils.AllKeys;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    SharedPreferences preferences;
    ImageView ivPhoto;
    TextView tvName, tvEmail;
    Button btnLogout;
    GoogleApiClient googleApiClient;
    GoogleSignInOptions signInOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        preferences=getSharedPreferences(AllKeys.SP_INSTANCE,MODE_PRIVATE);
        ivPhoto=findViewById(R.id.ivPhoto);
        tvName=findViewById(R.id.tvName);
        tvEmail=findViewById(R.id.tvEmail);
        btnLogout=findViewById(R.id.btnLogout);

        signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        btnLogout.setOnClickListener(this);
        loadProfile();
    }

    private void loadProfile() {
        tvName.setText(preferences.getString(AllKeys.SP_NAME,""));
        tvEmail.setText(preferences.getString(AllKeys.SP_EMAIL,""));
        try{
            Glide.with(this).load(preferences.getString(AllKeys.SP_PHOTO,"")).into(ivPhoto);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(this, "Your id is "+preferences.getString(AllKeys.SP_ID,"0"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        signOut();
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                SharedPreferences.Editor editor=preferences.edit();
                editor.clear();
                editor.commit();
                finish();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
