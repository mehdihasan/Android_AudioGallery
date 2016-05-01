package com.example.audiogallery;

import java.io.IOException;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AudioListAdapter extends ResourceCursorAdapter {

	private final ContentResolver mContentResolver;
	private final Context mApplicationContext;
	MediaPlayer mp;
	String playing_audio_id;
	private Bitmap mNoPictureBitmap;

	public AudioListAdapter(Context context, int layout, Cursor c, int flags) {
		super(context, layout, c, flags);
		mContentResolver = context.getContentResolver();
		mApplicationContext = context.getApplicationContext();
		playing_audio_id = "";

		// Default thumbnail bitmap for when contact has no thubnail
		// mNoPictureBitmap = new
		// ImageDecoder().decodeSampledBitmapFromResource(mApplicationContext.getResources(),
		// R.drawable.ic_launcher, 50, 50);
		mNoPictureBitmap = BitmapFactory.decodeResource(
				mApplicationContext.getResources(), R.drawable.ic_launcher);
	}

	// Called when a new view is needed

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		return inflater.inflate(R.layout.audio_gallery_row, parent, false);

	}

	@Override
	public void bindView(View view, Context ctx, Cursor cursor) {
		
		// set album thumb
		ImageView albumImage = (ImageView) view.findViewById(R.id.audio_thumb);
		long albumId = cursor.getLong(cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
		Bitmap photoBitmap = getAlbumImage(albumId);
		if (null == photoBitmap) {
			albumImage.setImageBitmap(mNoPictureBitmap);
		} else {
			albumImage.setImageBitmap(photoBitmap);
		}

		// Set display name
		TextView textView = (TextView) view.findViewById(R.id.display_name);
		textView.setText(cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));

		// set media file for play
		RelativeLayout mainView = (RelativeLayout) view
				.findViewById(R.id.main_view);
		final String path = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Media.DATA));
		final String audio_id = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Media._ID));

		mainView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (playing_audio_id.equalsIgnoreCase(audio_id)) {
					// stop the play + make the player null +
					stopPlayer(null, null);
				} else {
					// start a new play + make the player null + initiate a new
					// player + play the audio + insert into playing_audio_id
					stopPlayer(path, audio_id);
				}
			}
		});

	}

	private Bitmap getAlbumImage(long albumId) {

		final Uri ART_CONTENT_URI = Uri
				.parse("content://media/external/audio/albumart");
		Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, albumId);

		Bitmap bitmap = null;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(mContentResolver,
					albumArtUri);
		} catch (Exception exception) {
			return null;
		}

		return bitmap;
	}

	private void stopPlayer(String filePath, String audioId) {
		try {
			if (null != mp) {
				mp.stop();
				mp = null;
				playing_audio_id = "";
				if (null != filePath) {
					startPlayer(filePath, audioId);
				}
			} else {
				startPlayer(filePath, audioId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startPlayer(String filePath, String audioId) {
		try {
			mp = new MediaPlayer();
			mp.setDataSource(filePath);
			mp.prepare();
			mp.start();
			playing_audio_id = audioId;
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
