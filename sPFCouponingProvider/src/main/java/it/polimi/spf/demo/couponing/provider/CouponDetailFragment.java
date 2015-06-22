package it.polimi.spf.demo.couponing.provider;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import it.polimi.spf.shared.model.ProfileField;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CouponDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponDetailFragment extends Fragment {

    private ImageView mPhotoView;
    private TextView mTitleView, mTextView, mCategoryView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CouponCreateFragment.
     */
    public static CouponDetailFragment newInstance() {
        CouponDetailFragment fragment = new CouponDetailFragment();
        return fragment;
    }

    public CouponDetailFragment() {
        // Required empty public constructor
    }

    public void setPhotoAndCoupon(Bitmap photo, Coupon coupon) {

        mPhotoView.setImageBitmap(photo);
        mTitleView.setText(coupon.getTitle());
        mTextView.setText(coupon.getText());
        mCategoryView.setText(coupon.getCategory());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_coupon_detail, container, false);

        mPhotoView = (ImageView) view.findViewById(R.id.coupon_photo);
        mTitleView = (TextView) view.findViewById(R.id.coupon_title);
        mTextView = (TextView) view.findViewById(R.id.coupon_text);
        mCategoryView = (TextView) view.findViewById(R.id.coupon_category);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }
}
