package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.List;
import com.google.common.hash.Hashing;

public class Client {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        
        /*	Trying out basic GET/PUT for understanding 
        CacheServiceInterface cache = new DistributedCacheService(
                "http://localhost:3000");
        CacheServiceInterface cache1 = new DistributedCacheService(
                "http://localhost:3001");
        
        cache.put(1, "foo");
        cache1.put(2, "hoo");
        System.out.println("put(1 => foo)");
        String value = cache.get(1);
        
        System.out.println("put(2 => hoo)");
        String value1 = cache.get(1);
        
        System.out.println("get(1) => " + value);
        System.out.println("get(2) => " + value1);
        */
        
        List<CacheServiceInterface> servers = new ArrayList<CacheServiceInterface>();
        servers.add(new DistributedCacheService("http://localhost:3000"));
        servers.add(new DistributedCacheService("http://localhost:3001"));
        servers.add(new DistributedCacheService("http://localhost:3002"));
        
        //Defining set of values - ignore the 0th element of this array
        char[] values = {'-', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        
      //GET REQUEST
        System.out.println("----------------------------------PUT---------------------------------------------");
        for(int key=1; key<=10; key++)	{
        	int bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(key)), servers.size());
        	/*System.out.println("Checking the bucket hashed value: "+bucket);
        	System.out.println("Key#" +key+ " routed to the server: " +servers.get(bucket));*/
        	servers.get(bucket).put(key, Character.toString(values[key]));
        	System.out.println("PUT---> {Key "+key+"=> Value "+values[key]+""
        			+ "} routed to cache server: localhost@300" +bucket);
        }
        
        System.out.println("----------------------------------GET---------------------------------------------");
        //GET REQUEST - to retrieve all keys
        // check  http://localhost:300{0|1|2}/cache/ to verify the sharding of data between 3 cache servers
        for(int key1=1; key1<=10; key1++)	{
        	int bucket = Hashing.consistentHash(Hashing.md5().hashString(Integer.toString(key1)), servers.size());
        	//System.out.println("Checking the bucket hashed value: localhost@300"+bucket);
        	System.out.println("GET---> {Key " +key1+ "=> Value "+servers.get(bucket).get(key1)
        			+ "} fetched from cache server: localhost@300" +bucket);      			
        }
        System.out.println("Exiting Cache Client...");
    }

}
