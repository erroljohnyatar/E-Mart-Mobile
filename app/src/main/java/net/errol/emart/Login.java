package net.errol.emart;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.errol.emart.lib.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Login extends ActionBarActivity implements View.OnClickListener {

    private Button loginButton;

    class LoginTask extends AsyncTask<String, String, String> {
        private int SUCCESS = 0;

        @Override
        protected String doInBackground(String... args){
            EditText emailField = (EditText) findViewById(R.id.emailField);
            EditText passField = (EditText) findViewById(R.id.passField);
            String email = emailField.getText().toString();
            String pass = passField.getText().toString();
            try{
                //Building parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("emailadd",email));
                params.add(new BasicNameValuePair("password",pass));

                //Get JSON Object
                JSONParser jparser = new JSONParser();
                JSONObject j = jparser.makeHttpRequest(getString(R.string.login_url), "POST", params);

                //Check login status
                int success=0;
                try{
                    success = j.getInt("success");
                }catch(NullPointerException e){
                    SUCCESS=2;
                }
                if(success==1){
                    SUCCESS=1;
                    Intent intent = new Intent(Login.this, Index.class);
                    startActivity(intent);
                    finish();
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url){
            TextView wrongMessage = (TextView) findViewById(R.id.wrongMessage);
            Button loginButton = (Button) findViewById(R.id.btnLogin);
            if(SUCCESS==1){
                wrongMessage.setVisibility(View.VISIBLE);
                wrongMessage.setTextColor(ColorStateList.valueOf(Color.GREEN));
                wrongMessage.setText(R.string.login_success);
            }
            else if(SUCCESS==0){
                wrongMessage.setVisibility(View.VISIBLE);
                wrongMessage.setTextColor(ColorStateList.valueOf(Color.RED));
                wrongMessage.setText(R.string.invalid_login_credentials);
                loginButton.setText(R.string.retry_login);
            }
            else{
                wrongMessage.setVisibility(View.VISIBLE);
                wrongMessage.setTextColor(ColorStateList.valueOf(Color.RED));
                wrongMessage.setText(R.string.server_offline);
            }
            loginButton.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                loginButton.setText(R.string.button_login_wait);
                loginButton.setEnabled(false);
                new LoginTask().execute();
                break;
        }
    }
}
