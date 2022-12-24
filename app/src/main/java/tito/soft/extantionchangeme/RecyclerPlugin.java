package tito.soft.extantionchangeme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerPlugin  extends RecyclerView.Adapter<RecyclerPlugin.MyViewHolder> {
    private String[] foods;
   // private List<String> foods;
    private OnItemClickListener _ItemClickListener;
  //  private int[] images;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView myText;
     //   ImageView myImage;


        // each data item is just a string in this case
        // public TextView textView;
        public MyViewHolder(View v) {
            super(v);
            myText = v.findViewById(R.id.textList);
          //  myImage = v.findViewById(R.id.imageList);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerPlugin(String[]food, OnItemClickListener _ItemClickListener ) {
        foods = food;
        this._ItemClickListener = _ItemClickListener;
       // images = image;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerPlugin.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.ex_cards, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.myText.setText(foods[position]);
        //holder.myText.setText(foods.get(position));
     //   holder.myImage.setImageResource(images[position]);

        holder.itemView.setOnClickListener(
                View ->{
                    _ItemClickListener.onItemClick(foods[position]);
                });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return foods.length;

    }
}
