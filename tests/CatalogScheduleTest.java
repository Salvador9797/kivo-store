package app.kivo.store;
public class CatalogScheduleTest {
 static void check(boolean b){if(!b)throw new AssertionError();}
 public static void main(String[] args){long h=CatalogSchedule.HOUR,n=100*h;
 check(CatalogSchedule.due(n,0,0,6*h));
 check(!CatalogSchedule.due(n,n-2*h,n-2*h,6*h));
 check(CatalogSchedule.due(n,n-7*h,n-7*h,6*h));
 check(!CatalogSchedule.due(n,0,n-1000,6*h));
 check(CatalogSchedule.due(n,0,n-2*h,6*h));
 check(!CatalogSchedule.due(n,n-7*h,n-7*h,12*h));
 check(CatalogSchedule.due(n,n+1,n+1,6*h));
 System.out.println("PASS: first load, freshness, expiry, failed-attempt cooldown, slower GitHub and clock correction");}
}
