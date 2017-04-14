package com.blogspot.sontx.bottle.fs.service;

import com.blogspot.sontx.bottle.fs.bean.UploadResult;

import java.io.InputStream;

public interface StorageService {
    UploadResult upload(InputStream in);
}
