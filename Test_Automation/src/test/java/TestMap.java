/**
 * 
 */
import java.util.HashMap;
import java.util.Map;
/**
 * @author sujay
 *
 */
public class TestMap {
	
	


		public static void main(String[] args) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("key1", "value1");
			map.put("key2", "value2");

			map.forEach((key,value) -> System.out.println(key + " = " + value));
		}
	}


