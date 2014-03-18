package ocha.itolab.flowdiff.applet.flowdiff;

import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
	//setting.propertyファイルから読み込む
	private Properties conf = null;

	public PropertyLoader() throws IOException {

		conf = new Properties();
		conf.load(this.getClass().getResourceAsStream("/setting.properties"));
	}
	
	public String getValue(String key){
		return conf.getProperty(key);
	}
}
