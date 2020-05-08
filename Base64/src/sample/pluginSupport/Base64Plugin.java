package sample.pluginSupport;

import org.apache.commons.codec.binary.Base64;

import java.io.*;

public class Base64Plugin implements Plugin {
    private final String fileExtensionEnding="64";
    private final String encodingName="Base64";

    public String getFileExtensionEnding() {
        return this.fileExtensionEnding;
    }

    public String getEncodingName() {
        return this.encodingName;
    }

    public void encode(String fileName) throws IOException {
        byte[] arr = readBytesFromFile(fileName);
        if(!fileName.endsWith(this.fileExtensionEnding))
            fileName=fileName.concat(this.fileExtensionEnding);
        Base64 b64 = new Base64();
        byte[] line = b64.encode(arr);
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
        Base64 b64 = new Base64();
        byte[] out = b64.decode(new String(arr));
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
