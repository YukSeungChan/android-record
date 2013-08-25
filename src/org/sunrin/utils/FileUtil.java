/*
 * Copyright © 2013 Yuk SeungChan, All rights reserved.
 */

package org.sunrin.utils;

import android.os.Environment;

import java.io.File;

public class FileUtil
{
	public String mkdir(String dirName) throws Exception {
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dirName;
		try
	    {
			File fileRoot = new File(rootPath);
			if (fileRoot.exists() == false)
			{
				if (fileRoot.mkdirs() == false)
					throw new Exception("Fail");
			}
	    }
	    catch (Exception e)
	    {
            throw new Exception("Fail");
	    }
		return rootPath + "/";
	}
}
