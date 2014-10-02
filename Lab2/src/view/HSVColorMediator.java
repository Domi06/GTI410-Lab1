package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;


public class HSVColorMediator extends Object implements SliderObserver, ObserverIF {
	ColorSlider hueCS;
	ColorSlider saturationCS;
	ColorSlider valueCS;
	int red;
	int green;
	int blue;
	BufferedImage hueImage;
	BufferedImage saturationImage;
	BufferedImage valueImage;
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
	HSVColorMediator (ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		this.red 	= result.getPixel().getRed();
		this.green 	= result.getPixel().getGreen();
		this.blue 	= result.getPixel().getBlue();
		this.result = result;
		result.addObserver(this);
		
		hueImage 	 	= new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		saturationImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		valueImage	 	= new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);

		computeHueImage			(red, green, blue);
		computeSaturationImage	(red, green, blue);
		computeValueImage		(red, green, blue);
		}
	
	/**
	 * Gr�ce aux valeurs rgb on trouve la correspondance pour la Hue
	 * @param red between 0 and 255
	 * @param green between 0 and 255
	 * @param blue between 0 and 255
	 */
	public void computeHueImage(int red, int green, int blue) {
		Pixel p 		= new Pixel(red, green, blue, 255); 
		int tabRGB[] 	= new int[3];
		int hue 		= 0;
		
		//conversion des parametres RGB en HSV
		int saturation	= getSaturation(red, green, blue);
		int value 		= getValue(red, green, blue);
		
		// Pour chaque valeur on convertit sa couleur en HSV
		for (int i = 0; i<imagesWidth; ++i) {
			// La taille de l'image est de 255, il faut donc la faire aller jusqu'à 360° (valeur de Hue)
			hue= (int)(((double)i / (double)imagesWidth)*255.0);
			
			//conversion des parametres HSV en RGB pour l'affichage
			tabRGB = HSVtoRGB(hue, saturation, value);
			
			// Enregistrement des valeurs obtenues dans le pixel
			p.setRed	(tabRGB[0]);
			p.setGreen	(tabRGB[1]);
			p.setBlue	(tabRGB[2]);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j)
				hueImage.setRGB(i, j, rgb);
			
			if (hueCS != null)
				hueCS.update(hueImage);
		}
	}
	
	/**
	 * Grace aux valeurs rgb on trouve la correspondance pour la Saturation
	 * @param red between 0 and 255
	 * @param green between 0 and 255
	 * @param blue between 0 and 255
	 */
	public void computeSaturationImage(int red, int green, int blue){
		Pixel p 		= new Pixel(red, green, blue, 255); 
		int tabRGB[] 	= new int[3];
		int saturation 		= 0;
		
		//conversion des param�tres RGB en HSV
		int hue			= getHue(red, green, blue);
		int value		= getValue(red, green, blue);

		// Pour chaque valeur on convertit sa couleur en HSV
		for (int i = 0; i<imagesWidth; ++i) {
			// La taille de l'image est de 255, il faut donc la faire aller jusqu'� 100% (valeur de Saturation)
			saturation= (int)(((double)i / (double)imagesWidth)*255.0);
			
			//conversion des param�tres CMYK en RGB pour l'affichage
			tabRGB = HSVtoRGB(hue, saturation, value);
			
			// Enregistrement des valeurs obtenues dans le pixel
			p.setRed	(tabRGB[0]);
			p.setGreen	(tabRGB[1]);
			p.setBlue	(tabRGB[2]);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j)
				saturationImage.setRGB(i, j, rgb);
			if (saturationCS != null)
				saturationCS.update(saturationImage);
		}
	}
	
	/***
	 * Gr�ce aux valeurs rgb on trouve la correspondance pour la Value
	 * @param red between 0 and 255
	 * @param green between 0 and 255
	 * @param blue between 0 and 255
	 */
	public void computeValueImage(int red, int green, int blue){
		Pixel p 		= new Pixel(red, green, blue, 255); 
		int tabRGB[] 	= new int[3];
		int value 		= 0;
		
		//conversion des param�tres RGB en HSV
		int hue			= getHue(red, green, blue);
		int saturation	= getSaturation(red, green, blue);

		// Pour chaque valeur on convertit sa couleur en HSV
		for (int i = 0; i<imagesWidth; ++i) {
			// La taille de l'image est de 255, il faut donc la faire aller jusqu'� 100% (valeur de Value)
			value= (int)(((double)i / (double)imagesWidth)*255.0);
			
			//conversion des param�tres HSV en RGB
			tabRGB = HSVtoRGB(hue, saturation, value);

			// Enregistrement des valeurs obtenues dans le pixel
			p.setRed	(tabRGB[0]);
			p.setGreen	(tabRGB[1]);
			p.setBlue	(tabRGB[2]);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j)
				valueImage.setRGB(i, j, rgb);
			if (valueCS != null)
				valueCS.update(valueImage);
		}
	}
	/**
	 * @return hueImage : valeur temporaire
	 */
	public BufferedImage getHueImage() {
		return  hueImage;
	}

	/**
	 * @return saturationImage : valeur temporaire
	 */
	public BufferedImage getSaturationImage() {
		return saturationImage;
	}

	/**
	 * @return valueImage : valeur temporaire
	 */
	public BufferedImage getValueImage() {
		return valueImage;
	}
	
	/**
	 * Permet d'ajouter les observers pour que les �l�ments de la vue soient rafraichis
	 * @param slider : la couleur du slider
	 */
	public void setHueCS(ColorSlider slider) {
		hueCS = slider;
		slider.addObserver(this);
	}

	/**
	 * @param slider : la couleur du slider
	 */
	public void setSaturationCS(ColorSlider slider) {
		saturationCS = slider;
		slider.addObserver(this);
	}

	/**
	 * @param slider : la couleur du slider
	 */
	public void setValueCS(ColorSlider slider) {
		valueCS = slider;
		slider.addObserver(this);
	}
	
	/**
	 * Permet d'updater les curseurs d'un onglet � un autre si la valeur de r�sult ne correspond pas � l'endroit o� sont les curseurs
	 */
	@Override
	public void update() {
		
		/* les lignes suivantes sont commentées puisqu'en hsv, 
		 * l'action d'un mélangeur peut forcer le positionnement d'un autre curseur
		 * la mise à jour doit donc être constante
		 */
		//Pixel currentColor = new Pixel(red, green, blue, 255);
		//if(currentColor.getARGB() == result.getPixel().getARGB()) return;
		
		// On recupere les valeurs du pixel courrant
		red 	= result.getPixel().getRed();
		green 	= result.getPixel().getGreen();
		blue 	= result.getPixel().getBlue();
		
		// On recupere les valeurs HSV du pixel courant pour changer les valeurs du slider
		hueCS.setValue			(getHue			(red,green,blue));
		saturationCS.setValue	(getSaturation	(red,green,blue));
		valueCS.setValue		(getValue		(red,green,blue));
		
		computeHueImage			(red, green, blue);
		computeSaturationImage	(red, green, blue);
		computeValueImage		(red, green, blue);
	}

	/**
	 * Permet de rafraichir les degrades de couleur des sliders autres que celui qui est en cours de modification
	 * lorsque l'on modifie la valeur d'un slider
	 * @param cs : colorslider actuel
	 * @param v : valeur du slider
	 */
	@Override
	public void update(ColorSlider s, int v) {
		boolean updateHue 			= false;
		boolean updateSaturation 	= false;
		boolean updateValue 		= false;
		
		int hue			= getHue		(red, green, blue);
		int saturation	= getSaturation	(red, green, blue);
		int value		= getValue		(red, green, blue);;

		
		// S'il s'agit du color slider pour la Hue et que la valeur du slider ne correspond pas � la couleur
		if (s==  hueCS && v!= hue) {
			hue = v;
			updateSaturation 	= true;
			updateValue 		= true;
		}
		// S'il s'agit du color slider pour la Saturation et que la valeur du slider ne correspond pas � la couleur
		if (s==  saturationCS && v!= saturation) {
			saturation = v;
			updateHue 			= true;
			updateValue 		= true;
		}
		// S'il s'agit du color slider pour la Value et que la valeur du slider ne correspond pas � la couleur
		if (s==  valueCS && v!= value) {
			value = v;
			updateHue 			= true;
			updateSaturation 	= true;
		}

		// Recalcule les valeur RGB en fonction de HSV pour pouvoir rafra�chir les couleurs des autres sliders
		int tabRGB[] = new int[3];
		tabRGB 	= HSVtoRGB(hue,saturation,value);
		red		= tabRGB[0];
		green	= tabRGB[1];
		blue	= tabRGB[2];
		
		
		//redessine les sliders avec les nouvelles couleurs
		if (updateHue)
			computeHueImage(red, green, blue);
		if (updateSaturation) 
			computeSaturationImage(red, green, blue);
		if (updateValue) 
			computeValueImage(red, green, blue);
		
		Pixel pixel = new Pixel(red, green, blue, 255);
		result.setPixel(pixel);
	}
	
	/**
	 * Recupere la valeur de Hue selon les valeurs rgb
	 * Reference pour la conversion : http://www.easyrgb.com/
	 * 
	 * @param red	: entre 0 et 255
	 * @param green	: entre 0 et 255
	 * @param blue	: entre 0 et 255
	 * @return Hue : entre 0 et 255
	 */
	public int getHue(int red, int green, int blue){
		double r = (double) red/255.0;
		double g = (double) green/255.0;
		double b = (double) blue/255.0;
		
		double H=0;
		double del_R, del_G, del_B;
		
		double min = Math.min( Math.min(r, g), Math.min(g, b ));
		double max = Math.max( Math.max(r, g), Math.max(g, b ));
		double del_Max = max - min;

		if (del_Max == 0){
			del_R = 0;
			del_G =	0;
			del_B = 0;
		}
		else{
			del_R = ((( max-r )/6) + ( del_Max/2 ))/del_Max;
			del_G = ((( max-g )/6) + ( del_Max/2 ))/del_Max;
			del_B = ((( max-b )/6) + ( del_Max/2 ))/del_Max;
		}
		if ( r == max )		H = del_B - del_G;
		if ( g == max )		H = (1 + 3*del_R - 3*del_B)/3;
		if ( b == max )		H = (2 + 3*del_G - 3*del_R)/3;
		
		if ( H < 0 ) H += 1;
		if ( H > 1 ) H -= 1;
		
		return (int) (H*255);
	}
	
	/**
	 * Recupere la valeur de Saturation selon les valeurs rgb
	 * Reference pour la conversion : http://www.easyrgb.com/
	 * 
	 * @param red	: entre 0 et 255
	 * @param green	: entre 0 et 255
	 * @param blue	: entre 0 et 255
	 * @return Saturation : entre 0 et 255
	 */
	public int getSaturation(int red, int green, int blue){
		double r = (double) red/255.0;
		double g = (double) green/255.0;
		double b = (double) blue/255.0;
		
		int s;
		double sd;
		
		double min = Math.min( Math.min(r, g), Math.min(g, b ));
		double max = Math.max( Math.max(r, g), Math.max(g, b ));
		double del_Max = max - min;


		sd= del_Max / max * 255.0;
		s=(int)sd;

		
		
		return s;
	}
	
	/**
	 * R�cup�re la valeur de Value selon les valeurs rgb
	 * R�f�rence pour la conversion : http://www.easyrgb.com/
	 * 
	 * @param red	: entre 0 et 255
	 * @param green	: entre 0 et 255
	 * @param blue	: entre 0 et 255
	 * @return Value : entre 0 et 255
	 */
	public int getValue(int red, int green, int blue){
		double r = (double) red/255.0;
		double g = (double) green/255.0;
		double b = (double) blue/255.0;
		
		double max = Math.max( Math.max(r, g), Math.max(g, b ));
		double vd= max*255;
		int v= (int)vd;
		return v;
	}
	
	//
	/**
	 * Retourne un tableau avec les valeurs en rgb en fonction des compostantes HSV en 255
	 * utilis� pour coloriser les bandes de couleur
	 * R�f�rence pour la conversion : http://www.easyrgb.com/
	 * @param hue : entre 0 et 360�
	 * @param saturation : entre 0 et 100%
	 * @param value : entre 0 et 100%
	 * @return
	 */
	public int[] HSVtoRGB (double hue, double saturation, double value){
		int tabRGB[] = new int[3], var_i;
		double var_h, var_1, var_2, var_3, r, g, b;
		
//		//évite d'avoir un bug graphique sur le mélangeur de hue
//		if(value==0){
//			value=1;
//		}
				
		double h = hue/255.0;
		double s = saturation/255.0;
		double v = value/255.0;
		
		if ( s == 0 )
		{
			// On arrondi la valeur obtenue en double, puis la cast en int
			tabRGB[0] = (int) (v * 255.0);
			tabRGB[1] = (int) (v * 255.0);
			tabRGB[2] = (int) (v * 255.0);
		}
		else
		{
			var_h = h * 6.0;
			if ( var_h == 6.0 )
				var_h = 0.0;
			
			var_i = (int) Math.floor(var_h);
			var_1 = v * ( 1.0 - s );
			var_2 = v * ( 1.0 - s * ( var_h - var_i ) );
			var_3 = v * ( 1.0 - s * ( 1.0 - ( var_h - var_i ) ) );
			
			if      ( var_i == 0 ) { r = v     ; g = var_3 ; b = var_1; }
			else if ( var_i == 1 ) { r = var_2 ; g = v     ; b = var_1; }
			else if ( var_i == 2 ) { r = var_1 ; g = v     ; b = var_3; }
			else if ( var_i == 3 ) { r = var_1 ; g = var_2 ; b = v;     }
			else if ( var_i == 4 ) { r = var_3 ; g = var_1 ; b = v;     }
			else                   { r = v     ; g = var_1 ; b = var_2; }
			
			tabRGB[0] = (int) (r * 255.0);
			tabRGB[1] = (int) (g * 255.0);
			tabRGB[2] = (int) (b * 255.0);
			
		}	
		return tabRGB;
	}
}
