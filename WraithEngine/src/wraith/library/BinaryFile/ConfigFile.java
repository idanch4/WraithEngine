package wraith.library.BinaryFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.HashMap;

@SuppressWarnings("serial")
public class ConfigFile extends File{
	private HashMap<String,String> values = new HashMap<>();
	public boolean ensureExistance(){
		if(exists())return false;
		try{
			getParentFile().mkdirs();
			createNewFile();
		}catch(Exception exception){ exception.printStackTrace(); }
		return true;
	}
	public void read(){
		BufferedReader in = null;
		try{
			values.clear();
			in=new BufferedReader(new FileReader(this));
			String s;
			String[] parts;
			while((s=in.readLine())!=null){
				parts=s.split(": ");
				if(parts.length!=2)continue;
				values.put(parts[0], parts[1]);
			}
			in.close();
		}catch(Exception exception){
			values.clear();
			exception.printStackTrace();
		}finally{
			try{ in.close();
			}catch(Exception exception){ exception.printStackTrace(); }
		}
	}
	public String getValue(String key){
		if(values.containsKey(key))return values.get(key);
		return null;
	}
	public ConfigFile(File parent, String child){ super(parent, child); }
	public ConfigFile(String parent, String child){ super(parent, child); }
	public ConfigFile(URI uri){ super(uri); }
	public ConfigFile(String file){ super(file); }
}