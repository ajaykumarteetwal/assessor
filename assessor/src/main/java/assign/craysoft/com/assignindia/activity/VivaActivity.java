package assign.craysoft.com.assignindia.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import assign.craysoft.com.assignindia.R;
import assign.craysoft.com.assignindia.bean.QuestionBean;
import assign.craysoft.com.assignindia.fragment.QuestionFragment;
import assign.craysoft.com.assignindia.util.PermissionUtil;
import assign.craysoft.com.assignindia.util.Util;

public class VivaActivity extends AppCompatActivity {

    public static final String EXAM_TYPE = "EXAM_TYPE";
    public static final String STUDENT_ID = "STUDENT_ID";
    private static final int ACTION_TAKE_VIDEO = 222;
    private static final int ACTION_TAKE_ID_IMAGE = 223;
    private static final int ACTION_TAKE_STUDENT_IMAGE = 224;
    public static double time;
    public static double examDuration;
    private TextView vivaQuestion;
    private EditText vivaRemarkEditText;
    private Button vivaRecordVideoButton;
    private Button vivaMaxMarkButton;
    private Button vivaSelectMarkButton;
    private Button vivaNextButton;
    private Button vivaViewAllButton;
    private QuestionFragment questionFragment;
    private QuestionFragment.QuestionInteractionListener interactionListener;
    private ExamType examType;
    private String studentId;
    private QuestionBean bean;
    private RelativeLayout fragmentContainner;
    private CountDownTimer countDownTimer;
    private TextView vivaRemainingTimerText;
    private PermissionUtil permissionUtil;
    private boolean isExamDurationLimitEnable;
    private ImageView idImageView;
    private ImageView studentImageView;
    private Uri uri;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viva);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


        vivaQuestion = findViewById(R.id.vivaQuestion);
        vivaRemarkEditText = findViewById(R.id.vivaRemarkEditText);
        vivaRecordVideoButton = findViewById(R.id.vivaRecordVideoButton);
        vivaMaxMarkButton = findViewById(R.id.vivaMaxMarkButton);
        vivaSelectMarkButton = findViewById(R.id.vivaSelectMarkButton);
        vivaNextButton = findViewById(R.id.vivaNextButton);
        vivaViewAllButton = findViewById(R.id.vivaViewAllButton);
        examType = ExamType.values()[getIntent().getIntExtra(EXAM_TYPE, 0)];
        studentId = getIntent().getStringExtra(STUDENT_ID);
        fragmentContainner = findViewById(R.id.fragmentContainner);
        vivaRemainingTimerText = findViewById(R.id.vivaRemainingTimerText);
        addQuestionFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setQuestion(QuestionBean questionBean) {
        submitQuestion();
        if (questionBean != null) {
            this.bean = questionBean;
            time = questionBean.getDuration();
            vivaQuestion.setText(questionBean.getQuestion() == null ? "" : questionBean.getQuestion());
            if (questionBean.getRemark() != null) {
                vivaRemarkEditText.setText(questionBean.getRemark());
                vivaRemarkEditText.setSelection(questionBean.getRemark().length());
            } else
                vivaRemarkEditText.setText("");
            vivaMaxMarkButton.setText("Max. Mark : " + String.valueOf(questionBean.getMaxMark()));
            if (questionBean.getMark() == 0)
                vivaSelectMarkButton.setText("Select Mark");
            else
                vivaSelectMarkButton.setText("Mark " + questionBean.getMark());
        } else {
            Toast.makeText(this, "No more Questions", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitQuestion() {
        if (bean != null) {
            if (!vivaRemarkEditText.getText().toString().trim().isEmpty()) {
                bean.setRemark(vivaRemarkEditText.getText().toString().trim());
            }
            bean.setDuration((long) time);
            time = 0;
            if (interactionListener != null)
                interactionListener.onQuestionAnswered(bean);
            vivaQuestion.setText("");
            bean = null;
        }
    }

    private void startTimer() {
        vivaRemainingTimerText.setText("");
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer((long) examDuration, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (isExamDurationLimitEnable)
                        vivaRemainingTimerText.setText(String.valueOf("Time Left: " + Util.checkDigit((long) examDuration)));
//                    else
//                        vivaRemainingTimerText.setText(String.valueOf("Time: " + Util.checkDigit((long) time)));
                    time += 1000;
                    if (isExamDurationLimitEnable)
                        examDuration -= examDuration;
                }

                public void onFinish() {
                    if (isExamDurationLimitEnable) {
                        vivaRemainingTimerText.setText("Timer Over");
//                    else
//                        vivaRemainingTimerText.setText(String.valueOf("Time: " + Util.checkDigit((long) time)));

                        AlertDialog.Builder builder = new AlertDialog.Builder(VivaActivity.this);
                        builder.setMessage("Time over your " + getTitle() + " will be submit automatically");
                        builder.setCancelable(false);
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                if (interactionListener != null) {
                                    interactionListener.submitAllAnswers(VivaActivity.this, time);
                                }
                                onBackPressed();
                            }
                        });
                        builder.create().show();
                    }
                    countDownTimer = null;
                }
            };
            countDownTimer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void addQuestionFragment() {
        questionFragment = QuestionFragment.newInstance(examType.toString(), studentId);
        interactionListener = questionFragment.new QuestionInteractionListener() {

            @Override
            public boolean onQuestionsChangeRequest(QuestionBean question) {
                if (question.getQuestionId() != bean.getQuestionId())
                    setQuestion(question);
                else
                    Toast.makeText(VivaActivity.this, "Already Selected", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onQuestionsReceived(int questionCount) {
                takePhoto();
            }
        };
        Util.loadFragment(this, questionFragment, "Questions");
    }

    private void takePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_capture_images, null);
        idImageView = view.findViewById(R.id.idImageView);
        studentImageView = view.findViewById(R.id.studentImageView);
        Button idImage = view.findViewById(R.id.idImage);
        Button studentImage = view.findViewById(R.id.studentImage);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage(view.getId() != R.id.studentImage ? 0 : 1);
            }
        };
        Button doneButton = view.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interactionListener.getIdImageUri() != null && interactionListener.getStudentImageUri() != null) {
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                        alertDialog = null;
                    }
                    String type = interactionListener.getType();
                    if (type != null)
                        setTitle(type);
                    QuestionBean bean = interactionListener.getNextQuestion();
                    setQuestion(bean);
                    isExamDurationLimitEnable = interactionListener.getDuration() > 0;
                    if (isExamDurationLimitEnable)
                        examDuration = interactionListener.getDuration();
                    else
                        examDuration = 24 * 60 * 60 * 1000;
                    time = 0;
                    startTimer();
                } else
                    Toast.makeText(VivaActivity.this, "Please Capture Both Images ", Toast.LENGTH_SHORT).show();
            }
        });
        idImage.setOnClickListener(listener);
        studentImage.setOnClickListener(listener);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void onRecordVideoClick(View view) {
        if (permissionUtil == null)
            permissionUtil = new PermissionUtil();
        String[] strings = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        permissionUtil.requestPermission(this, strings, "To store video in local storage", new PermissionUtil.PermissionObserver() {
            @Override
            public void notifyObservers(boolean status) {
                if (status) {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
//                    intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 5491520L);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    File file = Util.getOutputMediaFile(getApplicationContext());
//                    Uri fromFile = Uri.fromFile(file);
//                    fromFile = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileProvider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file.getAbsolutePath());
                    startActivityForResult(intent, ACTION_TAKE_VIDEO);
                } else permissionUtil.openAppSetting(VivaActivity.this);
            }
        });
    }

    private void captureImage(final int imageOf) {
        if (permissionUtil == null)
            permissionUtil = new PermissionUtil();
        String[] strings = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        permissionUtil.requestPermission(this, strings, "To store video in local storage", new PermissionUtil.PermissionObserver() {
            @Override
            public void notifyObservers(boolean status) {
                if (status) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = Util.getOutputImageFile(VivaActivity.this);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getUri(file, intent));
                    startActivityForResult(intent, imageOf == 0 ? ACTION_TAKE_ID_IMAGE : ACTION_TAKE_STUDENT_IMAGE);
                } else permissionUtil.openAppSetting(VivaActivity.this);
            }
        });
    }

    private Uri getUri(File file, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else
            uri = Uri.fromFile(file);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return uri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionUtil != null)
            permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ACTION_TAKE_VIDEO: {
                    if (data != null) {
                        Uri videoUri = data.getData();
                        MediaPlayer mp = MediaPlayer.create(this, videoUri);
                        int duration = mp.getDuration();
                        mp.release();
                        if (duration < (30 * 1000)) {
                            Toast.makeText(this, "Video Length should be minimum 30 second please record again", Toast.LENGTH_LONG).show();
                            getContentResolver().delete(videoUri, null, null);
                        } else if (bean != null)
                            bean.setVideoUri(videoUri);
                    }
                }
                break;
                case ACTION_TAKE_ID_IMAGE:
                    if (uri != null && idImageView != null) {
                        idImageView.setImageURI(uri);
                        interactionListener.setIdImageUri(uri);
                        uri = null;
                    }
                    break;
                case ACTION_TAKE_STUDENT_IMAGE:
                    if (uri != null && idImageView != null) {
                        studentImageView.setImageURI(uri);
                        interactionListener.setStudentImageUri(uri);
                        uri = null;
                    }
                    break;
                default:
            }
        }
    }

    public void onMaxMarkClick(View view) {
        Toast.makeText(this, "You can give maximum " + bean.getMaxMark() + " Mark.", Toast.LENGTH_SHORT).show();
    }

    public void onSelectMarkClick(View view) {
        if (examType.ordinal() == VivaActivity.ExamType.EXAM_TYPE_VIVA.ordinal() || bean.getVideoUri() != null) {
            if (!vivaRemarkEditText.getText().toString().trim().isEmpty()) {
                bean.setRemark(vivaRemarkEditText.getText().toString().trim());
            }
            if (bean.getRemark() != null && bean.getRemark().length() > 4) {
                PopupMenu popup = new PopupMenu(this, view);
                for (int index = 0; index < bean.getMaxMark(); index++) {
                    MenuItem menuItem = popup.getMenu().add(String.valueOf(index + 1));
                    if (bean.getMark() == (index + 1))
                        menuItem.setIcon(android.R.drawable.ic_menu_crop);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int selection = Integer.valueOf(item.getTitle().toString());
                        if (bean != null)
                            bean.setMark(selection);
                        vivaSelectMarkButton.setText("Mark " + selection);
                        return true;
                    }
                });
                popup.show();
            } else {
                Util.showDialog(VivaActivity.this, "Please give remark first minimum length is 4", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        vivaRemarkEditText.requestFocus();
                    }
                });
            }
        } else {
            Util.showDialog(this, "Please Capture Video first minimum length is 30 Sec", null);
        }
    }

    public void onNextClick(View view) {
        if (bean != null && bean.getMark() == 0) {
            Util.showDialog(VivaActivity.this, "Current question is not complete want to continue ?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (interactionListener != null) {
                        QuestionBean questionBean = interactionListener.getNextQuestion();
                        setQuestion(questionBean);
                    }
                }
            });
        } else if (interactionListener != null) {
            QuestionBean questionBean = interactionListener.getNextQuestion();
            setQuestion(questionBean);
        }
    }

    public void onViewAllClick(View view) {
        View vivaQuestionLinearLayout = findViewById(R.id.vivaQuestionLinearLayout);
        if (vivaQuestionLinearLayout != null) {
            vivaQuestionLinearLayout.setVisibility(View.GONE);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragmentContainner.getLayoutParams();
        layoutParams.weight = 100;
    }

    public void onSubmitAllClick(View view) {
        if (interactionListener != null && interactionListener.getNonAttemptedCount() > 0 && !interactionListener.isSubmit()) {
            Util.showDialog(VivaActivity.this, "Currently " + interactionListener.getNonAttemptedCount() + " question is not complete\n\n" +
                    "Are you sure want to submit ?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (interactionListener != null) {
                        interactionListener.submitAllAnswers(VivaActivity.this, time);
                        onBackPressed();
                    }
                }
            });
        } else {
            if (interactionListener != null) {
                interactionListener.submitAllAnswers(this, time);
                onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        View vivaQuestionLinearLayout = findViewById(R.id.vivaQuestionLinearLayout);
        if (vivaQuestionLinearLayout != null && vivaQuestionLinearLayout.getVisibility() == View.GONE) {
            vivaQuestionLinearLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragmentContainner.getLayoutParams();
            layoutParams.weight = 35;
        } else {
            if (interactionListener != null && interactionListener.getNonAttemptedCount() > 0 && !interactionListener.isSubmit()) {
                Util.showDialog(VivaActivity.this, "Current questions is not complete\n\n" +
                        "All question will be submit with current answered\n\n" +
                        "Are you sure want to back ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VivaActivity.super.onBackPressed();
                        finish();
                    }
                });
            } else {
                VivaActivity.super.onBackPressed();
                finish();
            }
        }
    }

    public enum ExamType {
        EXAM_TYPE_VIVA("Viva"), EXAM_TYPE_DEMO("Demo");
        private String type;

        ExamType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
