package com.allintheloop.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.allintheloop.Bean.ExhibitorListClass.FavoritedExhibitor;
import com.allintheloop.Fragment.ExhibitorFragment.ExhibitorList_Fragment_New;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenPdfFragment extends Fragment implements VolleyInterface {


    WebView pdfView;
    SessionManager sessionManager;
    String pdfName;
    ProgressDialog pd;

    public OpenPdfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open_pdf, container, false);
        pdfView = (WebView) view.findViewById(R.id.pdfView);
        sessionManager = new SessionManager(getActivity());
        pd = new ProgressDialog(getActivity());
        openUrl();
        return view;
    }

    private void openUrl() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getCheckinPdf, Param.getCheckinPdf(sessionManager.getEventId(), sessionManager.checkingAttendeeID), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonPdf = jsonObject.getJSONObject("data");
                        pdfName = jsonPdf.getString("pdf_name");

                        generatePdf();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generatePdf() {
        pdfView.getSettings().setJavaScriptEnabled(true);
//        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +pdfName);

        PrintDocumentAdapter pda = new PrintDocumentAdapter() {
            @Override
            public void onWrite(PageRange[] pages, final ParcelFileDescriptor destination, CancellationSignal cancellationSignal, final WriteResultCallback callback) {
                new OpenPDFView(pdfName, destination, callback).execute();
            }

            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }
                PrintDocumentInfo pdi = new PrintDocumentInfo.Builder(sessionManager.checkingAttendeeID).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
                callback.onLayoutFinished(pdi, true);
            }
        };

        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
        printManager.print("FirstFile", pda, null);
    }


    public class OpenPDFView extends AsyncTask<Void, Void, Void> {
        String url;
        ParcelFileDescriptor destination;
        PrintDocumentAdapter.WriteResultCallback callback;

        public OpenPDFView(String url, ParcelFileDescriptor destination, PrintDocumentAdapter.WriteResultCallback callback) {
            this.url = url;
            this.destination = destination;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            InputStream input = null;
            OutputStream output = null;

            try {
                input = new URL(url).openStream();
                output = new FileOutputStream(destination.getFileDescriptor());

                byte[] buf = new byte[1024];
                int bytesRead;

                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                }

            } catch (FileNotFoundException ee) {
                //TODO Handle Exception
            } catch (Exception e) {
                //TODO Handle Exception
            }

            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            new getExhibitorInAsyncTask().execute();
            //  getExhibitors();
            super.onPostExecute(aVoid);
        }
    }
}
