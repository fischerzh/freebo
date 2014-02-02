package ch.mobileking.nfcreceipt;

import java.io.StringWriter;
import java.security.PublicKey;
import org.apache.commons.codec.binary.Base64;
import org.json.*;
import java.util.*;

/**
 *
 * @author csc
 */
public class MessageGenerator {    
    public static Hashtable verifySignedInvoiceOperation(String message) throws Exception {
        Base64 base64 = new Base64();
        Hashtable hash = new Hashtable();
        JSONObject parser = new JSONObject(message);
        String vacsig = parser.getString("vacsig");
        if(vacsig==null || vacsig.trim().length()==0)
            throw new Exception("no vac signature");
        hash.put("vacsig",vacsig);
        String vacid = parser.getString("vacid");
        if(vacid==null || vacid.trim().length()==0)
            throw new Exception("no vac id");
        hash.put("vacid",vacid);
        String mID = parser.getString("mID");
        if(mID==null || mID.trim().length()==0)
            throw new Exception("no mID");
        hash.put("mID",mID);
        String vacalg = parser.getString("vacalg");
        if(vacalg==null || vacalg.trim().length()==0)
            throw new Exception("no vacalg");
        hash.put("vacalg",vacalg);
        String mDesc = parser.getString("mDesc");
        if(mDesc==null || mDesc.trim().length()==0)
            throw new Exception("no mDesc");
        hash.put("mDesc",mDesc);
        String pk = parser.getString("pk");
        if(pk==null || pk.trim().length()==0)
            throw new Exception("no pk");
        hash.put("pk",pk);
        String vacpk = parser.getString("vacpk");
        if(vacpk==null || vacpk.trim().length()==0)
            throw new Exception("no pk");
        hash.put("vacpk",vacpk);
        String texttodsign = pk+":"+vacid+":"+mID+":"+mDesc;
        PublicKey vacPublicKey = CryptoTool.getPublicKeyFromString(vacpk);
        boolean verify = CryptoTool.verify(vacPublicKey, texttodsign.getBytes(), base64.decode(vacsig.getBytes()), "SHA1withRSA");
        if(!verify)
            throw new Exception("Fail to verify signature");
        PublicKey publicKey = CryptoTool.getPublicKeyFromString(pk);
        JSONObject jdetails = parser.getJSONObject("details");
        String alg = jdetails.getString("alg");
        String type = jdetails.getString("type");
        hash.put("type",type);
        String sig = jdetails.getString("sig");
        String request = jdetails.getString("request");
        verify = CryptoTool.verify(publicKey, request.getBytes(), base64.decode(sig.getBytes()), "SHA1withRSA");
        if(!verify)
            throw new Exception("Fail to verify signature");
        hash.put("request",new String(base64.decode(request.getBytes())));
        return hash;
    }
}
