package com.piotrekwitkowski.libraryreader;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {
    private static final String TAG = "MainActivity";
    private static NfcAdapter nfcAdapter;
    private static LibraryReader libraryReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        libraryReader = new LibraryReader();
        TextView logTextView = findViewById(R.id.logTextView);
        Log.setLogTextView(logTextView);
        Log.reset(TAG, "onCreate()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
        Log.i(TAG, "NFC adapter enabled. Waiting for a card...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        nfcAdapter.disableReaderMode(this);
        Log.i(TAG, "NFC adapter disabled.");
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        Log.reset(TAG, "onTagDiscovered()");
        libraryReader.processTag(tag);
    }
}
