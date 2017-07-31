package test.vemishra.cafepic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void LoginClicked(View v){
        Intent i = new Intent(Login.this, MainActivity.class);
        startActivity(i);

        // close this activity
        finish();
    }
}
