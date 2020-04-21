package sample;

import java.util.Map;

public interface ControllerInterface {
   void setValues(Map<String, Object> map, String deviceName, Class<?> c, Controller con);
}
