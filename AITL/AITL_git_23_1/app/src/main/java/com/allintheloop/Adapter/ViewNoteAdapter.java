package com.allintheloop.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.ViewNote;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by nteam on 2/6/16.
 */
public class ViewNoteAdapter extends RecyclerView.Adapter<ViewNoteAdapter.ViewHolder> implements Filterable, VolleyInterface {


    ArrayList<ViewNote> arrayList;
    ArrayList<ViewNote> tmparrayList;
    Context context;
    Bundle bundle;
    FragmentManager fragmentManager;
    Dialog delete_Dialog;
    SessionManager sessionManager;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;

    public ViewNoteAdapter(ArrayList<ViewNote> arrayList, Context context, FragmentManager fragmentManager) {
        this.arrayList = arrayList;
        this.context = context;
        bundle = new Bundle();
        this.fragmentManager = fragmentManager;
        sessionManager = new SessionManager(context);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
        tmparrayList = new ArrayList<>();
        tmparrayList.addAll(arrayList);
        delete_Dialog = new Dialog(context);
        delete_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delete_Dialog.setContentView(R.layout.dialog_notes_delete);
        delete_Dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ViewNote viewnoteObj = tmparrayList.get(position);
        holder.note_heading.setText(viewnoteObj.getHeading());
        holder.date.setText(viewnoteObj.getCreated_at());
        holder.txt_title.setText(viewnoteObj.getTitle());


        holder.view_note_card.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        holder.edt_button.setOnClickListener(v -> {
            delete_Dialog.show();
            TextView alertDialog_txt_yes = (TextView) delete_Dialog.findViewById(R.id.alertDialog_txt_yes);
            TextView alertDialog_txt_no = (TextView) delete_Dialog.findViewById(R.id.alertDialog_txt_no);

            alertDialog_txt_yes.setOnClickListener(view -> {
                deleteNote(viewnoteObj.getId());
                tmparrayList.remove(position);
                arrayList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                delete_Dialog.dismiss();
            });

            alertDialog_txt_no.setOnClickListener(view -> delete_Dialog.dismiss());


        });


        holder.linear_middleView.setOnClickListener(view -> {


            bundle.putString("edit", "1");
            bundle.putString("heading", viewnoteObj.getHeading());
            bundle.putString("title", viewnoteObj.getTitle());
            bundle.putString("desc", viewnoteObj.getDesc());
            bundle.putString("note_id", viewnoteObj.getId());
            bundle.putString("isCMS", viewnoteObj.getIs_cms());
            bundle.putString("menu_id", viewnoteObj.getMenu_id());
            bundle.putString("Module_id", viewnoteObj.getModule_id());
            bundle.putString("is_custome_title", viewnoteObj.getIs_custom_title());

            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.NotesDetail_Fragment;
            ((MainActivity) context).loadFragment(bundle);
        });

    }


    private void deleteNote(String noteId) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.noteDetele, Param.DeleteNote(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), noteId), 0, false, this);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public ViewNote getItem(int position) {
        return tmparrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return tmparrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new ViewNoteFilter(this, arrayList);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL  DELETE QUERY", jsonObject.toString());
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonData.getJSONArray("note_list");
                        if (sqLiteDatabaseHandler.isNotesListExist(sessionManager.getEventId(), sessionManager.getUserId())) {
                            sqLiteDatabaseHandler.deleteNotesListingData(sessionManager.getEventId(), sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertNotesListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonArray.toString());
                        } else {
                            sqLiteDatabaseHandler.insertNotesListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonArray.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView note_heading, date, txt_title;
        ImageView edt_button;
        CardView view_note_card;
        LinearLayout linear_middleView;

        public ViewHolder(View itemView) {
            super(itemView);
            note_heading = (TextView) itemView.findViewById(R.id.heading);
            date = (TextView) itemView.findViewById(R.id.date);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            edt_button = (ImageView) itemView.findViewById(R.id.edt_button);
            view_note_card = (CardView) itemView.findViewById(R.id.view_note_card);
            linear_middleView = (LinearLayout) itemView.findViewById(R.id.linear_middleView);
        }
    }

    private static class ViewNoteFilter extends Filter {
        private final ViewNoteAdapter adapter;
        private final ArrayList<ViewNote> arrayList;
        private final ArrayList<ViewNote> tmparrayList;

        public ViewNoteFilter(ViewNoteAdapter adapter, ArrayList<ViewNote> arrayList) {
            this.adapter = adapter;
            this.arrayList = arrayList;
            tmparrayList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("AdapterSequnce", constraint.toString());
            tmparrayList.clear();

            final FilterResults results = new FilterResults();
            if (constraint.length() <= 0) {
                tmparrayList.addAll(arrayList);

            } else {
                final String filterString = constraint.toString().toLowerCase();
                for (ViewNote viewnoteObj : arrayList) {

                    String heading = viewnoteObj.getHeading().toLowerCase();
                    String title = viewnoteObj.getTitle().toLowerCase();
                    Log.d("AITL ", "Heading : " + heading);
                    Log.d("AITL ", "title : " + title);
                    if (heading.contains(filterString) || title.contains(filterString)) {

                        tmparrayList.add(viewnoteObj);
                    }
                }
            }
            results.values = tmparrayList;
            results.count = tmparrayList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.tmparrayList.clear();
            adapter.tmparrayList.addAll((ArrayList<ViewNote>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

}

