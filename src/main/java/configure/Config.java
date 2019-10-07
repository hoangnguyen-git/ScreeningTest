package configure;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import helper.FileUtility;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;

public class Config {
  private static Config config;
  private Log logger = Log.getInstance();
  private String browserName;
  private boolean isDriverDownload;
  private Boolean isHeadLessMode;
  private Boolean isIncognito;
  private Integer timeOut;
  private String env;
  private String platformName;
  private String testingType;
  private String deviceModel;

  public static synchronized Config getInstance() {
    if (config == null) {
      config = new Config();
      config.loadConfigData();
    }
    return config;
  }

  public String getBrowserName() {
    return browserName;
  }

  public boolean isDriverDownload() {
    return isDriverDownload;
  }

  public Boolean getHeadLessMode() {
    return isHeadLessMode;
  }

  public Boolean getIncognito() {
    return isIncognito;
  }

  public Integer getTimeOut() {
    return timeOut;
  }

  public String getPlatformName() {
    return platformName;
  }

  public String getTestingType() {
    return testingType;
  }

  public String getDeviceModel() {
    return deviceModel;
  }

  public void loadConfigData() {
    Properties prop = new Properties();
    try {
      prop = FileUtility.getConfigFile(PathConstant.configPath + "/config.properties");
    } catch (Exception e) {
      logger.ERROR("Exception while read property file config.\n" + e.toString());
    }
    if (prop != null) {
      config.browserName = prop.getProperty("BROWSER_NAME");
      config.isDriverDownload = Boolean.valueOf(prop.getProperty("DRIVER_MANAGER_DOWNLOAD"));
      config.isHeadLessMode = Boolean.valueOf(prop.getProperty("IS_HEADLESS_MODE"));
      config.isIncognito = Boolean.valueOf(prop.getProperty("IS_INCOGNITO"));
      config.timeOut = Integer.valueOf(prop.getProperty("TIME_OUT"));
      config.env = prop.getProperty("ENV").trim();
      config.platformName = prop.getProperty("PLATFORM_NAME").trim();
      config.testingType = prop.getProperty("TESTING_TYPE").trim();
      config.deviceModel = prop.getProperty("DEVICE_MODEL").trim();
    }
  }

  public HashMap<String, String> getWebEnvironment() {
    HashMap<String, String> env = new HashMap<>();
    try {
      JsonObject jsonObject =
          new JsonParser()
              .parse(new FileReader(PathConstant.configPath + "/env_webapp.json"))
              .getAsJsonObject();
      String value = jsonObject.getAsJsonObject(this.env.toUpperCase()).get("url").getAsString();
      env.put("url", value);
      value = jsonObject.getAsJsonObject(this.env.toUpperCase()).get("username").getAsString();
      env.put("username", value);
      value = jsonObject.getAsJsonObject(this.env.toUpperCase()).get("password").getAsString();
      env.put("password", value);
    } catch (Exception e) {
      return null;
    }
    return env;
  }

  public HashMap<String, String> getAPIEnvironment() {
    HashMap<String, String> env = new HashMap<>();
    try {
      JsonObject jsonObject =
          new JsonParser()
              .parse(new FileReader(PathConstant.configPath + "env_api.json"))
              .getAsJsonObject();
      String value = jsonObject.getAsJsonObject(this.env.toUpperCase()).get("url").getAsString();
      env.put("url", value);
      value = jsonObject.getAsJsonObject(this.env.toUpperCase()).get("username").getAsString();
      env.put("username", value);
      value = jsonObject.getAsJsonObject(this.env.toUpperCase()).get("password").getAsString();
      env.put("password", value);
      value = jsonObject.getAsJsonObject(this.env.toUpperCase()).get("token").getAsString();
      env.put("token", value);
    } catch (Exception e) {
      return null;
    }
    return env;
  }

  public HashMap<String, String> getAppiumSetting() {
    HashMap<String, String> env = new HashMap<>();
    try {
      JsonObject jsonObject =
          new JsonParser()
              .parse(new FileReader(PathConstant.configPath + "appiumSetting.json"))
              .getAsJsonObject();
      String value = jsonObject.get("ip").getAsString();
      env.put("ip", value);
      value = jsonObject.get("port").getAsString();
      env.put("port", value);
      value = jsonObject.get("bundleId").getAsString();
      env.put("bundleId", value);
      value = jsonObject.get("xcodeOrgId").getAsString();
      env.put("xcodeOrgId", value);
      value = jsonObject.get("xcodeSigningId").getAsString();
      env.put("xcodeSigningId", value);
      value = jsonObject.get("updatedWDABundleId").getAsString();
      env.put("updatedWDABundleId", value);
    } catch (Exception e) {
      return null;
    }
    return env;
  }
}
