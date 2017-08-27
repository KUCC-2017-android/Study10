package kr.ac.korea.kucc.study10.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.ac.korea.kucc.study10.R;
import kr.ac.korea.kucc.study10.adapter.MemoAdapter;
import kr.ac.korea.kucc.study10.data.Memo;

public class MemoListActivity extends AppCompatActivity {
    public static int REQ_CODE_INPUT = 100;
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";

    private MemoAdapter adapter;
    private List<Memo> memoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        memoList = loadMemoList();

        adapter = new MemoAdapter(memoList, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_memo);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_memolist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startMemoAddActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void startMemoAddActivity() {
        Intent intent = new Intent(this, MemoInputActivity.class);
        int id = 0;
        if (memoList.size() != 0) {
            id = Integer.parseInt(memoList.get(memoList.size()-1).getId()) + 1;
        }
        intent.putExtra(MemoInputActivity.KEY_ID, id);
        startActivityForResult(intent, REQ_CODE_INPUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_INPUT) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra(KEY_ID);
                String title = data.getStringExtra(KEY_TITLE);
                String content = data.getStringExtra(KEY_CONTENT);
                Memo memo = new Memo(id, title, content);
                try {
                    saveMemo(memo);
                    int index = Integer.parseInt(memo.getId());
                    if (index < memoList.size()) {
                        Memo oldMemo = memoList.get(index);
                        oldMemo.setTitle(memo.getTitle());
                        oldMemo.setContent(memo.getContent());
                    } else {
                        memoList.add(memo);
                    }
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "파일 저장에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private List<Memo> loadMemoList() {
        List<Memo> result = new ArrayList<>();
        String[] fileList = getFilesDir().list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches("[0-9+]");
            }
        });
        for (String filename : fileList) {
            try {
                result.add(loadMemo(filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void saveMemo(Memo memo) throws IOException {
        FileOutputStream outputStream = openFileOutput(memo.getId(), MODE_PRIVATE);
        outputStream.write(memo.getTitle().getBytes());
        outputStream.write("\n".getBytes());
        outputStream.write(memo.getContent().getBytes());
        outputStream.close();
    }

    private Memo loadMemo(String fileName) throws IOException {
        FileInputStream inputStream = openFileInput(fileName);
        byte[] buffer = new byte[1024];
        StringBuilder builder = new StringBuilder();
        int count = -1;
        while ((count = inputStream.read(buffer)) != -1) {
            builder.append(new String(buffer, 0, count));
        }
        String[] parsed = builder.toString().split("\n", 2);
        inputStream.close();
        return new Memo(fileName, parsed[0], parsed[1]);
    }
}
