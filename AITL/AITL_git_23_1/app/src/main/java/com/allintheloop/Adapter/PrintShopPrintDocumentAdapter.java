package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.pdf.PrintedPdfDocument;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.allintheloop.Bean.ImageAndTextContainer;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Aiyaz on 18/5/17.
 */

public class PrintShopPrintDocumentAdapter extends PrintDocumentAdapter {


    private PrintedPdfDocument pdfDocument;
    private Context context;
    private int pageCount;
    private ImageAndTextContainer imageAndTextContainer;
    String printName = "";

    public PrintShopPrintDocumentAdapter(ImageAndTextContainer imageAndTextContainer, Context context, String print_name) {
        this.imageAndTextContainer = imageAndTextContainer;
        this.context = context;
        this.printName = print_name;
    }

    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, final LayoutResultCallback layoutResultCallback, Bundle bundle) {
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                layoutResultCallback.onLayoutCancelled();
            }
        });

        int newPageCount;
        // Mils is 1/1000th of an inch. Obviously.
        if (printAttributes1.getMediaSize().getHeightMils() < 10000) {
            newPageCount = 2;
        } else {
            newPageCount = 1;
        }


        boolean layoutChanged = newPageCount != pageCount;
        pageCount = newPageCount;

        pdfDocument = new PrintedPdfDocument(context, printAttributes1);
        PrintDocumentInfo info = new PrintDocumentInfo
                .Builder(printName)
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(0)
                .build();
        layoutResultCallback.onLayoutFinished(info, layoutChanged);

    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, final WriteResultCallback callback) {

        // Register a cancellation listener
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                // If cancelled then ensure that the PDF doc gets thrown away
                pdfDocument.close();
                pdfDocument = null;
                // And callback
                callback.onWriteCancelled();
            }
        });

        // Iterate through the pages
        for (int currentPageNumber = 0; currentPageNumber < pageCount; currentPageNumber++) {
            // Has this page been requested?
            if (!pageRangesContainPage(currentPageNumber, pages)) {
                // Skip this page
                continue;
            }

            // Start the current page
            PdfDocument.Page page = pdfDocument.startPage(currentPageNumber);

            // Get the canvas for this page
            Canvas canvas = page.getCanvas();

            // Draw on the page
            drawPage(currentPageNumber, canvas);

            // Finish the page
            pdfDocument.finishPage(page);
        }

        // Attempt to send the completed doc out
        try {
            pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            pdfDocument.close();
            pdfDocument = null;
        }

        // The print is complete
        callback.onWriteFinished(pages);
    }

    private boolean pageRangesContainPage(int pageNumber, PageRange[] ranges) {
        for (PageRange range : ranges) {
            if (pageNumber >= range.getStart() && pageNumber <= range.getEnd()) {
                return true;
            }
        }
        return false;
    }


    private void drawPage(int pageNumber, Canvas canvas) {
        if (pageCount == 1) {
            // We're putting everything on one page
            Rect imageRect = new Rect(10, 10, canvas.getWidth() - 30, canvas.getHeight() / 2 - 10);
            drawImage(imageAndTextContainer.getImage(), canvas, imageRect);
            Rect textRect = new Rect(10, canvas.getHeight() / 2 + 10, canvas.getWidth() - 10, canvas.getHeight() - 10);
            drawText(imageAndTextContainer.getText(), canvas, textRect);
        } else {
            // Same rect for image and text
            Rect contentRect = new Rect(10, 10, canvas.getWidth() - 30, canvas.getHeight() - 10);
            // Image on page 0, text on page 1
            if (pageNumber == 0) {
                drawImage(imageAndTextContainer.getImage(), canvas, contentRect);
            } else {
                drawText(imageAndTextContainer.getText(), canvas, contentRect);
            }
        }
    }

    private void drawText(String text, Canvas canvas, Rect rect) {

        TextPaint paint = new TextPaint();
        paint.setColor(Color.BLACK);

        StaticLayout sl = new StaticLayout(text, paint, (int) rect.width(), Layout.Alignment.ALIGN_CENTER, 1, 1, false);

        canvas.save();
        canvas.translate(rect.left, rect.top);
        sl.draw(canvas);
        canvas.restore();

    }

    private void drawImage(Bitmap image, Canvas canvas, Rect r) {
        canvas.drawBitmap(image, null, r, new Paint());
    }
}
