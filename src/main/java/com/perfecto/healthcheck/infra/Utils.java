package com.perfecto.healthcheck.infra;


import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;


public class Utils {

	public static String REPORT_LIB = "target";
	public static String SCREENSHOTS_LIB = "target";
	private static final String UTF_8 = "UTF-8";
	private static final String HTTPS = "https://";
	private static final String MEDIA_REPOSITORY = "/services/repositories/media/";
	private static final String UPLOAD_OPERATION = "operation=upload&overwrite=true";
	 static String host = HealthcheckProps.getPerfectoHost();
	 static String username = HealthcheckProps.getPerfectoUser();
	 static String password = HealthcheckProps.getPerfectoPassword();

	public static String getScreenShot(RemoteWebDriver driver, String name, String deviceID )
	{
		String screenShotName = SCREENSHOTS_LIB+name+"_"+deviceID+".png";
		driver   = (RemoteWebDriver) new Augmenter().augment( driver );
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(scrFile, new File(screenShotName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return screenShotName;
	}

	public static void startApp(String property,String name, RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put(property, name);
//		params.put("name", appName);
		d.executeScript("mobile:application:open", params);

	}


	public static void stoptApp(String property,String name,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put(property , name);
		d.executeScript("mobile:application:close", params);
	}

	public static void setLocation(String address,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("address", address);
		d.executeScript("mobile:location:set", params);
	}


	public static void setLocationCoordinates(String latlong,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("coordinates", latlong);
		d.executeScript("mobile:location:set", params);
	}

	public static void pressKey(String key,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("keySequence", key);
		d.executeScript("mobile:presskey:", params);
	}


	public static void longtouch(String key,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("location", key);  // 145,449
		params.put("duration", key);
		d.executeScript("mobile:touch:tap", params);
	}


	public static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}
	public static void swipe(String start,String end,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("start", start);  //50%,50%
		params.put("end", end);  //50%,50%

		d.executeScript("mobile:touch:swipe", params);

	}
	public static void waitForVisible(RemoteWebDriver driver, final By by, String string, int waitTime) {
		int timeoutInSeconds = 0;
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		for (int attempt = 0; attempt < waitTime; attempt++) {
			try {
				driver.findElement(by).getAttribute("value").equalsIgnoreCase(string);
				break;
			} catch (Exception e) {
				driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			}
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public static void rotateDevice (String stat,WebDriver d )
	{
		// operation - next or reset
		Map<String,String> params = new HashMap<String,String>();
		params.put("operation", stat);
		((RemoteWebDriver) d).executeScript("mobile:handset:rotate", params);
	}

	public static void home(RemoteWebDriver driver){
		Map<String,String> params1 = new HashMap<String,String>();
		params1.clear();
		params1.put("keySequence", "HOME");
		driver.executeScript("mobile:presskey", params1);

	}

	public static void downloadReport(RemoteWebDriver driver, String type, String fileName) throws IOException {
		try {
			String command = "mobile:report:download";
			Map<String, Object> params = new HashMap<>();
			params.put("type", "html");
			String report = (String)driver.executeScript(command, params);
			File reportFile = new File(getReprtName(fileName, true) );
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile));
			byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report);
			output.write(reportBytes); output.close();
		} catch (Exception ex) {
			System.out.println("Got exception " + ex); }
	}

