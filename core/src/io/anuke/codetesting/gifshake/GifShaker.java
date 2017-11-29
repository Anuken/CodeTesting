package io.anuke.codetesting.gifshake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import io.anuke.ucore.UCore;
import io.anuke.ucore.util.Mathf;

public class GifShaker{

	public static void shake(File input, File output, int intensity, int time, int frames, 
			int shakex, int shakey, int shakewidth, int shakeheight, Color background) throws Exception{

		BufferedImage image = ImageIO.read(input);
		Graphics2D graphics = image.createGraphics();
		OutputStream o = new FileOutputStream(output);
		//GifSequenceWriter writer = new GifSequenceWriter(outputStream, image.getType(), time, true);
		Color color = background;

		AnimatedGIFWriter e = new AnimatedGIFWriter();
		e.prepareForWrite(o, -1, -1);
		e.setLoopCount(-1);

		/*
		 * ; e.start(outputStream); e.setRepeat(0); e.setTransparent(color);
		 * e.setDelay(time); e.setQuality(256);
		 */

		BufferedImage[] images = new BufferedImage[frames];
		int[] delays = new int[frames];
		Arrays.fill(delays, time);
		
		int copies1 = 1, copies2 = 1;
		boolean shakeimage = false;

		for(int i = 0; i < frames; i++){
			UCore.log("Generating frame " + i + " of " + frames + "...");
			BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
			Graphics2D cg = copy.createGraphics();

			for(int j = 0; j < copies1; j++){
				int offsetxm = Mathf.range(intensity), offsetym = Mathf.range(intensity);
				if(!shakeimage){
					offsetxm = offsetym = 0;
				}

				cg.drawImage(image, offsetxm, offsetym, null);

				cg.setBackground(color);
				cg.clearRect(shakex + offsetxm, shakey + offsetym, shakewidth, shakeheight);

			}

			for(int j = 0; j < copies2; j++){
				int offsetx = Mathf.range(intensity), offsety = Mathf.range(intensity);

				cg.drawImage(image, shakex + offsetx, shakey + offsety, shakex + shakewidth + offsetx, shakey + shakeheight + offsety, 
						shakex, shakey, shakex + shakewidth, shakey + shakeheight, color, null);
			}

			images[i] = copy;
			//writer.writeToSequence(copy);
			e.writeFrame(o, copy, time);
		}

		//e.writeAnimatedGIF(images, delays, o);

		e.finishWrite(o);
		graphics.dispose();
	}

	private static Graphics2D copy(BufferedImage source, BufferedImage dest, int offsetx, int offsety){
		Graphics2D graphics = dest.createGraphics();
		graphics.drawImage(source, offsetx, offsety, null);
		return graphics;
	}
}
