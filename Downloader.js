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
 * @param source {String}             URL of the server to receive the file
 * @param target {String}             Full path of the file on the device
 * @param successCallback (Function}  Callback to be invoked when upload has completed
 * @param errorCallback {Function}    Callback to be invoked upon error
 */
Downloader.prototype.downloadFile = function(source, target, successCallback, errorCallback) {
    PhoneGap.exec(successCallback, errorCallback, 'Downloader', 'download', [source, target]);
};

PhoneGap.addConstructor(function() {
    PhoneGap.addPlugin("downloader", new Downloader());
});

