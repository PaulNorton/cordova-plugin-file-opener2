/*
The MIT License (MIT)

Copyright (c) 2013 pwlin - pwlin05@gmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package io.github.pwlin.cordova.plugins.fileopener2;

import android.os.ParcelFileDescriptor;
import android.os.CancellationSignal;
import android.net.Uri;
import android.content.Context;
import java.io.File;

/*
 * http://stackoverflow.com/questions/40746144/error-with-duplicated-fileprovider-in-manifest-xml-with-cordova/41550634#41550634
 */
public class FileProvider extends android.support.v4.content.FileProvider {
    public static Uri getMassagedUriForFile(Context context, String authority, File file, String contentType)
    {
        Uri uri = null;

        try {
            uri = FileProvider.getUriForFile(context, authority, file);
        } catch(Exception e) {
            return null;
        }

        String path = uri.toString();

        if(contentType.equals("application/pdf") && !path.endsWith(".pdf")) {
            path = path.replace(".opener.provider", ".opener.provider/extension-added");
            path = path + ".pdf";
        }

        return Uri.parse(path);
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode)
    {
        uri = massageUri(uri);

        try {
            return super.openFile(uri, mode);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal arg2)
    {
        uri = massageUri(uri);

        try {
            return super.openFile(uri, mode, arg2);
        } catch(Exception e) {
            return null;
        }
    }

    private Uri massageUri(Uri uri) {
        String path = uri.toString();
        
        if(path.contains(".opener.provider/extension-added")) {
            path = path.replace(".opener.provider/extension-added", ".opener.provider");
            path = path.substring(0, path.lastIndexOf("."));
        }

        return Uri.parse(path);
    }
}
