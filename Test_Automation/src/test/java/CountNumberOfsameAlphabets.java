import java.util.HashMap;
import java.util.Map;

/**
 * 
 */

/**
 * @author sujay
 *
 */
public class CountNumberOfsameAlphabets {
	
	
	
	public static void main(String[] args) {
		
		Map <String,Integer> CounttSameAlphabets = new HashMap<String,Integer>();
		//int value =1;
		for (int i = 0; i < args.length; i++) {
			
			if (CounttSameAlphabets.containsKey(args[i])){
				int value = CounttSameAlphabets.get(args[i]);
				CounttSameAlphabets.put(args[i], ++value);
			}else {
				int value =1;
				CounttSameAlphabets.put(args[i], value);
				
			}
			
		}
		CounttSameAlphabets.forEach((key,value) -> System.out.println(key+","+ value));
		}
	}


