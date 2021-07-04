package Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazon.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Cart;
import Prevalent.Prevalent;
import ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount,txtMsg1;

    private  int overTotalPrive = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = findViewById(R.id.next_process_btn);
        txtTotalAmount = findViewById(R.id.total_price);

        txtMsg1=findViewById(R.id.msg1);




        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


              Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
              intent.putExtra("Total price",String.valueOf(overTotalPrive));
              startActivity(intent);

          }
      });

    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
        txtTotalAmount.setText("Total Price = "+String.valueOf(overTotalPrive));



        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options
                = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                .child(Prevalent.currentOnlineUser.getPhone()).child("Products"),Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {


                cartViewHolder.txtProductQuantity.setText("Quantity = "+cart.getQuantity());
                cartViewHolder.txtProductPrice.setText("Price = "+cart.getPrice()+"$");
                cartViewHolder.txtProductName.setText(cart.getPname());

                int oneTypeProductTPrice =(Integer.valueOf(cart.getPrice()) * Integer.valueOf(cart.getQuantity()));

                overTotalPrive+=oneTypeProductTPrice;



                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Char Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0)
                                {
                                    Intent intent =new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);
                                }
                                else if(which==1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Item removed Successfully!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(CartActivity.this,HomeActivity.class));
                                                    }



                                                }
                                            });

                                }

                            }
                        });
                        builder.show();

                    }
                });



            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);

                CartViewHolder holder = new CartViewHolder(view);
                return  holder;

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public  void CheckOrderState()
    {
        DatabaseReference orderRef;
        orderRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(Prevalent.currentOnlineUser.getPhone());


        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 if(dataSnapshot.exists())
                 {

                     String shippingState = dataSnapshot.child("state").getValue().toString();
                     String userName = dataSnapshot.child("name").getValue().toString();

                     if(shippingState.equals("shipped"))
                     {
                         txtTotalAmount.setText("Dear "+userName+"\n order is shipped successfully!");
                         recyclerView.setVisibility(View.GONE);
                         txtMsg1.setVisibility(View.VISIBLE);
                         NextProcessBtn.setVisibility(View.GONE);

                         Toast.makeText(CartActivity.this, "You can add more product Once you get your first Order!", Toast.LENGTH_SHORT).show();
                     }
                     else if(shippingState.equals("not shipped"))
                     {
                         txtTotalAmount.setText("Shipping State = Nor Shipped");
                         recyclerView.setVisibility(View.GONE);
                         txtMsg1.setVisibility(View.VISIBLE);
                         NextProcessBtn.setVisibility(View.GONE);

                     }
                 }



            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



}
