/*
 * Copyright © 2013 Yuk SeungChan, All rights reserved.
 */

package org.sunrin.manager;

import android.media.MediaRecorder;
import android.util.Log;
import org.sunrin.utils.FileUtil;

import java.io.IOException;

public class RecordManager
{
	private MediaRecorder recorder = null;
	private String filePath, fileName = "record.amr";
	private boolean isRecorded = false;

	public RecordManager()
	{
        try
        {
		    filePath = new FileUtil().mkdir("loup");
        }
        catch (Exception e)
        {
            filePath = "/";
        }
    }
	
	public void start()
	{
		isRecorded = true;
		if (recorder == null) recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(filePath + fileName);
        try
        {
            recorder.prepare();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            isRecorded = false;
        }
        recorder.start();
    }
	  
	  public void stop()
	  {
          isRecorded = false;
          if (recorder == null) return;
          try
		  {
			  recorder.stop();
		  }
		  catch(Exception e){}
		  finally
		  {
			  recorder.release();
			  recorder = null;
		  }
	  }
	  
	  public boolean isRecorded()
	  {
		  return isRecorded;
	  }
	  
	  public void setRecorded(boolean recorded)
	  {
		  recorded = recorded;
	  }
}
