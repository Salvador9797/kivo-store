package app.kivo.store;
import org.json.*;import java.io.*;
public class UpdatesReviewsTest {
 static void check(boolean yes,String why){if(!yes)throw new AssertionError(why);}
 static JSONObject comment(long id,String login,String body,String date)throws Exception{return new JSONObject().put("id",id).put("user",new JSONObject().put("login",login).put("type","User")).put("body",body).put("updated_at",date);}
 public static void main(String[] args)throws Exception{
  check(UpdatePolicy.newer("fdroid",101,"1.0",100,"9.0"),"F-Droid must use versionCode");check(!UpdatePolicy.newer("fdroid",100,"99.0",100,"1.0"),"Same build not an update");check(!UpdatePolicy.newer("fdroid",99,"99.0",100,"1.0"),"Downgrade");check(UpdatePolicy.newer("github",0,"v2.1",10,"2.0"),"Numeric Github release");check(!UpdatePolicy.newer("github",0,"nightly",10,"2.0"),"Ambiguous version");
  String pkg="org.example.test";JSONArray all=new JSONArray().put(comment(1,"alice",ReviewPolicy.body(pkg,2,"Original"),"2026-10-01")).put(comment(2,"bob",ReviewPolicy.body(pkg,4,"<img src=x>"),"2026-10-02")).put(comment(3,"ALICE",ReviewPolicy.body(pkg,5,"Updated"),"2026-10-03")).put(comment(4,"mallory",ReviewPolicy.body("org.other.app",1,"Wrong app"),"2026-10-04")).put(comment(5,"spam","bad JSON","2026-10-04"));
  JSONObject result=ReviewPolicy.aggregate(all,pkg);check(result.getInt("count")==2,"One latest rating per Github identity and exact package");check(result.getDouble("average")==4.5,"Average");check(result.getJSONArray("reviews").getJSONObject(0).getString("login").equals("ALICE"),"Server identity retained");
  for(int value:new int[]{0,6,-1})try{ReviewPolicy.body(pkg,value,"");throw new AssertionError("Bad rating accepted");}catch(IOException expected){}
  try{ReviewPolicy.body(pkg,3,new String(new char[2001]).replace('\0','a'));throw new AssertionError("Oversized review");}catch(IOException expected){}
  System.out.println("PASS: exact build/numeric version rules, no downgrade, deduplicated per-account ratings, wrong-package/malformed comments ignored and rating/text bounds.");
 }
}
