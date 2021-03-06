package aversitoca.aversitoca;

/**
 * Created by Marcos Muñoz.
 *
 * Adapter que bindea y genera la lista de decimos
 * conectado los datos de bbdd con su correspondiente vista
 */

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

    List<Decimo> listDecimos;


    public class ViewHolderDecimos extends RecyclerView.ViewHolder {

        TextView numeroDecimo;
        TextView nombreSorteo;
        TextView premio;
        TextView estado;
        ImageView foto;
        public RelativeLayout viewBackground, viewForeground;




        public ViewHolderDecimos(View itemView) {
            super(itemView);
            numeroDecimo= itemView.findViewById(R.id.numero2);
            nombreSorteo=itemView.findViewById(R.id.sorteo2);
            premio=itemView.findViewById(R.id.premio2);
            estado= itemView.findViewById(R.id.estadoSorteo);
            foto= itemView.findViewById(R.id.foto2);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }

    }

    public AdapterDecimos(Context context,List<Decimo> listDecimos) {
        Context context1 = context;
        this.listDecimos = listDecimos;
    }

    public AdapterDecimos(List<Decimo> listDecimos) {
        this.listDecimos = listDecimos;
    }


    @Override
    public ViewHolderDecimos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_decimo,parent,false);

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
                holder.estado.setText(R.string.notHappened);
                holder.premio.setText("");
                break;
            case 1:
                holder.premio.setText(posDecimo.getPremio() + "€ ");
                holder.estado.setText(R.string.happening);
                break;
            case 2:
                holder.premio.setText(posDecimo.getPremio() + "€ ");
                holder.estado.setText(R.string.provisional);
                break;
            case 3:
                holder.premio.setText(posDecimo.getPremio() + "€ ");
                holder.estado.setText(R.string.provisional);
                break;
            case 4:
                holder.premio.setText(posDecimo.getPremio() + "€ ");
                holder.estado.setText(R.string.definitive);
                break;
        }



        //Insertar foto en lista
        String fotostring=posDecimo.getFoto();
        String sorteo=posDecimo.getSorteo() ;
        if (fotostring!=null) {
            if (fotostring.length() != 0) {
                Bitmap myBitmap = BitmapFactory.decodeFile(fotostring);
                holder.foto.setImageBitmap(myBitmap);
            }else {
                if (sorteo.equals("Loteria del Niño")) {
                    holder.foto.setImageResource(R.drawable.ic_nino);
                } else {
                    holder.foto.setImageResource(R.mipmap.ic_launcher);
                }
            }

        }else{
            if (sorteo.equals("Loteria del Niño")) {
                holder.foto.setImageResource(R.drawable.ic_nino);
            } else {
                holder.foto.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listDecimos.size();
    }

    public void removeItem(int position) {
        listDecimos.remove(position);
        // notificar eliminacion del item con la posicion
        // para la actulzacion de la vista y las animaciones correspondientes
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listDecimos.size());
    }

    public void restoreItem(Decimo item, int position) {
        listDecimos.add(position, item);
        // notificar insercion en posicion
        notifyItemInserted(position);
    }



}
