/**
 * aflx - always flexible
 * http://www.aflx.de
 * ak@aflx.de
 *
 * Copyright 2011 Alexander Keller
 * All Rights Reserved.
 */

function Downloader() {
}

/**
 * Downloads a file form a given URL and saves it to the specified directory.
 * @param sourceUrl {String}          URL of the server to receive the file
 * @param targetFile {String}         Full path of the file on the device
 * @param successCallback (Function}  Callback to be invoked when upload has completed
 * @param errorCallback {Function}    Callback to be invoked upon error
 */
Downloader.prototype.downloadFile = function(sourceUrl, targetFile, successCallback, errorCallback) {
    PhoneGap.exec(successCallback, errorCallback, 'Downloader', 'download', [targetFile, sourceUrl]);
};

PhoneGap.addConstructor(function() {
    PhoneGap.addPlugin("downloader", new Downloader());
});

