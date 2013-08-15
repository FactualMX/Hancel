package org.hansel.myAlert.WelcomeInfo;

import org.hansel.myAlert.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Welcome01Fragment extends Fragment {

	private int number;
	
	public void setNumber(int num){
		number = num;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup rootView = (ViewGroup) inflater.inflate(
                org.hansel.myAlert.R.layout.bienvenida01, container, false);

		TextView texto = (TextView) rootView.findViewById(R.id.textView1);
		ImageView icono = (ImageView) rootView.findViewById(R.id.imageView1);
		
		switch (number) {
		case 0:
			texto.setText("Hancel crea un puente entre periodistas y organizaciones dedicadas a proteger la libertad de expresión.");
			icono.setImageResource(R.drawable.icono1);
			break;
		case 1:
			texto.setText("Es posible programar Hancel para que acompañe al usuario durante coberturas de riesgo y envíe alertas automáticas si el periodista no atiende notificaciones. ");
			icono.setImageResource(R.drawable.icono5);
			break;
		case 2:
			texto.setText("Hancel lanza una alerta de emergencia a contactos de confianza con la ubicación exacta del GPS del teléfono. ");
			icono.setImageResource(R.drawable.icono4);
			break;
		case 3:
			texto.setText("Hancel crea una comunidad de organizaciones e individuos de confianza de manera instantánea al emitir una alerta. ");
			icono.setImageResource(R.drawable.icono3);
			break;
		case 4:
			texto.setText("Hancel le da herramientas a los periodistas para que tengan mayor control de su propia seguridad.");
			icono.setImageResource(R.drawable.icono2);
			break;
		default:
			break;
		}
		
        return rootView;
		
	}
}