	public static String getReprtName(String repID,boolean withPath) {
		if (withPath)
		{
			return REPORT_LIB+"/rep_"+repID+".html";
		}
		else
		{
			return  "/rep_"+repID+".html";
		}

	}
	public static void pointOfInterest(RemoteWebDriver d ,String poiName, String poiStatus) {
		String command;
		Map<String,Object> params = new HashMap<String,Object>();
		command = "mobile:status:event";
		params.put("description", poiName);
		params.put("status", poiStatus);
		Object result = d.executeScript(command, params);
	}
	public static long timerGet(RemoteWebDriver d,String timerType) {
		String command = "mobile:timer:info";
		Map<String,String> params = new HashMap<String,String>();
		params.put("type", timerType);
		long result = (long)d.executeScript(command, params);
		return result;
	}
	public static void timerReport(RemoteWebDriver d,long timerResult, int threashold, String description, String name) {
		String command;
		Map<String,Object> params = new HashMap<String,Object>();
		command = "mobile:status:timer";
		params.put("result", timerResult);
		params.put("threshold", threashold);
		params.put("description", description);
		//params.put("status", status);
		params.put("name", name);
        Object result = d.executeScript(command, params);


	}
	public static boolean isElementPresent(By by, RemoteWebDriver driver){
		try{
			driver.findElement(by);
			return true;
		}
		catch(NoSuchElementException e){
			return false;
		}
	}
	public static void visualWithScroll(RemoteWebDriver driver, String string,String match, String threshold){
		try{
		switchToContext(driver, "VISUAL");
		Map<String, Object> params1 = new HashMap<>();
		params1.clear();
		params1.put("content", string);
		params1.put("scrolling", "scroll");
		params1.put("maxscroll", "100");
		params1.put("next", "SWIPE_UP");
		params1.put("match", match);
		params1.put("threshold", threshold);
		String  check = driver.executeScript("mobile:text:find", params1).toString();
		if (check.contains(string)) {
			Assert.assertTrue(false, "Visual not found");
		}
		}catch (Throwable t) {
			t.printStackTrace();
			ExceptionAnalyzer.analyzeException(t,"visual scroll failed");
			//rethrow exception if not critical device exception
			throw t;
		}

	}
	public static void uploadMedia(String host, String user, String password, URL mediaURL, String repositoryKey) throws IOException {
		byte[] content = readURL(mediaURL);
		uploadMedia(host, user, password, content, repositoryKey);
	}
	public static void uploadMedia(String host, String user, String password, byte[] content, String repositoryKey) throws UnsupportedEncodingException, MalformedURLException, IOException {
		if (content != null) {
			String encodedUser = URLEncoder.encode(user, "UTF-8");
			String encodedPassword = URLEncoder.encode(password, "UTF-8");
			String urlStr = HTTPS + host + MEDIA_REPOSITORY + repositoryKey + "?" + UPLOAD_OPERATION + "&user=" + encodedUser + "&password=" + encodedPassword;
			URL url = new URL(urlStr);

			sendRequest(content, url);
		}
	}
	private static void sendRequest(byte[] content, URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/octet-stream");
		connection.connect();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		outStream.write(content);
		outStream.writeTo(connection.getOutputStream());
		outStream.close();
		int code = connection.getResponseCode();
		if (code > HttpURLConnection.HTTP_OK) {
			handleError(connection);
		}
	}
	private static byte[] readURL(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoOutput(true);
		int code = connection.getResponseCode();
		if (code > HttpURLConnection.HTTP_OK) {
			handleError(connection);
		}
		InputStream stream = connection.getInputStream();

		if (stream == null) {
			throw new RuntimeException("Failed to get content from url " + url + " - no response stream");
		}
		byte[] content = read(stream);
		return content;
	}
	private static byte[] read(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int nBytes = 0;
			while ((nBytes = input.read(buffer)) > 0) {
				output.write(buffer, 0, nBytes);
			}
			byte[] result = output.toByteArray();
			return result;
		} finally {
			try{
				input.close();
			} catch (IOException e){

			}
		}
	}

	private static byte[] readFile(File path) throws FileNotFoundException, IOException {
		int length = (int)path.length();
		byte[] content = new byte[length];
		InputStream inStream = new FileInputStream(path);
		try {
			inStream.read(content);
		}
		finally {
			inStream.close();
		}
		return content;
	}
	private static void handleError(HttpURLConnection connection) throws IOException {
		String msg = "Failed to upload media.";
		InputStream errorStream = connection.getErrorStream();
		if (errorStream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(errorStream, UTF_8);
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			try {
				StringBuilder builder = new StringBuilder();
				String outputString;
				while ((outputString = bufferReader.readLine()) != null) {
					if (builder.length() != 0) {
						builder.append("\n");
					}
					builder.append(outputString);
				}
				String response = builder.toString();
				msg += "Response: " + response;
			}
			finally {
				bufferReader.close();
			}
		}
		throw new RuntimeException(msg);
	}

	// =============================== private methods / xml helper  =========================================

    public static String writeToXml (List<Device> list) throws IOException{
           XStream xstream = new XStream();
           String xml = null;
           try {
   			xml = xstream.toXML(list);
         File newTextFile = new File("target/t.xml");
         FileWriter fw = new FileWriter(newTextFile);
         fw.write(xml);
         fw.close();

          }
           catch (IOException iox) {

          }
           System.out.println("file created");
           return xml;
    }

    public static String[][] readFromXml() throws IOException{
           XStream xstream = new XStream();
           String xml = null;
           BufferedReader br = new BufferedReader(new FileReader("target/t.xml"));
           try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
   		     xml = sb.toString();
           }
           catch (IOException e) {
                  e.printStackTrace();
           }
            finally {
                br.close();
           }

           List<Device> DevicesList = (List<Device>)xstream.fromXML(xml);
           String[][] tabArray = null;
           tabArray=new String[DevicesList.size()][5];
           for (int i = 0; i < DevicesList.size(); i++) {
        	   tabArray[i][0] = DevicesList.get(i).getPlatform();
               tabArray[i][1] = DevicesList.get(i).getApp();
               tabArray[i][2] = DevicesList.get(i).getDeviceID();
               tabArray[i][3] = DevicesList.get(i).getmodel();
               tabArray[i][4] = DevicesList.get(i).getOsVersion();
           }

         return tabArray;
    }
	public static void visualOnWeb(RemoteWebDriver driver, String string) throws Exception{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.clear();
			params.put("content", string);
			params.put("timeout", "60");
			String check = driver.executeScript("mobile:text:find", params).toString();
			if (check.contains("false")) {
                Assert.assertTrue(false, "Visual not found");
            }
		}catch (Throwable t) {
			t.printStackTrace();
			ExceptionAnalyzer.analyzeException(t,"visual analysis failed");
			throw t;
		}
	}

    public static String[][] readFromXmlForWeb() throws IOException{
        XStream xstream = new XStream();
        String xml = null;
        BufferedReader br = new BufferedReader(new FileReader("target/t.xml"));
        try {
             StringBuilder sb = new StringBuilder();
             String line = br.readLine();
             while (line != null) {
                 sb.append(line);
                 sb.append(System.lineSeparator());
                 line = br.readLine();
             }
		     xml = sb.toString();
        }
        catch (IOException e) {
               e.printStackTrace();
        }
         finally {
             br.close();
        }

        List<Device> DevicesList = (List<Device>)xstream.fromXML(xml);
        String[][] tabArray = null;
        tabArray=new String[DevicesList.size()][4];
        for (int i = 0; i < DevicesList.size(); i++) {
     	   tabArray[i][0] = DevicesList.get(i).getPlatform();
     	   tabArray[i][1] = DevicesList.get(i).getDeviceID();
			tabArray[i][2] = DevicesList.get(i).getmodel();
			tabArray[i][3] = DevicesList.get(i).getOsVersion();
         }

      return tabArray;
 }
	public static void openGeneralSettingsiOS(RemoteWebDriver driver)throws Exception{
		try {
			startApp("identifier", "com.apple.Preferences", driver);
			stoptApp("identifier", "com.apple.Preferences", driver);
			startApp("identifier", "com.apple.Preferences", driver);

			try{
				driver.findElementByXPath("//UIATableCell[@label=\"General\"]|//XCUIElementTypeCell[@label=\"General\"]").click();
			}catch (NoSuchElementException e){
				URL url = new URL("https://s3-eu-west-1.amazonaws.com/perfecto-beat-regression/beatMedia/Images/GeneralSettingsiOS.png");
				uploadMedia(host, username, password, url, "PRIVATE:GeneralSettingsiOS.png");
				Map<String, Object> params1 = new HashMap<>();
				params1.put("content", "PRIVATE:GeneralSettingsiOS.png");
				params1.put("source", "camera");
				params1.put("match", "bounded");
				params1.put("imageBounds.needleBound", "50");
				Object result1 = driver.executeScript("mobile:image:select", params1);
				ExceptionAnalyzer.analyzeException(e, " element Click failed so used visual click");
			}
//				startApp("identifier", "com.apple.Preferences", driver);
//				driver.findElementByXPath("//UIATableCell[@label=\"General\"]").click();
			switchToContext(driver, "VISUAL");
			visualOnWeb(driver, "handoff");
		}catch (Throwable t) {
			t.printStackTrace();
			ExceptionAnalyzer.analyzeException(t,"Open general settings failed");
			//rethrow exception if not critical device exception
			throw t;
		}
	}



	public static void openSettingsiOS(RemoteWebDriver driver)throws Exception{
		try {
			HashMap<String, Object> params1 = new HashMap<>();
			params1.put("identifier", "com.apple.Preferences");
			driver.executeScript("mobile:application:open", params1);
			driver.executeScript("mobile:application:close", params1);
			driver.executeScript("mobile:application:open", params1);
			params1.clear();
		}catch (Throwable t) {
			t.printStackTrace();
			ExceptionAnalyzer.analyzeException(t,"Open and settings failed");
			//rethrow exception if not critical device exception
			throw t;}
	}

		public static void sleep(long millis) {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
			}
		}
		public static void scrolliPadTable(RemoteWebDriver driver, String text){
            try{
                switchToContext(driver,"NATIVE");
                WebElement tbl1 = driver.findElementByXPath("//UIATableView[2]|//XCUIElementTypeOther[3]//XCUIElementTypeTable[1]");
                HashMap<String, Object> params1 = new HashMap<>();
                params1.put("direction", "down");
                params1.put("element",((RemoteWebElement) tbl1).getId());
                params1.put("text", params1.containsValue(text));
                driver.executeScript("mobile: scroll", params1);
                params1.clear();
		    }catch (Throwable t) {
                t.printStackTrace();
                ExceptionAnalyzer.analyzeException(t,"visual on iPad  "+text+" failed");
                //rethrow exception if not critical device exception
                throw t;
		    }
		}

	public static void scrollTo(RemoteWebDriver driver, String parameter, String text){
	try{
		HashMap<String, Object> params1 = new HashMap<>();
		params1.put("direction", "down");
		params1.put(parameter, text);
		driver.executeScript("mobile: scroll", params1);
		params1.clear();
	}catch (Throwable t) {
		t.printStackTrace();
		ExceptionAnalyzer.analyzeException(t,"visual on iPad  "+text+" failed");
		//rethrow exception if not critical device exception
		throw t;
	}
	}
	 public static void scrollToText(RemoteWebDriver driver, String parameter,String text) {
		 try {
			 switchToContext(driver,"VISUAL");
			 HashMap<String, Object> params1 = new HashMap<>();
			 params1.put(parameter, text);
			 params1.put("source", "camera");
			 params1.put("scrolling", "scroll");
			 params1.put("next", "SWIPE_UP");
			 params1.put("duration","5");
			 driver.executeScript("mobile:text:find", params1);
			 params1.clear();
		 }catch (Throwable t) {
			 t.printStackTrace();
			 ExceptionAnalyzer.analyzeException(t,"visual on "+text+" failed");
			 //rethrow exception if not critical device exception
			 throw t;}

	 }
	public static void selectText(RemoteWebDriver driver, String property,String text,String offset)throws Exception{
		try {
			switchToContext(driver, "VISUAL");
			HashMap<String, Object> params1 = new HashMap<>();
			params1.put(property, text);
			params1.put("source", "camera");
			params1.put("shift", "above");
			params1.put("offset", offset);
			driver.executeScript("mobile:text:select", params1);
			params1.clear();
		}catch (Throwable t) {
			t.printStackTrace();
			ExceptionAnalyzer.analyzeException(t,"visual click on "+text+" failed");
			//rethrow exception if not critical device exception
			throw t;}
	}
	public static void retryClick(RemoteWebDriver driver,String xpath)throws Exception{
		driver.findElementByXPath(xpath).click();
		try{
			driver.findElementByXPath(xpath).click();
		}catch(NoSuchElementException e){

			System.out.println("element Click failed");
			ExceptionAnalyzer.analyzeException(e, " element Click failed");

		}


	}

	public static String handsetInfo(RemoteWebDriver driver,String key,String value){

		HashMap<String, Object> params1= new HashMap<>();
		params1.put(key, value);
		String cap1 = (String) driver.executeScript("mobile:handset:info", params1);
		params1.clear();
		return cap1;
	}

}
