package app.kivo.store;
import org.json.*;import java.io.*;
/** OAuth errors may arrive as JSON on HTTP 400. Never log response credentials. */
final class OAuthProtocol {
 static JSONObject response(int status,String body)throws Exception{JSONObject value;try{value=new JSONObject(body);}catch(JSONException e){throw new IOException("Respuesta de GitHub no válida.");}if(status==200||status==400&&value.has("error"))return value;throw new IOException("GitHub respondió HTTP "+status+".");}
 static boolean waiting(String error){return error.equals("authorization_pending")||error.equals("slow_down");}
 static int nextInterval(JSONObject response,int previous){return response.optString("error").equals("slow_down")?Math.max(previous+5,response.optInt("interval",previous+5)):Math.max(5,previous);}
}
