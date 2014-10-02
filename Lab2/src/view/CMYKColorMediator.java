package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;

public class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {
	ColorSlider cyanCS;
	ColorSlider magentaCS;
	ColorSlider yellowCS;
	ColorSlider blackCS;
	int red;
	int green;
	int blue;
	BufferedImage cyanImage;
	BufferedImage magentaImage;
	BufferedImage yellowImage;
	BufferedImage blackImage;
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
	/**
	 * Constructeur
	 * @param result
	 * @param imagesWidth
	 * @param imagesHeight
	 */
	CMYKColorMediator (ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		this.red = result.getPixel().getRed();
		this.green = result.getPixel().getGreen();
		this.blue = result.getPixel().getBlue();
		this.result = result;
		result.addObserver(this);
		
		cyanImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		magentaImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		yellowImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		blackImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		computeCyanImage(red, green, blue);
		computeMagentaImage(red, green, blue);
		computeYellowImage(red, green, blue);
		computeBlackImage(red, green, blue);
	}
	

	/**
	 * dessine le dégradé de cyan pour le mélangeur
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void computeCyanImage(int red, int green, int blue) { 
		Pixel p = new Pixel(red, green, blue, 255); 
		int tabRGB[]=new int[3];
		int cyan=0;
		
		//conversion des paramètres RGB en CMYK pour faire le mélange avec le cyan
		int magenta=getMagenta(red, green, blue);
		int yellow=getYellow(red, green, blue);
		int black=getBlack(red, green, blue);
		
		for (int i = 0; i<imagesWidth; ++i) {
			
			cyan= (int)(((double)i / (double)imagesWidth)*255.0);
			
			//conversion des paramètres CMYK en RGB pour l'affichage
			tabRGB = CMYKtoRGB(cyan,magenta, yellow, black);
			
			p.setRed(tabRGB[0]);
			p.setGreen(tabRGB[1]);
			p.setBlue(tabRGB[2]);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				cyanImage.setRGB(i, j, rgb);
			}
		}
		if (cyanCS != null) {
			cyanCS.update(cyanImage);
		}
	}
	
	/**
	 * dessine le dégradé de Magenta pour le mélangeur de magenta
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void computeMagentaImage(int red, int green, int blue){
		Pixel p = new Pixel(red, green, blue, 255); 
		int tabRGB[]=new int[3];
		int magenta=0;
		
		//conversion des paramètres RGB en CMYK pour faire le mélange avec le magenta
		int cyan=getCyan(red, green, blue);
		int yellow=getYellow(red, green, blue);
		int black=getBlack(red, green, blue);
		
		for (int i = 0; i<imagesWidth; ++i) {
			
			magenta= (int)(((double)i / (double)imagesWidth)*255.0);
			
			//conversion des paramètres CMYK en RGB pour l'affichage
			tabRGB = CMYKtoRGB(cyan,magenta, yellow, black);
			
			p.setRed(tabRGB[0]);
			p.setGreen(tabRGB[1]);
			p.setBlue(tabRGB[2]);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				magentaImage.setRGB(i, j, rgb);
			}
		}
		if (magentaCS != null) {
			magentaCS.update(magentaImage);
		}
	}
	
	/**
	 * dessine le dégradé de Magenta pour le mélangeur de jaune
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void computeYellowImage(int red, int green, int blue){
		Pixel p = new Pixel(red, green, blue, 255); 
		int tabRGB[]=new int[3];
		int yellow=0;
		
		//conversion des paramètres RGB en CMYK pour faire le mélange avec le jaune
		int cyan=getCyan(red, green, blue);
		int magenta=getMagenta(red, green, blue);
		int black=getBlack(red, green, blue);
		
		for (int i = 0; i<imagesWidth; ++i) {
			
			yellow= (int)(((double)i / (double)imagesWidth)*255.0);
			
			//conversion des paramètres CMYK en RGB pour l'affichage
			tabRGB = CMYKtoRGB(cyan,magenta, yellow, black);
			
			p.setRed(tabRGB[0]);
			p.setGreen(tabRGB[1]);
			p.setBlue(tabRGB[2]);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				yellowImage.setRGB(i, j, rgb);
			}
		}
		if (yellowCS != null) {
			yellowCS.update(yellowImage);
		}
	}
	
	
	/**
	 * dessine le dégradé de Magenta pour le mélangeur de noir
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void computeBlackImage(int red, int green, int blue){
		Pixel p = new Pixel(red, green, blue, 255); 
		int tabRGB[]=new int[3];
		int black=0;
		
		//conversion des paramètres RGB en CMYK pour faire le mélange avec le noir
		int cyan=getCyan(red, green, blue);
		int magenta=getMagenta(red, green, blue);
		int yellow=getYellow(red, green, blue);
		
		for (int i = 0; i<imagesWidth; ++i) {
			
			black= (int)(((double)i / (double)imagesWidth)*255.0);
			
			//conversion des paramètres CMYK en RGB pour l'affichage
			tabRGB = CMYKtoRGB(cyan,magenta, yellow, black);
			
			p.setRed(tabRGB[0]);
			p.setGreen(tabRGB[1]);
			p.setBlue(tabRGB[2]);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				blackImage.setRGB(i, j, rgb);
			}
		}
		if (blackCS != null) {
			blackCS.update(blackImage);
		}
	}

	/**
	 * 
	 * @return image pour le melangeur de cyan
	 */
	public BufferedImage getCyanImage() {
		return cyanImage;
	}

