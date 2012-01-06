package de.fhworms.inf1743.tawk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public final class CreateMessage extends Activity {

	static final String DATA_STORE = "dataStore";

	static final String USER_NAME = "userName";
	static final String PASSWORD = "password";
	static final String MESSAGE = "message";
	static final String MESSAGE_COUNT = "messageCount";
	static final String MESSAGE_TIME = "timestamp";

	SharedPreferences dataStore;
	Editor ed;

	EditText newMessage;
	TextView username, characterCount;

	// TODO: this should not be in the UI but back end
	final int MAX_CHARACTER_COUNT = 300;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_message);

		dataStore = getSharedPreferences(DATA_STORE, MODE_PRIVATE);

		newMessage = (EditText) findViewById(R.id.new_message_edittext);
		characterCount = (TextView) findViewById(R.id.character_count);
		username = (TextView) findViewById(R.id.username_textview);

		username.setText(dataStore.getString(USER_NAME, ""));

		newMessage.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// This sets a TextView to show the available character count
				characterCount.setText(String.valueOf(MAX_CHARACTER_COUNT
						- s.length()));
			}

			public void afterTextChanged(Editable s) {
			}
		});
	}

	public void sendNewMessage(View view) {

		ed = dataStore.edit();
		ed.putString(MESSAGE, newMessage.getText().toString());

		// ++message count (add one before storing it)
		ed.putInt(MESSAGE_COUNT, dataStore.getInt(MESSAGE_COUNT, 0) + 1);

		// store time (as milliseconds after 1970) to message
		ed.putLong(MESSAGE_TIME, System.currentTimeMillis());
		ed.commit();

		finish();
	}

	public void close(View view) {
		finish();
	}
}
