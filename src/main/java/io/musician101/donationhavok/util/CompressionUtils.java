package io.musician101.donationhavok.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressionUtils {

    private CompressionUtils() {

    }

    public static byte[] compress(String string) {
        ByteArrayOutputStream baOS = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOS = new GZIPOutputStream(baOS)) {
            gzipOS.write(string.getBytes(StandardCharsets.UTF_8));
            gzipOS.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return baOS.toByteArray();
    }

    public static String decompress(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(byteArray))))) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
