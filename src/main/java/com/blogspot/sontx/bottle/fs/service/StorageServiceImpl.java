package com.blogspot.sontx.bottle.fs.service;

import com.blogspot.sontx.bottle.fs.bean.UploadResult;
import com.blogspot.sontx.bottle.fs.utils.ConfigUtils;
import com.blogspot.sontx.bottle.fs.utils.SecuredTokenFactory;
import lombok.extern.log4j.Log4j;
import org.joda.time.DateTime;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;

@Log4j
public class StorageServiceImpl implements StorageService {
    private String resourceDir;

    public StorageServiceImpl() {
        resourceDir = ConfigUtils.getValue("default.res.dir");
    }

    private String generateUploadFileName(int userId, String mimeType) {
        DateTime now = DateTime.now();
        String randomToken = SecuredTokenFactory.generateSecuredToken();
        String validMimeType = mimeType.toLowerCase();
        // yyyy MM dd hh mm ss - random - id - filename
        return String.format("%04d%02d%02d%02d%02d%02d-%s-%013d-%s",
                now.getYear(),
                now.getMonthOfYear(),
                now.getDayOfMonth(),
                now.getHourOfDay(),
                now.getMinuteOfHour(),
                now.getSecondOfMinute(),
                randomToken,
                userId,
                validMimeType);
    }

    @Override
    public UploadResult upload(InputStream in) {
        String fileName = generateUploadFileName(0, "jpg");

        File resourceDirFile = new File(resourceDir);
        if (!resourceDirFile.isDirectory()) {
            resourceDirFile.mkdirs();
            log.info("create resource directory at " + resourceDirFile.getAbsolutePath());
        }

        File uploadedFileLocation = new File(resourceDirFile, fileName);

        OutputStream out = null;
        try {
            int read;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(uploadedFileLocation);
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();

            UploadResult uploadResult = new UploadResult();
            uploadResult.setName(fileName);
            return uploadResult;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    @Override
    public StreamingOutput getStreamingOutput(String fileName) {
        File downloadFileLocation = new File(resourceDir, fileName);
        try {
            InputStream in = new FileInputStream(downloadFileLocation);
            return new FileStreamingOutput(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File getFile(String fileName) {
        return new File(resourceDir, fileName);
    }

    @Override
    public boolean delete(String fileName) {
        File file = getFile(fileName);
        return file.exists() && file.delete();
    }

    private static class FileStreamingOutput implements StreamingOutput {
        private final InputStream in;

        private FileStreamingOutput(InputStream in) {
            this.in = in;
        }

        @Override
        public void write(OutputStream out) throws IOException, WebApplicationException {
            byte[] buffer = new byte[1024];
            int chunk;
            while ((chunk = in.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, chunk);
            }
            in.close();
        }
    }
}
