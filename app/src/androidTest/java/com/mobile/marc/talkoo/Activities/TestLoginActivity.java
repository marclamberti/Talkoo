package com.mobile.marc.talkoo.Activities;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.InstrumentationTestCase;
import android.widget.Button;
import android.widget.EditText;

import com.mobile.marc.talkoo.LoginActivity;
import com.mobile.marc.talkoo.R;

/**
 * Created by Marc on 15/12/14.
 */
public class TestLoginActivity extends ActivityUnitTestCase<LoginActivity> {

    private LoginActivity activity;
    private int button_id;
    private int edit_text_login_id;

    public TestLoginActivity() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), LoginActivity.class);
        startActivity(intent, null, null);
        activity = getActivity();
    }

    public void testLayout() {
        button_id = R.id.button_welcome_sign_in;
        assertNotNull(activity.findViewById(button_id));
        edit_text_login_id = R.id.welcome_edit_text_login;
        assertNotNull(activity.findViewById(edit_text_login_id));

        Button button = (Button)activity.findViewById(button_id);
        assertEquals("The button's label is incorrect", "Sign In", button.getText());
        EditText editText = (EditText)activity.findViewById(edit_text_login_id);
        editText.setText("Jordy");
        assertEquals("The text in the edit box does not match", "Jordy", editText.getText().toString());
    }

    public void testIntentTriggerViaOnclick() {
        button_id = R.id.button_welcome_sign_in;
        Button button = (Button)activity.findViewById(button_id);
        assertNotNull("Button not allowed to be null", button);
        edit_text_login_id = R.id.welcome_edit_text_login;
        EditText edit_text = (EditText)activity.findViewById(edit_text_login_id);
        assertNotNull("Edit text not allowed to be null", edit_text);

        edit_text.setText("Youhou");
        button.performClick();
        Intent triggered_intent = getStartedActivityIntent();
        assertNotNull("Intent was null", triggered_intent);
        String login = triggered_intent.getExtras().get(LoginActivity.EXTRA_LOGIN).toString();
        assertEquals("Incorrect login passed via the intent", "Youhou", login);
    }
}