package cl.btolabs.panel;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

import java.io.File;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(ApkInstalador.class);
        super.onCreate(savedInstanceState);
        // Sin esto, una actualización "encima" puede dejar al WebView sirviendo el index.html viejo desde caché.
        if (getBridge() != null && getBridge().getWebView() != null) {
            getBridge().getWebView().clearCache(true);
        }
    }

    // Al volver a la app (después de confirmar o cancelar la instalación) se borra el .apk descargado:
    // nunca queda una copia ambigua dando vueltas.
    @Override
    public void onResume() {
        super.onResume();
        try {
            File apkDescargado = new File(getExternalFilesDir(null), ApkInstalador.NOMBRE_ARCHIVO);
            if (apkDescargado.exists()) apkDescargado.delete();
        } catch (Exception e) {
            // no bloquea el arranque por esto
        }
    }
}
