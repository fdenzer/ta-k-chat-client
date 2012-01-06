package de.fhworms.inf1743.tawk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public final class Start extends Activity {

	static final String DATA_STORE = "dataStore";

	static final String USER_NAME = "userName";
	static final String PASSWORD = "password";
	static final String USE_TEMP = "useTemporaryVariables";
	static final String MESSAGE = "message";
	static final String MESSAGE_COUNT = "messageCount";

	// results could be handled from the following Activities
	final int USER_LOGIN = 0;
	final int CREATE_MESSAGE = 1;
	final int VIEW_MESSAGES = 2;

	SharedPreferences dataStore;
	Editor ed;

	TextView outboxText, outboxNumber, currentUser;
	CheckBox autosend;
	Button viewMessages, createMessage, trySend;

	String tempUserName, tempPassword;
	boolean useTemp, reseted;

	// Called when the activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		currentUser = (TextView) findViewById(R.id.current_user_name_textview);

		outboxText = (TextView) findViewById(R.id.outbox_textview);

		outboxNumber = (TextView) findViewById(R.id.outbox_number);

		autosend = (CheckBox) findViewById(R.id.autosend_checkbox);

		viewMessages = (Button) findViewById(R.id.start_to_view_messages_button);
		createMessage = (Button) findViewById(R.id.start_to_create_message_button);
		trySend = (Button) findViewById(R.id.try_send_button);

		// hideitems(); // does not work

		// load user name, messages and password
		dataStore = getSharedPreferences(DATA_STORE, MODE_PRIVATE);

		// NEVER load the non-permanent temp-variables from shared preferences
		tempUserName = tempPassword = "";
		useTemp = reseted = false;

	}

	@Override
	protected void onResume() {

		if (!useTemp && reseted) {
			// show previously saved user name
			showItems(dataStore.getString(USER_NAME, ""));
			// TODO: import password and properties as they are needed for
			// messages
		} else {
			// actually, only useful if reseted == false, but no harm done if
			// overdone, since if() isn't free of overhead either
			reseted = false;
		}

		Integer sendThese = (Integer) dataStore.getInt(MESSAGE_COUNT, 0);
		outboxNumber.setText(sendThese.toString());
		super.onPause();
	}

	// Called when the menu is opened for the very first time
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.start_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.close_app_menu_item:
			finish();

		case R.id.reset_menu_item:

			// clear all stored data
			ed = dataStore.edit();
			ed.clear();
			ed.commit();

			// restore look of new start

			// hideItems(); // hide- and showItems don't work

			reseted = true;

		default:
			break;
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case USER_LOGIN:
			if (resultCode == RESULT_OK) {
				useTemp = data.getBooleanExtra(USE_TEMP, false);
				if (useTemp) {
					tempUserName = data.getStringExtra(USER_NAME);
					tempPassword = data.getStringExtra(PASSWORD);
					showItems(tempUserName);
				} else {
					// TODO: also import password and properties, as they are
					// needed for messages
					showItems(dataStore.getString(USER_NAME, ""));
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @param name
	 *            The user name. For user Bob the String "User: Bob" is shown
	 */
	public void showItems(String name) {
		currentUser.setText(this.getString(R.string.user_name) + " " + name);
		viewMessages.setVisibility(0);
		createMessage.setVisibility(0);
		outboxText.setVisibility(0);
		outboxNumber.setVisibility(0);
		autosend.setVisibility(0);
		trySend.setVisibility(0);
	}

	public void hideItems() {
		currentUser.setText("");
		viewMessages.setVisibility(1);
		createMessage.setVisibility(1);
		outboxText.setVisibility(1);
		outboxNumber.setVisibility(1);
		autosend.setVisibility(1);
		autosend.setVisibility(1);
		trySend.setVisibility(1);

		// setVisibility(0/1) does NOT work, useless workaround does not allow
		// restore
		// viewMessages.setBackgroundColor(0);
		// createMessage.setBackgroundColor(0);
		// outboxText.setHeight(0);
		// outboxNumber.setHeight(0);
		// autosend.setWidth(0);
		// autosend.setBackgroundColor(0);
		// trySend.setBackgroundColor(0);
	}

	public void goToUserLogin(View view) {
		startActivityForResult(new Intent(view.getContext(), UserLogin.class),
				USER_LOGIN);
	}

	public void goToCreateMessage(View view) {

		startActivityForResult(new Intent(view.getContext(),
				CreateMessage.class), CREATE_MESSAGE);

	}

	public void goToViewMessages(View view) {
		startActivityForResult(
				new Intent(view.getContext(), ViewMessages.class),
				VIEW_MESSAGES);
	}

	public void trySend(View view) {
		// TODO: actually send messages
		// Toast.makeText(getApplicationContext(), "sending...",
		// Toast.LENGTH_SHORT)
		// .show();
		ProgressDialog sending = ProgressDialog.show(Start.this, "Sending",
				"Please wait...", true);
		sending.setCanceledOnTouchOutside(true);
	}

}