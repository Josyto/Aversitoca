package aversitoca.aversitoca;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Marcos Muñoz on 10/01/2018.
 */

public class AdapterDecimos extends RecyclerView.Adapter<AdapterDecimos.ViewHolderDecimos>{

    ArrayList<Decimo> listDecimos;

    public AdapterDecimos(ArrayList<Decimo> listDecimos) {
        this.listDecimos = listDecimos;
    }


    @Override
    public ViewHolderDecimos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_decimo,null,false);

        return new ViewHolderDecimos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDecimos holder, int position) {
        holder.numeroDecimo.setText(listDecimos.get(position).getNumero().toString());
        holder.nombreSorteo.setText(listDecimos.get(position).getSorteo());
        holder.premio.setText(listDecimos.get(position).getPremio().toString());
       //TODO: Insertar foto en -
        // holder.foto.setImageResoruce(listDecimos.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listDecimos.size();
    }

    public class ViewHolderDecimos extends RecyclerView.ViewHolder {

        TextView numeroDecimo;
        TextView nombreSorteo;
        TextView premio;
        ImageView foto;




        public ViewHolderDecimos(View itemView) {
            super(itemView);
            numeroDecimo=(TextView) itemView.findViewById(R.id.text_numero_boleto);
            nombreSorteo=(TextView)itemView.findViewById(R.id.nombre_sorteo);
            premio=(TextView) itemView.findViewById(R.id.second_price_text_view);
            foto=(ImageView) itemView.findViewById(R.id.foto_decimo);
        }

    }
}
