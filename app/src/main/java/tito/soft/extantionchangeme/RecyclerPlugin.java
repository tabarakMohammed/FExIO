package tito.soft.extantionchangeme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerPlugin extends RecyclerView.Adapter<RecyclerPlugin.MyViewHolder> {
    private final String[] extensions;
    private final OnItemClickListener _ItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView myText;

        public MyViewHolder(View v) {
            super(v);
            myText = v.findViewById(R.id.textList);
        }
    }

    public RecyclerPlugin(String[] _extensions, OnItemClickListener _ItemClickListener) {
        extensions = _extensions;
        this._ItemClickListener = _ItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerPlugin.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.ex_cards, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.myText.setText(extensions[position]);
        holder.itemView.setOnClickListener(
                View -> _ItemClickListener.onItemClick(extensions[position]));
    }

    @Override
    public int getItemCount() {
        return extensions.length;

    }
}
