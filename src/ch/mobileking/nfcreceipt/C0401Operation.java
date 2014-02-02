package ch.mobileking.nfcreceipt;

import java.util.*;
import org.json.*;
import java.io.StringWriter;


/**
 *
 * @author csc
 */
public class C0401Operation implements InvoiceOperation {
    public String invoiceNumber;
    public String invoiceDate;
    public String invoiceTime;
    public String seller;
    public String buyer;
    public String mainRemark;
    public String clearanceMark;
    public String invoiceType;
    public String donateMark;
    public String carrierType;
    public String carrierId1;
    public String carrierId2;
    public String printMark;
    public String NPOBAN;
    public String randomNumber;
    public Vector details;
    
    public String salesAmount;
    public String freeTaxSalesAmount;
    public String zeroTaxSalesAmount;
    public String taxType;
    public String taxRate;
    public String taxAmount;
    public String totalAmount;
    public String discountAmount;
    public String originalCurrencyAmount;
    public String exchangeRate;
    public String currencyCodeEnum;
    
    
    public C0401Operation(String jsonStr) throws Exception {
        JSONObject parser = new JSONObject(jsonStr);
        String Type = parser.getString("Type");
        if(Type.compareTo("C0401")!=0)
            throw new Exception("Operation Type error!");
        JSONObject jsonobjm = parser.getJSONObject("Main");
        invoiceNumber = jsonobjm.getString("invoiceNumber");
        if(invoiceNumber == null || invoiceNumber.trim().length()==0)
            throw new Exception("no invoiceNumber");
        invoiceDate = jsonobjm.getString("invoiceDate");
        if(invoiceDate == null || invoiceDate.trim().length()==0)
            throw new Exception("no invoiceDate");
        invoiceTime = jsonobjm.getString("invoiceTime");
        if(invoiceTime == null || invoiceTime.trim().length()==0)
            throw new Exception("no invoiceTime");
        JSONObject jsonobjs = jsonobjm.getJSONObject("seller");
        seller = jsonobjs.getString("identifier");
        if(seller == null || seller.trim().length()==0)
            throw new Exception("no seller id");
        JSONObject jsonobjb = jsonobjm.getJSONObject("buyer");
        buyer = jsonobjb.getString("identifier");
        if(buyer == null || buyer.trim().length()==0)
            buyer="";
        mainRemark = jsonobjm.getString("mainRemark");
        if(mainRemark == null || mainRemark.trim().length()==0)
            mainRemark="";
        clearanceMark = jsonobjm.getString("clearanceMark");
        if(clearanceMark == null || clearanceMark.trim().length()==0)
            clearanceMark="";
        invoiceType = jsonobjm.getString("invoiceType");
        if(invoiceType == null || invoiceType.trim().length()==0)
            throw new Exception("no invoiceType");
        donateMark = jsonobjm.getString("donateMark");
        if(donateMark == null || donateMark.trim().length()==0)
            throw new Exception("no donateMark");    
        carrierType = jsonobjm.getString("carrierType");
        if(carrierType == null || carrierType.trim().length()==0)
            carrierType="";    
        carrierId1 = jsonobjm.getString("carrierId1");
        if(carrierId1 == null || carrierId1.trim().length()==0)
            carrierId1="";    
        carrierId2 = jsonobjm.getString("carrierId2");
        if(carrierId2 == null || carrierId2.trim().length()==0)
            carrierId2="";    
        printMark = jsonobjm.getString("printMark");
        if(printMark == null || printMark.trim().length()==0)
            throw new Exception("no printMark");    
        NPOBAN = jsonobjm.getString("NPOBAN");
        if(NPOBAN == null || NPOBAN.trim().length()==0)
            NPOBAN="";    
        randomNumber = jsonobjm.getString("randomNumber");
        if(randomNumber == null || randomNumber.trim().length()==0)
            throw new Exception("no randomNumber"); 
         JSONArray jsonobjdetails = parser.getJSONArray("details");
         if(jsonobjdetails==null || jsonobjdetails.length()==0)
             details = new Vector();
         else {
             details = new Vector();
             for(int i=0;i<jsonobjdetails.length();i++) {
                 JSONObject productinfo = (JSONObject)jsonobjdetails.get(i);
                 String [] str = new String[7];
                 str[0] = productinfo.getString("description");
                 str[1] = productinfo.getString("quantity");
                 str[2] = productinfo.getString("Unit");
                 str[3] = productinfo.getString("unitPrice");
                 str[4] = productinfo.getString("amount");
                 str[5] = productinfo.getString("sequenceNumber");
                 str[6] = productinfo.getString("remark");
                 details.add(str);
             }            
         }
         
        JSONObject jsonobja = parser.getJSONObject("amounts");
        salesAmount = jsonobja.getString("salesAmount");
        if(salesAmount == null || salesAmount.trim().length()==0)
            throw new Exception("no salesAmount"); 
       freeTaxSalesAmount = jsonobja.getString("freeTaxSalesAmount");
        if(freeTaxSalesAmount == null || freeTaxSalesAmount.trim().length()==0)
            freeTaxSalesAmount=""; 
        zeroTaxSalesAmount = jsonobja.getString("zeroTaxSalesAmount");
        if(zeroTaxSalesAmount == null || zeroTaxSalesAmount.trim().length()==0)
            zeroTaxSalesAmount=""; 
        taxType = jsonobja.getString("taxType");
        if(taxType == null || taxType.trim().length()==0)
            throw new Exception("no taxType"); 
        taxRate = jsonobja.getString("taxRate");
        if(taxRate == null || taxRate.trim().length()==0)
            throw new Exception("no taxRate"); 
        taxAmount = jsonobja.getString("taxAmount");
        if(taxAmount == null || taxAmount.trim().length()==0)
            throw new Exception("no taxAmount"); 
        totalAmount = jsonobja.getString("totalAmount");
        if(totalAmount == null || totalAmount.trim().length()==0)
            throw new Exception("no totalAmount"); 
        discountAmount = jsonobja.getString("discountAmount");
        if(discountAmount == null || discountAmount.trim().length()==0)
            discountAmount=""; 
        originalCurrencyAmount = jsonobja.getString("originalCurrencyAmount");
        if(originalCurrencyAmount == null || originalCurrencyAmount.trim().length()==0)
            originalCurrencyAmount=""; 
        exchangeRate = jsonobja.getString("exchangeRate");
        if(exchangeRate == null || exchangeRate.trim().length()==0)
            exchangeRate=""; 
        currencyCodeEnum = jsonobja.getString("currencyCodeEnum");
        if(currencyCodeEnum == null || currencyCodeEnum.trim().length()==0)
            currencyCodeEnum="";         
    }    
    
    
    public String getType() {
        return "C0401";
    }

    
}
