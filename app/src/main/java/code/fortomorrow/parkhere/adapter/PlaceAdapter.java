package code.fortomorrow.parkhere.adapter;// PlaceAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import code.fortomorrow.parkhere.R;
import code.fortomorrow.parkhere.model.PlaceModel;
import code.fortomorrow.parkhere.onclick.ItemClick;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private final List<PlaceModel> placeList;
    private final Context context;
    private final ItemClick itemClick;

    public PlaceAdapter(Context context, List<PlaceModel> placeList, ItemClick itemClick) {
        this.context = context;
        this.placeList = placeList;
      this.itemClick = itemClick;

    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_dashboard, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {



        PlaceModel place = placeList.get(position);
        holder.placeName.setText(place.getPlaceName());
        holder.tvNumber.setText(place.getNumberOfPlaces());
        holder.tvUp.setText(place.getPercentageUp());
        holder.tvDetails.setText(place.getDetails());

        holder.itemView.setOnClickListener(v -> {
            itemClick.onClick(place.getPlaceName());
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, tvNumber, tvUp, tvDetails;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvUp = itemView.findViewById(R.id.tvUp);
            tvDetails = itemView.findViewById(R.id.tvDetails);
        }
    }
}
