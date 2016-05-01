package com.example.audiogallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

@SuppressLint("NewApi")
public class AudioGalleryMainActivity extends Activity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private AudioListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_gallery_grid);
		GridView audioGrid = (GridView) findViewById(R.id.audio_grid);
		
		// Create and set empty adapter
		mAdapter = new AudioListAdapter(this, R.layout.audio_gallery_row, null, 0);
		audioGrid.setAdapter(mAdapter);

		// Initialize the loader
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**************************************************************************/
	/**************************************************************************/
	/**
	 * mehdi:: 25th Nov, 2014 LoaderManager callbacks
	 * 
	 */
	/**************************************************************************/
	/**************************************************************************/

	static final String[] AUDIO_LIST = new String[]{
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.SIZE,
        MediaStore.Audio.Media.MIME_TYPE,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.ALBUM_ID,
	};
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " ASC";

		return new CursorLoader(
				this, 
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
				AUDIO_LIST,
				null, 
				null, 
				sortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

		mAdapter.swapCursor(null);
	}
}
