package app.kivo.store;
public class SelectedVersionTest {
 public static void main(String[] args){
  long selected=UpdatePolicy.selectedCode("fdroid",63,62);
  if(UpdatePolicy.newer("fdroid",selected,"1.2.0",62,"1.2.0"))throw new AssertionError("arm64 code 62 must not be compared to x86_64 code 63");
  if(!UpdatePolicy.newer("fdroid",UpdatePolicy.selectedCode("fdroid",64,64),"1.2.1",62,"1.2.0"))throw new AssertionError("new compatible build must be detected");
  if(UpdatePolicy.selectedCode("github",17,12345)!=17)throw new AssertionError("GitHub asset id is not version code");
  System.out.println("PASS selected ABI version code, genuine update, GitHub version identity");
 }
}
