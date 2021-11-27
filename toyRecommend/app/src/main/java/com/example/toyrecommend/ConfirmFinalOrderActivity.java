package com.example.toyrecommend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toyrecommend.Model.Products;
import com.example.toyrecommend.Model.Users;
import com.example.toyrecommend.Prevalent.Prevalent;
import com.example.toyrecommend.ViewHolder.ProductViewHolder;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderBtn;
    private TextView msg;

    private String totalAmount = "";
    private String productRandomKey;
    final int[] count = {0};
    private RecyclerView recyclerView;
    private int[] discounts = {1,2,3,};

    List<Integer> list = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText) findViewById(R.id.shipment_name);
        phoneEditText = (EditText) findViewById(R.id.shipment_phone_number);
        addressEditText = (EditText) findViewById(R.id.shipment_address);
        cityEditText = (EditText) findViewById(R.id.shipment_city);
        msg = findViewById(R.id.txt_discount);

        list.add(1);
        list.add(2);
        list.add(3);

        Toast.makeText(this, "Total Amount - "+totalAmount, Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.recycler_dis);
        recyclerView.setLayoutManager(new LinearLayoutManager(ConfirmFinalOrderActivity.this));

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(Prevalent.currentOnlineUser.getPhone()).exists()){
                    Users userData = snapshot.child("Users").child(Prevalent.currentOnlineUser.getPhone()).getValue(Users.class);

                    count[0] = userData.getoCount();

                    if(count[0] > 3){

                        msg.setText("Upon completion of this order you will receive the following free offer.");
                        DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

                        FirebaseRecyclerOptions<Products> options =
                                new FirebaseRecyclerOptions.Builder<Products>()
                                        .setQuery(ProductsRef.orderByChild("discounts").equalTo("1").limitToFirst(1), Products.class)
                                        .build();

                        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                                    {
                                        holder.txtProductName.setText(model.getPname());
                                        holder.txtProductDescription.setText(model.getDescription());
                                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "LKR");
                                        holder.txtProductRatings.setText(" "+model.getRatings());
                                        Picasso.get().load(model.getImage()).into(holder.imageView);

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(ConfirmFinalOrderActivity.this, ProductDetailsActivity.class);
                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("role", "no");
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    @NonNull
                                    @Override
                                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                                    {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout, parent, false);
                                        ProductViewHolder holder = new ProductViewHolder(view);
                                        return holder;
                                    }
                                };
                       // recyclerView.getLayoutManager().findViewByPosition(list.get(new Random().nextInt(list.size())));
                        recyclerView.setAdapter(adapter);
                        adapter.startListening();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Check()
    {
        if (TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "Please provide your full name.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "Please provide your address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(this, "Please provide your city.", Toast.LENGTH_SHORT).show();
        }
        else {
            confirmOrder();
            updateOrderCount();
        }
    }

    private void updateOrderCount() {
        int val = ++count[0];
        DatabaseReference ref_up = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("oCount", val);
        ref_up.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
    }

    private void confirmOrder() {
        final String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY ");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance()
                .getReference().child(("Orders"))
                .child(productRandomKey);

        final HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("oid", Prevalent.currentOnlineUser.getPhone());
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ConfirmFinalOrderActivity.this, "Order Placed Successfully!.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("role", "no");
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });

    }
}