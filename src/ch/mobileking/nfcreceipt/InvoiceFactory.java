package ch.mobileking.nfcreceipt;

/**
*
* @author csc
*/
public interface InvoiceFactory {
   public InvoiceOperation getInstance(String jsonStr) throws Exception;
}
