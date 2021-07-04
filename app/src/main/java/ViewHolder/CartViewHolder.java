package ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazon.R;

import org.w3c.dom.Text;

import Interface.ItemClickListener;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuantity;

    public ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);


    }


    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(),false);

    }

    public  void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener= itemClickListener;
    }

}
