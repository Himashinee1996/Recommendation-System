package com.example.toyrecommend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.toyrecommend.R;

public class dialogReviews extends AppCompatDialogFragment {
    private EditText editTextReview;
    private RatingBar rates;
    private  dialogReviewsListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Add Review")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String review = editTextReview.getText().toString();
                        String rating = String.valueOf(rates.getRating());
                        listener.applyTexts(review,rating);
                    }
                });

        editTextReview = view.findViewById(R.id.comment);
        rates= view.findViewById(R.id.rating_bar);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (dialogReviewsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement dialogReviewsListener");
        }
    }

    public interface dialogReviewsListener{
        void applyTexts(String review, String rates);

    }
}
