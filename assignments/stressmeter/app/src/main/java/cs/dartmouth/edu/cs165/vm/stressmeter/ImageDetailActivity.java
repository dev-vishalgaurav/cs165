package cs.dartmouth.edu.cs165.vm.stressmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

    /*
     Main setup
      */
    private void initViews() {
        mImgDetail = (ImageView) findViewById(R.id.imgDetail);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mBtnSubmit.setOnClickListener(mOnClickListener);
        mBtnCancel.setOnClickListener(mOnClickListener);

    }

    /*
    Update view
     */
    private void updateView() {
        mImgDetail.setImageResource(imageId);
        Log.e("VVV","updateView");
    }

    /*
    Update date from intent
     */
    private boolean updateDateFromIntent() {
        Intent intent = getIntent();
        imageId = intent.getIntExtra(EXTRA_IMAGE_ID, -1);
        imagePosition = intent.getIntExtra(EXTRA_IMAGE_POSITION, -1);
        Log.e("VVV", "Grid position = " + imagePosition + " resId = " + imageId);
        if (imageId > 0 && imagePosition >= 0) {
            updateView();
            return true;
        }
        return false;
    }

    /*
     Save info upon user submission
      */
    private void onClickSubmit() {
        if(PSM.saveRecordSuccess(imagePosition)) {
            setResult(RESULT_OK);
        }else{
            Toast.makeText(getBaseContext(),getString(R.string.error_in_recording),Toast.LENGTH_SHORT).show();
        }
        finish();

    }

    /*
    Exit when cancel clicked, save info when submit clicked
     */
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
