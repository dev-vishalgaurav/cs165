package tests;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import edu.dartmouth.cs.myruns.ProfileActivity;
import edu.dartmouth.cs.myruns.R;

/**
 *
 * Individual tests are defined as any method beginning with 'test'.
 *
 * ActivityInstrumentationTestCase2 allows these tests to run alongside a running
 * copy of the application under inspection. Calling getActivity() will return a
 * handle to this activity (launching it if needed).
 */
public class ProfileActivityTest extends ActivityInstrumentationTestCase2<ProfileActivity> {

    //Constructor
    public ProfileActivityTest() {
        super(ProfileActivity.class);
    }

    /**
     * Test to make sure the image is persisted after screen rotation.
     *
     * Launches the main activity, sets a test bitmap, rotates the screen.
     * Checks to make sure that the bitmap value matches what we set it to.
     */
    public void testImagePersistedAfterRotate() throws InterruptedException {
        // Launch the activity
        Activity activity = getActivity();

        // Define a test bitmap
        final Bitmap TEST_BITMAP = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.blue_pushpin);

        // Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TEST_BITMAP.compress(Bitmap.CompressFormat.PNG, 100, bos);
        final byte[] TEST_BITMAP_VALUE = bos.toByteArray();

        final ImageView mImageView = (ImageView) activity.findViewById(R.id.imageProfile);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Attempts to manipulate the UI must be performed on a UI thread.
                // Calling this outside runOnUiThread() will cause an exception.
                //
                // You could also use @UiThreadTest, but activity lifecycle methods
                // cannot be called if this annotation is used.
                //set the test bitmap to the image view
                mImageView.setImageBitmap(TEST_BITMAP);
            }
        });

        // Suspends the current thread for 1 second. This is no necessary.
        // But you can see the change on your phone.
        Thread.sleep(1000);

        // Information about a particular kind of Intent that is being monitored.
        // It is required to open your phone screen, otherwise the test will be hanging.
        Instrumentation.ActivityMonitor monitor =
                new Instrumentation.ActivityMonitor(ProfileActivity.class.getName(), null, false);
        getInstrumentation().addMonitor(monitor);
        // Rotate the screen
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getInstrumentation().waitForIdleSync();
        // Updates current activity
        activity = getInstrumentation().waitForMonitor(monitor);

        // Suspends the current thread for 1 second. This is no necessary.
        // But you can see the change on your phone.
        Thread.sleep(1000);

        final ImageView mImageView2 = (ImageView) activity.findViewById(R.id.imageProfile);
        // Get the current bitmap from image view
        Bitmap currentBitMap = ((BitmapDrawable) mImageView2.getDrawable()).getBitmap();

        // Convert bitmap to byte array
        bos = new ByteArrayOutputStream();
        currentBitMap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] currentBitmapValue = bos.toByteArray();

        // Check if these two bitmaps have the same byte values.
        // If the program executes correctly, they should be the same
        assertTrue(java.util.Arrays.equals(TEST_BITMAP_VALUE, currentBitmapValue));
    }


    /**
     * Test to make sure that value of name is persisted across activity restarts.
     *
     * Launches the main activity, sets a name value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the name value match what we
     * set it to.
     */
    public void testNameValuePersistedBetweenLaunches() {
        //create a unique name to ensure every test is unique
        final String TEST_NAME_VALUE = System.currentTimeMillis() + "";

        // Launch the activity
        Activity activity = getActivity();

        // Get name edit text and save button
        final EditText text = (EditText) activity.findViewById(R.id.editName);
        final Button save = (Button) activity.findViewById(R.id.btnSave);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Attempts to manipulate the UI must be performed on a UI thread.
                // Calling this outside runOnUiThread() will cause an exception.
                //
                // You could also use @UiThreadTest, but activity lifecycle methods
                // cannot be called if this annotation is used.
                text.requestFocus();
                //set the name to the edit text
                text.setText(TEST_NAME_VALUE);
                //click the save button
                save.performClick();
            }
        });

        // Close the activity
        activity.finish();
        setActivity(null);  // Required to force creation of a new activity

        // Relaunch the activity
        activity = this.getActivity();

        // Verify that the name was saved at the name edit text
        final EditText name2 = (EditText) activity.findViewById(R.id.editName);
        String currentName = name2.getText().toString();

        // Assert that the name value should be the same
        assertEquals(TEST_NAME_VALUE, currentName);
    }

    /**
     * Test to make sure that value of email is persisted across activity restarts.
     *
     * Launches the main activity, sets a email value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the email value match what we
     * set it to.
     */
    public void testEmailValuePersistedBetweenLaunches() {
        //implement the test case here
        //create a unique name to ensure every test is unique
        final String TEST_NAME_VALUE = System.currentTimeMillis() + "";

        // Launch the activity
        Activity activity = getActivity();

        // Get name edit text and save button
        final EditText text = (EditText) activity.findViewById(R.id.editEmail);
        final Button save = (Button) activity.findViewById(R.id.btnSave);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Attempts to manipulate the UI must be performed on a UI thread.
                // Calling this outside runOnUiThread() will cause an exception.
                //
                // You could also use @UiThreadTest, but activity lifecycle methods
                // cannot be called if this annotation is used.
                text.requestFocus();
                //set the name to the edit text
                text.setText(TEST_NAME_VALUE);
                //click the save button
                save.performClick();
            }
        });

        // Close the activity
        activity.finish();
        setActivity(null);  // Required to force creation of a new activity

        // Relaunch the activity
        activity = this.getActivity();

        // Verify that the name was saved at the name edit text
        final EditText name2 = (EditText) activity.findViewById(R.id.editEmail);
        String currentName = name2.getText().toString();

        // Assert that the name value should be the same
        assertEquals(TEST_NAME_VALUE, currentName);
    }

    /**
     * Test to make sure that value of phone is persisted across activity restarts.
     *
     * Launches the main activity, sets a phone value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the phone value match what we
     * set it to.
     */
    public void testPhoneValuePersistedBetweenLaunches() {
        //implement the test case here
        //create a unique name to ensure every test is unique
        final String TEST_NAME_VALUE = System.currentTimeMillis() + "";

        // Launch the activity
        Activity activity = getActivity();

        // Get name edit text and save button
        final EditText text = (EditText) activity.findViewById(R.id.editPhone);
        final Button save = (Button) activity.findViewById(R.id.btnSave);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Attempts to manipulate the UI must be performed on a UI thread.
                // Calling this outside runOnUiThread() will cause an exception.
                //
                // You could also use @UiThreadTest, but activity lifecycle methods
                // cannot be called if this annotation is used.
                text.requestFocus();
                //set the name to the edit text
                text.setText(TEST_NAME_VALUE);
                //click the save button
                save.performClick();
            }
        });

        // Close the activity
        activity.finish();
        setActivity(null);  // Required to force creation of a new activity

        // Relaunch the activity
        activity = this.getActivity();

        // Verify that the name was saved at the name edit text
        final EditText name2 = (EditText) activity.findViewById(R.id.editPhone);
        String currentName = name2.getText().toString();

        // Assert that the name value should be the same
        assertEquals(TEST_NAME_VALUE, currentName);
    }

    /**
     * Test to make sure that value of gender is persisted across activity restarts.
     *
     * Launches the main activity, sets a gender value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the gender value match what we
     * set it to.
     */
    public void testGenderValuePersistedBetweenLaunches() {
        //implement the test case here
        //create a unique name to ensure every test is unique
        final boolean isMale = new Random().nextInt(1000) % 2 == 0 ? true : false ;

        // Launch the activity
        Activity activity = getActivity();

        // Get name edit text and save button
        final RadioGroup radioG = (RadioGroup) activity.findViewById(R.id.radioGender);
        final RadioButton radioM = (RadioButton) activity.findViewById(R.id.radioGenderM);
        final RadioButton radioF = (RadioButton) activity.findViewById(R.id.radioGenderF);
        final Button save = (Button) activity.findViewById(R.id.btnSave);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Attempts to manipulate the UI must be performed on a UI thread.
                // Calling this outside runOnUiThread() will cause an exception.
                //
                // You could also use @UiThreadTest, but activity lifecycle methods
                // cannot be called if this annotation is used.
                if(isMale){
                    radioM.setChecked(true);
                }else{
                    radioF.setChecked(true);
                }
                //click the save button
                save.performClick();
            }
        });

        // Close the activity
        activity.finish();
        setActivity(null);  // Required to force creation of a new activity

        // Relaunch the activity
        activity = this.getActivity();

        // Get name edit text and save button
        final RadioButton radioM1 = (RadioButton) activity.findViewById(R.id.radioGenderM);
        final RadioButton radioF1 = (RadioButton) activity.findViewById(R.id.radioGenderF);
        if(isMale){
            assertEquals(isMale, radioM1.isChecked());
        }else{
            assertEquals(true, radioF1.isChecked());
        }
        // Assert that the name value should be the same
    }

    /**
     * Test to make sure that value of class is persisted across activity restarts.
     *
     * Launches the main activity, sets a class value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the class value match what we
     * set it to.
     */
    public void testClassValuePersistedBetweenLaunches() {
        //implement the test case here
        //create a unique name to ensure every test is unique
        final String TEST_NAME_VALUE = System.currentTimeMillis() + "";

        // Launch the activity
        Activity activity = getActivity();

        // Get name edit text and save button
        final EditText text = (EditText) activity.findViewById(R.id.editClass);
        final Button save = (Button) activity.findViewById(R.id.btnSave);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Attempts to manipulate the UI must be performed on a UI thread.
                // Calling this outside runOnUiThread() will cause an exception.
                //
                // You could also use @UiThreadTest, but activity lifecycle methods
                // cannot be called if this annotation is used.
                text.requestFocus();
                //set the name to the edit text
                text.setText(TEST_NAME_VALUE);
                //click the save button
                save.performClick();
            }
        });

        // Close the activity
        activity.finish();
        setActivity(null);  // Required to force creation of a new activity

        // Relaunch the activity
        activity = this.getActivity();

        // Verify that the name was saved at the name edit text
        final EditText name2 = (EditText) activity.findViewById(R.id.editClass);
        String currentName = name2.getText().toString();

        // Assert that the name value should be the same
        assertEquals(TEST_NAME_VALUE, currentName);
    }

    /**
     * Test to make sure that value of major is persisted across activity restarts.
     *
     * Launches the main activity, sets a major value, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the major value match what we
     * set it to.
     */
    public void testMajorValuePersistedBetweenLaunches() {
        //implement the test case here
        //create a unique name to ensure every test is unique
        final String TEST_NAME_VALUE = System.currentTimeMillis() + "";

        // Launch the activity
        Activity activity = getActivity();

        // Get name edit text and save button
        final EditText text = (EditText) activity.findViewById(R.id.editMajor);
        final Button save = (Button) activity.findViewById(R.id.btnSave);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Attempts to manipulate the UI must be performed on a UI thread.
                // Calling this outside runOnUiThread() will cause an exception.
                //
                // You could also use @UiThreadTest, but activity lifecycle methods
                // cannot be called if this annotation is used.
                text.requestFocus();
                //set the name to the edit text
                text.setText(TEST_NAME_VALUE);
                //click the save button
                save.performClick();
            }
        });

        // Close the activity
        activity.finish();
        setActivity(null);  // Required to force creation of a new activity

        // Relaunch the activity
        activity = this.getActivity();

        // Verify that the name was saved at the name edit text
        final EditText name2 = (EditText) activity.findViewById(R.id.editMajor);
        String currentName = name2.getText().toString();

        // Assert that the name value should be the same
        assertEquals(TEST_NAME_VALUE, currentName);
    }

    /**
     * Test to make sure that image is persisted across activity restarts.
     *
     * Launches the main activity, sets an image, clicks the save button, closes the activity,
     * then relaunches that activity. Checks to make sure that the image matches what we
     * set it to.
     */
    public void testImagePersistedBetweenLaunches() throws InterruptedException {
        //implement the test case here
        // Launch the activity
        Activity activity = getActivity();

        // Define a test bitmap
        final Bitmap TEST_BITMAP = BitmapFactory.decodeResource(activity.getResources(), R.drawable.blue_pushpin);

        // Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TEST_BITMAP.compress(Bitmap.CompressFormat.PNG, 100, bos);
        final byte[] TEST_BITMAP_VALUE = bos.toByteArray();

        final ImageView mImageView = (ImageView) activity.findViewById(R.id.imageProfile);
        final Button save = (Button) activity.findViewById(R.id.btnSave);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Attempts to manipulate the UI must be performed on a UI thread.
                // Calling this outside runOnUiThread() will cause an exception.
                //
                // You could also use @UiThreadTest, but activity lifecycle methods
                // cannot be called if this annotation is used.
                //set the test bitmap to the image view
                mImageView.setImageBitmap(TEST_BITMAP);
                save.performClick();
            }
        });
        // Close the activity
        activity.finish();
        setActivity(null);  // Required to force creation of a new activity

        // Relaunch the activity
        activity = this.getActivity();
        final ImageView mImageView2 = (ImageView) activity.findViewById(R.id.imageProfile);
        // Get the current bitmap from image view
        Bitmap currentBitMap = ((BitmapDrawable) mImageView2.getDrawable()).getBitmap();

        // Convert bitmap to byte array
        bos = new ByteArrayOutputStream();
        currentBitMap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] currentBitmapValue = bos.toByteArray();

        // Check if these two bitmaps have the same byte values.
        // If the program executes correctly, they should be the same
        assertTrue(java.util.Arrays.equals(TEST_BITMAP_VALUE, currentBitmapValue));
    }
}