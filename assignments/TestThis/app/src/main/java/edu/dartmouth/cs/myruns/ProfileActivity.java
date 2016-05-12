package edu.dartmouth.cs.myruns;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.soundcloud.android.crop.Crop;


public class ProfileActivity extends Activity {
    private Uri mImageCaptureUri;
    private ImageView mImageView;

    // temp buffer for storing the image
    private byte[] mProfilePictureArray;

    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    private static final String TMP_PROFILE_IMG_KEY = "saved_uri";

    private static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
    public static final int REQUEST_CODE_CROP_PHOTO = 2;


    public void onSaveClicked(View v) {
        // Save profile
        saveProfile();
        // Making a "toast" informing the user the profile is saved.
        Toast.makeText(getApplicationContext(),
                getString(R.string.ui_profile_toast_save_text),
                Toast.LENGTH_SHORT).show();
        // Close the activity
        finish();
    }



    // generate a temp file uri for capturing profile photo
    private Uri getPhotoUri() {
        Uri photoUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "tmp_"
                + String.valueOf(System.currentTimeMillis()) + ".jpg"));

        return photoUri;
    }

    public void onChangePhotoClicked(View v) {
        // Take photo from camera，
        // Construct an intent with action
        // MediaStore.ACTION_IMAGE_CAPTURE

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Construct temporary image path and name to save the taken
        // photo
        mImageCaptureUri = getPhotoUri();
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);

    }

    public void onCancelClicked(View v) {
        // Making a "toast" informing the user changes are canceled.
        Toast.makeText(getApplicationContext(),
                getString(R.string.ui_profile_toast_cancel_text),
                Toast.LENGTH_SHORT).show();
        // Close the activity
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_TAKE_FROM_CAMERA: {
                if (resultCode == RESULT_OK) {
                    beginCrop(mImageCaptureUri);
                    break;
                }
            }
            break;

            case Crop.REQUEST_CROP: {
                // Update image view after image crop
                // Set the picture image in UI
                try {
                    handleCrop(resultCode, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) throws IOException {
        if (resultCode == RESULT_OK) {
            Uri mTempUri = Crop.getOutput(result);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTempUri);
            dumpImage(bitmap);

            // load the byte array to the image view
            loadImageToView();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //load profile from SharedPreferences
    private void loadProfile() {

        String key, str_val;
        int int_val;

        // Load and update all profile views
        key = getString(R.string.preference_name);
        SharedPreferences prefs = getSharedPreferences(key, MODE_PRIVATE);

        // Load user name
        key = getString(R.string.preference_key_profile_name);
        str_val = prefs.getString(key, "");
        ((EditText) findViewById(R.id.editName)).setText(str_val);

        // Load user email
        key = getString(R.string.preference_key_profile_email);
        str_val = prefs.getString(key, "");
        ((EditText) findViewById(R.id.editEmail)).setText(str_val);

        // Load user phone number
        key = getString(R.string.preference_key_profile_phone);
        str_val = prefs.getString(key, "");
        ((EditText) findViewById(R.id.editPhone)).setText(str_val);

        // Load gender info and set radio box
        key = getString(R.string.preference_key_profile_gender);
        int_val = prefs.getInt(key, -1);

        if (int_val >= 0) {
            RadioButton radioBtn = (RadioButton) ((RadioGroup) findViewById(R.id.radioGender))
                    .getChildAt(int_val);
            radioBtn.setChecked(true);
        }

        // Load class info
        key = getString(R.string.preference_key_profile_class);
        str_val = prefs.getString(key, "");
        ((TextView) findViewById(R.id.editClass)).setText(str_val);

        // Load student major info
        key = getString(R.string.preference_key_profile_major);
        str_val = prefs.getString(key, "");
        ((TextView) findViewById(R.id.editMajor)).setText(str_val);

        // Load profile photo from internal storage
        try {
            // open the file using a file input stream
            FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
            // the file's data will be read into a bytearray output stream
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // inputstream -> buffer -> outputstream
            byte[] buffer = new byte[5 * 1024];
            int n;
            // read data in a while loop
            while ((n = fis.read(buffer)) > -1) {
                bos.write(buffer, 0, n); // Don't allow any extra bytes to creep
                // in, final write
            }
            fis.close();
            //get the byte array from the ByteArrayOutputStream
            mProfilePictureArray = bos.toByteArray();
            bos.close();
        } catch (IOException e) {
        }

        // load the byte array to the image view
        loadImageToView();
    }

    //save profile to SharedPreferences
    private void saveProfile() {

        String key, str_val;
        int int_val;

        key = getString(R.string.preference_name);
        SharedPreferences prefs = getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Write screen contents into corresponding editor fields.
        key = getString(R.string.preference_key_profile_name);
        str_val = ((EditText) findViewById(R.id.editName)).getText().toString();
        editor.putString(key, str_val);

        key = getString(R.string.preference_key_profile_email);
        str_val = ((EditText) findViewById(R.id.editEmail)).getText()
                .toString();
        editor.putString(key, str_val);

        key = getString(R.string.preference_key_profile_phone);
        str_val = ((EditText) findViewById(R.id.editPhone)).getText()
                .toString();
        editor.putString(key, str_val);

        key = getString(R.string.preference_key_profile_gender);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGender);
        int_val = radioGroup.indexOfChild(findViewById(radioGroup
                .getCheckedRadioButtonId()));
        editor.putInt(key, int_val);

        key = getString(R.string.preference_key_profile_class);
        str_val = ((EditText) findViewById(R.id.editClass)).getText()
                .toString();
        editor.putString(key, str_val);

        key = getString(R.string.preference_key_profile_major);
        str_val = ((EditText) findViewById(R.id.editMajor)).getText()
                .toString();
        editor.putString(key, str_val);

        // Commit all the changes into preference file
        editor.apply();

        // Save profile image into internal storage.
        try {
            // if the user did not change default profile
            // picture, mProfilePictureArray will be null
            Bitmap currentBitMap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            dumpImage(currentBitMap);
            if( mProfilePictureArray != null ){
                FileOutputStream fos = openFileOutput(
                        getString(R.string.profile_photo_file_name), MODE_PRIVATE);
                fos.write(mProfilePictureArray);
                fos.flush();
                fos.close();
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
    // load image byte array to image view
    private void loadImageToView() {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(
                    mProfilePictureArray);
            Bitmap bmap = BitmapFactory.decodeStream(bis);
            mImageView.setImageBitmap(bmap);
            bis.close();
        } catch (Exception ex) {
            // Default profile photo if no photo saved before.
            mImageView.setImageResource(R.drawable.default_profile);
        }
    }
    // convert bitmap to byte array
    private void dumpImage(Bitmap bmap) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            mProfilePictureArray = bos.toByteArray();
            bos.close();
        } catch (IOException ioe) {

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bitmap currentBitMap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        dumpImage(currentBitMap);

        // Save profile image into internal storage.
        if (mProfilePictureArray != null) {
            // use putByteArray() to put a byte array into a Bundle
            // see http://developer.android.com/reference/android/os/Bundle.html#putByteArray(java.lang.String, byte[])
            outState.putByteArray(TMP_PROFILE_IMG_KEY, mProfilePictureArray);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mImageView = (ImageView) findViewById(R.id.imageProfile);

        if (savedInstanceState != null) {
            // Load profile photo from internal storage, the key should be the same as
            // the one used to put into the Bundle in onSaveInstanceState()
            mProfilePictureArray = savedInstanceState
                    .getByteArray(TMP_PROFILE_IMG_KEY);

            // load the byte array to the image view
            loadImageToView();
        }
        else{
            loadProfile();
        }

    }
}
