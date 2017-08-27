package kr.ac.korea.kucc.study10.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import kr.ac.korea.kucc.study10.R;

public class MemoInputActivity extends AppCompatActivity {
    public static final String KEY_MODIFY = "modify";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";

    private EditText titleEdit;
    private EditText contentEdit;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.titleEdit = (EditText) findViewById(R.id.input_title);
        this.contentEdit = (EditText) findViewById(R.id.input_content);

        Intent intent = getIntent();

        if (intent.getBooleanExtra(KEY_MODIFY, false)) {
            titleEdit.setText(intent.getStringExtra(KEY_TITLE));
            contentEdit.setText(intent.getStringExtra(KEY_CONTENT));
        }

        id = intent.getIntExtra(KEY_ID, -1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_memoinput, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_confirm:
                closeActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void closeActivity() {
        Intent result = new Intent();
        result.putExtra(MemoListActivity.KEY_ID, String.valueOf(id));
        result.putExtra(MemoListActivity.KEY_TITLE, titleEdit.getText().toString());
        result.putExtra(MemoListActivity.KEY_CONTENT, contentEdit.getText().toString());
        setResult(RESULT_OK, result);
        finish();
    }
}
