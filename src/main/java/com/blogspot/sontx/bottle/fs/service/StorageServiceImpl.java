package com.blogspot.sontx.bottle.fs.service;

import com.blogspot.sontx.bottle.fs.bean.UploadResult;
import com.blogspot.sontx.bottle.fs.utils.SecuredTokenFactory;
import org.joda.time.DateTime;

import java.io.*;

public class StorageServiceImpl implements StorageService {
    private static final String RESOURCE_DIR = "F:/res";

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
        File uploadedFileLocation = new File(RESOURCE_DIR, fileName);

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
            uploadResult.setUrl("http://localhost:8080/bottlfs/rest/storage?f=" + fileName);
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
}