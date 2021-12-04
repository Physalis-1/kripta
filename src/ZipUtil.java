import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    ZipUtil(String path) {
//        long start = System.nanoTime();

        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream("temp_archive.zip"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File file = new File(path);

        try {
            doZip(file, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        long finish = System.nanoTime();
//        long elapsed = finish - start;
//        System.out.println("Прошло времени, нс: " + elapsed);
    }

    private static void doZip(File dir, ZipOutputStream out) throws IOException {
        for (File f : dir.listFiles()) {
            if (f.isDirectory())
                doZip(f, out);
            else {
                out.putNextEntry(new ZipEntry(f.getPath()));
                write(new FileInputStream(f), out);
            }
        }
    }

    private static void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);
        in.close();
    }
}