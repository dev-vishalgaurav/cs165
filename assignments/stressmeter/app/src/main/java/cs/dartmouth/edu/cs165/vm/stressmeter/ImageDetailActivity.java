package cs.dartmouth.edu.cs165.vm.stressmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageDetailActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_ID = "extra_image_id";
    public static final String EXTRA_IMAGE_POSITION = "extra_image_position";

    private ImageView mImgDetail = null;
    private Button mBtnCancel = null;
    private Button mBtnSubmit = null;
    private int imageId = -1;
    private int imagePosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        initViews();
        setResult(RESULT_CANCELED);
        updateDateFromIntent();
    }

    private void initViews() {
        mImgDetail = (ImageView) findViewById(R.id.imgDetail);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mBtnSubmit.setOnClickListener(mOnClickListener);
        mBtnCancel.setOnClickListener(mOnClickListener);

    }

    private void updateView() {
        mImgDetail.setImageResource(imageId);
    }

    private boolean updateDateFromIntent() {
        Intent intent = getIntent();
        imageId = intent.getIntExtra(EXTRA_IMAGE_ID, -1);
        imagePosition = intent.getIntExtra(EXTRA_IMAGE_POSITION, -1);
        if (imageId > 0 && imagePosition > 0) {
            updateView();
            return true;
        }
        return false;
    }

    private void onClickSubmit() {
        setResult(RESULT_OK);
        finish();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCancel: {
                    finish();
                }
                break;
                case R.id.btnSubmit: {
                    onClickSubmit();
                }
                break;
            }
        }
    };
}
