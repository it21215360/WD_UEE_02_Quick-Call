    package com.example.ueeapp;

    import android.content.Context;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.List;

    public class LaundererOrders_Adapter extends RecyclerView.Adapter<LaundererOrders_Adapter.ViewHolder> {

        private Context context ;
        private List<LaundererOrders_Model> laundererOrders_modelList;

        public LaundererOrders_Adapter(Context context, List<LaundererOrders_Model> laundererOrders_models){
            this.context = context;
            this.laundererOrders_modelList = laundererOrders_models;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_laundry_orders, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            LaundererOrders_Model laundererOrders_model = laundererOrders_modelList.get(position);
            holder.user_email.setText(laundererOrders_model.getUserEmail());

            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userEmail = laundererOrders_model.getUserEmail();
                    LaundererOrders_Model laundererOrders_model = new LaundererOrders_Model(userEmail);

                    Intent intent = new Intent(view.getContext(), CheckSchedule.class);
                    intent.putExtra("userEmail", userEmail);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                    Toast.makeText(view.getContext(), "Check Order Details ", Toast.LENGTH_SHORT).show();
                }
            });


        }

        @Override
        public int getItemCount() {
            return laundererOrders_modelList.size();
        }
        public static class ViewHolder extends RecyclerView.ViewHolder{
            private TextView user_email;
            public Button addButton;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                user_email = itemView.findViewById(R.id.user_email);
                addButton = itemView.findViewById(R.id.addButton);
            }
        }
    }
