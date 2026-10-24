package app.kivo.store;
import org.json.*;import java.io.*;
public class OAuthProtocolTest {
 static void check(boolean b,String message){if(!b)throw new AssertionError(message);}
 public static void main(String[] args)throws Exception{
 JSONObject pending=OAuthProtocol.response(400,"{\"error\":\"authorization_pending\"}");check(OAuthProtocol.waiting(pending.getString("error")),"HTTP400 pending must continue polling");
 JSONObject slow=OAuthProtocol.response(400,"{\"error\":\"slow_down\",\"interval\":15}");int interval=OAuthProtocol.nextInterval(slow,5);check(interval==15,"Server interval must be respected");check(OAuthProtocol.nextInterval(slow,interval)==20,"Further slowdown adds5 seconds");check(OAuthProtocol.nextInterval(pending,20)==20,"Pending cannot reset longer interval");
 check(!OAuthProtocol.waiting("access_denied")&&!OAuthProtocol.waiting("expired_token"),"Denial and expiry stop polling");
 check(OAuthProtocol.response(200,"{\"access_token\":\"test-only\"}").has("access_token"),"Token result");
 for(int code:new int[]{403,429,500,503})try{OAuthProtocol.response(code,"{\"error\":\"temporary\"}");throw new AssertionError("Unexpected HTTP accepted");}catch(IOException expected){}
 try{OAuthProtocol.response(200,"<html>proxy</html>");throw new AssertionError("HTML accepted");}catch(IOException expected){}
 System.out.println("PASS: device-flow JSON HTTP400 pending, progressive server interval, terminal errors, token response and transient/malformed HTTP.");
 }
}
