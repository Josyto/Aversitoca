package aversitoca.aversitoca;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Marcos Muñoz on 10/01/2018.
 */

public class AdapterDecimos extends RecyclerView.Adapter<AdapterDecimos.ViewHolderDecimos>{

    private Context context;
    ArrayList<Decimo> listDecimos;

    public AdapterDecimos(Context context,ArrayList<Decimo> listDecimos) {
        this.context=context;
        this.listDecimos = listDecimos;
    }

    public AdapterDecimos(ArrayList<Decimo> listDecimos) {
        this.listDecimos = listDecimos;
    }


    @Override
    public ViewHolderDecimos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_decimo2,null,false);

        return new ViewHolderDecimos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDecimos holder, int position) {
        holder.numeroDecimo.setText(listDecimos.get(position).getNumero().toString());
        holder.nombreSorteo.setText(listDecimos.get(position).getSorteo());
        holder.premio.setText(listDecimos.get(position).getPremio().toString() + "€");
       //TODO: Insertar foto en -
        // holder.foto.setImageResoruce(listDecimos.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listDecimos.size();
    }

    public void removeItem(int position) {
        listDecimos.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void restoreItem(Decimo item, int position) {
        listDecimos.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }


    public class ViewHolderDecimos extends RecyclerView.ViewHolder {

        TextView numeroDecimo;
        TextView nombreSorteo;
        TextView premio;
        ImageView foto;
        public RelativeLayout viewBackground, viewForeground;




        public ViewHolderDecimos(View itemView) {
            super(itemView);
            numeroDecimo=(TextView) itemView.findViewById(R.id.numero2);
            nombreSorteo=(TextView)itemView.findViewById(R.id.sorteo2);
            premio=(TextView) itemView.findViewById(R.id.premio2);
            foto=(ImageView) itemView.findViewById(R.id.foto2);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }

    }
}
