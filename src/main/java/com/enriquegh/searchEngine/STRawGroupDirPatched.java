package com.enriquegh.searchEngine;

import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.STRawGroupDir;

import java.net.MalformedURLException;
import java.net.URL;

public class STRawGroupDirPatched extends STRawGroupDir {

    public STRawGroupDirPatched(String dirName, char delimiterStartChar, char delimiterStopChar) {
        super(dirName, delimiterStartChar, delimiterStopChar);
        if (root != null && root.toString().endsWith("/")) {
            try {
                String url = root.toString();
                root = new URL(url.substring(0, url.length() - 1));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

}
