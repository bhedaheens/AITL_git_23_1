package com.allintheloop.Adapter;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Bean.Document;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;


import me.grantland.widget.AutofitTextView;


/**
 * Created by nteam on 14/6/16.
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> implements Filterable {
    ArrayList<Document> docArrayList;
    ArrayList<Document> tmp_docArrayList;
    Context con;
    String doc_file;
    String str_exten;
    SessionManager sessionManager;
    int position;

    public DocumentAdapter(ArrayList<Document> docArrayList, Context con) {
        this.docArrayList = docArrayList;
        this.con = con;
        tmp_docArrayList = new ArrayList<>();
        tmp_docArrayList.addAll(docArrayList);
        sessionManager = new SessionManager(this.con);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_document, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.position = position;
        Log.d("DocArray", "" + tmp_docArrayList.size());
        final Document docObj = tmp_docArrayList.get(position);

        holder.doc_name.setText(docObj.getDoc_title());


        Log.d("Docfile", "hello" + docObj.getDoc_file());


        holder.linear_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(con)) {
                    if (docObj.getDoc_file() == null || docObj.getDoc_file().equals("")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Document_file_Fragment;
                        Log.d("Bhavdip FolderId", docObj.getDoc_id());
                        sessionManager.setDocumentHirarchy("Docuements / " + docObj.getDoc_title());
                        sessionManager.folder_id(docObj.getDoc_id());
                        ((MainActivity) con).loadFragment();
                    } else {
                        if (docObj.getDoc_type().equalsIgnoreCase("1")) {
                            if (!docObj.getDoc_file().equals("")) {
                                doc_file = docObj.getDoc_file();

                                String[] file_split = doc_file.split("\\.(?=[^\\.]+$)");
                                str_exten = file_split[1];
                                try {
                                    if (str_exten.equalsIgnoreCase("ppt") || str_exten.equalsIgnoreCase("odg")) {
                                        File storagePath = new File(Environment.getExternalStorageDirectory(), "AllInTheLoop");
                                        if (!storagePath.exists()) {
                                            storagePath.mkdirs();
                                        }
                                        con.registerReceiver(onComplete,
                                                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                        con.registerReceiver(onNotificationClick,
                                                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
                                        downloadFile(MyUrls.doc_url + docObj.getDoc_file(), storagePath.getPath(), docObj.getDoc_file());
                                    } else {
                                        Bundle bundle = new Bundle();
                                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                        GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                                        Log.d("Bhavdip FolderId", MyUrls.doc_url + docObj.getDoc_file());
                                        bundle.putString("document_file", MyUrls.doc_url + docObj.getDoc_file());
                                        bundle.putString("file_name", docObj.getDoc_file());
                                        ((MainActivity) con).loadFragment(bundle);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    ToastC.show(con, con.getResources().getString(R.string.noInernet));
                }
            }
        });


        if (docObj.getDoc_file() == null || docObj.getDoc_file().equals("")) {
            holder.app_back.setCardBackgroundColor(con.getResources().getColor(R.color.edittext_backcolor));
            holder.txt_counts.setVisibility(View.VISIBLE);
            holder.txt_documets.setVisibility(View.VISIBLE);
            holder.txt_counts.setText(docObj.getCount());
            if (docObj.getDocicon().equalsIgnoreCase("")) {
                holder.doc_icon.setImageResource(R.drawable.folder_icon);
            } else {
                loadGlideData(MyUrls.doc_url + docObj.getDocicon(), holder.doc_icon);
            }
        } else {
            holder.app_back.setCardBackgroundColor(con.getResources().getColor(R.color.white));
            holder.txt_counts.setVisibility(View.GONE);
            holder.txt_documets.setVisibility(View.GONE);
            if (docObj.getDoc_type().equalsIgnoreCase("1")) {
                if (!docObj.getDoc_file().equals("")) {
                    Log.d("Bhavdip If Condition", docObj.getDoc_file());

                    try {

                        doc_file = docObj.getDoc_file();

                        String[] file_split = doc_file.split("\\.(?=[^\\.]+$)");
                        str_exten = file_split[1];
                        Log.d("Bhavdip file", str_exten);
                        if (str_exten.equalsIgnoreCase("pdf")) {
                            if (docObj.getDocicon().equalsIgnoreCase("")) {
                                holder.doc_icon.setImageResource(R.drawable.pdf_icon);
                            } else {
                                loadGlideData(MyUrls.doc_url + docObj.getDocicon(), holder.doc_icon);
                            }
                        } else if (str_exten.equalsIgnoreCase("doc") || str_exten.equalsIgnoreCase("docx")) {
                            if (docObj.getDocicon().equalsIgnoreCase("")) {
                                holder.doc_icon.setImageResource(R.drawable.docs_file);
                            } else {
                                loadGlideData(MyUrls.doc_url + docObj.getDocicon(), holder.doc_icon);
                            }
                        } else if (str_exten.equalsIgnoreCase("ppt") || str_exten.equalsIgnoreCase("odg")) {
                            if (docObj.getDocicon().equalsIgnoreCase("")) {
                                holder.doc_icon.setImageResource(R.drawable.ppt_icon);
                            } else {
                                loadGlideData(MyUrls.doc_url + docObj.getDocicon(), holder.doc_icon);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void downloadFile(String uRl, String dir, String filename) {
        DownloadManager mgr = (DownloadManager) con.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Downloading")
                .setDestinationInExternalPublicDir(dir, filename);
        mgr.enqueue(request);
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            ToastC.show(con, "Download Completed");
            con.unregisterReceiver(onComplete);
        }
    };

    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
            con.unregisterReceiver(onNotificationClick);
        }
    };

    @Override
    public int getItemCount() {
        return tmp_docArrayList.size();
    }

    public Document getItem(int position) {
        return tmp_docArrayList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Filter getFilter() {
        return new documentFilter(this, docArrayList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView doc_icon;
        AutofitTextView doc_name;
        TextView txt_counts, txt_documets;
        LinearLayout linear_view;
        CardView app_back;

        public ViewHolder(View itemView) {
            super(itemView);
            doc_icon = (ImageView) itemView.findViewById(R.id.doc_icon);
            doc_name = (AutofitTextView) itemView.findViewById(R.id.doc_name);
            linear_view = (LinearLayout) itemView.findViewById(R.id.linear_view);
            app_back = (CardView) itemView.findViewById(R.id.app_back);
            txt_counts = (TextView) itemView.findViewById(R.id.txt_counts);
            txt_documets = (TextView) itemView.findViewById(R.id.txt_documets);
        }
    }

    private static class documentFilter extends Filter {

        private final DocumentAdapter documentAdapter;
        private final ArrayList<Document> docArrayList;
        private final ArrayList<Document> tmp_docArrayList;

        public documentFilter(DocumentAdapter documentAdapter, ArrayList<Document> docArrayList) {
            this.documentAdapter = documentAdapter;
            this.docArrayList = docArrayList;
            tmp_docArrayList = new ArrayList<>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults results = new FilterResults();
            if (constraint.length() <= 0) {
                tmp_docArrayList.addAll(docArrayList);
            } else {
                final String filter_string = constraint.toString().toLowerCase();
                for (Document docObj : docArrayList) {
                    String title = docObj.getDoc_title().toLowerCase();
                    if (title.contains(filter_string)) {
                        tmp_docArrayList.add(docObj);
                    }
                }
            }
            results.values = tmp_docArrayList;
            results.count = tmp_docArrayList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            documentAdapter.tmp_docArrayList.clear();
            documentAdapter.tmp_docArrayList.addAll((ArrayList<Document>) results.values);
            documentAdapter.notifyDataSetChanged();
        }
    }


    private void loadGlideData(String name, ImageView imageView) {
        Glide.with(con)
                .load(name)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

}