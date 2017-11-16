package io.anuke.codetesting.gifshake;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import io.anuke.gif.GifSequenceWriter;

public class GifShaker{
	
	public static void shake(File input, File output, int intensity, int time) throws IOException{
		BufferedImage image = ImageIO.read(input);
		ImageOutputStream outputStream = new FileImageOutputStream(output);
		GifSequenceWriter writer = new GifSequenceWriter(null, time, time, false);
		
	}
}
