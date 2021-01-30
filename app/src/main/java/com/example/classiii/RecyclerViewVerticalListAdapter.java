package com.example.classiii;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import static android.content.Context.MODE_PRIVATE;


public class RecyclerViewVerticalListAdapter  extends RecyclerView.Adapter<RecyclerViewVerticalListAdapter.ScannedFaceViewHolder>{
    private List<ScannedFace> verticalScannedFaceList;
    Context context;

    public RecyclerViewVerticalListAdapter(List<ScannedFace> verticalScannedFaceList, Context context){
        this.verticalScannedFaceList= verticalScannedFaceList;
        this.context = context;
    }

    @Override
    public ScannedFaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View ScannedFaceProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_list_item, parent, false);
        ScannedFaceViewHolder gvh = new ScannedFaceViewHolder(ScannedFaceProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(ScannedFaceViewHolder holder, final int position) {

        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(verticalScannedFaceList.get(position).getFace().toString()));
        holder.imageViewDelete.setImageResource(R.drawable.delete);
        holder.txtviewResult.setText(verticalScannedFaceList.get(position).getResult());
        holder.txtviewDate.setText(verticalScannedFaceList.get(position).getDate());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String result = verticalScannedFaceList.get(position).getResult();
                //Toast.makeText(context, result + " is selected", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(context, DisplaySaved.class);
                myIntent.putExtra("facescan", verticalScannedFaceList.get(position).getFace().toString() + "---" + verticalScannedFaceList.get(position).getResult() + "---" + verticalScannedFaceList.get(position).getAngles());
                /// 2901
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); /// 2901
                context.startActivity(myIntent);
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = verticalScannedFaceList.get(position).getKey();

                SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", MODE_PRIVATE);
                //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                System.out.println(" XX key XX  " + key);
                editor.remove(key).commit();
                editor.apply();
                editor.commit();
                verticalScannedFaceList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, verticalScannedFaceList.size());
                Toast.makeText(context,  "Silindi", Toast.LENGTH_SHORT).show();

                ///// 4444 CONFIRMATION ASK WHILE DELETING
                // 111 /////* KAYITY YERİ İZNİ
                ///  222 ////* PERMISSION CHECK
                ///  111  //// FOTOGRAF İSLE BİRDEN FAZLA KEZ BASILMASINI ENGELLE  +++++
                ///  333  //// INFO PAGE  +++++
                /////// SPLASH SCREEN

                ////// RAW RESİMLERİ DE SİL

                ///// CHANGE METHOD +++++
            }
        });
    }

    @Override
    public int getItemCount() {
        return verticalScannedFaceList.size();
    }

    public class ScannedFaceViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView imageViewDelete;
        TextView txtviewDate;
        TextView txtviewResult;
        public ScannedFaceViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.face);
            txtviewResult=view.findViewById(R.id.result);
            txtviewDate=view.findViewById(R.id.date);
            imageViewDelete=view.findViewById(R.id.delete);
        }
    }
}