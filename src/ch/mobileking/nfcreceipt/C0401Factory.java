package ch.mobileking.nfcreceipt;
/**
*
* @author csc
*/
public class C0401Factory implements InvoiceFactory {

   
   public InvoiceOperation getInstance(String jsonStr) throws Exception {
       C0401Operation cpobj = new C0401Operation(jsonStr);
       return cpobj;
   }
   
}
