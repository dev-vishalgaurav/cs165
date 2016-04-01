package edu.cs.dartmouth.cs165.myruns.vishal.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.preferences.PreferenceUtils;
import edu.cs.dartmouth.cs165.myruns.vishal.utils.Validator;

public class ProfileSettings extends BaseActivity {


    private static final int REQUEST_PICK_IMAGE = 1;

    private Toolbar mToolBar = null;
    private EditText mEdtName = null;
    private EditText mEdtEmail = null;
    private EditText mEdtPhone = null;
    private EditText mEdtClass = null;
    private EditText mEdtMajor = null;
    private RadioGroup mRgGender = null;
    private RadioButton mRbMale = null;
    private RadioButton mRbFemale = null;
    private Button mBtnCancel = null;
    private Button mBtnSave = null;
    private Button mBtnChange = null;
    private ImageView mImgProfile = null;
    private boolean isChecked = false;
    private boolean isMale = false;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        initViews();
        savedInstanceState = (savedInstanceState!=null) ? savedInstanceState : PreferenceUtils.getProfileSettings(getBaseContext()) ;
        updateValues(savedInstanceState);
    }
    private void updateImage(){
        if(imageUri != null){
            mImgProfile.setImageURI(imageUri);
        }else{
            mImgProfile.setImageResource(R.drawable.dartmouth);
        }
    }
    private void initViews() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mEdtName = (EditText) findViewById(R.id.edtName);
        mEdtEmail = (EditText) findViewById(R.id.edtEmail);
        mEdtPhone = (EditText) findViewById(R.id.edtPhone);
        mEdtClass = (EditText) findViewById(R.id.edtClass);
        mEdtMajor = (EditText) findViewById(R.id.edtMajor);
        mRgGender = (RadioGroup) findViewById(R.id.rgGender);
        mRbMale = (RadioButton) findViewById(R.id.rbMale);
        mRbFemale = (RadioButton) findViewById(R.id.rbFemale);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mBtnSave = (Button) findViewById(R.id.btnSave);
        mBtnChange = (Button) findViewById(R.id.btnChange);
        mImgProfile = (ImageView)findViewById(R.id.imgProfile);
        mBtnCancel.setOnClickListener(mOnClickListener);
        mBtnSave.setOnClickListener(mOnClickListener);
        mBtnChange.setOnClickListener(mOnClickListener);
        mRgGender.setOnCheckedChangeListener(mOnCheckChangedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_profile_settings, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        addValuesToBundle(outState);
        super.onSaveInstanceState(outState);
    }

    private void addValuesToBundle(Bundle bundle) {
        bundle.putString(PreferenceUtils.EXTRA_NAME, mEdtName.getText().toString());
        bundle.putString(PreferenceUtils.EXTRA_EMAIL, mEdtEmail.getText().toString());
        bundle.putString(PreferenceUtils.EXTRA_PHONE, mEdtPhone.getText().toString());
        bundle.putString(PreferenceUtils.EXTRA_CLASS, mEdtClass.getText().toString());
        bundle.putString(PreferenceUtils.EXTRA_MAJOR, mEdtMajor.getText().toString());
        bundle.putParcelable(PreferenceUtils.EXTRA_IMAGE, imageUri);
        bundle.putBoolean(PreferenceUtils.EXTRA_GENDER_SELECTED, isChecked);
        bundle.putBoolean(PreferenceUtils.EXTRA_GENDER, isMale);
    }

    private void updateValues(Bundle savedInstanceState) {
        if(savedInstanceState!=null) {
            mEdtName.setText(savedInstanceState.getString(PreferenceUtils.EXTRA_NAME));
            mEdtEmail.setText(savedInstanceState.getString(PreferenceUtils.EXTRA_EMAIL));
            mEdtPhone.setText(savedInstanceState.getString(PreferenceUtils.EXTRA_PHONE));
            mEdtClass.setText(savedInstanceState.getString(PreferenceUtils.EXTRA_CLASS));
            mEdtMajor.setText(savedInstanceState.getString(PreferenceUtils.EXTRA_MAJOR));
            isChecked = savedInstanceState.getBoolean(PreferenceUtils.EXTRA_GENDER_SELECTED);
            isMale = savedInstanceState.getBoolean(PreferenceUtils.EXTRA_GENDER);
            imageUri = savedInstanceState.getParcelable(PreferenceUtils.EXTRA_IMAGE);
            updateRadioButton();
            updateImage();
        }
    }

    private void updateRadioButton() {
        if (isChecked) {
            if (isMale) {
                mRbMale.setChecked(true);
                mRbFemale.setChecked(false);
            } else {
                mRbFemale.setChecked(true);
                mRbMale.setChecked(false);
            }
        }
    }

    private void onClickCancel() {
        deleteTempFile();
        finish();
    }

    private void onClickSave() {
        if (isValidationPassed()) {
            // Success save to preferences
            Bundle values = new Bundle();
            updateImageFile();
            addValuesToBundle(values);
            PreferenceUtils.saveProfileSettings(getBaseContext(), values);
            showToast(getString(R.string.value_saved));
            finish();
        } else {
            // fail
        }
    }
    private void deleteTempFile(){
        File file = new File(Uri.fromFile(new File(getCacheDir(), "cropped_temp")).getPath());
        if (file.exists()) {
            file.delete();
        }
    }
    private void updateImageFile(){
        try {
            if (imageUri != null) {
                File file = new File(Uri.fromFile(new File(getCacheDir(), "cropped_temp")).getPath());
                if (file.exists()) {
                    File origFIle = new File(Uri.fromFile(new File(getCacheDir(), "cropped")).getPath());
                    if (origFIle.exists()) {
                        origFIle.delete();
                    }
                    copy(file, origFIle);
                    file.delete();
                    imageUri = Uri.fromFile(origFIle);
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


    private void onClickChangeImage() {
        openGallery();
    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped_temp"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            imageUri = Crop.getOutput(result);
            printTrace("Image Uri = " + imageUri);
            mImgProfile.setImageURI(imageUri);
        } else if (resultCode == Crop.RESULT_ERROR) {
           showToast(Crop.getError(result).getMessage());
        }
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_PICK_IMAGE);
    }

    private boolean checkEmail() {
        boolean result = false;
        String text = mEdtEmail.getText().toString().trim();
        result = Validator.isValidEmail(text);
        showError(result,mEdtEmail,getString(R.string.invalid_entry));
        return result;
    }

    private boolean checkName() {
        boolean result = false;
        String text = mEdtName.getText().toString().trim();
        result = Validator.isValidLength(text,Validator.NAME_LENGTH);
        showError(result,mEdtName,getString(R.string.invalid_entry));
        return result;
    }

    private boolean checkPhone() {
        boolean result = false;
        String text = mEdtPhone.getText().toString().trim();
        result = Validator.isValidLength(text,Validator.NAME_LENGTH);
        showError(result,mEdtPhone,getString(R.string.invalid_entry));
        return result;
    }

    private boolean checkMajor() {
        boolean result = false;
        String text = mEdtMajor.getText().toString().trim();
        result = Validator.isValidLength(text,Validator.NAME_LENGTH);
        showError(result,mEdtMajor,getString(R.string.invalid_entry));
        return result;
    }

    private boolean checkClass() {
        boolean result = false;
        String text = mEdtClass.getText().toString().trim();
        result = Validator.isValidLength(text,Validator.NAME_LENGTH);
        showError(result,mEdtClass,getString(R.string.invalid_entry));
        return result;
    }

    private boolean checkGender() {
        boolean result =  isChecked;
        if(!result){
            showToast(getString(R.string.required_message_gender));
        }
        return  result;
    }

    private boolean isValidationPassed() {
        boolean result = false;
        result = checkName() && checkEmail() && checkPhone() && checkGender() && checkClass() && checkMajor();
        return result;
    }

    private void onGenderSelected(int checkedId) {
        isChecked = true;
        isMale = checkedId == (R.id.rbMale);
    }

    private void onActivityResultImagePick(int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            if (imageUri!=null){
                beginCrop(imageUri);
            }
            printTrace(imageUri.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_PICK_IMAGE:{
                onActivityResultImagePick(resultCode,data);
            }
            break;
            case Crop.REQUEST_CROP:{
                handleCrop(resultCode,data);
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void saveToPreferences(Bundle bundle){

    }
    private RadioGroup.OnCheckedChangeListener mOnCheckChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (group.getId()) {
                case R.id.rgGender: {
                    onGenderSelected(checkedId);
                }
                break;
            }
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCancel: {
                    onClickCancel();
                }
                break;
                case R.id.btnSave: {
                    onClickSave();
                }
                break;
                case R.id.btnChange: {
                    onClickChangeImage();
                }
                break;

            }
        }
    };
}
