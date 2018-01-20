package in.mrasif.loginviagoogledemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import in.mrasif.loginviagoogledemo.utils.AllKeys;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    SignInButton btnSignIn;
    GoogleApiClient googleApiClient;
    GoogleSignInOptions signInOptions;
    private static final int REQUEST_CODE=9001;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn=findViewById(R.id.btnLogin);
        btnSignIn.setOnClickListener(this);
        preferences=getSharedPreferences(AllKeys.SP_INSTANCE,MODE_PRIVATE);
        signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        if(preferences.getBoolean(AllKeys.SP_IS_LOGIN,false)){
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        signIn();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn(){
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                SharedPreferences.Editor editor=preferences.edit();
                GoogleSignInAccount account=result.getSignInAccount();

                editor.putBoolean(AllKeys.SP_IS_LOGIN,true);
                editor.putString(AllKeys.SP_ID,account.getId());
                if (null!=account.getDisplayName()) {
                    editor.putString(AllKeys.SP_NAME,account.getDisplayName());
                }

                editor.putString(AllKeys.SP_EMAIL,account.getEmail());

                if(null!=account.getPhotoUrl()) {
                    editor.putString(AllKeys.SP_PHOTO,account.getPhotoUrl().toString());
                }

                editor.commit();
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                finish();
            }
        }
    }

}
