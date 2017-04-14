package com.blogspot.sontx.bottle.fs.service;

import com.blogspot.sontx.bottle.fs.bean.UploadResult;

import javax.ws.rs.core.StreamingOutput;
import java.io.InputStream;

public interface StorageService {
    UploadResult upload(InputStream in);

    StreamingOutput getStreamingOutput(String fileName);
}
