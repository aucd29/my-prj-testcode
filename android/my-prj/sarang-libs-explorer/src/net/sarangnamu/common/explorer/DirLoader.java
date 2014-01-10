/*
 * DirLoader.java
 * Copyright 2013 Burke Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.common.explorer;

import java.io.File;
import java.util.List;

import android.content.Context;

import com.ipaulpro.afilechooser.FileLoader;
import com.ipaulpro.afilechooser.utils.FileUtils;

public class DirLoader extends FileLoader {
    public DirLoader(Context context, String path) {
        super(context, path);
    }

    @Override
    public List<File> loadInBackground() {
        return FileUtils.getDirList(mPath);
    }
}
