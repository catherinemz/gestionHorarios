package mx.edu.utng.schedule;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Catherine on 3/5/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Upload> uploads;
    private OnItemClickListener listener;

    public ImageAdapter(Context context, List<Upload> uploads){
        this.context = context;
        this.uploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent,
                false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = uploads.get(position);
        holder.txvDayName.setText(uploadCurrent.getDayName());
        holder.txvOpen.setText(uploadCurrent.getOpenHour());
        holder.txvClose.setText(uploadCurrent.getCloseHour());
        Picasso.with(context)
                .load(uploadCurrent.getUrlImage())
                .fit().centerCrop().into(holder.imvImageDay);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        public TextView txvDayName;
        public TextView txvOpen;
        public TextView txvClose;
        public ImageView imvImageDay;

        public ImageViewHolder(View itemView) {
            super(itemView);

            txvDayName = itemView.findViewById(R.id.txv_day);
            txvOpen = itemView.findViewById(R.id.txv_open);
            txvClose = itemView.findViewById(R.id.txv_close);
            imvImageDay = itemView.findViewById(R.id.imv_day);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Selecciona una acción: ");
            MenuItem doWhatever = contextMenu.add(Menu.NONE, 1, 1, "Actualizar");
            MenuItem delete = contextMenu.add(Menu.NONE,2,2,"Eliminar");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            if(listener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            listener.onWhatEverClick(position);
                            return true;
                        case 2:
                            listener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}