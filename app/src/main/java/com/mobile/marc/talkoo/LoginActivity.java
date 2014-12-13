//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243311        mlamberti@ust.hk

package com.mobile.marc.talkoo;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;

import com.cengalabs.flatui.FlatUI;

public class LoginActivity extends Activity {
    public final static String EXTRA_LOGIN = "com.mobile.marc.talkoo.LOGIN";
    private final int APP_THEME = R.array.dark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Converts the default value to dp to be compatible with different screen sizes
        FlatUI.initDefaultValues(this);

        // Set the theme before adding content view
        FlatUI.setDefaultTheme(APP_THEME);

        // Set the layout
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // OnClick event for sign in button
    public void signIn(View view) {
        Intent intent = new Intent(this, NavigatorActivity.class);

        // Getting the login
        EditText    login_edit_text = (EditText)findViewById(R.id.welcome_edit_text_login);
        String      login = login_edit_text.getText().toString();
        intent.putExtra(EXTRA_LOGIN, login);

        startActivity(intent);
    }

}
