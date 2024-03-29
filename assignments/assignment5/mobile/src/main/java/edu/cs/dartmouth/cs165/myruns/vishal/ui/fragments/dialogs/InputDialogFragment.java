package edu.cs.dartmouth.cs165.myruns.vishal.ui.fragments.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import edu.cs.dartmouth.cs165.myruns.vishal.R;

/**
 * Wrapper to edit text input dialog
 */
public class InputDialogFragment extends DialogFragment {

    public interface OnTextEntered{
         void onTextEntered(String tag, String text);
         void onTextCancelled(String tag);
    }


    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_HINT = "extra_hint";
    public static final String EXTRA_INPUT_TYPE = "extra_input_type";
    public static final String EXTRA_INPUT_TEXT = "extra_text";

    private EditText mEdtInput;
    private View mRootView = null;
    private int mInputType;
    private int identifier;
    private String mHint;
    private String mTitle;
    private String mDefaultText = "";
    private AlertDialog mInputDialog = null;
    private OnTextEntered mOnTextEntered = null;


    public InputDialogFragment() {
    }

    public static InputDialogFragment getInstance(int inputType, String hint,String title){
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_HINT, hint);
        bundle.putInt(EXTRA_INPUT_TYPE, inputType);
        bundle.putString(EXTRA_TITLE, title);
        InputDialogFragment fragment = new InputDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInputType = getArguments().getInt(EXTRA_INPUT_TYPE, InputType.TYPE_CLASS_TEXT);
            mHint = getArguments().getString(EXTRA_HINT,"");
            mTitle = getArguments().getString(EXTRA_TITLE,"");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putString(EXTRA_INPUT_TEXT, mEdtInput.getText().toString());
    }
    private Bundle getUpdatedBundle(){
        Bundle bundle = getArguments();
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString(EXTRA_HINT, mHint);
        bundle.putInt(EXTRA_INPUT_TYPE, mInputType);
        bundle.putString(EXTRA_TITLE, mTitle);
        return bundle;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Log.e("VVV","onCreateDialog ");
        initInputDialog();
        updateViews(savedInstanceState);
        return mInputDialog;
    }

    private void updateViews(Bundle savedInstanceState){
        mEdtInput.setInputType(mInputType);
        mEdtInput.setHint(mHint);
        mDefaultText = (savedInstanceState != null) ? savedInstanceState.getString(EXTRA_INPUT_TEXT, mDefaultText) : mDefaultText ;
        mEdtInput.setText(mDefaultText);
    }
    private void initInputDialog() {
        if (mInputDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_input_dialog, null);
            mEdtInput = (EditText)dialogView.findViewById(R.id.edtInput);
            mEdtInput.setHint(mHint);
            mEdtInput.setInputType(mInputType);
            builder.setView(dialogView);
            builder.setTitle(mTitle);
            builder.setPositiveButton(getString(R.string.ok), mOnDialogClickPatientId);
            builder.setNegativeButton(getString(R.string.cancel), mOnDialogClickPatientId);
            mInputDialog = builder.create();
        }
    }
    public void setOnTextEnteredListener(OnTextEntered onTextEntered){
        mOnTextEntered = onTextEntered;
    }
    public void show(FragmentManager manager, String tag, String title, String hint,String defaultText, int inputType, int identifier){
        Log.e("VVV","show, tag =  " + tag + " default = " + defaultText);
        this.mInputType = inputType;
        this.mHint = hint;
        this.mTitle = title;
        this.identifier = identifier;
        this.mDefaultText = defaultText;
        show(manager,tag);
        setArguments(getUpdatedBundle());
    }

    public String getInputText(){
        String result = "";
        if(mEdtInput != null)
            result = mEdtInput.getText().toString();
        return result;
    }

    Dialog.OnClickListener mOnDialogClickPatientId = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE: {
                    if(mOnTextEntered!=null){
                        mOnTextEntered.onTextEntered(getTag(),mEdtInput.getText().toString());
                    }
                }
                break;

                case Dialog.BUTTON_NEGATIVE: {
                    if(mOnTextEntered!=null){
                        mOnTextEntered.onTextCancelled(getTag());
                    }
                }
                break;
            }
        }
    };


}
