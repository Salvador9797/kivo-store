package app.kivo.store;
final class Versions {
 /** Only unambiguous numeric releases are eligible for batch updates. */
 static int compare(String left,String right){if(left==null||right==null)return 0;left=left.trim().replaceFirst("^[vV]","");right=right.trim().replaceFirst("^[vV]","");if(!left.matches("[0-9]+(\\.[0-9]+){0,3}")||!right.matches("[0-9]+(\\.[0-9]+){0,3}"))return 0;String[] a=left.split("\\."),b=right.split("\\.");try{for(int i=0;i<Math.max(a.length,b.length);i++){long x=i<a.length?Long.parseLong(a[i]):0,y=i<b.length?Long.parseLong(b[i]):0;if(x!=y)return x>y?1:-1;}}catch(NumberFormatException e){return 0;}return 0;}
}
