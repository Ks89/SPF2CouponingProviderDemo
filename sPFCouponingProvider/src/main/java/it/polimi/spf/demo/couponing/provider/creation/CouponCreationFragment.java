package it.polimi.spf.demo.couponing.provider.creation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import it.polimi.spf.demo.couponing.provider.ProviderApplication;
import it.polimi.spf.demo.couponing.provider.R;
import it.polimi.spf.demo.couponing.provider.detail.InputException;
import lombok.Getter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CouponCreationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponCreationFragment extends Fragment {

    private static final String TAG = "CouponCreationActivity";
    private static final int CODE_EDIT_PHOTO = 1;

    @Getter private ImageView mPhotoInput;
    @Getter private EditText mTitleInput, mTextInput;
    @Getter private Spinner mCategoryInput;

    @Getter private Bitmap mPhoto;

    private View.OnClickListener mPhotoClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Crop.pickImage(getActivity());
        }
    };


    /**
     * Method to start the activity to crop an image.
     * @param source
     */
    public void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(this.getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this.getActivity());
    }

    /**
     * Method to set an show a cropped imaged.
     * @param resultCode
     * @param result
     */
    public void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            mPhotoInput.setImageURI(uri);

            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(uri.getPath());
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                myBitmap = Bitmap.createScaledBitmap(myBitmap,130,130,false);

//                mContainer.setFieldValue(ProfileField.PHOTO, myBitmap);
//                showPicture(myBitmap);
                mPhoto = myBitmap;
                this.mPhotoInput.setImageBitmap(myBitmap);
                this.mPhotoInput.invalidate();

            } catch (FileNotFoundException e) {
                Log.e(TAG, "handleCrop FileInputStream-file not found from uri.getpath", e);
            } finally {
                if(inputStream!=null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "handleCrop closing input stream error", e);
                    }
                }
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this.getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_EDIT_PHOTO) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            if (data != null && data.getExtras() != null) {
                mPhoto = data.getExtras().getParcelable("data");
                mPhotoInput.setImageBitmap(mPhoto);
                mPhotoInput.invalidate();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CouponCreateFragment.
     */
    public static CouponCreationFragment newInstance() {
        CouponCreationFragment fragment = new CouponCreationFragment();
        return fragment;
    }

    public CouponCreationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon_creation, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPhotoInput = (ImageView) this.getActivity().findViewById(R.id.coupon_photo);
        mTitleInput = (EditText) this.getActivity().findViewById(R.id.coupon_title);
        mTextInput = (EditText) this.getActivity().findViewById(R.id.coupon_text);
        mCategoryInput = (Spinner) this.getActivity().findViewById(R.id.coupon_category);

        String[] categories = ProviderApplication.get().getCouponDatabase().getCategories();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, categories);
        mCategoryInput.setAdapter(categoryAdapter);

        mPhotoInput.setOnClickListener(mPhotoClickListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void checkInput() throws InputException {
		if(mPhoto == null){
			throw new InputException(R.string.error_coupon_photo_empty);
		}

        if (mTitleInput.getText().length() == 0) {
            throw new InputException(R.string.error_coupon_title_empty);
        }

        if (mTextInput.getText().length() == 0) {
            throw new InputException(R.string.error_coupon_text_empty);
        }

        if (mCategoryInput.getSelectedItem() == null) {
            throw new InputException(R.string.error_coupon_category_empty);
        }
    }
}