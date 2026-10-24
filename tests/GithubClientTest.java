package app.kivo.store;
import java.io.*;import java.nio.file.*;import java.util.*;import java.util.concurrent.*;import org.json.*;
public class GithubClientTest {
 static void check(boolean b,String msg){if(!b)throw new AssertionError(msg);}
 static Map<String,String> headers(String... values){Map<String,String> m=new HashMap<>();for(int i=0;i<values.length;i+=2)m.put(values[i],values[i+1]);return m;}
 static class Time implements GithubClient.Clock{long ms=1700000000000L;public long now(){return ms;}}
 static class Net implements GithubClient.Transport{int calls;GithubClient.Reply next;String seenEtag;public GithubClient.Reply request(String p,String token,String etag){calls++;seenEtag=etag;return next;}}
 static GithubClient.Failure failure(GithubClient c,String path,String token)throws Exception{try{c.get(path,token,0);throw new AssertionError("Expected failure");}catch(GithubClient.Failure e){return e;}}
 public static void main(String[] args)throws Exception{
  Time time=new Time();Net net=new Net();File cache=Files.createTempDirectory("kivo-github-test-").toFile();GithubClient c=new GithubClient(cache,time,net,null);
  net.next=new GithubClient.Reply(200,"{\"id\":1}",headers("ETag","\"v1\"","X-RateLimit-Remaining","59","X-RateLimit-Resource","core"));c.get("repos/a/b","",1000);c.get("repos/a/b","",1000);check(net.calls==1,"Warm cache must avoid a second API call");
  time.ms+=1001;net.next=new GithubClient.Reply(304,"",headers("X-RateLimit-Remaining","58"));check(c.get("repos/a/b","",1000).getInt("id")==1,"304 must retain body");check(net.seenEtag.equals("\"v1\""),"Expired response must use conditional request");
  long reset=time.ms+60000;net.next=new GithubClient.Reply(403,"{\"message\":\"API rate limit exceeded\"}",headers("X-RateLimit-Remaining","0","X-RateLimit-Reset",String.valueOf(reset/1000),"X-RateLimit-Resource","core"));GithubClient.Failure e=failure(c,"repos/a/c","");check(e.limited&&e.retryAt==reset/1000*1000,"Primary limit must obey server reset");int count=net.calls;failure(c,"repos/a/d","");check(net.calls==count,"No HTTP calls allowed during cooldown");
  GithubClient restarted=new GithubClient(cache,time,net,null);failure(restarted,"repos/a/z","");check(net.calls==count,"Cooldown must survive client restart");
  net.next=new GithubClient.Reply(200,"{\"items\":[]}",headers("X-RateLimit-Remaining","9","X-RateLimit-Resource","search"));c.get("search/repositories?q=app","",1000);check(net.calls==count+1,"Search window must be independent of core");
  net.next=new GithubClient.Reply(403,"{\"message\":\"Resource not accessible\"}",headers("X-RateLimit-Remaining","45","X-RateLimit-Resource","core"));e=failure(c,"repos/a/c","personal-token-placeholder");check(!e.limited&&e.code==403,"Ordinary forbidden response must not be labelled rate limit");
  net.next=new GithubClient.Reply(429,"{\"message\":\"Too many requests\"}",headers("Retry-After","90","X-RateLimit-Resource","search"));e=failure(c,"search/repositories?q=other","");check(e.limited&&e.retryAt==time.ms+90000,"Secondary limit must honor Retry-After");count=net.calls;failure(c,"search/repositories?q=third","");check(net.calls==count,"Secondary window must block repeated requests");
  time.ms+=90001;net.next=new GithubClient.Reply(200,"{\"items\":[]}",headers("X-RateLimit-Remaining","8","X-RateLimit-Resource","search"));c.get("search/repositories?q=third","",1000);check(net.calls==count+1,"Explicit retry after cooldown must work");
  net.next=new GithubClient.Reply(200,"{\"id\":2}",headers("ETag","\"v2\"","X-RateLimit-Remaining","30","X-RateLimit-Resource","core"));c.get("repos/a/b","personal-token-placeholder",1000);check(net.seenEtag.isEmpty(),"Account caches must not share ETags");
  net.next=new GithubClient.Reply(200,"[{\"id\":12,\"body\":\"fixture\"}]",headers("X-RateLimit-Remaining","29","X-RateLimit-Resource","core"));check(c.get("repos/a/b/issues/1/comments","personal-token-placeholder",1000).getJSONArray("items").getJSONObject(0).getInt("id")==12,"Issue comment arrays must parse and cache correctly");
  net.calls=0;ExecutorService pool=Executors.newFixedThreadPool(6);List<Future<?>> work=new ArrayList<>();for(int i=0;i<6;i++)work.add(pool.submit(()->{try{c.get("repos/same/path","",10000);}catch(Exception ex){throw new RuntimeException(ex);}}));for(Future<?> f:work)f.get();pool.shutdown();check(net.calls==1,"Concurrent identical queries must be serialized and cached");
  for(File f:cache.listFiles()){check(!f.getName().contains("personal-token-placeholder"),"Token in filename");check(!new String(Files.readAllBytes(f.toPath()),"UTF-8").contains("personal-token-placeholder"),"Token in disk cache");}
  System.out.println("PASS: cached/conditional requests, primary and secondary cooldown, persisted limits, independent resources, non-rate 403, account isolation and concurrent deduplication.");
 }
}
