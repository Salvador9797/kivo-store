package app.kivo.store;
import org.json.*;
/** A verified install from either source qualifies for the same exact Android package. */
final class ReviewEligibility {
 static boolean allows(String pkg,String signer,JSONArray verified){if(pkg==null||pkg.isEmpty()||signer==null||signer.isEmpty())return false;for(int i=0;i<verified.length();i++){JSONObject entry=verified.optJSONObject(i);if(entry!=null&&pkg.equals(entry.optString("package"))&&signer.equals(entry.optString("signer")))return true;}return false;}
}
