package org.grails.plugins.resources

import net.matthaynes.jsmin.JSMin;


class JsMinMinifier implements Minifier {

    void minify (File sourceFile, File targetFile) {

        def out = new FileOutputStream(targetFile)
        try {
            new JSMin(new FileInputStream(sourceFile), out).jsmin()
        } finally {
            out.close()
        }
    }
}
