package app.kivo.store;
import java.util.*;
/** Small independently testable collector. One failing source or package cannot erase the others. */
final class InstalledPackages<T> {
 interface Backend<T>{List<T> all()throws Exception;List<String> launchers()throws Exception;T lookup(String pkg)throws Exception;String id(T item);}
 static final class Result<T>{final Map<String,T> packages=new LinkedHashMap<>();final Set<String> launchable=new HashSet<>();int sourceFailures,packageFailures;}
 final Backend<T> backend;InstalledPackages(Backend<T> value){backend=value;}
 Result<T> collect(Collection<String> known){Result<T> result=new Result<>();try{List<T> all=backend.all();if(all!=null)for(T item:all)try{String id=backend.id(item);if(id!=null&&!id.isEmpty())result.packages.put(id,item);}catch(Exception e){result.packageFailures++;}}catch(Exception e){result.sourceFailures++;}
 Set<String> names=new LinkedHashSet<>(known);try{List<String> launchers=backend.launchers();if(launchers!=null)for(String name:launchers)if(name!=null&&!name.isEmpty()){result.launchable.add(name);names.add(name);}}catch(Exception e){result.sourceFailures++;}
 for(String name:names){if(name==null||name.isEmpty()||result.packages.containsKey(name))continue;try{T item=backend.lookup(name);if(item!=null&&name.equals(backend.id(item)))result.packages.put(name,item);}catch(Exception e){result.packageFailures++;}}
 return result;
 }
}
