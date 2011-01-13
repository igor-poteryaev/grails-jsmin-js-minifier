package org.grails.plugins.resources

import org.apache.commons.io.FilenameUtils;


class MinifyResourceMapper {

    static defaultExcludes = [
        '/**/*.ico',
        '/**/*.png',
        '/**/*.gif',
        '/**/*.jpg',
        '/**/*.jpeg',
        '/**/*.gz',
        '/**/*.zip'
    ]

    def priority = 200

    def minifiers = [
        'js': new JsMinMinifier()
    ]


    def map (resource, config) {
        def file = resource.processedFile
        def extension = FilenameUtils.getExtension(file.name)
        if (minifiers.containsKey(extension) && !file.name.endsWith(".min." + extension)) {
            resource.processedFile = minify (minifiers[extension], file)
        }
    }


    private File minify (minifier, sourceFile) {
        def targetFile = createTargetFile(sourceFile)
        if (log.debugEnabled) {
            log.debug "minifying $sourceFile to $targetFile"
        }
        try {
            minifier.minify(sourceFile, targetFile)
        } catch (e) {
            log.error("problem minifying ${sourceFile.name}: $e.message")
        }
        return targetFile
    }


    private File createTargetFile (File sourceFile) {
        String extension = FilenameUtils.getExtension(sourceFile.name)
        return new File(sourceFile.parentFile, FilenameUtils.removeExtension(sourceFile.name) + '.min.' + extension)
    }
}
