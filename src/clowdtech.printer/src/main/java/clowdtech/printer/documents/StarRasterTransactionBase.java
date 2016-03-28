package clowdtech.printer.documents;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

import clowdtech.printer.ReceiptLineInfo;
import clowdtech.printer.StarBitmap;
import clowdtech.printer.formatinfo.PrintFormatInfoOrder;

public class StarRasterTransactionBase extends StarDocument {
    protected int printableArea = 576; // for raster data

    protected byte[] createRasterCommand(String printText, int textSize, int bold) {
        byte[] command;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        Typeface typeface;

        try {
            typeface = Typeface.create(Typeface.MONOSPACE, bold);
        } catch (Exception e) {
            typeface = Typeface.create(Typeface.DEFAULT, bold);
        }

        paint.setTypeface(typeface);
        paint.setTextSize(textSize * 2);
        paint.setLinearText(true);

        TextPaint textpaint = new TextPaint(paint);
        textpaint.setLinearText(true);
        StaticLayout staticLayout = new StaticLayout(printText, textpaint, printableArea, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        int height = staticLayout.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(staticLayout.getWidth(), height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.WHITE);
        c.translate(0, 0);
        staticLayout.draw(c);

        StarBitmap starbitmap = new StarBitmap(bitmap, false, printableArea);

        command = starbitmap.getImageRasterDataForPrinting(true);

        return command;
    }

    protected void printSeparator(ArrayList<byte[]> list) {
        list.add(createRasterCommand("------------------------------------\n", 13, 0));
    }

    protected void printHeader(PrintFormatInfoOrder formatInfo, ArrayList<byte[]> list) {
        list.add(createRasterCommand("\n", 13, 0));

        list.add(createRasterCommand(formatInfo.getHeader() + "\n", 13, 0));

        String date = DateTimeFormat.forPattern("dd/MM/yyyy").print(formatInfo.getDate());
        String time = DateTimeFormat.forPattern("hh:mm a").print(formatInfo.getDate());

        list.add(createRasterCommand(String.format("Date: %s\t\t\t\tTime: %s", date, time), 13, 0));

        printSeparator(list);
    }


    protected void printLines(PrintFormatInfoOrder formatInfo, ArrayList<byte[]> list) {
        list.add(createRasterCommand("Description            Total", 13, 0));

        //char limits
        //total - 36
        //description - 33
        //line count - 5
        //total - 10

        for (ReceiptLineInfo line : formatInfo.getLines()) {
            String description = line.getDescription().length() > 18 ? line.getDescription().substring(0, 17) : line.getDescription();

            list.add(createRasterCommand(String.format("%1$-" + 18 + "s%2$-" + 5 + "s%3$-" + 10 + "s", description, line.getLineCount(), line.getLineCost()), 13, 0));

        }

        printSeparator(list);
    }

    protected void printGrandTotal(PrintFormatInfoOrder formatInfo, ArrayList<byte[]> list) {
        list.add(createRasterCommand(String.format("Total \t\t\t\t\t %s\n", formatInfo.getTotal()), 13, 0));

        printSeparator(list);
    }

    protected void printFooter(PrintFormatInfoOrder formatInfo, ArrayList<byte[]> list) {
        list.add(createRasterCommand(String.format("%s\r\n\r\n", formatInfo.getFooter()), 13, 0));
    }
}
