package cl.btolabs.panel;

import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// Descarga el .apk de actualización a la carpeta PRIVADA de la app (getExternalFilesDir),
// nunca a la carpeta pública de Descargas: ahí Android agrega "(1)", "(2)"... y se termina
// instalando una copia vieja sin darse cuenta. El archivo se sobreescribe siempre y
// MainActivity.onResume lo borra al volver a la app. Mismo patrón que DAOMA (ApkInstalador.java).
@CapacitorPlugin(name = "ApkInstalador")
public class ApkInstalador extends Plugin {

    public static final String NOMBRE_ARCHIVO = "actualizacion.apk";

    @PluginMethod
    public void descargarEInstalar(PluginCall call) {
        String url = call.getString("url");
        if (url == null || url.isEmpty()) {
            call.reject("falta la url");
            return;
        }

        new Thread(() -> {
            try {
                File destino = new File(getContext().getExternalFilesDir(null), NOMBRE_ARCHIVO);
                if (destino.exists()) destino.delete();

                HttpURLConnection conexion = (HttpURLConnection) new URL(url).openConnection();
                conexion.setInstanceFollowRedirects(true);
                conexion.connect();

                try (InputStream entrada = conexion.getInputStream();
                     FileOutputStream salida = new FileOutputStream(destino)) {
                    byte[] buffer = new byte[8192];
                    int leidos;
                    while ((leidos = entrada.read(buffer)) != -1) {
                        salida.write(buffer, 0, leidos);
                    }
                }

                Uri uriArchivo = FileProvider.getUriForFile(
                    getContext(),
                    getContext().getPackageName() + ".fileprovider",
                    destino
                );

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uriArchivo, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

                JSObject resultado = new JSObject();
                resultado.put("ok", true);
                call.resolve(resultado);
            } catch (Exception e) {
                call.reject("no se pudo descargar/instalar: " + e.getMessage(), e);
            }
        }).start();
    }
}
