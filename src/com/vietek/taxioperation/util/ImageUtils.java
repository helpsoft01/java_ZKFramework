package com.vietek.taxioperation.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.zkoss.image.AImage;

import com.vietek.taxioperation.common.AppLogger;

public class ImageUtils {
	public static File[] lstFile(String pathFolder) {
		File[] listOfFiles = null;
		File folder = new File(pathFolder);
		try {
			listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					AppLogger.logDebug.info("File " + listOfFiles[i].getName());
				}
			}
		} catch (Exception e) {
		}
		return listOfFiles;
	}

	
	public static void convertToByteArray(File[] list, StringBuilder str, Map<String, String> map, List<Integer> lst ) {
		try {
			for (int i = 0; i < list.length; i++) {
				String fileName = list[i].getName().substring(0,list[i].getName().indexOf("."));
				if (list[i].isFile() && list[i].getName().contains(".jpg") || list[i].isFile() && list[i].getName().contains(".JPG")) {
					AppLogger.logDebug.info(fileName);
					str.append(fileName).append(",");
					lst.add(Integer.parseInt(fileName));
					readFile(list[i], fileName,map);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void readFile(File readFile, String fileName,Map<String, String> map ) {
		// You can get an inputStream using any IO API
		byte[] bytes;
		byte[] buffer = new byte[8192];
		int bytesRead;
		String encodedString = null;
		try {
			@SuppressWarnings("resource")
			InputStream inputStream = new FileInputStream(readFile);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			bytes = output.toByteArray();
			encodedString = Base64.encodeBase64String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		map.put(fileName, encodedString);
	}

	public static void main(String[] argv) {
//		String folder = "D:/avatar";
//		File[] lstFile = lstFile(folder);
	}
	
	

	public static String convertAimage2String(AImage aImage) {
		if (aImage == null)
			return null;
		try {
			String imageEncoded = Base64.encodeBase64String(aImage.getByteData());
			return imageEncoded;
		} catch (Exception e) {
			return null;
		}

	}

	public static AImage convertString2AImage(String base64Img) {
		AImage decodedimg = null;
		if (base64Img == null)
			return null;
		byte[] decodedString = Base64.decodeBase64(base64Img);
		if (decodedString.length > 10) {
			try {
				decodedimg = new AImage("img", decodedString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return decodedimg;
	}

}
