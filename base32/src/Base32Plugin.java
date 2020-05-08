import org.apache.commons.codec.binary.Base32;
import sample.pluginSupport.Plugin;

import java.io.*;

public class Base32Plugin implements Plugin {
    private final String fileExtensionEnding="32";
    private final String encodingName="Base32";

    @Override
    public String getFileExtensionEnding() {
        return this.fileExtensionEnding;
    }

    @Override
    public String getEncodingName() {
        return this.encodingName;
    }

    public void encode(String fileName) throws IOException {
        byte[] arr = readBytesFromFile(fileName);
        if(!fileName.endsWith(this.fileExtensionEnding))
            fileName=fileName.concat(this.fileExtensionEnding);
        Base32 b32 = new Base32();
        byte[] line = b32.encode(arr);
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        try (BufferedOutputStream fos = new BufferedOutputStream(fileOutputStream)) {
            fos.write(line);
            fos.flush();
        }
    }

    public void decode(String fileName) throws IOException {
        byte[] arr = readBytesFromFile(fileName);
        if(fileName.endsWith(this.fileExtensionEnding))
            fileName=fileName.substring(0,fileName.length()-2);
        Base32 b32 = new Base32();
        byte[] out = b32.decode(new String(arr));
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(out);
            fos.flush();
        }
    }

    private byte[] readBytesFromFile(String fileName) throws IOException {
        File tmpFile = new File(fileName);
        InputStream is = new FileInputStream(tmpFile);
        byte[] arr = is.readAllBytes();
        is.close();
        tmpFile.delete();
        return arr;
    }
}
