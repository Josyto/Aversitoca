package aversitoca.aversitoca;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;



public class AdapterDecimos extends RecyclerView.Adapter<AdapterDecimos.ViewHolderDecimos>{

    private Context context;
    List<Decimo> listDecimos;


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

    public AdapterDecimos(Context context,List<Decimo> listDecimos) {
        this.context=context;
        this.listDecimos = listDecimos;
    }

    public AdapterDecimos(List<Decimo> listDecimos) {
        this.listDecimos = listDecimos;
    }


    @Override
    public ViewHolderDecimos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_decimo2,parent,false);

        return new ViewHolderDecimos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDecimos holder, int position) {
        //Carga datos del decimo en la lista
        final Decimo posDecimo = listDecimos.get(position);
        holder.numeroDecimo.setText(posDecimo.getNumero());
        holder.nombreSorteo.setText(posDecimo.getSorteo());

        //holder.premio.setText(posDecimo.getPremio() + "€");


        //Se informa al usuario del estado del sorteo, y del premio conocido en cada momento
        switch (posDecimo.getCelebrado()){
            case 0:
                holder.premio.setText(R.string.notHappened);
                break;
            case 1:
                holder.premio.setText(posDecimo.getPremio() + "€ " + context.getString(R.string.happening));
                break;
            case 2:
                holder.premio.setText(posDecimo.getPremio() + "€ " + context.getString(R.string.provisional));
                break;
            case 3:
                holder.premio.setText(posDecimo.getPremio() + "€ " + context.getString(R.string.provisional));
                break;
            case 4:
                holder.premio.setText(posDecimo.getPremio() + "€ " + context.getString(R.string.definitive));
                break;
        }



        //Insertar foto en lista
        String fotostring=posDecimo.getFoto();
        if (fotostring.length()!=0) {
            Bitmap myBitmap = BitmapFactory.decodeFile(fotostring);
            holder.foto.setImageBitmap(myBitmap);
        } else{
            holder.foto.setImageResource(R.mipmap.ic_launcher);
        }
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
        notifyItemRangeChanged(position, listDecimos.size());
    }

    public void restoreItem(Decimo item, int position) {
        listDecimos.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }



}