	/**
	 * @return image pour le melangeur de magenta
	 */
	public BufferedImage getMagentaImage() {
		return magentaImage;
	}

	/**
	 * @return image pour le melangeur de jaune
	 */
	public BufferedImage getYellowImage() {
		return yellowImage;
	}
	
	/**
	 * @return image pour le melangeur de noir
	 */
	public BufferedImage getBlackImage() {
		return blackImage;
	}

	/**
	 * permet d'ajouter l'observer pour le slider cyan pour que les �l�ments de la vue soient rafraichis
	 * @param slider
	 */
	//
	public void setCyanCS(ColorSlider slider) {
		cyanCS = slider;
		slider.addObserver(this);
	}

	/**
	 * permet d'ajouter l'observer pour le slider magenta pour que les �l�ments de la vue soient rafraichis
	 * @param slider
	 */
	public void setMagentaCS(ColorSlider slider) {
		magentaCS = slider;
		slider.addObserver(this);
	}

	/**
	 * permet d'ajouter l'observer pour le slider jaune pour que les �l�ments de la vue soient rafraichis
	 * @param slider
	 */
	public void setYellowCS(ColorSlider slider) {
		yellowCS = slider;
		slider.addObserver(this);
	}
	
	/**
	 * permet d'ajouter l'observer pour le slider noir pour que les �l�ments de la vue soient rafraichis
	 * @param slider
	 */
	public void setBlackCS(ColorSlider slider) {
		blackCS = slider;
		slider.addObserver(this);
	}
	
