package com.sgf.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloadThread extends Thread {

	private boolean isCompleted = false;

	private int downloadLength = 0;

	private File file;

	private URL downloadUrl;

	private int threadId;

	private int blockSize;

	public FileDownloadThread(URL downloadUrl, File file, int blocksize,
			int threadId) {
		this.downloadUrl = downloadUrl;
		this.file = file;
		this.threadId = threadId;
		this.blockSize = blocksize;
	}

	@Override
	public void run() {

		BufferedInputStream bis = null;
		RandomAccessFile raf = null;

		try {
			URLConnection conn = downloadUrl.openConnection();
			conn.setAllowUserInteraction(true);

			int startPos = blockSize * (threadId - 1);
			int endPos = blockSize * threadId - 1;

			conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);

			byte[] buffer = new byte[1024];
			bis = new BufferedInputStream(conn.getInputStream());

			raf = new RandomAccessFile(file, "rwd");
			raf.seek(startPos);
			int len;
			while ((len = bis.read(buffer, 0, 1024)) != -1) {
				raf.write(buffer, 0, len);
				downloadLength += len;
			}
			isCompleted = true;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public int getDownloadLength() {
		return downloadLength;
	}

}
