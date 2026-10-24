package app.kivo.store;
public class VersionsTest {
 static void check(String a,String b,int expected){int got=Versions.compare(a,b);if(got!=expected)throw new AssertionError(a+" / "+b+": "+got+" != "+expected);}
 public static void main(String[] args){check("v2.0.0","1.9.9",1);check("1.10","1.9",1);check("1.0","1.0.0",0);check("1.0.1","2.0",-1);check("release-2","1",0);check("2.0-beta","1.0",0);check(null,"1",0);check("V3","2",1);check("99999999999999999999999999999","1",0);System.out.println("PASS: conservative numeric version comparison, padded versions, tags, prereleases, null and overflow.");}
}
