package edu.cs.dartmouth.cs165.myruns.vishal.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.storage.preferences.PreferenceUtils;
import edu.cs.dartmouth.cs165.myruns.vishal.utils.Validator;

/**
 * Implements the user profile and its settings.
 */
public class ProfileSettings extends BaseActivity {

    private static final int REQUEST_PICK_IMAGE_GALLERY = 1;
    private static final int REQUEST_PICK_IMAGE_CAMERA = 2;

    private static final String EXTRA_DIALOG_SHOWN_GALLERY = "extra_dialog_shown";

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
    private boolean isDialogGallery = false;
    private Uri imageUri = null;
    private AlertDialog mDialogImageChange;
    private Uri mCurrentCameraFilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        initViews();
        savedInstanceState = (savedInstanceState!=null) ? savedInstanceState : PreferenceUtils.getProfileSettings(getBaseContext()) ;
        updateValues(savedInstanceState);
    }

    /**
     * Updates the profile picture to the stored image.
     * If nothing stored, updates it to the default Dartmouth picture.
     */
    private void updateImage(){
        if(imageUri != null){
            mImgProfile.setImageURI(imageUri);
        }else{
            mImgProfile.setImageResource(R.drawable.dartmouth);
        }
    }

    /**
     * Main setup
     */
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

    /**
     * Handles dialog for picture-taking according to the user's choice of camera or gallery.
     */
    private void initDialogChangeImage(){
        Dialog.OnClickListener mOnDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isDialogGallery = false;
                switch (which) {
                    case Dialog.BUTTON_POSITIVE: {
                        openCamera();
                    }
                    break;

                    case Dialog.BUTTON_NEGATIVE: {
                        openGallery();
                    }
                    break;
                    case Dialog.BUTTON_NEUTRAL: {
                    }
                    break;
                }
            }
        };
        if (mDialogImageChange == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettings.this);
            builder.setTitle(R.string.dialog_image_picker_title);
            builder.setMessage(getString(R.string.dialog_image_picker_message));
            builder.setPositiveButton(getString(R.string.camera), mOnDialogClickListener);
            builder.setNeutralButton(getString(R.string.cancel), mOnDialogClickListener);
            builder.setNegativeButton(getString(R.string.gallery), mOnDialogClickListener);
            mDialogImageChange = builder.create();
        }
    }

    /**
     * Inits dialog for picture-taking, showing user option of camera or gallery.
     */
    private void showDialogChangeImage(){
        initDialogChangeImage();
        mDialogImageChange.show();
        isDialogGallery = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        addValuesToBundle(outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * Saves user-entered data.
     * @param bundle
     */
    private void addValuesToBundle(Bundle bundle) {
        bundle.putString(PreferenceUtils.EXTRA_NAME, mEdtName.getText().toString());
        bundle.putString(PreferenceUtils.EXTRA_EMAIL, mEdtEmail.getText().toString());
        bundle.putString(PreferenceUtils.EXTRA_PHONE, mEdtPhone.getText().toString());
        bundle.putString(PreferenceUtils.EXTRA_CLASS, mEdtClass.getText().toString());
        bundle.putString(PreferenceUtils.EXTRA_MAJOR, mEdtMajor.getText().toString());
        bundle.putParcelable(PreferenceUtils.EXTRA_IMAGE, imageUri);
        bundle.putBoolean(PreferenceUtils.EXTRA_GENDER_SELECTED, isChecked);
        bundle.putBoolean(PreferenceUtils.EXTRA_GENDER, isMale);
        bundle.putBoolean(EXTRA_DIALOG_SHOWN_GALLERY, isDialogGallery);
    }

    /**
     * Loads user-entered data.
     * @param savedInstanceState
     */
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
            isDialogGallery = savedInstanceState.getBoolean(EXTRA_DIALOG_SHOWN_GALLERY);
            updateRadioButton();
            updateImage();
            if(isDialogGallery){
                showDialogChangeImage();
            }

        }
    }

    /**
     * Helper function for updating the stored gender.
     */
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

    /**
     * Exits the user profile activity upon clicking cancel.
     */
    private void onClickCancel() {
        deleteTempFile();
        finish();
    }

    /**
     * Saves user profile information upon clicking save.
     */
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

    /**
     * Deletes temporary image file.
     */
    private void deleteTempFile(){
        File file = new File(Uri.fromFile(new File(getExternalCacheDir(), "cropped_temp")).getPath());
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Updates uri with the stored image file.
     */
    private void updateImageFile(){
        try {
            if (imageUri != null) {
                File file = new File(Uri.fromFile(new File(getExternalCacheDir(), "cropped_temp")).getPath());
                if (file.exists()) {
                    File origFIle = new File(Uri.fromFile(new File(getExternalCacheDir(), "cropped")).getPath());
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

    /**
     * @param src
     * @param dst
     * @throws IOException
     */
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

    /**
     * Show change image dialogue when "change image" clicked.
     */
    private void onClickChangeImage() {
        showDialogChangeImage();
    }

    /**
     * Begin crop
     * @param source
     */
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getExternalCacheDir(), "cropped_temp"));
        Crop.of(source, destination).asSquare().start(this);
    }

    /**
     * Handle crop
     * @param resultCode
     * @param result
     */
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            imageUri = Crop.getOutput(result);
            printTrace("Image Uri = " + imageUri);
            mImgProfile.setImageURI(null);
            mImgProfile.invalidate();
            mImgProfile.setImageURI(imageUri);
        } else if (resultCode == Crop.RESULT_ERROR) {
           showToast(Crop.getError(result).getMessage());
        }
    }

    /**
     * Open gallery and select image
     */
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_PICK_IMAGE_GALLERY);
    }

    /**
     * Get file path of camera image
     * @return
     * @throws IOException
     */
    private File getCameraFilePath() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentCameraFilePath = Uri.fromFile(image);
        return image;
    }

    /**
     * Open camera activity for picture-taking
     */
    private void openCamera(){
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = getCameraFilePath();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_PICK_IMAGE_CAMERA);
                } else {
                    showToast(getString(R.string.error));
                }
            } else {
                showToast(getString(R.string.error));
            }
        }catch (Exception ex){
            showToast(getString(R.string.error));
            ex.printStackTrace();
        }


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
        result = Validator.isValidLength(text, Validator.NAME_LENGTH);
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

    private void onActivityResultCamera(int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            data.setData(mCurrentCameraFilePath);
            onActivityResultImagePick(resultCode,data);
            printTrace(mCurrentCameraFilePath.toString());
        }
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
            case REQUEST_PICK_IMAGE_GALLERY:{
                onActivityResultImagePick(resultCode,data);
            }
            break;
            case REQUEST_PICK_IMAGE_CAMERA:{
                onActivityResultCamera(resultCode,data);
            }
            break;
            case Crop.REQUEST_CROP:{
                handleCrop(resultCode,data);
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
