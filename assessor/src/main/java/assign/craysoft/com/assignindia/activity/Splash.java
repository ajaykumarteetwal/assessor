package assign.craysoft.com.assignindia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import org.json.JSONObject;

import assign.craysoft.com.assignindia.R;
import assign.craysoft.com.assignindia.bean.Home;
import assign.craysoft.com.assignindia.network.NetworkUtil;
import assign.craysoft.com.assignindia.util.Constant;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        animation2(findViewById(R.id.logoImage));
        initHome();
    }

    private void animation2(View view) {
        float scaleFrom = 1f;
        float scaleTo = .95f;
        Animation scaleAnimation = new ScaleAnimation(scaleFrom, scaleTo, scaleFrom, scaleTo, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        scaleAnimation.setDuration(10000);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setInterpolator(new android.view.animation.BounceInterpolator());
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(scaleAnimation);
    }

    private void initHome() {
        NetworkUtil.getInstance().connectPostRequest(this, Constant.URL_TEACHER_HOME, "", new NetworkUtil.CallBack() {
            @Override
            public void done(JSONObject jsonObject) {
                Home home = new Home(jsonObject);
                launchHomeScreen(home);
            }

            @Override
            public void error(JSONObject jsonObject) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }, false);
    }

    private void launchHomeScreen(Home home) {
        Intent intent = new Intent(Splash.this, AssessmentHomeActivity.class);
        intent.putExtra(AssessmentHomeActivity.DATA, home);
        startActivityForResult(intent, 101);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        super.startActivityForResult(intent, requestCode);
    }
}
