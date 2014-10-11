package ImageUtils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtils {

	/**
	 * 读取JPEG图片
	 * 
	 * @param filename
	 * @return
	 */
	
/*	  public static BufferedImage readJPEGImage(String filename){
		  try {
			  //BufferedImage image=ImageIO.read(new File(filename));
			  
			  InputStream imageIn = new FileInputStream(new File(filename)); 
			  //得到输入的编码器，将文件流进行jpg格式编码
			  JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn); // 得到编码后的图片对象 
			  BufferedImage image =  decoder.decodeAsBufferedImage();
			  return image; 
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  return null;
	  }*/
	 
	/**
	 * 读取PNG图片
	 * 
	 * @param filename
	 *            文件名
	 * @return BufferedImage 图片对象
	 */
	public static BufferedImage readPNGImage(String filename) {
		try {
			File inputFile = new File(filename);
			BufferedImage sourceImage = ImageIO.read(inputFile);
			return sourceImage;
		} catch (Exception e) {
			ImageIcon ii=new ImageIcon(filename);
			Image i=ii.getImage();
			BufferedImage bufferedImage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(i, null, null);
			//waitForImage(bufferedImage);
			return bufferedImage;
			//e.printStackTrace();
		}
		//return null;
	}

	/**
	 * 形成缩略图s
	 * 
	 * @param source
	 *            图片源
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param b
	 *            是否等比缩放
	 * @return
	 */
	public static BufferedImage abbreviativeGraphic(BufferedImage source,
			int width, int height, boolean b) {

		int s_width = source.getWidth();
		int s_height = source.getHeight();
		BufferedImage target = null;
		int type = source.getType();
		double sx = (double) width / s_width;
		double sy = (double) height / s_height;

		if (b) { // 同比例缩放
			if (sx > sy) {
				sx = sy;
				width = (int) (sx * s_width);
			} else {
				sy = sx;
				height = (int) (sy * s_height);
			}
		}
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(width,
					height);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(width, height, type);
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

	/**
	 * 计算灰度值
	 * 
	 * @param pixels
	 * @return
	 */
	public static int rgb2gray(int pixels) {

		int red = (pixels >> 16) & 0xff;
		int green = (pixels >> 8) & 0xff;
		int blue = pixels & 0xff;
		return (int) (0.3 * red + 0.59 * green + 0.11 * blue);
	}

	/**
	 * 计算平均值
	 * 
	 * @param pixels
	 * @return
	 */
	public static int average(int pixels[]) {
		float aver = 0;
		for (int i = 0; i < pixels.length; i++) {
			aver += pixels[i];
		}
		//System.out.println("aver" + aver);
		aver = aver / pixels.length;
		return (int) aver;
	}

	/**
	 * 像素灰度比较
	 * 
	 * @param pixels
	 * @param average
	 * @return
	 */
	public static int[] comp(int pixels[], int average) {
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] >= average) {
				pixels[i] = 1;
			} else {
				pixels[i] = 0;
			}
		}
		return pixels;
	}

	public static StringBuffer hashCoder(int grays[]) {
		StringBuffer hashCode = new StringBuffer();
		for (int i = 0; i < grays.length; i += 4) {
			int result = grays[i] * (int) Math.pow(2, 3) + grays[i + 1]
					* (int) Math.pow(2, 2) + grays[i + 2]
					* (int) Math.pow(2, 1) + grays[i + 2];
			hashCode.append(binaryToHex(result));
		}
		return hashCode;

	}

	/**
	 * 二进制转为十六进制
	 * 
	 * @param int binary
	 * @return char hex
	 */
	private static char binaryToHex(int binary) {
		char ch = ' ';
		switch (binary) {
		case 0:
			ch = '0';
			break;
		case 1:
			ch = '1';
			break;
		case 2:
			ch = '2';
			break;
		case 3:
			ch = '3';
			break;
		case 4:
			ch = '4';
			break;
		case 5:
			ch = '5';
			break;
		case 6:
			ch = '6';
			break;
		case 7:
			ch = '7';
			break;
		case 8:
			ch = '8';
			break;
		case 9:
			ch = '9';
			break;
		case 10:
			ch = 'a';
			break;
		case 11:
			ch = 'b';
			break;
		case 12:
			ch = 'c';
			break;
		case 13:
			ch = 'd';
			break;
		case 14:
			ch = 'e';
			break;
		case 15:
			ch = 'f';
			break;
		default:
			ch = ' ';
		}
		return ch;
	}
}