	/**
	 * permet d'updater les curseurs d'un onglet � un autre si la valeur de r�sult ne correspond pas � l'endroit o� sont les curseurs
	 */
	@Override
	public void update() {
		// When updated with the new "result" color, if the "currentColor"
		// is already properly set, there is no need to recompute the images.
		Pixel currentColor = new Pixel(red, green, blue, 255);
		if(currentColor.getARGB() == result.getPixel().getARGB()) return;
		
		red = result.getPixel().getRed();
		green = result.getPixel().getGreen();
		blue = result.getPixel().getBlue();
		
		cyanCS.setValue(getCyan(red,green,blue));
		magentaCS.setValue(getMagenta(red,green,blue));
		yellowCS.setValue(getYellow(red,green,blue));
		blackCS.setValue(getBlack(red,green,blue));
		computeCyanImage(red, green, blue);
		computeMagentaImage(red, green, blue);
		computeYellowImage(red, green, blue);
		computeBlackImage(red, green, blue);
		
		// Efficiency issue: When the color is adjusted on a tab in the 
		// user interface, the sliders color of the other tabs are recomputed,
		// even though they are invisible. For an increased efficiency, the 
		// other tabs (mediators) should be notified when there is a tab 
		// change in the user interface. This solution was not implemented
		// here since it would increase the complexity of the code, making it
		// harder to understand.
		
	}

	
	/*
	 * @see View.SliderObserver#update(double)
	 */
	//permet de rafraichir les dégradés de couleur des sliders autre que celui qui est en cours de modification
	//lorsque l'on modifie la valeur d'un slider
	@Override
	/**
	 * permet de rafraichir les d�grad�s de couleur des sliders autre que celui qui est en cours de modification
	 *lorsque l'on modifie la valeur d'un slider
	 * @param s nom du slider
	 * @param v valeur du slider
	 */
	public void update(ColorSlider s, int v) {
		boolean updateCyan = false;
		boolean updateMagenta = false;
		boolean updateYellow = false;
		boolean updateBlack = false;
		
		//mémorise les valeurs des couleurs actuelles de cyan, magenta, yellow, black en fonction des valeurs RGB
		int cyan = getCyan(red,green,blue);
		int magenta = getMagenta(red,green,blue);
		int yellow = getYellow(red,green,blue);
		int black = getBlack(red,green,blue);
		
		//s'il s'agit du color slider pour le cyan et que la valeur du slider ne correspond pas � la couleur
		if (s == cyanCS && v != getCyan(red,green,blue)) {
			
			cyan = v;
			updateMagenta = true;
			updateYellow = true;
			updateBlack = true;
		}
		if (s == magentaCS && v != magenta) {
			
			magenta = v;
			updateCyan = true;
			updateYellow = true;
			updateBlack = true;
		}
		if (s == yellowCS && v != yellow) {
			
			yellow = v;
			updateCyan = true;
			updateMagenta = true;
			updateBlack = true;
		}
		if (s == blackCS && v != black) {
			
			black = v;
			updateCyan = true;
			updateMagenta = true;
			updateYellow = true;
		}
		
		//recalcule les valeur RGB en fonction de CMYK pour pouvoir rafraîchir les couleurs des autres sliders
		int tabRGB[]= new int[3];
		tabRGB=CMYKtoRGB(cyan,magenta,yellow,black);
		red=tabRGB[0];
		green=tabRGB[1];
		blue=tabRGB[2];
		
		//redessine les sliders avec les nouvelles couleurs
		if (updateCyan) {
			computeCyanImage(red, green, blue);
		}
		if (updateMagenta) {
			computeMagentaImage(red, green, blue);
		}
		if (updateYellow) {
			computeYellowImage(red, green, blue);
		}
		if (updateBlack) {
			computeBlackImage(red, green, blue);
		}
		
		Pixel pixel = new Pixel(red, green, blue, 255);
		result.setPixel(pixel);
		
	}
	
	
	/**
	 * ref pour les conversions :http://www.rapidtables.com/convert/color/rgb-to-cmyk.htm
	 * fonction qui prend en param�tres les valeurs RGB et qui sort la valeur du noir (de 0 à 255)
	 * @param red
	 * @param green
	 * @param blue
	 * @return noir de 0 à 255
	 */
	public int getBlack(int red, int green, int blue){
		double red1= (double)red/255.0;
		double green1=(double)green/255.0;
		double blue1=(double)blue/255.0;
		
		//1 - max de red green et blue puis conversion sur 255
		if(red >= green && red >=blue){
			return (int)(((double)1-red1)*255.0);
		}
		else if (green >=red && green >= blue){
			return (int)(((double)1-green1)*255.0);
		}
		else{
			return (int)(((double)1-blue1)*255.0);
		}
		
	}
	
	
	/**
	 * fonction qui prend en param�tres les valeurs RGB et qui sort la valeur du cyan (de 0 à 255)
	 * @param red
	 * @param green
	 * @param blue
	 * @return cyan de 0 à 255
	 */
	public int getCyan(int red, int green, int blue){
		double red1= (double)red/255.0;
		
		//valeur du cyan sur 1
		double black1 = (double)getBlack(red,green,blue)/255.0;
		double cyan1 = ((double)1-red1-black1) / ((double)1-black1);
		
		//valeur du cyan sur 255
		return (int)(cyan1*255.0);
	}
	

	/**
	 * fonction qui prend en paramètres les valeurs RGB et qui sort la valeur du magenta (de 0 à 255)
	 * @param red
	 * @param green
	 * @param blue
	 * @return magenta de 0 à 255
	 */
	public int getMagenta(int red, int green, int blue){
		double green1=(double)green/255.0;
		
		//valeur du magenta sur 1
		double black1 = (double)getBlack(red,green,blue)/255.0;
		double magenta1 = ((double)1-green1-black1) / ((double)1-black1);
				
		//valeur du magenta sur 255
		return (int)(magenta1*255.0);
	}
	
	/**
	 * fonction qui prend en paramètres les valeurs RGB et qui sort la valeur du jaune (de 0 à 255)
	 * @param red
	 * @param green
	 * @param blue
	 * @return jaune de 0 à 255
	 */
	public int getYellow(int red, int green, int blue){
		double blue1=(double)blue/255.0;
		
		//valeur du jaune sur 1
		double black1 = (double)getBlack(red,green,blue)/255.0;
		double yellow1 = ((double)1-blue1-black1) / ((double)1-black1);
						
		//valeur du jaune sur 255
		return (int)(yellow1*255.0);
		
	}
	
	
	/**
	 * retourne un tableau avec les valeurs en rgb en fonction des compostantes CMYK en 255
	 * utilisé pour coloriser les bandes de couleur
	 * ref pour la conversion : http://www.rapidtables.com/convert/color/cmyk-to-rgb.htm
	 * @param cyan
	 * @param magenta
	 * @param yellow
	 * @param black
	 * @return tableau de type int[3] avec les valeurs de RGB 
	 */
	public int[] CMYKtoRGB (int cyan, int magenta, int yellow, int black){
		int tabRGB[] = new int[3];
		
		//rouge
		tabRGB[0]=(255-cyan)*(255-black)/255;
		
		//vert
		tabRGB[1]=(255-magenta)*(255-black)/255;
		
		//bleu
		tabRGB[2]=(255-yellow)*(255-black)/255;
		
		return tabRGB;
	}
}
