package app.kivo.store;
/** Persisted check times are separate from the signed repository timestamp. */
final class CatalogSchedule {
 static final long HOUR=3600000L;
 static boolean due(long now,long success,long attempt,long interval){
  if(attempt>0&&now>=attempt&&now-attempt<HOUR)return false;
  return success<=0||now<success||now-success>=interval;
 }
}
