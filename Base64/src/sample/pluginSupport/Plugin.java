package sample.pluginSupport;

import java.io.IOException;

public interface Plugin {
    void encode(String fileName) throws IOException;
    void decode(String fileName) throws IOException;
    String getFileExtensionEnding();
    String getEncodingName();
}
