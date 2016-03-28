package clowdtech.printer.documents;

import android.content.Context;

import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

import java.util.ArrayList;
import java.util.List;

//TODO: this base class should probably become a piece designed to print a document type
public class StarDocument {
    protected final byte[] COMMAND_CUT = new byte[]{0x1b, 0x64, 0x02};

    protected void sendCommand(Context context, String portName, String portSettings, ArrayList<byte[]> byteList) throws StarIOPortException {
        StarIOPort port = null;
        try {
            /*
             * using StarIOPort3.1.jar (support USB Port) Android OS Version: upper 2.2
			 */
            port = StarIOPort.getPort(portName, portSettings, 10000, context);
			/*
			 * using StarIOPort.jar Android OS Version: under 2.1 port = StarIOPort.getPort(portName, portSettings, 10000);
			 */
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }

			/*
			 * Using Begin / End Checked Block method When sending large amounts of raster data,
			 * adjust the value in the timeout in the "StarIOPort.getPort" in order to prevent
			 * "timeout" of the "endCheckedBlock method" while a printing.
			 *
			 * If receipt print is success but timeout error occurs(Show message which is "There
			 * was no response of the printer within the timeout period." ), need to change value
			 * of timeout more longer in "StarIOPort.getPort" method.
			 * (e.g.) 10000 -> 30000
			 */
            StarPrinterStatus status = port.beginCheckedBlock();

            if (status.offline) {
                throw new StarIOPortException("A printer is offline");
            }

            byte[] commandToSendToPrinter = convertFromListByteArrayTobyteArray(byteList);
            port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);

            port.setEndCheckedBlockTimeoutMillis(30000);// Change the timeout time of endCheckedBlock method.
            status = port.endCheckedBlock();

            if (status.coverOpen) {
                throw new StarIOPortException("Printer cover is open");
            } else if (status.receiptPaperEmpty) {
                throw new StarIOPortException("Receipt paper is empty");
            } else if (status.offline) {
                throw new StarIOPortException("Printer is offline");
            }
//		} catch (StarIOPortException e) {
//			Builder dialog = new Builder(context);
//			dialog.setNegativeButton("OK", null);
//			AlertDialog alert = dialog.create();
//			alert.setTitle("Failure");
//			alert.setMessage(e.getMessage());
//			alert.setCancelable(false);
//			alert.show();
        } finally {
            if (port != null) {
                try {
                    StarIOPort.releasePort(port);
                } catch (StarIOPortException ignored) {
                }
            }
        }
    }

    private static byte[] convertFromListByteArrayTobyteArray(List<byte[]> ByteArray) {
        int dataLength = 0;
        for (int i = 0; i < ByteArray.size(); i++) {
            dataLength += ByteArray.get(i).length;
        }

        int distPosition = 0;
        byte[] byteArray = new byte[dataLength];
        for (int i = 0; i < ByteArray.size(); i++) {
            System.arraycopy(ByteArray.get(i), 0, byteArray, distPosition, ByteArray.get(i).length);
            distPosition += ByteArray.get(i).length;
        }

        return byteArray;
    }
}
