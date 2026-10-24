package app.kivo.store;
import java.io.*;import java.security.*;import java.security.cert.Certificate;import java.util.jar.*;
/** Trust anchor published by F-Droid. Verify the signed entry BEFORE trusting index hashes. */
public final class RepoTrust {
 public static final String PIN="43238d512c1e5eb2d6569f4a3afbf5523418b82e0a3ed1552770abb9a9c9ccab";
 public static byte[] entry(File jar)throws Exception{
  try(JarFile jf=new JarFile(jar,true)){
   JarEntry item=jf.getJarEntry("entry.json");if(item==null)throw new SecurityException("Falta entry.json firmado.");
   ByteArrayOutputStream out=new ByteArrayOutputStream();try(InputStream in=jf.getInputStream(item)){byte[] b=new byte[8192];int n;while((n=in.read(b))!=-1){if(out.size()+n>1024*1024)throw new SecurityException("Entrada demasiado grande.");out.write(b,0,n);}}
   Certificate[] cs=item.getCertificates();if(cs!=null)for(Certificate c:cs)if(PIN.equals(hex(MessageDigest.getInstance("SHA-256").digest(c.getEncoded()))))return out.toByteArray();
   throw new SecurityException("La firma del índice no pertenece a F-Droid.");
  }
 }
 public static String hex(byte[] bytes){StringBuilder s=new StringBuilder();for(byte b:bytes)s.append(String.format(java.util.Locale.ROOT,"%02x",b&255));return s.toString();}
 public static void main(String[] args)throws Exception{System.out.write(entry(new File(args[0])));}
}
