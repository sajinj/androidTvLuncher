package com.jacky.compatible.launcher.features.garbageclear;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jacky.launcher.R;
import com.jacky.compatible.launcher.utils.ClearUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class GarbageClear extends Activity {
    protected static final int FOUND_FINISH = 0;
    protected static final int CLEAR_FINISH = 1;
    private Button startFound, startClear;
    private List<File> list;
    private ProgressBar progressDisplay;
    private static final String[] CLEAR_TYPE = {".apk", ".log"};
    private boolean isInstall;
    private RelativeLayout foundLayout;
    private FrameLayout clearLayout;
    private ImageView roundImg;
    private Animation animation;
    private TextView filePath;
    private ImageView dialogImg;
    private long fileGrbagesize;
    private TextView grbageSize;
    private boolean found;      // If false then scan did not complete
    private int taskNum;
    private int progressbarNum;
    private List<FoundTask> tasklist;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FOUND_FINISH:
                    // Find it
                    found = true;
                    startFound.setText(getResources().getString(R.string.start_cleaning));
                    filePath.setText(getResources().getString(R.string.find_it));
                    startFound.setClickable(true);
                    progressDisplay.setProgress(100);
                    progressbarNum = 0;
                    dialogImg.setImageResource(R.drawable.dialog_center_img);
                    break;
                case CLEAR_FINISH:
                    // Clean up
                    found = false;
                    startClear.setClickable(true);
                    startFound.setText(getResources().getString(R.string.start_clear));
                    startClear.setText(getResources().getString(R.string.clean_up));
                    grbageSize.setText(getResources().getString(R.string.zero));
                    startFound.setClickable(true);
                    animation = null;
                    fileGrbagesize = 0;
                    progressDisplay.setProgress(progressbarNum);
                    roundImg.setAnimation(animation);
                    roundImg.setVisibility(View.GONE);
                    dialogImg.setImageResource(R.drawable.finish_clear);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.garbage_activity_main);
        Init();
        Linstener();
    }

    public void Init() {
        list = new ArrayList<>();
        tasklist = new ArrayList<>();
        animation = AnimationUtils.loadAnimation(GarbageClear.this, R.anim.dialog_anmiation);
        progressDisplay = (ProgressBar) findViewById(R.id.progressBar1);
        startFound = (Button) findViewById(R.id.start_found);
        filePath = (TextView) findViewById(R.id.file_path);
        startClear = (Button) findViewById(R.id.start_clear);
        roundImg = (ImageView) findViewById(R.id.round_img);
        grbageSize = (TextView) findViewById(R.id.garbage_size);
        dialogImg = (ImageView) findViewById(R.id.dialog_img);
        foundLayout = (RelativeLayout) findViewById(R.id.found_layout);
        clearLayout = (FrameLayout) findViewById(R.id.clear_layout);
    }

    @SuppressLint("SdCardPath")
    public void Linstener() {
        startClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                clearLayout.setVisibility(View.GONE);
                foundLayout.setVisibility(View.VISIBLE);
            }
        });
        startFound.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startFound.setClickable(false);
                if (!found) {
                    startFound.setText(getResources().getString(R.string.scanning));
                    startClear.setClickable(false);
                    new FoundTask(Environment.getExternalStorageDirectory()
                            + "/", CLEAR_TYPE).execute();
                } else {
                    if (fileGrbagesize != 0) {
                        startClear.setClickable(false);
                        clearLayout.setVisibility(View.VISIBLE);
                        foundLayout.setVisibility(View.GONE);
                        roundImg.setAnimation(animation);
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                DeleteFile();
                                Message message = new Message();
                                message.what = CLEAR_FINISH;
                                handler.sendMessage(message);
                            }
                        }).start();
                    } else {
                        startFound.setClickable(true);
                        Toast.makeText(GarbageClear.this, getResources().getString(R.string.no_current_cleanup_required), Toast.LENGTH_SHORT)
                                .show();
                    }
                }

            }
        });
    }

    public void DeleteFile() {
        for (File file : list) {
            file.delete();
        }
        list.clear();
    }

    /*
     * Scan file asynchronous task
     */
    class FoundTask extends AsyncTask<Void, String, List<FoundTask>> {
        private final String path;
        private final String[] extension;

        public FoundTask(String path, String[] extension) {
            this.path = path;
            this.extension = extension;
        }

        @Override
        protected List<FoundTask> doInBackground(Void... arg0) {
            final File[] files = new File(path).listFiles();
            String filePath = null;
            for (File file : files) {
                if (file.isFile()) {
                    publishProgress(file.getPath());
                    for (int i = 0; i < CLEAR_TYPE.length; i++) {
                        if (file.getPath()
                                .substring(
                                        file.getPath().length()
                                                - extension[i].length())
                                .equals(extension[i])) {
                            filePath = file.getAbsolutePath();
                            isInstall = ClearUtil.TakeIsInstallApk(filePath,
                                    GarbageClear.this);
                            // Determine if it is not installed
                            if (!isInstall) {
                                long size = ClearUtil.getFileSize(file);
                                fileGrbagesize = fileGrbagesize + size;
                                list.add(file);
                            }
                        }
                    }
                } else if (file.isDirectory() && file.getPath().indexOf("/.") == -1) {
                    tasklist.add(new FoundTask(file.getPath(), extension));
                    taskNum++;
                }
            }
            return tasklist;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // Change the displayed file path
            String value = values[0];
            filePath.setText(value);
        }

        @Override
        protected void onPostExecute(List<FoundTask> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Completed execution starts the next task
            if (result != null && taskNum != 0) {
                tasklist.get(0).execute();
                grbageSize.setText(String.valueOf((int) ((float) fileGrbagesize / 1024 / 1024 / 2)));
                taskNum--;
                tasklist.remove(0);
                if (progressbarNum < 100) {
                    progressbarNum++;
                    progressDisplay.setProgress(progressbarNum);
                } else if (progressbarNum == 100) {
                    progressbarNum = 0;
                }
            }
            // Task execution completed
            else if (taskNum == 0) {
                Message message = new Message();
                message.what = FOUND_FINISH;
                handler.sendMessage(message);
            }

        }

    }
}