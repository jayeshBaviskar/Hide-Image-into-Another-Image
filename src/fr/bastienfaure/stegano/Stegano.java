package fr.bastienfaure.stegano;

import java.awt.image.BufferedImage;

public class Stegano {
	private BufferedImage imageSource;
	private BufferedImage imageToHide;
	private BufferedImage imageResult;
	private int width;
	private int height;

	// Constructor with 2 images (hiding process)
	public Stegano(BufferedImage sourceImg, BufferedImage hiddenImg) {
		imageSource = sourceImg;
		imageToHide = hiddenImg;
		width = sourceImg.getWidth();
		height = sourceImg.getHeight();
		imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	// Constructor with 1 image (revealing process)
	public Stegano(BufferedImage sourceImg) {
		this(sourceImg, null);
	}

	// Hide the first 3 MSB of pixelB in pixelA
	private int hidePixel(int pixelA, int pixelB) {
		return pixelA & 0xFFF8F8F8 | (pixelB & 0x00E0E0E0) >> 5;
	}

	// Extract the last 3 LSB
	private int revealPixel(int pixel) {
		return (pixel & 0xFF070707) << 5;
	}

	// Hide hiddenImage into coverImage
	public void hide() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				imageResult.setRGB(i, j, hidePixel(imageSource.getRGB(i, j), imageToHide.getRGB(i, j)));
			}
		}
	}

	// Reveal hidden image into resultImage
	public void reveal() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				imageResult.setRGB(i, j, revealPixel(imageSource.getRGB(i, j)));
			}
		}
	}

	// Return resultImage
	public BufferedImage getResultImage() {
		return imageResult;
	}
}
