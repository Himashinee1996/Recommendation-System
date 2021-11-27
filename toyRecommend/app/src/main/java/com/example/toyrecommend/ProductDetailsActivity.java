package com.example.toyrecommend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.toyrecommend.Model.Products;
import com.example.toyrecommend.Model.Reviews;
import com.example.toyrecommend.Prevalent.Prevalent;
import com.example.toyrecommend.ViewHolder.ReviewViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity implements dialogReviews.dialogReviewsListener{

    private Button addToCart;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName, productBranch;
    private String productID = "";
    private String role ;
    private TextView addReview;
    private String productRandomKey;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");
        role = getIntent().getStringExtra("role");

        addToCart = (Button) findViewById(R.id.pd_add_to_cart_button);
        numberButton = (ElegantNumberButton) findViewById((R.id.number_btn));
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById((R.id.product_name_details));
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById((R.id.product_price_details));
        productBranch = (TextView) findViewById((R.id.product_branch));
        addReview = (TextView) findViewById((R.id.review_txt2));

        if (role.equals("Guest")){
            addToCart.setEnabled(false);
            addToCart.setText("Create an Account to Purchase.");
        }
        getProductDetails(productID);

        recyclerView = findViewById(R.id.recycler_menu_reviews);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getReviews(productID);

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }

    private void openDialog() {
        dialogReviews dr = new dialogReviews();
        dr.show(getSupportFragmentManager(), "review dialog");
    }


    private void getReviews(String productID){

        DatabaseReference ReviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews");

        FirebaseRecyclerOptions<Reviews> options =
                new FirebaseRecyclerOptions.Builder<Reviews>()
                        .setQuery(ReviewsRef.orderByChild("pid").equalTo(productID), Reviews.class)
                        .build();

        FirebaseRecyclerAdapter<Reviews, ReviewViewHolder> adapter =
                new FirebaseRecyclerAdapter<Reviews, ReviewViewHolder>(options) {

                    @NonNull
                    @Override
                    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review, parent, false);
                        ReviewViewHolder holder = new ReviewViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i, @NonNull Reviews review) {

                        reviewViewHolder.txtuser.setText(review.getRname());
                        reviewViewHolder.txtRate.setText(review.getRate());
                        reviewViewHolder.txtReview.setText(review.getReview());
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY ");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child(("Cart List"));

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();;

                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                intent.putExtra("role", "no");
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void getProductDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    Products products = snapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    productBranch.setText("Frequently sold out branch - "+products.getBranch());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void applyTexts(String review, String rates) {
        Log.d("Review:",review);
        Log.d("Rating:",rates);

        final String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY ");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        productRandomKey = productID + "_"+ saveCurrentTime;

        final DatabaseReference reviewsRef = FirebaseDatabase.getInstance()
                .getReference().child(("Reviews"))
                .child(productRandomKey);

        final HashMap<String, Object> reviewsMap = new HashMap<>();
        reviewsMap.put("pid", productID);
        reviewsMap.put("rate", rates);
        reviewsMap.put("review", review);
        reviewsMap.put("rname", Prevalent.currentOnlineUser.getName());

        reviewsRef.updateChildren(reviewsMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ProductDetailsActivity.this, "Successfully posted!", Toast.LENGTH_SHORT).show();
//                            loadingBar.dismiss();
//
//                            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
//                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(ProductDetailsActivity.this, "Network Error. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}