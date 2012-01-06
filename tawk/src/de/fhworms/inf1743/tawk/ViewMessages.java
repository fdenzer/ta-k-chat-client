package de.fhworms.inf1743.tawk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

public final class ViewMessages extends Activity {

	// if you change the default displayed message number,
	// change R.string.default_list_more accordingly
	final int DEFAULT_MESSAGE_NUMBER = 20;

	static final String MESSAGE = "message";

	// returns will be handled from
	final int CREATE_MESSAGE = 1;

	// Dialog will be shown for
	final int USER_INFO = 0;

	SharedPreferences dataStore;

	Dialog dialog;

	Button listMore;
	TextView topMessage;
	Integer n;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.view_messages);
		listMore = (Button) findViewById(R.id.list_more_button);

		// TODO : for only one of n messages a listener is implemented
		topMessage = (TextView) findViewById(R.id.user_text1);

		topMessage.setOnClickListener(new OnClickListener() {

			boolean resized = false;

			public void onClick(View v) {

				resized = resize(resized, v);
			}

		});

		topMessage.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View v) {
				showUserInfo();
				return false;
			}
		});

		n = DEFAULT_MESSAGE_NUMBER;
	}

	/** This is called only once. Changes to the dialog go in onPrepareDialog(). */
	@Override
	protected Dialog onCreateDialog(int id) {
		dialog = null;
		switch (id) {
		case USER_INFO:
			dialog = new Dialog(this);
			dialog.setContentView(R.layout.user_info);
			dialog.setTitle("User information");
			Button back = (Button) dialog
					.findViewById(R.id.cancel_dialog_button);

			back.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			break;
		default:
			break;
		}

		return dialog;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_messages_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		if (item.getItemId() == R.id.refresh_menu_item) {
			// TODO: refresh messages
		}
		return false;
	}

	public void listMore(View view) {
		n += n;
		listMore.setText("list " + n + " more");
	}

	public boolean resize(boolean resized, View v) {
		if (resized) {
			((TextView) v).setHeight(40);
		} else {
			((TextView) v).setHeight(200);
		}
		return !resized;
	}

	public void showUserInfo() {
		showDialog(USER_INFO);
	}

	public void goToCreateMessage(View view) {

		startActivityForResult(new Intent(view.getContext(),
				CreateMessage.class), CREATE_MESSAGE);
	}
}
